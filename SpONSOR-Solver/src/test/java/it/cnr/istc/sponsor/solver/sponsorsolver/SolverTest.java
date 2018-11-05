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

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class SolverTest {

    @Test
    public void test0() {
        ISolver s = SolverFactory.newSolver(SolverType.HeuristicChoco);

        User u0 = s.newUser("u0", new int[]{5, 4, 3, 8, 9, 1, 6, 7}, new Interval[]{new Interval(5, 20), new Interval(30, 40)});
        User u1 = s.newUser("u1", new int[]{8, 6, 7, 4, 8, 21, 32, 7}, new Interval[]{new Interval(2, 8), new Interval(12, 22)});

        Activity a0 = s.newActivity("a0", new boolean[]{true, false, false, true, false, false, true, true}, new Interval(5, 8));
        Activity a1 = s.newActivity("a1", new boolean[]{true, false, false, true, false, false, true, true}, new Interval(7, 10));

        boolean init = s.init();
        Assert.assertTrue(init);

        Map<User, List<Activity>> sol = new HashMap<>();
        boolean solve = s.solve(sol);
        Assert.assertTrue(solve);
    }

    @Test
    public void test1() {
        ISolver s = SolverFactory.newSolver(SolverType.HeuristicChoco);

        User u0 = s.newUser("u0", new int[]{5, 4, 3, 8, 9, 1, 6, 7}, new Interval[]{new Interval(5, 20), new Interval(30, 40)});
        User u1 = s.newUser("u1", new int[]{8, 6, 7, 4, 8, 21, 32, 7}, new Interval[]{new Interval(2, 8), new Interval(12, 22)});
        User u2 = s.newUser("u2", new int[]{8, 6, 7, 4, 8, 21, 32, 7}, new Interval[]{new Interval(2, 8), new Interval(12, 22)});

        Activity a0 = s.newActivity("a0", new boolean[]{true, false, false, true, false, false, true, true}, new Interval(5, 8));
        Activity a1 = s.newActivity("a1", new boolean[]{true, false, false, true, false, false, true, true}, new Interval(7, 10));

        boolean init = s.init();
        Assert.assertFalse("The number of users is greater than the number of activities..", init);
    }

    @Test
    public void test2() {
        ISolver s = SolverFactory.newSolver(SolverType.HeuristicChoco);

        User u0 = s.newUser("u0", new int[]{5, 4, 3, 8, 9, 1, 6, 7}, new Interval[]{new Interval(5, 20), new Interval(30, 40)});
        User u1 = s.newUser("u1", new int[]{8, 6, 7, 4, 8, 21, 32, 7}, new Interval[]{new Interval(2, 8), new Interval(12, 22)});
        User u2 = s.newUser("u2", new int[]{7, 2, 8, 6, 4, 1, 8, 3}, new Interval[]{new Interval(3, 8), new Interval(12, 22)});

        Activity a0 = s.newActivity("a0", new boolean[]{true, false, false, true, false, false, true, true}, new Interval(5, 8));
        Activity a1 = s.newActivity("a1", new boolean[]{true, false, false, false, true, true, true, true}, new Interval(7, 10));
        Activity a2 = s.newActivity("a2", new boolean[]{false, false, true, true, false, false, true, true}, new Interval(15, 20));
        Activity a3 = s.newActivity("a3", new boolean[]{true, false, false, true, true, false, true, true}, new Interval(15, 20));

        boolean init = s.init();
        Assert.assertTrue(init);

        Map<User, List<Activity>> sol = new HashMap<>();
        boolean solve = s.solve(sol);
        Assert.assertTrue(solve);
    }

    @Test
    public void testBiggerProblem0() {
        int n_users = 100;
        int n_activities = 100;

        ISolver s = SolverFactory.newSolver(SolverType.HeuristicChoco);

        for (int i = 0; i < n_users; i++) {
            s.newUser("u" + i, new int[]{2, 3, 5}, new Interval[]{new Interval(0, 100)});
        }
        for (int i = 0; i < n_activities; i++) {
            s.newActivity("a" + i, new boolean[]{true, false, true}, new Interval(0, 100));
        }

        boolean init = s.init();
        Assert.assertTrue(init);

        Map<User, List<Activity>> sol = new HashMap<>();
        // we find a first solution..
        boolean solve = s.solve(sol);
        Assert.assertTrue(solve);
    }

    @Test
    public void testAtMostN() {
        ISolver s = SolverFactory.newSolver(SolverType.HeuristicChoco);

        User u0 = s.newUser("u0", new int[]{5, 7, 3}, new Interval[]{new Interval(5, 20), new Interval(30, 40)});
        User u1 = s.newUser("u1", new int[]{8, 6, 7}, new Interval[]{new Interval(2, 8), new Interval(12, 22)});
        User u2 = s.newUser("u2", new int[]{7, 2, 8}, new Interval[]{new Interval(3, 8), new Interval(12, 22)});

        Activity a0 = s.newActivity("a0", new boolean[]{true, false, false}, new Interval(5, 8));
        Activity a1 = s.newActivity("a1", new boolean[]{true, false, true}, new Interval(7, 10));
        Activity a2 = s.newActivity("a2", new boolean[]{false, true, true}, new Interval(15, 20));
        Activity a3 = s.newActivity("a3", new boolean[]{true, true, false}, new Interval(15, 20));

        boolean init = s.init();
        Assert.assertTrue(init);

        Map<User, List<Activity>> sol = new HashMap<>();
        boolean solve = s.solve(sol);
        Assert.assertTrue(solve);

        AtMostN amn = s.at_most_n(new Activity[]{a0, a3}, 1);

        init = s.init();
        Assert.assertTrue(init);

        solve = s.solve(sol);
        Assert.assertTrue(solve);
    }

    @Test
    public void testAtLeastOneOf() {
        ISolver s = SolverFactory.newSolver(SolverType.HeuristicChoco);

        User u0 = s.newUser("u0", new int[]{5, 7, 3}, new Interval[]{new Interval(5, 20), new Interval(30, 40)});
        User u1 = s.newUser("u1", new int[]{8, 6, 7}, new Interval[]{new Interval(2, 8), new Interval(12, 22)});
        User u2 = s.newUser("u2", new int[]{7, 2, 8}, new Interval[]{new Interval(3, 8), new Interval(12, 22)});

        Activity a0 = s.newActivity("a0", new boolean[]{true, false, false}, new Interval(5, 8));
        Activity a1 = s.newActivity("a1", new boolean[]{true, false, true}, new Interval(7, 10));
        Activity a2 = s.newActivity("a2", new boolean[]{false, true, true}, new Interval(15, 20));
        Activity a3 = s.newActivity("a3", new boolean[]{true, true, false}, new Interval(15, 20));

        boolean init = s.init();
        Assert.assertTrue(init);

        Map<User, List<Activity>> sol = new HashMap<>();
        boolean solve = s.solve(sol);
        Assert.assertTrue(solve);

        AtLeastOneAmong aloa = s.at_least_one_of(new Activity[]{a2, a3}, new User[]{u0});

        init = s.init();
        Assert.assertTrue(init);

        solve = s.solve(sol);
        Assert.assertTrue(solve);
    }

    @Test
    public void testAtLeastOneOf2() {
        ISolver s = SolverFactory.newSolver(SolverType.HeuristicChoco);

        User u0 = s.newUser("u0", new int[]{5, 7, 3}, new Interval[]{new Interval(5, 20), new Interval(30, 40)});
        User u1 = s.newUser("u1", new int[]{8, 6, 7}, new Interval[]{new Interval(2, 10), new Interval(12, 22)});
        User u2 = s.newUser("u2", new int[]{7, 2, 8}, new Interval[]{new Interval(3, 8), new Interval(12, 22)});
        User u3 = s.newUser("u3", new int[]{8, 6, 7}, new Interval[]{new Interval(2, 10), new Interval(12, 22)});
        User u4 = s.newUser("u4", new int[]{7, 2, 8}, new Interval[]{new Interval(3, 8), new Interval(12, 22)});
        User u5 = s.newUser("u5", new int[]{8, 6, 7}, new Interval[]{new Interval(2, 10), new Interval(12, 22)});
        User u6 = s.newUser("u6", new int[]{7, 2, 8}, new Interval[]{new Interval(3, 8), new Interval(12, 22)});

        Activity a0 = s.newActivity("a0", new boolean[]{true, false, false}, new Interval(5, 8));
        Activity a1 = s.newActivity("a1", new boolean[]{true, false, true}, new Interval(7, 10));
        Activity a2 = s.newActivity("a2", new boolean[]{false, true, true}, new Interval(15, 20));
        Activity a3 = s.newActivity("a3", new boolean[]{true, true, false}, new Interval(15, 20));
        Activity a4 = s.newActivity("a4", new boolean[]{true, false, true}, new Interval(7, 10));
        Activity a5 = s.newActivity("a5", new boolean[]{false, true, true}, new Interval(15, 20));
        Activity a6 = s.newActivity("a6", new boolean[]{true, true, false}, new Interval(15, 20));

        boolean init = s.init();
        Assert.assertTrue(init);

        Map<User, List<Activity>> sol = new HashMap<>();
        boolean solve = s.solve(sol);
        Assert.assertTrue(solve);

        AtLeastOneAmong aloa1 = s.at_least_one_of(new Activity[]{a2, a3}, new User[]{u0});
        AtLeastOneAmong aloa2 = s.at_least_one_of(new Activity[]{a0, a1}, new User[]{u6});

        init = s.init();
        Assert.assertTrue(init);

        solve = s.solve(sol);
        Assert.assertTrue(solve);
    }

    @Test
    public void testOnlyIn() {
        ISolver s = SolverFactory.newSolver(SolverType.HeuristicChoco);

        User u0 = s.newUser("u0", new int[]{5, 7, 3}, new Interval[]{new Interval(5, 20), new Interval(30, 40)});
        User u1 = s.newUser("u1", new int[]{8, 6, 7}, new Interval[]{new Interval(2, 8), new Interval(12, 22)});
        User u2 = s.newUser("u2", new int[]{7, 2, 8}, new Interval[]{new Interval(3, 8), new Interval(12, 22)});

        Activity a0 = s.newActivity("a0", new boolean[]{true, false, false}, new Interval(5, 8));
        Activity a1 = s.newActivity("a1", new boolean[]{true, false, true}, new Interval(7, 10));
        Activity a2 = s.newActivity("a2", new boolean[]{false, true, true}, new Interval(15, 20));
        Activity a3 = s.newActivity("a3", new boolean[]{true, true, false}, new Interval(15, 20));

        boolean init = s.init();
        Assert.assertTrue(init);

        Map<User, List<Activity>> sol = new HashMap<>();
        boolean solve = s.solve(sol);
        Assert.assertTrue(solve);

        OnlyIn oi = s.only_in(new User[]{u2}, new Activity[]{a0});

        init = s.init();
        Assert.assertTrue(init);

        solve = s.solve(sol);
        Assert.assertTrue(solve);
    }

    @Test
    public void testNotIn() {
        ISolver s = SolverFactory.newSolver(SolverType.HeuristicChoco);

        User u0 = s.newUser("u0", new int[]{5, 7, 3}, new Interval[]{new Interval(5, 20), new Interval(30, 40)});
        User u1 = s.newUser("u1", new int[]{8, 6, 7}, new Interval[]{new Interval(2, 8), new Interval(12, 22)});
        User u2 = s.newUser("u2", new int[]{7, 2, 8}, new Interval[]{new Interval(3, 8), new Interval(12, 22)});

        Activity a0 = s.newActivity("a0", new boolean[]{true, false, false}, new Interval(5, 8));
        Activity a1 = s.newActivity("a1", new boolean[]{true, false, true}, new Interval(7, 10));
        Activity a2 = s.newActivity("a2", new boolean[]{false, true, true}, new Interval(15, 20));
        Activity a3 = s.newActivity("a3", new boolean[]{true, true, false}, new Interval(15, 20));

        boolean init = s.init();
        Assert.assertTrue(init);

        Map<User, List<Activity>> sol = new HashMap<>();
        boolean solve = s.solve(sol);
        Assert.assertTrue(solve);

        NotIn ni = s.not_in(new User[]{u2}, new Activity[]{a2});

        init = s.init();
        Assert.assertTrue(init);

        solve = s.solve(sol);
        Assert.assertTrue(solve);
    }

    @Test
    public void testExplanation0() {
        ISolver s = SolverFactory.newSolver(SolverType.HeuristicChoco);

        User u0 = s.newUser("u0", new int[]{5, 7, 3}, new Interval[]{new Interval(5, 20), new Interval(30, 40)});
        Activity a0 = s.newActivity("a0", new boolean[]{true, false, false}, new Interval(20, 30));

        boolean init = s.init();
        Assert.assertFalse(init);
    }

    @Test
    public void testReadWrite() {
        ISolver s = SolverFactory.newSolver(SolverType.HeuristicChoco);

        try {
            s.read(Paths.get(SolverTest.class.getResource("sponsor_benchmark_0.json").toURI()));
            s.write(Paths.get(SolverTest.class.getResource("sponsor_benchmark_0.json").toURI()));
        } catch (IOException | URISyntaxException ex) {
            Assert.fail();
        }
    }

    @Test
    public void testBenchmark0ChocoFirstSolution() {
        ISolver s = SolverFactory.newSolver(SolverType.Choco);

        try {
            s.read(Paths.get(SolverTest.class.getResource("sponsor_benchmark_0.json").toURI()));
        } catch (IOException | URISyntaxException ex) {
            Assert.fail();
        }

        boolean init = s.init();
        Assert.assertTrue(init);

        Map<User, List<Activity>> sol = new HashMap<>();
        boolean solve = s.solve(sol);
        Assert.assertTrue(solve);
    }

    @Test
    public void testBenchmark0HeuristicChocoFirstSolution() {
        ISolver s = SolverFactory.newSolver(SolverType.HeuristicChoco);

        try {
            s.read(Paths.get(SolverTest.class.getResource("sponsor_benchmark_0.json").toURI()));
        } catch (IOException | URISyntaxException ex) {
            Assert.fail();
        }

        boolean init = s.init();
        Assert.assertTrue(init);

        Map<User, List<Activity>> sol = new HashMap<>();
        boolean solve = s.solve(sol);
        Assert.assertTrue(solve);
    }

    @Test
    public void testBenchmark0ChocoOptimalSolution() {
        ISolver s = SolverFactory.newSolver(SolverType.Choco);

        try {
            s.read(Paths.get(SolverTest.class.getResource("sponsor_benchmark_0.json").toURI()));
        } catch (IOException | URISyntaxException ex) {
            Assert.fail();
        }

        boolean init = s.init();
        Assert.assertTrue(init);

        Map<User, List<Activity>> sol = new HashMap<>();
        while (s.solve(sol)) {
        }
    }

    @Test
    public void testBenchmark0HeuristicChocoOptimalSolution() {
        ISolver s = SolverFactory.newSolver(SolverType.HeuristicChoco);

        try {
            s.read(Paths.get(SolverTest.class.getResource("sponsor_benchmark_0.json").toURI()));
        } catch (IOException | URISyntaxException ex) {
            Assert.fail();
        }

        boolean init = s.init();
        Assert.assertTrue(init);

        Map<User, List<Activity>> sol = new HashMap<>();
        while (s.solve(sol)) {
        }
    }

    @Test
    public void testZ3Opt() {
        ISolver s = SolverFactory.newSolver(SolverType.Z3Opt);
        try {
            s.read(Paths.get(SolverTest.class.getResource("sponsor_benchmark_0.json").toURI()));
        } catch (IOException | URISyntaxException ex) {
            Assert.fail();
        }

        boolean init = s.init();
        Assert.assertTrue(init);

        Map<User, List<Activity>> sol = new HashMap<>();
        boolean solve = s.solve(sol);
        Assert.assertTrue(solve);
    }

    @Test
    public void testZ3Incremental() {
        ISolver s = SolverFactory.newSolver(SolverType.Z3Incremental);
        try {
            s.read(Paths.get(SolverTest.class.getResource("sponsor_benchmark_0.json").toURI()));
        } catch (IOException | URISyntaxException ex) {
            Assert.fail();
        }

        boolean init = s.init();
        Assert.assertTrue(init);

        Map<User, List<Activity>> sol = new HashMap<>();
        boolean solve = s.solve(sol);
        Assert.assertTrue(solve);
    }

    @Test
    public void testUnsatisfableALOA() {
        ISolver s = SolverFactory.newSolver(SolverType.HeuristicChoco);

        User u0 = s.newUser("u0", new int[]{5, 7, 3}, new Interval[]{new Interval(5, 20), new Interval(30, 40)});
        User u1 = s.newUser("u1", new int[]{8, 6, 7}, new Interval[]{new Interval(2, 8), new Interval(12, 22)});
        User u2 = s.newUser("u2", new int[]{5, 7, 3}, new Interval[]{new Interval(5, 20), new Interval(30, 40)});
        User u3 = s.newUser("u3", new int[]{8, 6, 7}, new Interval[]{new Interval(2, 8), new Interval(12, 22)});

        Activity a0 = s.newActivity("a0", new boolean[]{true, false, false}, new Interval(5, 8));
        Activity a1 = s.newActivity("a1", new boolean[]{true, false, false}, new Interval(5, 8));
        Activity a2 = s.newActivity("a2", new boolean[]{true, false, false}, new Interval(5, 8));
        Activity a3 = s.newActivity("a3", new boolean[]{true, false, false}, new Interval(5, 8));

        AtLeastOneAmong aloa0 = s.at_least_one_of(new Activity[]{a0}, new User[]{u0, u1});
        AtLeastOneAmong aloa1 = s.at_least_one_of(new Activity[]{a0}, new User[]{u2, u3});

        boolean init = s.init();
        Assert.assertTrue(init);

        Map<User, List<Activity>> sol = new HashMap<>();
        boolean solve = s.solve(sol);
        Assert.assertFalse(solve);

        Collection<Explanation> explanations = s.getExplanations();
    }

    @Test
    public void testJaCoP() {
        ISolver s = SolverFactory.newSolver(SolverType.JaCoP);
        try {
            s.read(Paths.get(SolverTest.class.getResource("sponsor_benchmark_0.json").toURI()));
        } catch (IOException | URISyntaxException ex) {
            Assert.fail();
        }

        boolean init = s.init();
        Assert.assertTrue(init);

        Map<User, List<Activity>> sol = new HashMap<>();
        boolean solve = s.solve(sol);
        Assert.assertTrue(solve);
    }

    @Test
    public void testJaCoPOpt() {
        ISolver s = SolverFactory.newSolver(SolverType.JaCoP);
        try {
            s.read(Paths.get(SolverTest.class.getResource("sponsor_benchmark_0.json").toURI()));
        } catch (IOException | URISyntaxException ex) {
            Assert.fail();
        }

        boolean init = s.init();
        Assert.assertTrue(init);

        Map<User, List<Activity>> sol = new HashMap<>();
        while (s.solve(sol)) {
        }
    }

    @Test
    public void testHeuristicJaCoP() {
        ISolver s = SolverFactory.newSolver(SolverType.HeuristicJaCoP);
        try {
            s.read(Paths.get(SolverTest.class.getResource("sponsor_benchmark_0.json").toURI()));
        } catch (IOException | URISyntaxException ex) {
            Assert.fail();
        }

        boolean init = s.init();
        Assert.assertTrue(init);

        Map<User, List<Activity>> sol = new HashMap<>();
        boolean solve = s.solve(sol);
        Assert.assertTrue(solve);
    }

    @Test
    public void testBiggerProblemJaCoP() {
        int n_users = 400;
        int n_activities = 400;

        ISolver s = SolverFactory.newSolver(SolverType.HeuristicJaCoP);

        for (int i = 0; i < n_users; i++) {
            s.newUser("u" + i, new int[]{2, 3, 5}, new Interval[]{new Interval(0, 100)});
        }
        for (int i = 0; i < n_activities; i++) {
            s.newActivity("a" + i, new boolean[]{true, false, true}, new Interval(0, 100));
        }

        boolean init = s.init();
        Assert.assertTrue(init);

        Map<User, List<Activity>> sol = new HashMap<>();
        // we find a first solution..
        boolean solve = s.solve(sol);
        Assert.assertTrue(solve);
    }

    @Test
    public void testCplex() {
        ISolver s = SolverFactory.newSolver(SolverType.IloCplex);
        try {
            s.read(Paths.get(SolverTest.class.getResource("sponsor_benchmark_9.json").toURI()));
        } catch (IOException | URISyntaxException ex) {
            Assert.fail();
        }

        boolean init = s.init();
        Assert.assertTrue(init);

        Map<User, List<Activity>> sol = new HashMap<>();
        boolean solve = s.solve(sol);
        Assert.assertTrue(solve);
    }
}
