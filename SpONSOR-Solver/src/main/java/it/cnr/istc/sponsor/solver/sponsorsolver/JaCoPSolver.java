/*
 * Copyright (C) 2017 Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.cnr.istc.sponsor.solver.sponsorsolver;

import static it.cnr.istc.sponsor.solver.sponsorsolver.ASolver.is_compatible;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jacop.constraints.LinearInt;
import org.jacop.constraints.XeqC;
import org.jacop.constraints.XgtC;
import org.jacop.core.BooleanVar;
import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.search.DepthFirstSearch;
import org.jacop.search.IndomainMax;
import org.jacop.search.Search;
import org.jacop.search.SelectChoicePoint;
import org.jacop.search.SimpleSelect;
import org.jacop.search.SmallestDomain;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class JaCoPSolver extends ASolver {

    private static final Logger LOG = Logger.getLogger(JaCoPSolver.class.getName());
    protected Store s;
    protected BooleanVar[][] vars;
    protected int[][] vals;
    protected IntVar[] workload;
    protected IntVar obj_var;

    @Override
    public boolean init() {
        LOG.info("Initializing the problem..");

        if (users.size() > activities.size()) {
            LOG.warning("The number of users is greater than the number of activities..");
            return false;
        }

        s = new Store(users.size() * activities.size());

        vars = new BooleanVar[users.size()][activities.size()];
        vals = new int[users.size()][activities.size()];
        workload = new IntVar[users.size()];

        // we create the variables..
        for (int i = 0; i < users.size(); i++) {
            for (int j = 0; j < activities.size(); j++) {
                if (is_compatible(activities.get(j), users.get(i))) {
                    vars[i][j] = new BooleanVar(s, "b_" + i + "_" + j);
                    vals[i][j] = evaluate(activities.get(j), users.get(i));
                } else {
                    vars[i][j] = new BooleanVar(s, "b_" + i + "_" + j, 0, 0);
                    vals[i][j] = 0;
                }
            }

            workload[i] = new IntVar(s, "load_" + i, 0, activities.size());
            IntVar[] c_vars = new IntVar[activities.size() + 1];
            System.arraycopy(vars[i], 0, c_vars, 0, c_vars.length - 1);
            c_vars[activities.size()] = workload[i];
            int[] coeffs = new int[activities.size() + 1];
            Arrays.fill(coeffs, 1);
            coeffs[coeffs.length - 1] = -1;
            s.impose(new LinearInt(s, c_vars, coeffs, "==", 0));
        }

        // every user must do something..
        for (int i = 0; i < users.size(); i++) {
            int[] coeffs = new int[activities.size()];
            Arrays.fill(coeffs, 1);
            s.impose(new LinearInt(s, vars[i], coeffs, ">=", 1));
        }

        // every activity must be performed by exactly one user..
        for (int j = 0; j < activities.size(); j++) {
            BooleanVar[] column = new BooleanVar[users.size()];
            for (int i = 0; i < users.size(); i++) {
                column[i] = vars[i][j];
            }
            int[] coeffs = new int[users.size()];
            Arrays.fill(coeffs, 1);
            s.impose(new LinearInt(s, column, coeffs, "==", 1));
        }

        // we add the at-most-n constraints..
        for (AtMostN at_most_n : at_most_ns) {
            for (User u : at_most_n.us) {
                BooleanVar[] c_vars = new BooleanVar[at_most_n.as.length];
                for (int j = 0; j < at_most_n.as.length; j++) {
                    c_vars[j] = vars[u.id][at_most_n.as[j].id];
                }
                int[] coeffs = new int[at_most_n.as.length];
                Arrays.fill(coeffs, 1);
                s.impose(new LinearInt(s, c_vars, coeffs, "<=", at_most_n.n));
            }
        }

        // we add the at-least-one-among-users constraints..
        for (AtLeastOneAmong aloa : at_least_one_among_users) {
            BooleanVar[] c_vars = new BooleanVar[aloa.as.length * aloa.us.length];
            for (int i = 0; i < aloa.us.length; i++) {
                for (int j = 0; j < aloa.as.length; j++) {
                    c_vars[i * aloa.as.length + j] = vars[aloa.us[i].id][aloa.as[j].id];
                }
            }
            int[] coeffs = new int[c_vars.length];
            Arrays.fill(coeffs, 1);
            s.impose(new LinearInt(s, c_vars, coeffs, ">=", 1));
        }

        // we add the only-in constraints..
        for (OnlyIn oi : only_in) {
            Set<Integer> allowed_activities = new HashSet<>(oi.as.length);
            for (Activity a : oi.as) {
                allowed_activities.add(a.id);
            }
            for (User u : oi.us) {
                for (int j = 0; j < activities.size(); j++) {
                    if (!allowed_activities.contains(j)) {
                        s.impose(new XeqC(vars[u.id][j], 0));
                    }
                }
            }
        }

        // we add the exclude-from constraints..
        for (NotIn ef : exclude_froms) {
            for (int i = 0; i < ef.us.length; i++) {
                for (Activity a : ef.as) {
                    s.impose(new XeqC(vars[ef.us[i].id][ef.as[i].id], 0));
                }
            }
        }

        // For each pulse the activities starting at that pulse
        Map<Long, Collection<Activity>> s_acts = new HashMap<>(activities.size());
        // For each pulse the activities ending at that pulse
        Map<Long, Collection<Activity>> e_acts = new HashMap<>(activities.size());
        // The pulses of the timeline
        Set<Long> c_pulses = new HashSet<>(activities.size() * 2);

        for (Activity a : activities) {
            long start = a.ti.lb;
            long end = a.ti.ub;

            if (!s_acts.containsKey(start)) {
                s_acts.put(start, new ArrayList<>(activities.size()));
            }
            s_acts.get(start).add(a);

            if (!e_acts.containsKey(end)) {
                e_acts.put(end, new ArrayList<>(activities.size()));
            }
            e_acts.get(end).add(a);

            c_pulses.add(start);
            c_pulses.add(end);
        }

        // Sort current pulses
        Long[] c_pulses_array = c_pulses.toArray(new Long[c_pulses.size()]);
        Arrays.sort(c_pulses_array);

        // Push values to timeline according to pulses...
        List<Activity> overlapping_activities = new ArrayList<>();

        for (Long p : c_pulses_array) {
            if (overlapping_activities.size() > 1) {
                for (int i = 0; i < users.size(); i++) {
                    BooleanVar[] c_vars = new BooleanVar[overlapping_activities.size()];
                    for (int j = 0; j < overlapping_activities.size(); j++) {
                        c_vars[j] = vars[i][overlapping_activities.get(j).id];
                    }
                    int[] coeffs = new int[c_vars.length];
                    Arrays.fill(coeffs, 1);
                    s.impose(new LinearInt(s, c_vars, coeffs, "<=", 1));
                }
            }
            if (s_acts.containsKey(p)) {
                overlapping_activities.addAll(s_acts.get(p));
            }
            if (e_acts.containsKey(p)) {
                overlapping_activities.removeAll(e_acts.get(p));
            }
        }

        // we add the objective variable..
        IntVar[] all_vars = new IntVar[users.size() * activities.size() + 1];
        int[] all_vals = new int[users.size() * activities.size() + 1];
        for (int i = 0; i < users.size(); i++) {
            System.arraycopy(vars[i], 0, all_vars, i * activities.size(), activities.size());
            System.arraycopy(vals[i], 0, all_vals, i * activities.size(), activities.size());
        }
        obj_var = new IntVar(s, "obj", 0, users.size() * activities.size() * 70);
        all_vars[users.size() * activities.size()] = obj_var;
        all_vals[users.size() * activities.size()] = -1;
        s.impose(new LinearInt(s, all_vars, all_vals, "==", 0));

        return s.consistency();
    }

    @Override
    public boolean solve(Map<User, List<Activity>> solution) {
        IntVar[] all_vars = new IntVar[users.size() * activities.size()];
        for (int i = 0; i < users.size(); i++) {
            System.arraycopy(vars[i], 0, all_vars, i * activities.size(), activities.size());
        }
        Search<IntVar> search = new DepthFirstSearch<>();
        search.setPrintInfo(false);
        SelectChoicePoint<IntVar> scp = new SimpleSelect<>(all_vars, new SmallestDomain(), new IndomainMax<>());
        s.setLevel(s.level + 1);
        if (search.labeling(s, scp)) {
            // we have found a solution..
            solution.clear();
            for (int i = 0; i < users.size(); i++) {
                List<Activity> c_acts = new ArrayList<>();
                for (int j = 0; j < activities.size(); j++) {
                    if (vars[i][j].value() == 1) {
                        c_acts.add(activities.get(j));
                    }
                }
                solution.put(users.get(i), c_acts);
            }
            int c_value = obj_var.value();
            LOG.log(Level.INFO, "Found new solution: {0}", c_value);
            s.removeLevel(s.level);
            s.setLevel(s.level - 1);
            s.impose(new XgtC(obj_var, c_value));
            return true;
        } else {
            // the problem has no solution anymore..
            return false;
        }
    }

    @Override
    public Collection<Explanation> getExplanations() {
        LOG.warning("it was not possible to generate explanations..");
        return Collections.emptyList();
    }
}
