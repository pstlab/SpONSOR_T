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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.chocosolver.solver.ICause;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.explanations.ExplanationEngine;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class ChocoSolver extends ASolver {

    private static final Logger LOG = Logger.getLogger(ChocoSolver.class.getName());
    protected Model model;
    protected BoolVar[][] vars;
    protected int[][] vals;
    protected IntVar[] workload;
    protected IntVar obj_var;
    protected final Map<Constraint, Explanation> expl_map = new HashMap<>();
    protected final Collection<Explanation> expls = new ArrayList<>();

    @Override
    public boolean init() {
        LOG.info("Initializing the problem..");
        expls.clear();
        if (users.size() > activities.size()) {
            LOG.warning("The number of users is greater than the number of activities..");
            return false;
        }

        model = new Model("SpONSOR");

        vars = new BoolVar[users.size()][activities.size()];
        vals = new int[users.size()][activities.size()];
        workload = new IntVar[users.size()];

        // we create the variables..
        for (int i = 0; i < users.size(); i++) {
            for (int j = 0; j < activities.size(); j++) {
                if (is_compatible(activities.get(j), users.get(i))) {
                    vars[i][j] = model.boolVar("b_" + i + "_" + j);
                    vals[i][j] = evaluate(activities.get(j), users.get(i));
                } else {
                    vars[i][j] = model.boolVar(false);
                    vals[i][j] = 0;
                }
            }

            workload[i] = model.intVar("load_" + i, 0, activities.size(), true);
            int[] coeffs = new int[activities.size()];
            Arrays.fill(coeffs, 1);
            model.scalar(vars[i], coeffs, "=", workload[i]).post();
        }

        // every user must do something..
        for (int i = 0; i < users.size(); i++) {
            int[] coeffs = new int[activities.size()];
            Arrays.fill(coeffs, 1);
            Constraint scalar = model.scalar(vars[i], coeffs, ">=", 1);
            expl_map.put(scalar, new IdlerUserExplanation(users.get(i)));
            scalar.post();
        }

        // every activity must be performed by exactly one user..
        for (int j = 0; j < activities.size(); j++) {
            BoolVar[] column = new BoolVar[users.size()];
            for (int i = 0; i < users.size(); i++) {
                column[i] = vars[i][j];
            }
            int[] coeffs = new int[users.size()];
            Arrays.fill(coeffs, 1);
            Constraint scalar = model.scalar(column, coeffs, "=", 1);
            expl_map.put(scalar, new UnassignableActivityExplanation(activities.get(j)));
            scalar.post();
        }

        // we add the at-most-n constraints..
        for (AtMostN at_most_n : at_most_ns) {
            for (User u : at_most_n.us) {
                BoolVar[] c_vars = new BoolVar[at_most_n.as.length];
                for (int j = 0; j < at_most_n.as.length; j++) {
                    c_vars[j] = vars[u.id][at_most_n.as[j].id];
                }
                int[] coeffs = new int[at_most_n.as.length];
                Arrays.fill(coeffs, 1);
                Constraint scalar = model.scalar(c_vars, coeffs, "<=", at_most_n.n);
                expl_map.put(scalar, new LessThanNExplanation(at_most_n, u));
                model.scalar(c_vars, coeffs, "<=", at_most_n.n).post();
            }
        }

        // we add the at-least-one-among-users constraints..
        for (AtLeastOneAmong aloa : at_least_one_among_users) {
            BoolVar[] c_vars = new BoolVar[aloa.as.length * aloa.us.length];
            for (int i = 0; i < aloa.us.length; i++) {
                for (int j = 0; j < aloa.as.length; j++) {
                    c_vars[i * aloa.as.length + j] = vars[aloa.us[i].id][aloa.as[j].id];
                }
            }
            int[] coeffs = new int[c_vars.length];
            Arrays.fill(coeffs, 1);
            Constraint scalar = model.scalar(c_vars, coeffs, ">=", 1);
            expl_map.put(scalar, new AnyAmongExplanation(aloa));
            scalar.post();
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
                        if (!model.addClauseFalse(vars[u.id][j])) {
                            return false;
                        }
                    }
                }
            }
        }

        // we add the exclude-from constraints..
        for (NotIn ef : exclude_froms) {
            for (int i = 0; i < ef.us.length; i++) {
                for (Activity a : ef.as) {
                    if (!model.addClauseFalse(vars[ef.us[i].id][ef.as[i].id])) {
                        return false;
                    }
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
                    BoolVar[] c_vars = new BoolVar[overlapping_activities.size()];
                    for (int j = 0; j < overlapping_activities.size(); j++) {
                        c_vars[j] = vars[i][overlapping_activities.get(j).id];
                    }
                    int[] coeffs = new int[c_vars.length];
                    Arrays.fill(coeffs, 1);
                    Constraint scalar = model.scalar(c_vars, coeffs, "<=", 1);
                    expl_map.put(scalar, new OverlappingActivitiesExplanation(overlapping_activities.toArray(new Activity[overlapping_activities.size()])));
                    scalar.post();
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
        BoolVar[] all_vars = new BoolVar[users.size() * activities.size()];
        int[] all_vals = new int[users.size() * activities.size()];
        for (int i = 0; i < users.size(); i++) {
            System.arraycopy(vars[i], 0, all_vars, i * activities.size(), activities.size());
            System.arraycopy(vals[i], 0, all_vals, i * activities.size(), activities.size());
        }
        obj_var = model.intVar("obj", 0, users.size() * activities.size() * 70, true);
        model.scalar(all_vars, all_vals, "=", obj_var).post();

        model.getSolver().setExplainer(new ExplanationEngine(model, false, true));

        // we perform initial propagation..
        try {
            model.getSolver().propagate();
        } catch (ContradictionException ex) {
            LOG.severe(ex.toString());
            for (ICause c : model.getSolver().getExplainer().explain(ex).getCauses()) {
                if (c instanceof Propagator) {
                    Constraint cnstr = ((Propagator) c).getConstraint();
                    if (expl_map.containsKey(cnstr)) {
                        expls.add(expl_map.get(cnstr));
                    }
                } else {
                    LOG.log(Level.SEVERE, "Unrecognized cause: {0}", c.getClass().getName());
                }
            }
            return false;
        }
        model.setObjective(true, obj_var);
        return true;
    }

    @Override
    public boolean solve(Map<User, List<Activity>> solution) {
        LOG.info("Solving..");
        if (model.getSolver().solve()) {
            // we have found a solution..
            solution.clear();
            for (int i = 0; i < users.size(); i++) {
                List<Activity> c_acts = new ArrayList<>();
                for (int j = 0; j < activities.size(); j++) {
                    if (vars[i][j].getLB() == 1) {
                        c_acts.add(activities.get(j));
                    }
                }
                solution.put(users.get(i), c_acts);
            }
            LOG.log(Level.INFO, "Found new solution: {0}", obj_var.getValue());
            return true;
        } else {
            // the problem has no solution anymore..
            expls.clear();
            ContradictionException ce = model.getSolver().getContradictionException();
            Set<ICause> causes = model.getSolver().getExplainer().explain(ce).getCauses();
            if (causes.isEmpty()) {
                if (ce.c instanceof Propagator) {
                    Constraint cnstr = ((Propagator) ce.c).getConstraint();
                    if (expl_map.containsKey(cnstr)) {
                        expls.add(expl_map.get(cnstr));
                    }
                } else {
                    LOG.log(Level.SEVERE, "Unrecognized cause: {0}", ce.c.getClass().getName());
                }
            } else {
                for (ICause c : causes) {
                    if (c instanceof Propagator) {
                        Constraint cnstr = ((Propagator) c).getConstraint();
                        if (expl_map.containsKey(cnstr)) {
                            expls.add(expl_map.get(cnstr));
                        }
                    } else {
                        LOG.log(Level.SEVERE, "Unrecognized cause: {0}", c.getClass().getName());
                    }
                }
            }
            return false;
        }
    }

    @Override
    public Collection<Explanation> getExplanations() {
        return expls;
    }
}
