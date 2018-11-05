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

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.IntNum;
import com.microsoft.z3.Model;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;
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

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class Z3IncrementalSolver extends ASolver {

    private static final Logger LOG = Logger.getLogger(Z3IncrementalSolver.class.getName());
    private Context ctx;
    private Solver s;
    private ArithExpr obj;
    private IntExpr[][] vars;
    protected int[][] vals;

    @Override
    public boolean init() {
        LOG.info("Initializing the problem..");
        if (users.size() > activities.size()) {
            return false;
        }
        HashMap<String, String> cfg = new HashMap<>();
        cfg.put("model", "true");
        ctx = new Context(cfg);
        s = ctx.mkSolver();

        vars = new IntExpr[users.size()][activities.size()];
        vals = new int[users.size()][activities.size()];

        for (int i = 0; i < users.size(); i++) {
            for (int j = 0; j < activities.size(); j++) {
                if (is_compatible(activities.get(j), users.get(i))) {
                    vars[i][j] = ctx.mkIntConst("b_" + i + "_" + j);
                    s.add(ctx.mkLe(ctx.mkInt("0"), vars[i][j]), ctx.mkLe(vars[i][j], ctx.mkInt("1")));
                    vals[i][j] = evaluate(activities.get(j), users.get(i));
                } else {
                    vars[i][j] = ctx.mkInt("0");
                    vals[i][j] = 0;
                }
            }
        }

        // every user must do something..
        for (int i = 0; i < users.size(); i++) {
            int[] coeffs = new int[activities.size()];
            Arrays.fill(coeffs, 1);
            s.add(ctx.mkGe(ctx.mkAdd(vars[i]), ctx.mkInt("1")));
        }

        // every activity must be performed by exactly one user..
        for (int j = 0; j < activities.size(); j++) {
            IntExpr[] column = new IntExpr[users.size()];
            for (int i = 0; i < users.size(); i++) {
                column[i] = vars[i][j];
            }
            s.add(ctx.mkEq(ctx.mkAdd(column), ctx.mkInt("1")));
        }

        // we add the at-most-n constraints..
        for (AtMostN at_most_n : at_most_ns) {
            for (User u : at_most_n.us) {
                IntExpr[] c_vars = new IntExpr[at_most_n.as.length];
                for (int j = 0; j < at_most_n.as.length; j++) {
                    c_vars[j] = vars[u.id][at_most_n.as[j].id];
                }
                s.add(ctx.mkLe(ctx.mkAdd(c_vars), ctx.mkInt("1")));
            }
        }

        // we add the at-least-one-among-users constraints..
        for (AtLeastOneAmong aloa : at_least_one_among_users) {
            IntExpr[] c_vars = new IntExpr[aloa.as.length * aloa.us.length];
            for (int i = 0; i < aloa.us.length; i++) {
                for (int j = 0; j < aloa.as.length; j++) {
                    c_vars[i * aloa.as.length + j] = vars[aloa.us[i].id][aloa.as[j].id];
                }
            }
            s.add(ctx.mkGe(ctx.mkAdd(c_vars), ctx.mkInt("1")));
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
                        s.add(ctx.mkEq(vars[u.id][j], ctx.mkInt("0")));
                    }
                }
            }
        }

        // we add the exclude-from constraints..
        for (NotIn ef : exclude_froms) {
            for (int i = 0; i < ef.us.length; i++) {
                for (Activity a : ef.as) {
                    s.add(ctx.mkEq(vars[ef.us[i].id][ef.as[i].id], ctx.mkInt("0")));
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
                    IntExpr[] c_vars = new IntExpr[overlapping_activities.size()];
                    for (int j = 0; j < overlapping_activities.size(); j++) {
                        c_vars[j] = vars[i][overlapping_activities.get(j).id];
                    }
                    s.add(ctx.mkLe(ctx.mkAdd(c_vars), ctx.mkInt("1")));
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
        IntExpr[] all_vars = new IntExpr[users.size() * activities.size()];
        int[] all_vals = new int[users.size() * activities.size()];
        for (int i = 0; i < users.size(); i++) {
            System.arraycopy(vars[i], 0, all_vars, i * activities.size(), activities.size());
            System.arraycopy(vals[i], 0, all_vals, i * activities.size(), activities.size());
        }
        ArithExpr[] all_exprs = new ArithExpr[users.size() * activities.size()];
        for (int i = 0; i < all_exprs.length; i++) {
            all_exprs[i] = ctx.mkMul(all_vars[i], ctx.mkInt(Integer.toString(all_vals[i])));
        }
        obj = ctx.mkAdd(all_exprs);

        return true;
    }

    @Override
    public boolean solve(Map<User, List<Activity>> solution) {
        LOG.info("Solving..");
        Status check = s.check();
        switch (check) {
            case UNSATISFIABLE:
                return false;
            case SATISFIABLE:
                Model m = s.getModel();
                IntNum obj_value = (IntNum) m.eval(obj, true);
                LOG.log(Level.INFO, "We have found a new solution: {0}", obj_value.toString());
                // we have found a solution..
                solution.clear();
                for (int i = 0; i < users.size(); i++) {
                    List<Activity> c_acts = new ArrayList<>();
                    for (int j = 0; j < activities.size(); j++) {
                        IntNum num = (IntNum) m.eval(vars[i][j], true);
                        if (num.getInt() == 1) {
                            c_acts.add(activities.get(j));
                        }
                    }
                    solution.put(users.get(i), c_acts);
                }
                s.add(ctx.mkGt(obj, obj_value));
                return true;
            default:
                throw new AssertionError(check.name());
        }
    }

    @Override
    public Collection<Explanation> getExplanations() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
