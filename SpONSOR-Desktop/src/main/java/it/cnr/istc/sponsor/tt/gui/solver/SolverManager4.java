/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.solver;

import it.cnr.istc.sponsor.solver.sponsorsolver.AnyAmongExplanation;
import it.cnr.istc.sponsor.solver.sponsorsolver.Explanation;
import it.cnr.istc.sponsor.solver.sponsorsolver.Interval;
import it.cnr.istc.sponsor.solver.sponsorsolver.ISolver;
import it.cnr.istc.sponsor.solver.sponsorsolver.IdlerUserExplanation;
import it.cnr.istc.sponsor.solver.sponsorsolver.LessThanNExplanation;
import it.cnr.istc.sponsor.solver.sponsorsolver.OverlappingActivitiesExplanation;
import it.cnr.istc.sponsor.solver.sponsorsolver.SolverFactory;
import it.cnr.istc.sponsor.solver.sponsorsolver.SolverType;
import it.cnr.istc.sponsor.solver.sponsorsolver.UnassignableActivityExplanation;
import it.cnr.istc.sponsor.solver.sponsorsolver.User;
import it.cnr.istc.sponsor.tt.gui.engine.GuiEventListener;
import it.cnr.istc.sponsor.tt.logic.ParsedAccount;
import it.cnr.istc.sponsor.tt.logic.TrainerManager;
import it.cnr.istc.sponsor.tt.logic.model.Account;
import it.cnr.istc.sponsor.tt.logic.model.Activity;
import it.cnr.istc.sponsor.tt.logic.model.ActivityName;
import it.cnr.istc.sponsor.tt.logic.model.ActivityTurn;
import it.cnr.istc.sponsor.tt.logic.model.ComfirmedTurn;
import it.cnr.istc.sponsor.tt.logic.model.FreeTimeToken;
import it.cnr.istc.sponsor.tt.logic.model.Job;
import it.cnr.istc.sponsor.tt.logic.model.JobTurn;
import it.cnr.istc.sponsor.tt.logic.model.Person;
import it.cnr.istc.sponsor.tt.logic.model.Skill;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import org.chocosolver.solver.Model;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class SolverManager4 implements GuiEventListener {

    private int currentValue = 0;
    private boolean allDoingSomething = true;
    private boolean allJobsMustBeDone = true;
    private boolean funzioneObiettivoActive = false;
    private List<SolverListener> solverListeners = new ArrayList<>();
    private List<ActivityTurn> allTurns = new ArrayList<>();

//    private Solution record = null;
    private Map<User, List<it.cnr.istc.sponsor.solver.sponsorsolver.Activity>> solution = new HashMap<>();
    private Thread thread = null;
    private Model model = null;
    private static SolverManager4 _instance = null;
    private Map<Integer, JobTurn> jobTurnmap = new HashMap<>();
    private Map<Integer, ActivityTurn> jobActivityTurnmap = new HashMap<>();
    private Map<Person, User> userMap = new HashMap<>();
    private Map<User, Person> personMap = new HashMap<>();
    private Map<Activity, List<it.cnr.istc.sponsor.solver.sponsorsolver.Activity>> riccardinoJobsMap = new HashMap<>();
    private Map<JobTurn, it.cnr.istc.sponsor.solver.sponsorsolver.Activity> riccardinoJobsActivityMap = new HashMap<>();
    private Map<Activity, List<Person>> personForcedForActivityMap = new HashMap();
    private Map<Activity, Boolean> weekActivityMap = new HashMap<>();

    private Map<User, Map<Integer, List<it.cnr.istc.sponsor.solver.sponsorsolver.Activity>>> actPerUserDayMap = new HashMap<>();
    private Map<User, Map<Integer, List<it.cnr.istc.sponsor.solver.sponsorsolver.Activity>>> actPerUserWeekMap = new HashMap<>();

    private Map<Integer, List<it.cnr.istc.sponsor.solver.sponsorsolver.Activity>> weekLRJobsMap = new HashMap<>();

    private Map<ActivityName, Activity> nameActivityMap = new HashMap<>();
    
    private SolverType solverType = SolverType.HeuristicJaCoP;

    public static SolverManager4 getInstance() {
        if (_instance == null) {
            _instance = new SolverManager4();
            return _instance;
        } else {
            return _instance;
        }
    }

    private SolverManager4() {
        super();
    }

    public void setActivityWeekConstraint(Activity activity, boolean contstraing) {
        this.weekActivityMap.put(activity, contstraing);
    }

    public void forceActivityPeopleGroup(Activity activity, Person person) {
        if (!this.personForcedForActivityMap.containsKey(activity)) {
            this.personForcedForActivityMap.put(activity, new ArrayList<>());
        }
        this.personForcedForActivityMap.get(activity).add(person);
    }

    public void setSolverType(SolverType solverType) {
        this.solverType = solverType;
    }

    
    public void stop() {
        if (thread != null) {
            thread.interrupt();
        }
        showSolution();
        //this.solverThread.stop();
    }

    public void silentStop() {
        if (thread != null) {
            System.out.println("S I L E N T    I N T E R R U P T");
            thread.interrupt();
        }
//        showSolution();
    }

    public void showSolution() {
        if (solution == null) {
            JOptionPane.showMessageDialog(null, "Non è stata trovata una soluzione", "Errore!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        System.out.println("SOLUZIONE TROVATA");

//        List<Person> people = TrainerManager.getInstance().getPeople();
        System.out.println("*******************************************************");
        System.out.println("                    S O L U Z I O N E");
        System.out.println("*******************************************************");
        System.out.println(model);
        System.out.println("*************************************************************");
//        for (ActivityTurn activityTurn : allTurns) {
//            activityTurn.getComfirmedTurns().clear();
//        }
//        //  int var_name = 0;
//        for (Person candidate : people) {
//            int j = 0;
//            for (ActivityTurn activityTurn : allTurns) {
////                        activityTurn.getComfirmedTurns().clear();
//
//                for (JobTurn job : activityTurn.getRequiredProfiles()) {
////                            BoolVar x = ctx.mkIntConst(person.getName() + "->" + activity.getName() + "->" + job.getName() + j + "->" + activityTurn.toString());
//                    //person.getName() + "->" + activity.getName() + "->" + job.getName() + j + "->" + activityTurn.toString()
//                    //BoolVar var = directMap.get("" + var_name);
//                    BoolVar var = solutionMatrix[peopleIdMap.get(candidate)][jobsIdMap.get(job)];
//                    int intVal = record.getIntVal(var);
//                    //   var_name++;
////                    Expr eval = model.eval(var, true);
////                    System.out.println("VAR: " + var + " ->  VALORE: " + eval);
//////                            System.out.println("VAR: " + var.);
//                    if (intVal == 1) {
////                                System.out.println("TOGLIERE");
//                        activityTurn.addComfirmedTurn(new ComfirmedTurn(candidate, job));
//
//                    }
//                    j++;
//                }
//            }
//        }
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

    public void addSolverListener(SolverListener listener) {
        this.solverListeners.add(listener);
    }

    public int getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
    }

    public void foundTemporarySolution(int value) {
        for (SolverListener solverListener : solverListeners) {
            solverListener.temporarySolutionFound(value);
        }
    }

    public void error(String message) {
        for (SolverListener solverListener : solverListeners) {
            solverListener.error(message);
        }
    }

    public void sendMessage(String message) {
        for (SolverListener solverListener : solverListeners) {
            solverListener.messageFromSolver(message);
        }
    }

    public boolean isAllDoingSomething() {
        return allDoingSomething;
    }

    public void setAllDoingSomething(boolean allDoingSomething) {
        this.allDoingSomething = allDoingSomething;
    }

    public boolean isAllJobsMustBeDone() {
        return allJobsMustBeDone;
    }

    public void setAllJobsMustBeDone(boolean allJobsMustBeDone) {
        this.allJobsMustBeDone = allJobsMustBeDone;
    }

    public boolean isFunzioneObiettivoActive() {
        return funzioneObiettivoActive;
    }

    public void setFunzioneObiettivoActive(boolean funzioneObiettivoActive) {
        this.funzioneObiettivoActive = funzioneObiettivoActive;
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

    public void solveBody() {

        ISolver solver = SolverFactory.newSolver(SolverType.HeuristicJaCoP);

        sendMessage("inizializzazione..");
//        allTurns.clear();
        jobTurnmap.clear();
        solution.clear();
        userMap.clear();
        personMap.clear();
        riccardinoJobsActivityMap.clear();
        riccardinoJobsMap.clear();
        weekLRJobsMap.clear();
        actPerUserDayMap.clear();
        actPerUserWeekMap.clear();
        nameActivityMap.clear();
        jobActivityTurnmap.clear();
        personForcedForActivityMap.clear();
        //     weekActivityMap.clear();
//        riccardinoJobsActCivityMap.clear();
//        personForcedForActivityMap.clear();
//        weekActivityMap.clear();
        for (ActivityTurn activityTurn : allTurns) {
            activityTurn.getComfirmedTurns().clear();
        }
        allTurns.clear();
        List<Activity> activities = TrainerManager.getInstance().getActivities();
        for (Activity activity : activities) {
//            sumJobs += activity.getTotalJobs();
//            allJobs.addAll(activity.getAllJobTurns());
            List<ActivityTurn> turns = activity.getActivityTurns();

            allTurns.addAll(turns);
            nameActivityMap.put(activity.getActivityName(), activity);
        }

        List<Person> people = TrainerManager.getInstance().getPeople();

        sendMessage("data fine progettazione: " + TrainerManager.getInstance().getEndPlanningDate());
        for (Person person : people) {
            if (person.isSleeping()) {
                continue;
            }
            person.fix();

            Account account = person.getAccount();
            ParsedAccount pa = new ParsedAccount(account);
            int skills[] = new int[8];
            skills[0] = pa.getLeader();
            skills[1] = pa.getPianificatore();
            skills[2] = pa.getBrillante();
            skills[3] = pa.getValutatore();
            skills[4] = pa.getConcreto();
            skills[5] = pa.getEsploratore();
            skills[6] = pa.getLavoratore();
            skills[7] = pa.getOggettivo();

            List<FreeTimeToken> expandedFreeTimes = person.getExpandedFreeTimes();

            Interval intervals[] = new Interval[expandedFreeTimes.size()];

            int i = 0;
            for (FreeTimeToken eft : expandedFreeTimes) {
//                System.out.println("\t\t\t EFT ---<-<--<<< "+eft);
                intervals[i] = new Interval(eft.getStarTime() - 1000, eft.getEndTime());
//                System.out.println("\t\t\t\t\tINTERVAL --------------> "+intervals[i]);
                i++;
            }
            User a = solver.newUser("" + person.getId() + ":" + person.toString(), skills, intervals);
            this.userMap.put(person, a);
            this.personMap.put(a, person);
        }

        sendMessage("volontari caricati [OK]");
        try {
            Thread.sleep(300);
        } catch (InterruptedException ex) {
            Logger.getLogger(SolverManager4.class.getName()).log(Level.SEVERE, null, ex);
        }

        sendMessage("caricamento attività");
//        List<Activity> activities = TrainerManager.getInstance().getActivities();

        int jobId = 0;
        for (Activity activity : activities) {
            for (ActivityTurn activityTurn : activity.getActivityTurns()) {
                System.out.println("THIS TURN HAS " + activityTurn.getRequiredProfiles().size() + " JOBS");
                for (JobTurn requiredProfile : activityTurn.getRequiredProfiles()) {
                    requiredProfile.setTurn(activityTurn);
                    boolean[] skills = new boolean[8];
                    List<Skill> skillsList = requiredProfile.getJob().getSkills();
                    List<String> names = new ArrayList<>();
                    for (Skill s : skillsList) {
                        names.add(s.getName());
                    }
                    skills[0] = names.contains("label.skill1");
                    skills[1] = names.contains("label.skill2");
                    skills[2] = names.contains("label.skill3");
                    skills[3] = names.contains("label.skill4");
                    skills[4] = names.contains("label.skill5");
                    skills[5] = names.contains("label.skill6");
                    skills[6] = names.contains("label.skill7");
                    skills[7] = names.contains("label.skill8");
                    it.cnr.istc.sponsor.solver.sponsorsolver.Activity newActivity = solver.newActivity("" + jobId, skills, new Interval(activityTurn.getStartTime(), activityTurn.getEndTime()));
                    if (!this.riccardinoJobsMap.containsKey(activity)) {
                        this.riccardinoJobsMap.put(activity, new ArrayList<>());
                    }
                    this.riccardinoJobsMap.get(activity).add(newActivity);
                    jobTurnmap.put(jobId, requiredProfile);
                    riccardinoJobsActivityMap.put(requiredProfile, newActivity);
                    Person wantedPerson = requiredProfile.getWantedPerson();
                    if (wantedPerson != null) {
                        solver.at_least_one_of(new it.cnr.istc.sponsor.solver.sponsorsolver.Activity[]{newActivity}, new User[]{this.userMap.get(wantedPerson)});
                    }

                    jobId++;
                }
            }
        }

        sendMessage(
                "attività caricate [OK]");
        try {
            Thread.sleep(300);
        } catch (InterruptedException ex) {
            Logger.getLogger(SolverManager4.class.getName()).log(Level.SEVERE, null, ex);
        }

        sendMessage(
                "aggiungo vincoli speciali (1)");

        Set<Activity> keySet = this.personForcedForActivityMap.keySet();
        for (Activity activity : keySet) {
            List<it.cnr.istc.sponsor.solver.sponsorsolver.Activity> acts = this.riccardinoJobsMap.get(activity);
            List<Person> ppp = this.personForcedForActivityMap.get(activity);
            User[] users = new User[ppp.size()];
            int i = 0;
            for (Person person : ppp) {
                users[i] = this.userMap.get(person);
                i++;
            }
            solver.at_least_one_of(acts.toArray(new it.cnr.istc.sponsor.solver.sponsorsolver.Activity[acts.size()]), users);
        }

        sendMessage(
                "aggiungo vincoli speciali (1) [OK]");
        try {
            Thread.sleep(300);
        } catch (InterruptedException ex) {
            Logger.getLogger(SolverManager4.class.getName()).log(Level.SEVERE, null, ex);
        }

        sendMessage(
                "preparo vincoli speciali (2)[weeks]");
        for (Activity activity : activities) {
            if (this.weekActivityMap.containsKey(activity) && this.weekActivityMap.get(activity)) {
                for (ActivityTurn activityTurn : activity.getActivityTurns()) {
                    GregorianCalendar gc = new GregorianCalendar();
                    gc.setTime(new Date(activityTurn.getStartTime()));
                    int weekNumber = gc.get(Calendar.WEEK_OF_YEAR) * 100000 + gc.get(Calendar.YEAR);
//                     JOptionPane.showMessageDialog(null, "WEEK:: "+weekNumber);
                    if (!weekLRJobsMap.containsKey(weekNumber)) {
                        weekLRJobsMap.put(weekNumber, new ArrayList<>());
                    }
                    for (JobTurn jt : activityTurn.getRequiredProfiles()) {
                        it.cnr.istc.sponsor.solver.sponsorsolver.Activity lrAct = this.riccardinoJobsActivityMap.get(jt);
                        weekLRJobsMap.get(weekNumber).add(lrAct);
                    }

                }
            }
        }

        sendMessage(
                "preparo vincoli speciali (2)[weeks][OK]");
        try {
            Thread.sleep(300);
        } catch (InterruptedException ex) {
            Logger.getLogger(SolverManager4.class.getName()).log(Level.SEVERE, null, ex);
        }

        sendMessage(
                "aggiungo vincoli speciali (2)[weeks]");

        for (Integer ix : weekLRJobsMap.keySet()) {
//            JOptionPane.showMessageDialog(null, "STO INSERENDO BLOCCO DA: "+weekLRJobsMap.get(ix).size()+" ELEMENTI");
            List<it.cnr.istc.sponsor.solver.sponsorsolver.Activity> lrActs = weekLRJobsMap.get(ix);
            solver.at_most_n(lrActs.toArray(new it.cnr.istc.sponsor.solver.sponsorsolver.Activity[lrActs.size()]), 1);
        }

        sendMessage(
                "aggiungo vincoli speciali (2)[weeks][OK]");
        try {
            Thread.sleep(300);
        } catch (InterruptedException ex) {
            Logger.getLogger(SolverManager4.class.getName()).log(Level.SEVERE, null, ex);
        }

        sendMessage(
                "aggiungo vincoli speciali (2)[keywords]");

        for (Activity activity : activities) {
            for (ActivityTurn activityTurn : activity.getActivityTurns()) {
                for (JobTurn requiredProfile : activityTurn.getRequiredProfiles()) {
                    if (requiredProfile.containsKeywords()) {
                        List<User> bravePersone = new ArrayList<>();
                        for (Person person : people) {
                            if (!person.isSleeping() && requiredProfile.isThisPersonOk(person)) {
                                bravePersone.add(this.userMap.get(person));
                            }
                        }
                        solver.at_least_one_of(new it.cnr.istc.sponsor.solver.sponsorsolver.Activity[]{this.riccardinoJobsActivityMap.get(requiredProfile)}, bravePersone.toArray(new User[bravePersone.size()]));
                    }
                }
            }
        }

        sendMessage("aggiungo vincoli speciali (2)[keywords][OK]");
        try {
            Thread.sleep(300);
        } catch (InterruptedException ex) {
            Logger.getLogger(SolverManager4.class.getName()).log(Level.SEVERE, null, ex);
        }

        sendMessage("calcolo vincoli speciali (3)[days]");
        for (Person person : people) {
            if (person.isOneTurnPerDay()) {
                User user = userMap.get(person);
                if (!this.actPerUserDayMap.containsKey(user)) {
                    this.actPerUserDayMap.put(user, new HashMap<>());
                }

                for (Activity activity : activities) {
                    for (ActivityTurn activityTurn : activity.getActivityTurns()) {
                        GregorianCalendar gc = new GregorianCalendar();
                        gc.setTime(new Date(activityTurn.getStartTime()));
                        int dayNumber = gc.get(Calendar.DAY_OF_YEAR) * 100000 + gc.get(Calendar.YEAR);
                        if (!this.actPerUserDayMap.get(user).containsKey(dayNumber)) {
                            this.actPerUserDayMap.get(user).put(dayNumber, new ArrayList<>());
                        }
                        for (JobTurn requiredProfile : activityTurn.getRequiredProfiles()) {
                            this.actPerUserDayMap.get(user).get(dayNumber).add(riccardinoJobsActivityMap.get(requiredProfile));
                        }
                    }
                }
            }
        }

        sendMessage("calcolo vincoli speciali (3)[days]");

        Set<User> uuu = this.actPerUserDayMap.keySet();
        for (User user : uuu) {
            Map<Integer, List<it.cnr.istc.sponsor.solver.sponsorsolver.Activity>> dayMaps = this.actPerUserDayMap.get(user);
            for (List<it.cnr.istc.sponsor.solver.sponsorsolver.Activity> dayActs : dayMaps.values()) {
                solver.at_most_n(user, dayActs.toArray(new it.cnr.istc.sponsor.solver.sponsorsolver.Activity[dayActs.size()]), 1);
            }
        }

        sendMessage("aggiungo vincoli speciali (3)[OK]");
        try {
            Thread.sleep(300);
        } catch (InterruptedException ex) {
            Logger.getLogger(SolverManager4.class.getName()).log(Level.SEVERE, null, ex);
        }

        sendMessage("calcolo vincoli speciali (4)[max in week]");

        for (Person person : people) {
            if (person.isSleeping()) {
                continue;
            }
            User user = userMap.get(person);

            if (!this.actPerUserWeekMap.containsKey(user)) {
                this.actPerUserWeekMap.put(user, new HashMap<>());
            }

            for (Activity activity : activities) {
                for (ActivityTurn activityTurn : activity.getActivityTurns()) {
                    GregorianCalendar gc = new GregorianCalendar();
                    gc.setTime(new Date(activityTurn.getStartTime()));
                    int weekNumber = gc.get(Calendar.WEEK_OF_YEAR) * 100000 + gc.get(Calendar.YEAR);
                    if (!this.actPerUserWeekMap.get(user).containsKey(weekNumber)) {
                        this.actPerUserWeekMap.get(user).put(weekNumber, new ArrayList<>());
                    }
                    for (JobTurn requiredProfile : activityTurn.getRequiredProfiles()) {
                        this.actPerUserWeekMap.get(user).get(weekNumber).add(riccardinoJobsActivityMap.get(requiredProfile));
                    }
                }
            }

        }

        Set<User> uuu2 = this.actPerUserWeekMap.keySet();
        for (User user : uuu2) {
            Map<Integer, List<it.cnr.istc.sponsor.solver.sponsorsolver.Activity>> weekMaps = this.actPerUserWeekMap.get(user);
            for (List<it.cnr.istc.sponsor.solver.sponsorsolver.Activity> weekActs : weekMaps.values()) {
                Person get = this.personMap.get(user);
                System.out.println("USER -> > " + user);
                System.out.println("PERGON GET <_> " + get + " weekActs: "+weekActs.size()+" user.getMaxTurn: "+this.personMap.get(user).getMaxTurnPerWeek());
                solver.at_most_n(user, weekActs.toArray(new it.cnr.istc.sponsor.solver.sponsorsolver.Activity[weekActs.size()]), this.personMap.get(user).getMaxTurnPerWeek());
            }
        }

        sendMessage("calcolo vincoli speciali (4)[OK]");
        try {
            Thread.sleep(300);
        } catch (InterruptedException ex) {
            Logger.getLogger(SolverManager4.class.getName()).log(Level.SEVERE, null, ex);
        }

        sendMessage(
                "calcolo vincoli speciali (5)[groupkeys]");

        for (Activity activity : activities) {
            for (ActivityTurn activityTurn : activity.getActivityTurns()) {

                if (activityTurn.containsKeywords()) {
                    List<User> bravePersone = new ArrayList<>();
//                    JOptionPane.showMessageDialog(null, "C'è UNA CHIAVE!!!"+activityTurn.getWantedKeywords().stream().map(i -> i.getKeyword()).collect(Collectors.joining(", ")));
//                    System.out.println("C'è UNA CHIAVE!!!" + activityTurn.getWantedKeywords().stream().map(i -> i.getKeyword()).collect(Collectors.joining(", ")));
                    for (Person person : people) {
                        if (!person.isSleeping()) {
                            if (activityTurn.isThisPersonOk(person)) {
//                                System.out.println("PERSONA OK: " + person);
                                bravePersone.add(this.userMap.get(person));
                            }
                        }
                    }
                    it.cnr.istc.sponsor.solver.sponsorsolver.Activity[] rris = new it.cnr.istc.sponsor.solver.sponsorsolver.Activity[activityTurn.getRequiredProfiles().size()];

                    int y = 0;
                    for (JobTurn requiredProfile : activityTurn.getRequiredProfiles()) {
                        rris[y] = this.riccardinoJobsActivityMap.get(requiredProfile);
                        y++;
                    }
                    if (bravePersone.isEmpty()) {
                        error("nessun utente possiede una o più chiavi specificate per il turno: " + activityTurn);
                        return;
                    }
                    User[] usersss = bravePersone.toArray(new User[bravePersone.size()]);
                    solver.at_least_one_of(rris, usersss);

                } else {
                    System.out.println("non me ne frega niente tanto questo turno non ha keys");
                }
            }
        }

        sendMessage(
                "calcolo vincoli speciali (5)[groupkeys][OK]");
        try {
            Thread.sleep(300);
        } catch (InterruptedException ex) {
            Logger.getLogger(SolverManager4.class.getName()).log(Level.SEVERE, null, ex);
        }

        sendMessage("calcolo vincoli speciali (6)[onlythese]..");

        for (Person p : people) {
            List<it.cnr.istc.sponsor.solver.sponsorsolver.Activity> coseChePossoFare = new ArrayList<>();
            if (!p.getOnlyTheseActivities().isEmpty()) {
                List<ActivityName> onlyTheseActivities = p.getOnlyTheseActivities();
                for (ActivityName onlyTheseActivity : onlyTheseActivities) {
                    Activity activity = nameActivityMap.get(onlyTheseActivity);
                    if (activity == null) {
                        continue;
                    }
                    for (ActivityTurn activityTurn : activity.getActivityTurns()) {
                        for (JobTurn requiredProfile : activityTurn.getRequiredProfiles()) {
                            coseChePossoFare.add(riccardinoJobsActivityMap.get(requiredProfile));
                        }
                    }
                }
                if(coseChePossoFare.isEmpty()){
                    JOptionPane.showMessageDialog(null, "" + p + " non può fare nessuna delle attività che hai selezionato","Errore",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                solver.only_in(new User[]{userMap.get(p)}, coseChePossoFare.toArray(new it.cnr.istc.sponsor.solver.sponsorsolver.Activity[coseChePossoFare.size()]));

            }

        }

        sendMessage("calcolo vincoli speciali (6)[onlythese][OK]");
        try {
            Thread.sleep(300);
        } catch (InterruptedException ex) {
            Logger.getLogger(SolverManager4.class.getName()).log(Level.SEVERE, null, ex);
        }

        boolean init = false;
        sendMessage("inizializzando il solver.. ");
        try {
            init = solver.init();
            sendMessage("Solver Inizializzato [" + init + "][OK]");
        } catch (AssertionError ex) {
            ex.printStackTrace();
            sendMessage("Errore interno, arrestare il solver..");
            System.out.println("errore e continuo..");
            for (SolverListener solverListener : solverListeners) {
                solverListener.error("Soluzione non trovata!");
            }
            return;
        }

        System.out.println(
                "init solving.. " + init);
        if (!init) {
            Collection<Explanation> explanations = solver.getExplanations();
            manageExplanation(explanations);
            sendMessage("Impossibile trovare una soluzione");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SolverManager4.class.getName()).log(Level.SEVERE, null, ex);
            }
            return;
        }

        this.currentValue = 0;

        // clean comfirmed turns
        Collection<JobTurn> values = jobTurnmap.values();
        for (JobTurn value : values) {
            value.getTurn().getComfirmedTurns().clear();
        }
        //----------------------
        sendMessage("solving.. ");

        boolean primociclo = true;

        while (solver.solve(solution)) {
            for (ActivityTurn allTurn : allTurns) {
                allTurn.getComfirmedTurns().clear();
            }
            for (int i = 0; i < people.size(); i++) {
                System.out.println("\t\tATTIVITA' di " + people.get(i));
                List<it.cnr.istc.sponsor.solver.sponsorsolver.Activity> actPerson = solution.get(userMap.get(people.get(i)));
                for (it.cnr.istc.sponsor.solver.sponsorsolver.Activity activity : actPerson) {
                    System.out.println("\t\t\tJOB: " + jobTurnmap.get(activity.id).getJob().toString());
                    ActivityTurn turn = jobTurnmap.get(activity.id).getTurn();
                    if (!turn.isThisPersonConfirmed(people.get(i))) {
                        turn.getComfirmedTurns().add(new ComfirmedTurn(people.get(i), jobTurnmap.get(activity.id)));
                    } else {
                        System.out.println("duplicate");
                    }
                }
            }
            this.currentValue++;
            System.out.println("soluzione temporanea!");
            for (SolverListener solverListener : solverListeners) {
                solverListener.temporarySolutionFound(this.currentValue);
            }
            if (primociclo) {
                primociclo = false;
            }
        }

        if (primociclo) {
            try {
                sendMessage("Interruzione dopo il primo ciclo");
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SolverManager4.class.getName()).log(Level.SEVERE, null, ex);
            }

            manageExplanation(solver.getExplanations()
            );
            this.stop();
            return;
        }

        System.out.println(
                "solution ottima");
        if (currentValue
                == 0) {
            //JOptionPane.showMessageDialog(null, "ERRORE NIENTE BUONO");
            for (SolverListener solverListener : solverListeners) {
                solverListener.error("Soluzione non trovata!");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SolverManager4.class.getName()).log(Level.SEVERE, null, ex);
                }
                return;
            }
            return;
        }
        for (SolverListener solverListener : solverListeners) {
            solverListener.solutionFound();
        }

        JOptionPane.showMessageDialog(
                null, "E' stata trovata la soluzione Ottima", "Soluzione Trovata!", JOptionPane.INFORMATION_MESSAGE);

    }

    @Override
    public void newActivityEvent(Activity activity) {
    }

    @Override
    public void removeActivityEvent(Activity activity) {
        this.weekActivityMap.remove(activity);
        this.personForcedForActivityMap.remove(activity);

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

    public void manageExplanation(Collection<Explanation> explanations) {
        if (explanations.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Il Risolutore non è riuscito a trovare una soluzione, non è possibile risalire alla causa di insolvibilità","Messaggio",JOptionPane.ERROR_MESSAGE);
            error("Per questo piano non c'è soluzione. Non ci sono ulteriori dettagli.");
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(SolverManager4.class.getName()).log(Level.SEVERE, null, ex);
//            }

        }
        for (Explanation explanation : explanations) {
            Person person;
            error("" + explanation.type + " << --- EXP TYPE");
            System.out.println("[TTTTTTTTTTTTTTTTTTTTTTTTTTTTTT] explanation.type " + explanation.type);
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(SolverManager4.class.getName()).log(Level.SEVERE, null, ex);
//            }
            switch (explanation.type) {
                case IdlerUser:
                    person = personMap.get(((IdlerUserExplanation) explanation).user);
                    sendMessage("" + person + " non può essere assegnata");
                    System.out.println("[TTTTTTTTTTTTTTTTTTTTTTTTTTTTTT]" + person + " non può essere assegnata");
//                    try {
//                        Thread.sleep(3000);
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(SolverManager4.class.getName()).log(Level.SEVERE, null, ex);
//                    }
                    break;

                case UnassignableActivity:
                    it.cnr.istc.sponsor.solver.sponsorsolver.Activity activity = ((UnassignableActivityExplanation) explanation).activity;

                    sendMessage("L'attività: " + activity + " non può essere svolta da nessuno!");
                    System.out.println("L'attività: " + activity + " non può essere svolta da nessuno!");
//                    try {
//                        Thread.sleep(3000);
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(SolverManager4.class.getName()).log(Level.SEVERE, null, ex);
//                    }
                    break;
                case LessThanN:
                    person = personMap.get(((LessThanNExplanation) explanation).user);
                    sendMessage("Non ci sono schedulate abbastanza attività eseguibili da " + person);
                    System.out.println("Non ci sono schedulate abbastanza attività eseguibili da " + person);
//                    try {
//                        Thread.sleep(3000);
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(SolverManager4.class.getName()).log(Level.SEVERE, null, ex);
//                    }
                    break;
                case AnyAmong:
                    it.cnr.istc.sponsor.solver.sponsorsolver.Activity[] acts = ((AnyAmongExplanation) explanation).as;
                    //elenco attività

                    //quel gruppo di utenti non va bene per quel gruppo di attività. .. se quel gruppo coincide con UN TURNO.. 
                    sendMessage("Il gruppo di volontari { } non può svolgere le seguenti attività {} " + acts);
                    System.out.println("Il gruppo di volontari { } non può svolgere le seguenti attività {} " + acts);
//                    try {
//                        Thread.sleep(3000);
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(SolverManager4.class.getName()).log(Level.SEVERE, null, ex);
//                    }
                    break;
                case OverlappingActivities:
                    //le attivi
                    it.cnr.istc.sponsor.solver.sponsorsolver.Activity[] acts2 = ((OverlappingActivitiesExplanation) explanation).activities;
                    sendMessage("Queste attività non possono essere svolte contemporaneamente perché non ci sono abbastanza persone per svolgere così come sono");
                    System.out.println("Queste attività non possono essere svolte contemporaneamente perché non ci sono abbastanza persone per svolgere così come sono");
//                    try {
//                        Thread.sleep(3000);
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(SolverManager4.class.getName()).log(Level.SEVERE, null, ex);
//                    }
                    break;

                default:
                    sendMessage("Errore staordinario: " + explanation.type.name());
                    System.out.println("Errore staordinario: " + explanation.type.name());
//                    try {
//                        Thread.sleep(3000);
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(SolverManager4.class.getName()).log(Level.SEVERE, null, ex);
//                    }
                    throw new AssertionError(explanation.type.name());
            }
        }
        sendMessage("Impossibile trovare una soluzione");
        System.out.println("Impossibile trovare una soluzione");
        return;

    }

    @Override
    public void ghostNumberUpdated() {
    }

    @Override
    public void timeAvailableChanged() {
    }
}
