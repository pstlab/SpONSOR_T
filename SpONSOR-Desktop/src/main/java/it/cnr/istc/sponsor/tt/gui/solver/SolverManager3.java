/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.solver;

import it.cnr.istc.sponsor.tt.gui.engine.GuiEventListener;
import it.cnr.istc.sponsor.tt.logic.TrainerManager;
import it.cnr.istc.sponsor.tt.logic.model.Activity;
import it.cnr.istc.sponsor.tt.logic.model.ActivityTurn;
import it.cnr.istc.sponsor.tt.logic.model.ComfirmedTurn;
import it.cnr.istc.sponsor.tt.logic.model.FreeTimeToken;
import it.cnr.istc.sponsor.tt.logic.model.Job;
import it.cnr.istc.sponsor.tt.logic.model.JobTurn;
import it.cnr.istc.sponsor.tt.logic.model.Keyword;
import it.cnr.istc.sponsor.tt.logic.model.Person;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class SolverManager3 implements GuiEventListener {

    private static SolverManager3 _instance = null;
    private List<SolverListener> solverListeners = new ArrayList<>();
    private boolean keywordForced = true;
    private List<Activity> activityOnePerWeekIDs = new ArrayList<>();
//    private SolverThread solverThread = null;
    private Solution record = null;
    private Thread thread = null;
    private Model model = null;
    private List<ActivityTurn> allTurns = new ArrayList<>();
    private BoolVar[][] solutionMatrix;
    private Map<String, BoolVar> directMap = new HashMap<>();
    private Map<Person, Integer> peopleIdMap = new IdentityHashMap<>();
    private Map<JobTurn, Integer> jobsIdMap = new IdentityHashMap<>();
    private int currentValue = 0;
    private boolean allDoingSomething = true;
    private boolean allJobsMustBeDone = true;
    private boolean funzioneObiettivoActive = false;

    public static SolverManager3 getInstance() {
        if (_instance == null) {
            _instance = new SolverManager3();
            return _instance;
        } else {
            return _instance;
        }
    }

    private SolverManager3() {
        super();
    }

    public void setFunzioneObiettivoActive(boolean funzioneObiettivoActive) {
        this.funzioneObiettivoActive = funzioneObiettivoActive;
    }

    public boolean isFunzioneObiettivoActive() {
        return funzioneObiettivoActive;
    }

    public void setAllDoingSomething(boolean allDoingSomething) {
        this.allDoingSomething = allDoingSomething;
    }

    public boolean isAllDoingSomething() {
        return allDoingSomething;
    }

    public void showTemporarySolution() {
        showSolution();
    }

    public void stop() {
        if (thread != null) {
            thread.interrupt();
        }
        showSolution();
        //this.solverThread.stop();
    }

    public void addWeekActivity(Activity activity) {
        this.activityOnePerWeekIDs.add(activity);
    }

    public void removeWeekActivity(Activity activity) {
        this.activityOnePerWeekIDs.remove(activity);
    }

    public boolean isKeywordForced() {
        return keywordForced;
    }

    public void setKeywordForced(boolean keywordForced) {
        this.keywordForced = keywordForced;
    }

    public void addSolverListener(SolverListener listener) {
        this.solverListeners.add(listener);
    }

    public void solve() {

        thread = new Thread(new Runnable() {
            public void run() {
                solveBody();
            }
        });
        thread.start();
//        java.awt.EventQueue.invokeLater(runnable);

    }

    public void foundTemporarySolution(int value) {
        for (SolverListener solverListener : solverListeners) {
            solverListener.temporarySolutionFound(value);
        }
    }

    public void sendMessage(String message) {
        for (SolverListener solverListener : solverListeners) {
            solverListener.messageFromSolver(message);
        }
    }

    public void solveBody() {

        Map<JobTurn, ActivityTurn> jobTurnMap = new HashMap<>();
        this.currentValue = 0;
        List<BoolVar> matchingList = new ArrayList<>();
        List<Integer> matchingIntList = new ArrayList<>();
        model = new Model("Televita model");
        allTurns.clear();
        sendMessage("Analisi dei candidati in corso..");
        System.out.println("****************************************************");
        System.out.println("                   CANDIDATES.. ");
        System.out.println("****************************************************");

        directMap.clear();
        peopleIdMap.clear();
        jobsIdMap.clear();

        List<Person> people = TrainerManager.getInstance().getPeople();
        List<Activity> activities = TrainerManager.getInstance().getActivities();
        List<JobTurn> allJobs = new ArrayList<>();

        int sumJobs = 0;
        for (Activity activity : activities) {
            sumJobs += activity.getTotalJobs();
            allJobs.addAll(activity.getAllJobTurns());
            List<ActivityTurn> turns = activity.getActivityTurns();
            for (ActivityTurn turn : turns) {
                List<JobTurn> jobs = turn.getRequiredProfiles();
                for (JobTurn job : jobs) {
                    jobTurnMap.put(job, turn);
                }
            }
            allTurns.addAll(turns);
        }

        //fill job turn map
        solutionMatrix = new BoolVar[people.size()][sumJobs];

        for (int j = 0; j < allJobs.size(); j++) {
            jobsIdMap.put(allJobs.get(j), j);
        }

        for (int i = 0; i < people.size(); i++) {
            peopleIdMap.put(people.get(i), i);
            for (int j = 0; j < allJobs.size(); j++) {
                JobTurn job = allJobs.get(j);

                ActivityTurn turn = jobTurnMap.get(job);

                if (isPersonCompatible(people.get(i), turn)) {
//                    BoolVar x = model.boolVar("" + people.get(i).toString() + "." + job.getName());
                    BoolVar x = model.boolVar("x." + i + "." + j);
                    solutionMatrix[i][j] = x;

                    matchingList.add(x);
                    matchingIntList.add(people.get(i).calculateActivityValue(job.getJob()));

                } else {
                    BoolVar constX = model.boolVar(false);
                    solutionMatrix[i][j] = constX;
                }

            }
        }
        //impostazone vincolo tutti devono fare qualcosa
        if (allDoingSomething) {
            for (int i = 0; i < people.size(); i++) {
                boolean addClauses = model.addClauses(solutionMatrix[i], new BoolVar[0]); //qua gli dico che su una riga ci deve stare almeno un TRUE
//                model.add
                assert addClauses;
            }
        }

        if (allJobsMustBeDone) {

            for (int k = 0; k < sumJobs; k++) {
                BoolVar[] colonna = new BoolVar[people.size()];
                for (int l = 0; l < people.size(); l++) {
                    colonna[l] = solutionMatrix[l][k];
                }
                boolean addClauses = model.addClauses(colonna, new BoolVar[0]);
                boolean addClausesAtMostOne = model.addClausesAtMostOne(colonna);
                assert addClauses;
                assert addClausesAtMostOne;
            }
        }

        //------------------ SOVRAPPOSIZIONI
        // For each pulse the atoms starting at that pulse
//        int totalJobs = activity.getTotalJobs();
        Map<Long, Collection<JobTurn>> starting_values = new HashMap<>(sumJobs);
        // For each pulse the atoms ending at that pulse
        Map<Long, Collection<JobTurn>> ending_values = new HashMap<>(sumJobs);
        // The pulses of the timeline
        Set<Long> c_pulses = new HashSet<>(sumJobs * 2);

        for (ActivityTurn activityTurn : allTurns) {
            long start_pulse = activityTurn.getStartTime();
            long end_pulse = activityTurn.getEndTime();

            if (!starting_values.containsKey(start_pulse)) {
                starting_values.put(start_pulse, new ArrayList<>(activityTurn.getRequiredProfiles().size()));
            }
            starting_values.get(start_pulse).addAll(activityTurn.getRequiredProfiles());

            if (!ending_values.containsKey(end_pulse)) {
                ending_values.put(end_pulse, new ArrayList<>(activityTurn.getRequiredProfiles().size()));
            }
            ending_values.get(end_pulse).addAll(activityTurn.getRequiredProfiles());
            c_pulses.add(start_pulse);
            c_pulses.add(end_pulse);
        }

        // Sort current pulses
        Long[] c_pulses_array = c_pulses.toArray(new Long[c_pulses.size()]);
        Arrays.sort(c_pulses_array);

        // Push values to timeline according to pulses...
        List<JobTurn> overlapping_formulas = new ArrayList<>();

        for (int m = 0; m < c_pulses_array.length; m++) {
            if (overlapping_formulas.size() > 1) {

                for (Person person : people) {
                    List<BoolVar> overLappingJobs = new ArrayList<>();
                    for (JobTurn job : overlapping_formulas) {
                        BoolVar overlappingJob = solutionMatrix[peopleIdMap.get(person)][jobsIdMap.get(job)];
                        overLappingJobs.add(overlappingJob);
                    }
                    boolean addClausesAtMostOne = model.addClausesAtMostOne(overLappingJobs.toArray(new BoolVar[overLappingJobs.size()]));
                    assert addClausesAtMostOne;
                }

            }
            if (starting_values.containsKey(c_pulses_array[m])) {
                overlapping_formulas.addAll(starting_values.get(c_pulses_array[m]));
            }
            if (ending_values.containsKey(c_pulses_array[m])) {
                overlapping_formulas.removeAll(ending_values.get(c_pulses_array[m]));
            }
        }

        System.out.println("STAMPA MODELLO SENZA OBIETTIVO:");

        System.out.println(model);

        System.out.println("----------------------------------------------------------------------------");

        IntVar obj_var = null;
        if (funzioneObiettivoActive) {
            obj_var = model.intVar(0, 100000, true); //sa
            int[] matchingIntArray = new int[matchingIntList.size()];
            for (int k = 0; k < matchingIntList.size(); k++) {
                matchingIntArray[k] = matchingIntList.get(k);
            }

            model.scalar(matchingList.toArray(new IntVar[matchingList.size()]), matchingIntArray, "=", obj_var).post();
            model.setObjective(Model.MAXIMIZE, obj_var);
        }

        sendMessage("Modello caricato [OK]");
        System.out.println(model);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(SolverManager2.class.getName()).log(Level.SEVERE, null, ex);
        }
        sendMessage("Solving..");
        //-------------------------------
        System.out.println("sto per trovare una soluzione");

        Solver solver = model.getSolver();
        solver.setRestartOnSolutions();
        int cicli = 0;
        while (solver.solve()) {
            System.out.println("HO TROVATO UNA SOLUZIONE !!!");

            solver.printStatistics();
            record = new Solution(model).record();
//            System.out.println(model);
            if (funzioneObiettivoActive) {
                System.out.println(obj_var.getLB() + ", " + obj_var.getUB() + " -> " + obj_var.getValue());
                System.out.println("--------------------------------------------------------------------");
                this.currentValue = obj_var.getValue();
                this.foundTemporarySolution(obj_var.getValue());
            }else{
                this.foundTemporarySolution(0);
            }
            cicli++;
        }
        if (cicli == 0) {
            JOptionPane.showMessageDialog(null, "ERRORE NIENTE BUONO");
            return;
        }
        for (SolverListener solverListener : solverListeners) {
            solverListener.solutionFound();
        }
        JOptionPane.showMessageDialog(null, "E' stata trovata la soluzione Ottima", "Soluzione Trovata!", JOptionPane.INFORMATION_MESSAGE);

    }

    private boolean isPersonCompatible(Person person, ActivityTurn turn) {

        System.out.println("size of availableTimes is: " + person.getName() + " ha " + person.getExpandedFreeTimes().size() + " turni liberi");
        for (FreeTimeToken availableTime : person.getExpandedFreeTimes()) {

            if (turn.getStartTime() + 1000 >= availableTime.getStarTime() && turn.getEndTime() - 1000 <= availableTime.getEndTime()) {

//                if (!turn.isThisPersonConteined(person)) {
//                    turn.getCandidates().add(person);
                return true;
//                }
            }

        }
//        JOptionPane.showMessageDialog(null,"NON COMPATIBILE: "+person);
        return false;
    }

    public void showSolution() {
        if (record == null) {
            JOptionPane.showMessageDialog(null, "Non è stata trovata una soluzione", "Errore!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Person> people = TrainerManager.getInstance().getPeople();
        System.out.println("*******************************************************");
        System.out.println("                    S O L U Z I O N E");
        System.out.println("*******************************************************");
        System.out.println(model);
        System.out.println("*************************************************************");
        for (ActivityTurn activityTurn : allTurns) {
            activityTurn.getComfirmedTurns().clear();
        }
        //  int var_name = 0;
        for (Person candidate : people) {
            int j = 0;
            for (ActivityTurn activityTurn : allTurns) {
//                        activityTurn.getComfirmedTurns().clear();

                for (JobTurn job : activityTurn.getRequiredProfiles()) {
//                            BoolVar x = ctx.mkIntConst(person.getName() + "->" + activity.getName() + "->" + job.getName() + j + "->" + activityTurn.toString());
                    //person.getName() + "->" + activity.getName() + "->" + job.getName() + j + "->" + activityTurn.toString()
                    //BoolVar var = directMap.get("" + var_name);
                    BoolVar var = solutionMatrix[peopleIdMap.get(candidate)][jobsIdMap.get(job)];
                    int intVal = record.getIntVal(var);
                    //   var_name++;
//                    Expr eval = model.eval(var, true);
//                    System.out.println("VAR: " + var + " ->  VALORE: " + eval);
////                            System.out.println("VAR: " + var.);
                    if (intVal == 1) {
//                                System.out.println("TOGLIERE");
                        activityTurn.addComfirmedTurn(new ComfirmedTurn(candidate, job));

                    }
                    j++;
                }
            }
        }
        System.out.println("***************************************************************");
        System.out.println("                       HIGH LEVEL ");
        System.out.println("***************************************************************");
        for (ActivityTurn activityTurn : allTurns) {
            System.out.println("TURNO: " + activityTurn);
            for (ComfirmedTurn ct : activityTurn.getComfirmedTurns()) {
                JobTurn job = ct.getJobTurn();
                System.out.println("A QUESTO TURNO SARANNO PRESENTI: " + ct.getPerson().getName() + " at " + ct.getJobTurn().getJob().getName());
                System.out.println("JOB richiede: " + job.getWantedKeywords().stream().map(o -> o.getKeyword()).collect(Collectors.joining(", ")));
                System.out.println("UTENTE HA:    " + ct.getPerson().getKeywords().stream().map(o -> o.getKeyword()).collect(Collectors.joining(", ")));

            }
            System.out.println("---------------------------------");
        }
        for (SolverListener solverListener : solverListeners) {
            solverListener.temporarySolutionFound(this.currentValue);
        }
    }

    public boolean personHasKeywords(Person person, List<Keyword> keywords) {
        int numberOfKeyToHave = keywords.size();
        int current = 0;
        List<Keyword> pKeys = person.getKeywords();
        for (Keyword keyword : keywords) {
            if (pKeys.contains(keyword)) {
                current++;
            }
        }
        if (current == numberOfKeyToHave) {
            return true;
        } else {
            return false;
        }
    }

    public int getNumberIfpersonHasKeywords(Person person, List<Keyword> wantedKeywords, List<Keyword> unwantedKeywords) {
        if (wantedKeywords.isEmpty()) {
            return 1;
        }
        int numberOfKeyToHave = wantedKeywords.size() + unwantedKeywords.size();
        int current = 0;
        List<Keyword> pKeys = person.getKeywords();
        for (Keyword keyword : wantedKeywords) {
            if (pKeys.contains(keyword)) {
                current++;
            }
        }
        for (Keyword keyword : unwantedKeywords) {
            if (pKeys.contains(keyword)) {
                current++;
            }
        }
        if (current == numberOfKeyToHave) {
            return 1;
        } else {
            System.out.println("VEDO UNO ZERO -> " + person.toString());
            return 0;
        }
    }

    @Override
    public void newActivityEvent(Activity activity) {

    }

    @Override
    public void removeActivityEvent(Activity activity) {
        activityOnePerWeekIDs.remove(activity);
    }

    @Override
    public void newProfileEvent(Job profile) {

    }

    @Override
    public void newActivityTurn(ActivityTurn turn) {

    }

    @Override
    public void changeDate(Date date) {

    }

    @Override
    public void changeTab(int tab) {

    }

    @Override
    public void newDormient(long id) {
       
    }

    @Override
    public void dormientWokeup(long id) {
       
    }

    @Override
    public void ghostNumberUpdated() {
    }

    @Override
    public void timeAvailableChanged() {
    }

    class SolverThread implements Callable<Boolean> {

        Solver solver;

        public SolverThread(Solver solver) {
            this.solver = solver;
        }

        @Override
        public Boolean call() throws Exception {

            return solver.solve();
        }

        public Boolean stop() {
            return Boolean.FALSE;
        }
    }
}
