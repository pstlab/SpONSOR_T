/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.solver;

import it.cnr.istc.sponsor.tt.gui.engine.GuiEventListener;
import it.cnr.istc.sponsor.tt.logic.SettingsManager;
import it.cnr.istc.sponsor.tt.logic.TrainerManager;
import it.cnr.istc.sponsor.tt.logic.Utils;
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
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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
public class SolverManager2 implements GuiEventListener {

    private static SolverManager2 _instance = null;
    private List<SolverListener> solverListeners = new ArrayList<>();
    private boolean keywordForced = true;
    private List<Activity> activityOnePerWeekIDs = new ArrayList<>();
    private SolverThread solverThread = null;
    private Solution record = null;
    private Thread thread = null;
    private Model model = null;
    private List<ActivityTurn> allTurns = new ArrayList<>();
    private BoolVar[][] solutionMatrix;
    private Map<String, BoolVar> directMap = new HashMap<>();
    private Map<Person, Integer> peopleIdMap = new IdentityHashMap<>();
    private Map<JobTurn, Integer> jobsIdMap = new IdentityHashMap<>();
    private int currentValue = 0;

    public static SolverManager2 getInstance() {
        if (_instance == null) {
            _instance = new SolverManager2();
            return _instance;
        } else {
            return _instance;
        }
    }

    private SolverManager2() {
        super();
    }

    public void showTemporarySolution(){
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

        this.currentValue  = 0;
        sendMessage("Analisi dei candidati in corso..");
        System.out.println("****************************************************");
        System.out.println("                   CANDIDATES.. ");
        System.out.println("****************************************************");

        System.out.println("* PEOPLE:");
        List<Person> people = TrainerManager.getInstance().getPeople();

//        List<ActivityTurn> allTurns = new ArrayList<>();
        allTurns.clear();
        List<Activity> activities = TrainerManager.getInstance().getActivities();
        Map<Long, Map<Activity, List<List<BoolVar>>>> mapWeek = new HashMap<>(); //chiave primaria è un id di una persona, chiave secondaria è un attività, e poi le liste di weeks

        Map<Person, List<List<BoolVar>>> mapTurnPerDay = new HashMap<>(); // chiave primaria: persone, liste raggruppate per giorno

        for (Activity activity : activities) {
            allTurns.addAll(activity.getActivityTurns());
        }
        for (Person person : people) {
            mapWeek.put(person.getId(), new HashMap<>());
            mapTurnPerDay.put(person, new ArrayList<>());

            for (Activity activity : activities) {
                mapWeek.get(person.getId()).put(activity, new ArrayList<List<BoolVar>>());
                mapWeek.get(person.getId()).get(activity).add(new ArrayList<BoolVar>());
            }
        }

        Collections.sort(allTurns);

        //FINDING CANDIDATES
        for (Person person : people) {
//            JOptionPane.showMessageDialog(null, "ANALIZZANDO LA PERSONA: "+person);
            person.fix();
            for (ActivityTurn activityTurn : allTurns) {

                System.out.println("TURN: " + activityTurn);

                System.out.println("size of availableTimes is: " + person.getName() + " ha " + person.getExpandedFreeTimes().size() + " turni liberi");
                for (FreeTimeToken availableTime : person.getExpandedFreeTimes()) {

//                    System.out.println("\tTEMPO LIBERO: " + availableTime);
//                    if (activityTurn.getStartTime() >= availableTime.getStartTime() && activityTurn.getEndTime() <= availableTime.getEndTime()) {
//                    Date availableStartTime = activityTurn.getStartTime();
//                    availableStartTime.setMonth(activityTurn.getStartTime().getMonth());
//                    availableStartTime.setDate(activityTurn.getStartTime().getDate());
//                    availableStartTime.setSeconds(activityTurn.getStartTime().getSeconds());
//                    availableStartTime.setMinutes(activityTurn.getStartTime().getMinutes());
//
//                    Date availableEndTime = activityTurn.getStartTime();
//                    availableEndTime.setMonth(activityTurn.getStartTime().getMonth());
//                    availableEndTime.setDate(activityTurn.getStartTime().getDate());
//                    availableEndTime.setSeconds(activityTurn.getStartTime().getSeconds());
//                    availableEndTime.setMinutes(activityTurn.getStartTime().getMinutes());
//  ATTENZIONE
//                    if (activityTurn.getStartTime().after(availableTime.getStarTime()) && activityTurn.getEndTime().before(availableTime.getEndTime())) {
                    if (activityTurn.getStartTime()+ 1000 >= availableTime.getStarTime() && activityTurn.getEndTime() - 1000 <= availableTime.getEndTime()) {

                        if (!activityTurn.isThisPersonConteined(person)) {
                            activityTurn.getCandidates().add(person);
//                            JOptionPane.showMessageDialog(null, "QUESTA PERSONA E' DISPONIBILE IN QUESTO TURNO: \n"+activityTurn+"\nPERSONA: "+person);
                        }
                        System.out.println(">>> CANDIDATE FOUND");
                    }

                }
                System.out.println("------------------------------");
            }
            System.out.println(" ==================================");
        }
        sendMessage("Analisi dei candidati[OK]");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(SolverManager2.class.getName()).log(Level.SEVERE, null, ex);
        }

        sendMessage("Preparazione Turni");
        System.out.println("****************************************************");
        System.out.println("                   TEST SOLVER");
        System.out.println("****************************************************");

        for (Activity activity : activities) {
            sendMessage("Preparazione turni di " + activity);
            System.out.println("* Activity: " + activity.getActivityName().getName());

            List<ActivityTurn> activityTurns = activity.getActivityTurns();

            System.out.println("* TURNS:");
            for (ActivityTurn activityTurn : activityTurns) {
                System.out.println("*   T: " + activityTurn.toString());

                System.out.println("*   Profiles:");
                for (JobTurn job : activityTurn.getRequiredProfiles()) {
                    System.out.println("*      P: " + job.toString());
                    List<Keyword> wantedKeywords = job.getWantedKeywords();
                    List<Keyword> unwantedKeywords = job.getUnwantedKeywords();
                    for (Keyword wantedKeyword : unwantedKeywords) {
                        System.out.println("*         WANTED KEY: " + wantedKeyword.getKeyword());
                    }
                    for (Keyword wantedKeyword : wantedKeywords) {
                        System.out.println("*         UNWANTED KEY: " + wantedKeyword.getKeyword());
                    }

                }
                List<Person> candidates = activityTurn.getCandidates();
                System.out.println("CANDIDATES FOR THIS TURN ARE:");
                for (Person candidate : candidates) {
                    System.out.println("\t" + candidate);
                }
            }
            System.out.println("* ----------------------------------------------");
        }

        sendMessage("Preparazione turni[OK]");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(SolverManager2.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("***************************************************");

//        for (Person person : people) {
//            System.out.println("AVAILABLE TIME OF: ");
//            System.out.println("*   P: " + person.toString());
//            System.out.println(":");
//            List<FreeTimeToken> availableTimes = person.getExpandedFreeTimes();
//            for (FreeTimeToken availableTime : availableTimes) {
//                System.out.println("    P - time: " + availableTime.toString());
//            }
//            System.out.println("-------------------------------------");
//        }
        sendMessage("Caricamento Vincoli..");
        model = new Model("Televita model");

//        System.out.println(optimizer);
//        System.exit(0);
        System.out.println("---------------------------------------------");
//        Params params = new Params
//        optimizer.setParameters(new );
//        Solver optimizer = ctx.mkSolver(); // OK

        //F BoolVar[][] solutionMatrix = new BoolVar[people.size()][activity.getActivityTurns().size()];
        int sumJobs = 0;
        for (Activity activity : activities) {
            sumJobs += activity.getTotalJobs();
        }

        System.out.println("SUM JOB -> " + sumJobs);
        //  int peppe = sumJobs*people.size();
        solutionMatrix = new BoolVar[people.size()][sumJobs];
//        BoolVar[][] keywordsMatrix = new BoolVar[people.size()][sumJobs];

        List<BoolVar> matchingList = new ArrayList<>();
        List<Integer> matchingIntList = new ArrayList<>();

        directMap.clear();
        peopleIdMap.clear();
        jobsIdMap.clear();
        int i = 0;
        for (Person person : people) {
            peopleIdMap.put(person, i);
            i++;
        }

        int j = 0;
        for (Activity activity : activities) {
            for (ActivityTurn activityTurn : activity.getActivityTurns()) {
                for (JobTurn job : activityTurn.getRequiredProfiles()) {
                    if (jobsIdMap.containsKey(job)) {
//                        JOptionPane.showMessageDialog(null, "OH NOOOOO");
                        continue;
                    }
                    jobsIdMap.put(job, j);
                    j++;

                }
            }
        }

//        for (Activity activity : activities) {
        //sum all turns;
        i = 0;

        int x_name = 0;
        for (Person person : people) {
            j = 0;

            Date previousMonday = Utils.getPreviousWeekMonday(new Date(allTurns.get(0).getStartTime()));
            Date nextMonday = Utils.getNextWeekMonday(new Date(allTurns.get(0).getStartTime()));
//            JOptionPane.showMessageDialog(null,"NEXT MONDAY: "+nextMonday);
//            JOptionPane.showMessageDialog(null, "NEXT MONDAY -> " + nextMondey);
            int daynumber = -1;
            for (ActivityTurn activityTurn : allTurns) {
                int mynumber = new Date(activityTurn.getStartTime()).getDay() + new Date(activityTurn.getStartTime()).getMonth() * 1000;
                if (daynumber != mynumber) {
                    daynumber = mynumber;
                    mapTurnPerDay.get(person).add(new ArrayList<>());
                }
                if (activityTurn.getStartTime() >= nextMonday.getTime()) {
//                    JOptionPane.showMessageDialog(null, "NEW FANTASTIC WEEK: ");
                    for (Activity activity : activities) {
                        mapWeek.get(person.getId()).put(activity, new ArrayList<List<BoolVar>>());
                        mapWeek.get(person.getId()).get(activity).add(new ArrayList<BoolVar>());
                    }
                    //aggiorno i confini della settimana in esame
                    previousMonday = Utils.getPreviousWeekMonday(new Date(activityTurn.getStartTime()));
                    nextMonday = Utils.getNextWeekMonday(new Date(activityTurn.getStartTime()));
                }

                for (JobTurn job : activityTurn.getRequiredProfiles()) {
                    BoolVar x = model.boolVar("" + x_name);
                    directMap.put("" + x_name, x);
                    if (!activityTurn.isThisPersonConteined(person)) {
//                        x = model.boolVar("" + x_name);
                        //se la persona, temporalmente non può stare in quel turno, impongo il vincolo a ZERO, perché non la voglio mai
                        boolean addClauseFalse = model.addClauseFalse(x);
                        assert addClauseFalse;
//                        BoolVar vincoloZero = ctx.mkEq(x, ctx.mkInt(0));

//                        optimizer.Add(vincoloZero);
                        System.out.println("i -> " + i);
                        System.out.println("j -> " + j);
                        solutionMatrix[i][j] = x;
                        //                        JOptionPane.showMessageDialog(null, "STA PERSONA NON PUO !! NE ORA NE MAI !\n"+person);
                    } else {
//                        x = ctx.mkIntConst("" + x_name);

//                        directMap.put("" + x_name, x);
//                        BoolVar vincoloFalse = ctx.mkLe(x, ctx.mkInt(1));
//                        BoolVar vincoloTrue = ctx.mkGe(x, ctx.mkInt(0));
//                        optimizer.Add(vincoloTrue, vincoloFalse);
                        System.out.println("i -> " + i);
                        System.out.println("j -> " + j);
                        solutionMatrix[i][j] = x;
                        GregorianCalendar gc = new GregorianCalendar();
                        gc.setTime(new Date(activityTurn.getStartTime()));

                        //inserisco la variabile X nell'apposita lista
                        Activity activityByTurn = TrainerManager.getInstance().getActivityByTurn(activityTurn);
                        //JOptionPane.showMessageDialog(null, "TURN -> "+activityTurn+" and ACTIVITY is: "+activityByTurn.getName());
                        int currentWeekList = mapWeek.get(person.getId()).get(activityByTurn).size() - 1;
//                        if (!this.activityOnePerWeekIDs.contains(activityByTurn)) {
                        mapWeek.get(person.getId()).get(activityByTurn).get(currentWeekList).add(x);//MAPWEEK
                        mapTurnPerDay.get(person).get(mapTurnPerDay.get(person).size() - 1).add(x); //MAPDAY
//                        }
                    }

//                    BoolVar k = ctx.mkInt(Integer.toString(person.calculateActivityValue(job)));
                    matchingList.add(x);
                    matchingIntList.add(person.calculateActivityValue(job.getJob()));
//                    matchingList.add(ctx.mkMul(k, x));

                    int keyNumber = getNumberIfpersonHasKeywords(person, job.getWantedKeywords(), job.getUnwantedKeywords());
                   // System.out.println("                                                                    ADDING KEY NUMEBR: (" + person.toString() + ") @ JOB: " + job.getName() + " ->" + keyNumber);
//                    keywordsMatrix[i][j] = ctx.mkInt("" + keyNumber);

                    if (keyNumber == 0) {
//                        BoolVar beq = ctx.mkEq(solutionMatrix[i][j], ctx.mkInt("0"));
//                        optimizer.Add(beq); //OK
                        boolean addClauseFalse = model.addClauseFalse(solutionMatrix[i][j]);
                        assert addClauseFalse;
                    }
                    j++;
                    x_name++;
                }
//                j++;
//                System.out.println("                                                    --------------------> ER VINCOLONE con i = "+i+" poniamo = a "+keyNumber);
//                solutionMatrix[i][sumJobs] = ctx.mkIntConst("" + keyNumber);

            }

//            BoolVar keyOK = ctx.mkEq(solutionMatrix[i][sumJobs], ctx.mkInt("0"));
//            optimizer.Add(keyOK);
            //LAST PIECE
//            if (!weekVariables.isEmpty()) {
//                BoolVar[] weekArray = new BoolVar[weekVariables.size()];
//                int z = 0;
//                for (BoolVar weekVar : weekVariables) {
//                    weekArray[z] = weekVar;
//                    z++;
//                }
////                weekArray[weekArray.length - 1] = solutionMatrix[i][sumJobs];
//
//                BoolVar unTurnoASettimana = ctx.mkLe(ctx.mkAdd(weekArray), ctx.mkInt("1"));
//                optimizer.Add(unTurnoASettimana);
//                weekVariables.clear();
//            }
            //END LAST PIECE
            i++;

//            }
        }

        //INJECT VARIABLE INTO THE SYSTEM
        Collection<Map<Activity, List<List<BoolVar>>>> personMap = mapWeek.values();
        System.out.println(" FOR PERSON.. size: " + personMap.size());
        for (Map<Activity, List<List<BoolVar>>> activityMap : personMap) {
            System.out.println("-----> FOR ACTIVITY SIZE:" + activityMap.values().size());
            for (Activity activity : activityMap.keySet()) {
                List<List<BoolVar>> weekList = activityMap.get(activity);

                System.out.println("---------------->WEEKLIST SIZE: " + weekList.size());

                for (List<BoolVar> week : weekList) {
                    if (week.isEmpty()) {
                        continue;
                    }
//                    System.out.println("----------------->NEW WEEK: ");

                    BoolVar[] weekArray = new BoolVar[week.size()];
                    int z = 0;
                    for (BoolVar day : week) {
//                        System.out.println("--------------------------->DAY - " + z);
                        weekArray[z] = day;
                        z++;
                    }
//                    BoolVar unTurnoASettimana = ctx.mkLe(ctx.mkAdd(weekArray), ctx.mkInt("1"));
                    if (!this.activityOnePerWeekIDs.contains(activity)) {
//                        optimizer.Add(unTurnoASettimana);
                        model.addClausesAtMostOne(weekArray);

                    }
                }
            }
        }
        //END INJECTION
        //INJECT DAY
        Collection<List<List<BoolVar>>> personList = mapTurnPerDay.values();
        for (List<List<BoolVar>> days : personList) {
            for (List<BoolVar> day : days) {
                if (day.size() == 0) {
                    // JOptionPane.showMessageDialog(null, "DAY ZIZZE ZERO !!!! MA COME SE FA");
                    System.out.println("discard unknown error");
                    continue;
                }
                BoolVar[] dayArray = new BoolVar[day.size()];
                int z = 0;
                for (BoolVar intExpr : day) {
                    dayArray[z] = intExpr;
                    z++;
                }
//                if (dayArray != null && dayArray.length != 0) {
//                BoolVar unTurnoAlGiorno = ctx.mkLe(ctx.mkAdd(dayArray), ctx.mkInt("1"));
//                optimizer.Add(unTurnoAlGiorno);
                model.addClausesAtMostOne(dayArray);
//                }
            }
        }
        //END INJECTION
        System.out.println("SIZE OF " + matchingList.size());
        IntVar obj_var = model.intVar(0, 100000, true); //sa
        int[] matchingIntArray = new int[matchingIntList.size()];
        for (int k = 0; k < matchingIntList.size(); k++) {
            matchingIntArray[k] = matchingIntList.get(k);
        }
        model.scalar(matchingList.toArray(new IntVar[matchingList.size()]), matchingIntArray, "=", obj_var).post();
        model.setObjective(Model.MAXIMIZE, obj_var);
//        Optimize.Handle handle = optimizer.MkMaximize(ctx.mkAdd(matchingList.toArray(new ArithExpr[matchingList.size()])));

//        System.out.println("H A  N D LE "+handle);
//        for (j = 0; j < solutionMatrix.length; j++) {
//            BoolVar b1 = ctx.mkGe(ctx.mkAdd(solutionMatrix[j]), ctx.mkInt("1"));
//            optimizer.Add(b1);
//            BoolVar[] colonna = new BoolVar[people.size()];
//            for (int k = 0; k < activity.getActivityTurns().size(); k++) {
//                colonna[k] = solutionMatrix[j][k];
//            }
//
//            BoolVar b2 = ctx.mkEq(ctx.mkAdd(colonna), ctx.mkInt("1"));
//            optimizer.Add(b2);
//
//        }
        for (j = 0; j < solutionMatrix.length; j++) {
            //ogni persona deve fare ALMENO una cosa  
//            BoolVar b1 = ctx.mkGe(ctx.mkAdd(solutionMatrix[j]), ctx.mkInt("1"));
////            BoolVar b2 = ctx.mkGe(ctx.mkAdd(solutionMatrix[j]), ctx.mkInt("0")); //considera le keys
//            optimizer.Add(b1);

            model.addClauses(solutionMatrix[j], new BoolVar[0]);

        }

        for (int k = 0; k < sumJobs; k++) {
            BoolVar[] colonna = new BoolVar[people.size()];
            for (int l = 0; l < people.size(); l++) {
                colonna[l] = solutionMatrix[l][k];
            }
            //Ogni singolo job va espletato da una e una sola persona
//            BoolVar b2 = ctx.mkEq(ctx.mkBool(true), ctx.mkBool(personHasKeywords(person, wantedKeywords)));

//            BoolVar b2 = ctx.mkEq(ctx.mkAdd(colonna), ctx.mkInt("1"));
//            optimizer.Add(b2);
            boolean addClauses = model.addClauses(colonna, new BoolVar[0]);
            boolean addClausesAtMostOne = model.addClausesAtMostOne(colonna);
            assert addClauses;
            assert addClausesAtMostOne;
        }
//        for (int k = 0; k < people.size(); k++) {
//            BoolVar[] colonna = new BoolVar[people.size()];
//            for (int l = 0; l < sumJobs; l++) {
//                colonna[l] = solutionMatrix[k][l];
//            }
//            BoolVar b2 = ctx.mkEq(ctx.mkAdd(colonna), ctx.mkInt("1"));
//            optimizer.Add(b2);
//
//        }
//        for (int z = 0; z < people.size(); z++) {
//             BoolVar[] riga = new BoolVar[sumJobs];
//             BoolVar[] rigaKey = new BoolVar[sumJobs];
//            for (int r = 0; r < sumJobs; r++) {
//                riga[r] = solutionMatrix[z][r];
//                rigaKey[r] = keywordsMatrix[z][r];
////                if(this.)
//                BoolVar b2 = 
//                        
////                        ctx.mkNot(
////                                ctx.mkEq(keywordsMatrix[z][r], ctx.mkInt("1")),
//                                ctx.mkOr(
//                                    ctx.mkEq(solutionMatrix[z][r], ctx.mkInt("0")),
//                                    ctx.mkEq(keywordsMatrix[z][r], ctx.mkInt("1"))
////                                )
//                        );
//                optimizer.Add(b2);
//            }
////            BoolVar dadda = ctx.mkEq(ctx.mkInt(""+sumJobs), ctx.mkAdd(rigaKey));
////            optimizer.Add(dadda);
//            
//        }

        sendMessage("Analisi sovrapposizioni..");

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
//        if (starting_values.containsKey(c_pulses_array[0])) {
//            overlapping_formulas.addAll(starting_values.get(c_pulses_array[0]));
//        }
//        if (ending_values.containsKey(c_pulses_array[0])) {
//            overlapping_formulas.removeAll(ending_values.get(c_pulses_array[0]));
//        }

        System.out.println("CHECK MAP ");
//        for (Person person : people) {
//            System.out.println("MAP PERSON : " + peopleIdMap.get(person));
//        }

        for (int m = 0; m < c_pulses_array.length; m++) {
            if (overlapping_formulas.size() > 1) {

                for (Person person : people) {
                    List<BoolVar> overLappingJobs = new ArrayList<>();
                    for (JobTurn jobTurn : overlapping_formulas) {
                        BoolVar overlappingJob = solutionMatrix[peopleIdMap.get(person)][jobsIdMap.get(jobTurn)];
//                        System.out.println("MAP PERSON: "+peopleIdMap.get(person));
//                        System.out.println("MAP JOB:    "+jobsIdMap.get(job));
//                        System.out.println("OVERLAPPING JOB: "+overlappingJob);
                        overLappingJobs.add(overlappingJob);
                    }

                    boolean addClausesAtMostOne = model.addClausesAtMostOne(overLappingJobs.toArray(new BoolVar[overLappingJobs.size()]));
                    assert addClausesAtMostOne;
//                            ctx.mkLe(ctx.mkAdd(overLappingJobs.toArray(new ArithExpr[overLappingJobs.size()])), ctx.mkInt("1"));
//                    System.out.println("VINCOLO "+vinc);
//                    optimizer.Add(vinc);

                }

            }
            if (starting_values.containsKey(c_pulses_array[m])) {
                overlapping_formulas.addAll(starting_values.get(c_pulses_array[m]));
            }
            if (ending_values.containsKey(c_pulses_array[m])) {
                overlapping_formulas.removeAll(ending_values.get(c_pulses_array[m]));
            }
        }
//
//        if (isKeywordForced()) {
//
//            System.out.println(" ******** CALCULATING KEYWORDS *******************");
//            for (ActivityTurn activityTurn : allTurns) {
//                List<Job> requiredProfiles = activityTurn.getRequiredProfiles();
//                List<Person> personToDiscard = new ArrayList<>();
//                for (Job job : requiredProfiles) {
//                    List<Keyword> wantedKeywords = job.getWantedKeywords();  
//                    for (Person candidate : activityTurn.getCandidates()) {
//                        System.out.println("O CANDIDO IL PESSIMISTA: "+candidate);
//                        BoolVar b2 = ctx.mkEq(ctx.mkBool(true), ctx.mkBool(personHasKeywords(candidate, wantedKeywords)));
//                        optimizer.Add(b2);
//                        
//                        //FIXARE QUI
//                    }
//
//                }
//            }
//        }

        sendMessage("Modello caricato [OK]");
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

//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        boolean ok = true;
//        while (ok) {
//            solverThread = new SolverThread(solver);
//            Future<Boolean> future = executor.submit(solverThread);
//            while (future.isDone()) {
//                try {
//                    if (!future.get().booleanValue()) {
//                        ok = false;
//                    } else {
//                        record = new Solution(model).record();
//                    }
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(SolverManager2.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (ExecutionException ex) {
//                    Logger.getLogger(SolverManager2.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//            System.out.println(model);
//            System.out.println(obj_var.getLB() + ", " + obj_var.getUB() + " -> " + obj_var.getValue());
//        }
        while (solver.solve()) {
            System.out.println("HO TROVATO UNA SOLUZIONE !!!");

            solver.printStatistics();
            record = new Solution(model).record();
//            System.out.println(model);
            System.out.println(obj_var.getLB() + ", " + obj_var.getUB() + " -> " + obj_var.getValue());
            System.out.println("--------------------------------------------------------------------");
            this.currentValue = obj_var.getValue();
            this.foundTemporarySolution(obj_var.getValue());
        }
        for (SolverListener solverListener : solverListeners) {
            solverListener.solutionFound();
        }
        JOptionPane.showMessageDialog(null, "E' stata trovata la soluzione Ottima", "Soluzione Trovata!", JOptionPane.INFORMATION_MESSAGE);

//        Status status = null;
//        try {
//            status = optimizer.Check(); //trova una soluzione
//        } catch (com.microsoft.z3.Z3Exception ex) {
//
//            System.out.println("EXCE PROBLEMA: ");
//            System.out.println("HANDLE");
//            System.out.println(handle.toString());
//            System.out.println("handle upper: " + handle.getUpper());
//            System.out.println("handle lower: " + handle.getLower());
//            System.out.println("handle value: " + handle.getValue());
//            System.out.println("FINE HANDLE");
//            status = Status.SATISFIABLE;
//            ex.printStackTrace();
//        }
//        Statistics statistics = optimizer.getStatistics();
//        System.out.println("STATISTICS!!!!!!!!!!!");
//        System.out.println(statistics);
//        System.out.println("!!!!!!!!!!!!!!!!!!!!STATISTICS!!!!!!!!!!!");
//        System.out.println(
//                "STATUS = " + status);
//        switch (status) {
//            case SATISFIABLE: {
//                System.out.println("OKKKK");
//                Model model = optimizer.getModel();
//                System.out.println("OPTIMIZER: \n" + optimizer);
//
//                System.out.println("*******************************************************");
//                System.out.println("                    S O L U Z I O N E");
//                System.out.println("*******************************************************");
//                System.out.println(model);
//                System.out.println("*************************************************************");
//                System.out.println(handle);
//                for (ActivityTurn activityTurn : allTurns) {
//                    activityTurn.getComfirmedTurns().clear();
//                }
//                //  int var_name = 0;
//                for (Person candidate : people) {
//                    j = 0;
//                    for (ActivityTurn activityTurn : allTurns) {
////                        activityTurn.getComfirmedTurns().clear();
//
//                        for (Job job : activityTurn.getRequiredProfiles()) {
////                            BoolVar x = ctx.mkIntConst(person.getName() + "->" + activity.getName() + "->" + job.getName() + j + "->" + activityTurn.toString());
//                            //person.getName() + "->" + activity.getName() + "->" + job.getName() + j + "->" + activityTurn.toString()
//                            //BoolVar var = directMap.get("" + var_name);
//                            BoolVar var = solutionMatrix[peopleIdMap.get(candidate)][jobsIdMap.get(job)];
//                            //   var_name++;
//                            Expr eval = model.eval(var, true);
//                            System.out.println("VAR: " + var + " ->  VALORE: " + eval);
////                            System.out.println("VAR: " + var.);
//                            if (eval.toString().equals("1")) {
////                                System.out.println("TOGLIERE");
//                                activityTurn.addComfirmedTurn(new ComfirmedTurn(candidate, job));
//
//                            }
//                            j++;
//                        }
//                    }
//                }
//                System.out.println("***************************************************************");
//                System.out.println("                       HIGH LEVEL ");
//                System.out.println("***************************************************************");
//                for (ActivityTurn activityTurn : allTurns) {
//                    System.out.println("TURNO: " + activityTurn);
//                    for (ComfirmedTurn ct : activityTurn.getComfirmedTurns()) {
//                        Job job = ct.getJob();
//                        System.out.println("A QUESTO TURNO SARANNO PRESENTI: " + ct.getPerson().getName() + " at " + ct.getJob().getName());
//                        System.out.println("JOB richiede: " + job.getWantedKeywords().stream().map(o -> o.getKeyword()).collect(Collectors.joining(", ")));
//                        System.out.println("UTENTE HA:    " + ct.getPerson().getKeywords().stream().map(o -> o.getKeyword()).collect(Collectors.joining(", ")));
//
//                    }
//                    System.out.println("---------------------------------");
//                }
//                for (SolverListener solverListener : solverListeners) {
//                    solverListener.solutionFound();
//                }
//                JOptionPane.showMessageDialog(null, "E' stata trovata una soluzione", "Soluzione Trovata!", JOptionPane.INFORMATION_MESSAGE);
//                break;
//            }
//            case UNKNOWN:
//                System.out.println("boooh");
//                break;
//            case UNSATISFIABLE:
//                Model model = optimizer.getModel();
//
//                System.out.println("OPTIMIZER: \n" + optimizer);
//                System.out.println("NOT BUONO");
//
//                for (String key : directMap.keySet()) {
////                    System.out.println("VARIABLE: " + key);
//                    BoolVar var = directMap.get(key);
//                    Expr eval = model.eval(var, true);
//                    System.out.println("VAR: " + var + " ->  VALORE: " + eval);
//                }
//
//                JOptionPane.showMessageDialog(null, "Non è stata trovata una soluzione", "Errore!", JOptionPane.ERROR_MESSAGE);
//                break;
//        }
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
                Job job = ct.getJobTurn().getJob();
                System.out.println("A QUESTO TURNO SARANNO PRESENTI: " + ct.getPerson().getName() + " at " + ct.getJobTurn().getJob().getName());
                System.out.println("JOB richiede: " + ct.getJobTurn().getWantedKeywords().stream().map(o -> o.getKeyword()).collect(Collectors.joining(", ")));
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
