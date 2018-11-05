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

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jacop.constraints.PrimitiveConstraint;
import org.jacop.constraints.XgtC;
import org.jacop.core.IntVar;
import org.jacop.search.DepthFirstSearch;
import org.jacop.search.Search;
import org.jacop.search.SelectChoicePoint;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class HeuristicJaCoPSolver extends JaCoPSolver {

    private static final Logger LOG = Logger.getLogger(HeuristicJaCoPSolver.class.getName());

    @Override
    public boolean solve(Map<User, List<Activity>> solution) {
        IntVar[] all_vars = new IntVar[users.size() * activities.size()];
        for (int i = 0; i < users.size(); i++) {
            System.arraycopy(vars[i], 0, all_vars, i * activities.size(), activities.size());
        }
        Search<IntVar> search = new DepthFirstSearch<>();
        search.setPrintInfo(false);
        s.setLevel(s.level + 1);
        if (search.labeling(s, new JaCoPHeuristic(all_vars))) {
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

    private class JaCoPHeuristic implements SelectChoicePoint<IntVar> {

        final IntVar[] all_vars;
        final IdentityHashMap<IntVar, Integer> position;
        private IntVar c_var;

        JaCoPHeuristic(IntVar[] all_vars) {
            this.all_vars = all_vars;
            this.position = new IdentityHashMap<>(all_vars.length);
            for (int i = 0; i < all_vars.length; i++) {
                position.put(all_vars[i], i);
            }
        }

        @Override
        public IntVar getChoiceVariable(int index) {
            // these constraints can create problems: we give priority to them..
            for (AtLeastOneAmong aloa : at_least_one_among_users) {
                boolean is_aloa = false;
                for (User u : aloa.us) {
                    for (Activity a : aloa.as) {
                        if (vars[u.id][a.id].min() == 1) {
                            is_aloa = true;
                        }
                    }
                }
                if (!is_aloa) {
                    for (User u : aloa.us) {
                        for (Activity a : aloa.as) {
                            if (vars[u.id][a.id].max() == 1) {
                                c_var = vars[u.id][a.id];
                                return c_var;
                            }
                        }
                    }
                }
            }

            // we select the least loaded user..
            int best_load = Integer.MAX_VALUE;
            int least_loaded_user = -1;
            for (int i = 0; i < users.size(); i++) {
                if (workload[i].min() < best_load && !workload[i].singleton()) {
                    best_load = workload[i].min();
                    least_loaded_user = i;
                }
            }
            if (least_loaded_user == -1) {
                return null;
            }

            // we select the best activity for the selected user..
            int best_value = Integer.MIN_VALUE;
            int best_activity = -1;
            for (int i = 0; i < activities.size(); i++) {
                if (vals[least_loaded_user][i] > best_value && !vars[least_loaded_user][i].singleton()) {
                    best_value = vals[least_loaded_user][i];
                    best_activity = i;
                }
            }
            assert (best_activity != -1);

            // we return the variable representing the best activity for the least loaded user..
            c_var = vars[least_loaded_user][best_activity];
            return c_var;
        }

        @Override
        public int getChoiceValue() {
            assert (!c_var.singleton());
            return 1;
        }

        @Override
        public PrimitiveConstraint getChoiceConstraint(int index) {
            return null;
        }

        @Override
        public IdentityHashMap<IntVar, Integer> getVariablesMapping() {
            return position;
        }

        @Override
        public int getIndex() {
            return position.get(c_var);
        }
    }
}
