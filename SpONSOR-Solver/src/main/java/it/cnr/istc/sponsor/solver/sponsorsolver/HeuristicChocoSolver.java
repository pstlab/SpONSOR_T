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

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.search.strategy.selectors.values.IntDomainMax;
import org.chocosolver.solver.variables.IntVar;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class HeuristicChocoSolver extends ChocoSolver {

    private static final Logger LOG = Logger.getLogger(HeuristicChocoSolver.class.getName());

    @Override
    public boolean solve(Map<User, List<Activity>> solution) {
        model.getSolver().setSearch(Search.intVarSearch((IntVar[] variables) -> {
            // these constraints can create problems: we give priority to them..
            for (AtLeastOneAmong aloa : at_least_one_among_users) {
                boolean is_aloa = false;
                for (User u : aloa.us) {
                    for (Activity a : aloa.as) {
                        if (vars[u.id][a.id].getLB() == 1) {
                            is_aloa = true;
                        }
                    }
                }
                if (!is_aloa) {
                    for (User u : aloa.us) {
                        for (Activity a : aloa.as) {
                            if (vars[u.id][a.id].getUB() == 1) {
                                return vars[u.id][a.id];
                            }
                        }
                    }
                }
            }

            // we select the least loaded user..
            int best_load = Integer.MAX_VALUE;
            int least_loaded_user = -1;
            for (int i = 0; i < users.size(); i++) {
                if (workload[i].getLB() < best_load && workload[i].getDomainSize() > 1) {
                    best_load = workload[i].getLB();
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
                if (vals[least_loaded_user][i] > best_value && vars[least_loaded_user][i].getDomainSize() > 1) {
                    best_value = vals[least_loaded_user][i];
                    best_activity = i;
                }
            }
            assert (best_activity != -1);

            // we return the variable representing the best activity for the least loaded user..
            return vars[least_loaded_user][best_activity];
        }, new IntDomainMax()));
        return super.solve(solution);
    }
}
