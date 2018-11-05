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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public interface ISolver {

    /**
     * Reads a problem from the given path.
     *
     * @param path the path on which the problem is located.
     * @throws FileNotFoundException
     */
    public void read(Path path) throws IOException;

    /**
     * Writes a problem on the given path.
     *
     * @param path the path on which the problem is written.
     * @throws IOException
     */
    public void write(Path path) throws IOException;

    /**
     * Returns some statistics about the current problem.
     *
     * @return some statistics about the current problem.
     */
    public Statistics getStatistics();

    /**
     * Creates a new User instance given a name, a set of parameters and a set
     * of temporal availabilities.
     *
     * @param name the name of the user.
     * @param pars an array of ints representing the profile of the user.
     * @param ta an array of intervals representing the temporal availabilities
     * of the user.
     * @return the new User instance.
     */
    public User newUser(String name, int[] pars, Interval[] ta);

    /**
     * Creates a new Activity instance given a name, a set of required
     * parameters and an interval representing the activity's temporal span.
     *
     * @param name the name of the activity.
     * @param pars an array of boolean parameters representing the required
     * skills for performing the activity.
     * @param ti a temporal interval representing the temporal span of the
     * activity.
     * @return the new activity.
     */
    public Activity newActivity(String name, boolean[] pars, Interval ti);

    /**
     * Enforces a constraint stating that each of the users can perform at most
     * {@code n} of the given activities. This can be used, for example, to
     * forbid that a user works for three Sundays in a quarter.
     *
     * @param as an array of activities.
     * @param n the maximum number of the allowed activities.
     * @return an instance representing the new constraint.
     */
    public AtMostN at_most_n(Activity[] as, int n);

    /**
     * Enforces a constraint stating that the user can perform at most {@code n}
     * of the given activities.
     *
     * @param u the user that can perform at most {@code n} of the given
     * activities.
     * @param as an array of activities.
     * @param n the maximum number of the allowed activities.
     * @return an instance representing the new constraint.
     */
    public AtMostN at_most_n(User u, Activity[] as, int n);

    /**
     * Removes the given constraint from the set of constraints.
     *
     * @param amn the constraint to remove.
     */
    public void remove(AtMostN amn);

    /**
     * Enforces a constraint stating that the given activities can be performed
     * only by the given users. In other words, the assignments to one of the
     * given activities is allowed only among the given users.
     *
     * @param as an array of activities.
     * @param us an array of users.
     * @return an instance representing the new constraint.
     */
    public AtLeastOneAmong at_least_one_of(Activity[] as, User[] us);

    /**
     * Removes the given constraint from the set of constraints.
     *
     * @param aloa the constraint to remove.
     */
    public void remove(AtLeastOneAmong aloa);

    /**
     * Enforces a constraint stating that the given users can perform only the
     * given activities. In other words, other activities cannot be performed by
     * these users.
     *
     * @param us an array of users.
     * @param as an array of activities.
     * @return an instance representing the new constraint.
     */
    public OnlyIn only_in(User[] us, Activity[] as);

    /**
     * Removes the given constraint from the set of constraints.
     *
     * @param oa the constraint to remove.
     */
    public void remove(OnlyIn oa);

    /**
     * Enforces a constraint excluding the given users from performing the given
     * set of activities. In other words, the these users cannot perform these
     * activities.
     *
     * @param us an array of users.
     * @param as an array of activities.
     * @return an instance representing the new constraint.
     */
    public NotIn not_in(User[] us, Activity[] as);

    /**
     * Removes the given constraint from the set of constraints.
     *
     * @param ef the constraint to remove.
     */
    public void remove(NotIn ef);

    /**
     * Initializes the current assignment problem, creating the internal
     * variables, and returns a boolean representing the consistency of the
     * initial problem.
     *
     * @return {@code true} if the initial problem is consistent and
     * {@code false} otherwise.
     */
    public boolean init();

    /**
     * Solves the initialized problem and returns {@code true} if a solution is
     * found. This method can be called multiple times returning, if possible, a
     * solution better than before.
     *
     * @param solution a {@code Map} which will be filled with the solution if a
     * solution is found.
     * @return {@code true} if a solution is found and {@code false} otherwise.
     */
    public boolean solve(Map<User, List<Activity>> solution);

    /**
     * Returns a collection of explanations for the unsatisfiability. This
     * method could be called either after {@link ISolver#init()} returned
     * {@code false} or after {@link ISolver#init()} returned {@code false}.
     *
     * @return a collection of explanations for the unsatisfiability.
     */
    public Collection<Explanation> getExplanations();
}
