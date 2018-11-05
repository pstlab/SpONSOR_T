/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.test;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.Model;
import com.microsoft.z3.Optimize;
import com.microsoft.z3.Status;
import static com.microsoft.z3.Status.SATISFIABLE;
import static com.microsoft.z3.Status.UNKNOWN;
import static com.microsoft.z3.Status.UNSATISFIABLE;
import it.cnr.istc.i18n.annotations.I18N;
import it.cnr.istc.i18n.translator.Language;
import it.cnr.istc.i18n.translator.TranslatorManager;
import it.cnr.istc.sponsor.tt.logic.model.Activity;
import it.cnr.istc.sponsor.tt.logic.model.ActivityTurn;
import it.cnr.istc.sponsor.tt.logic.model.AvailableTimeToken;
import it.cnr.istc.sponsor.tt.logic.model.ComfirmedTurn;
import it.cnr.istc.sponsor.tt.logic.model.Person;
import it.cnr.istc.sponsor.tt.logic.model.Job;
import it.cnr.istc.sponsor.tt.logic.model.Skill;
import it.cnr.istc.sponsor.tt.logic.model.SkillValue;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class SolverTester {

    @I18N(key = "saluto")
    private String saluto;
    private Activity activity = null;
    private List<Person> people = null;

    public static void main(String[] args) {

        TranslatorManager.getInstance().loadLanguage(Language.IT);
        try {
            TranslatorManager.getInstance().translate();
        } catch (IOException ex) {
            Logger.getLogger(SolverTester.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Optimus Prime");
        SolverTester st = new SolverTester();
//        st.orderPerTime();
//        st.test1();

    }

//    public void orderPerTime() {
//
//        Skill s1 = new Skill("S1");
//        Skill s2 = new Skill("S2");
//        Skill s3 = new Skill("S3");
//        Skill s4 = new Skill("S4");
//        Skill s5 = new Skill("S5");
//
//        Job job_telesoccorso = new Job("Telesoccorso", s1);
//        Job job_supporto_spirituale = new Job("Supporto Spirituale", s1, s5);
//        Job job_help = new Job("Help", s4);
//        Job job_lavapiatti = new Job("Lavapiatti", s4);
//
//        people = new ArrayList<>();
//
//        Person p1 = new Person();
//        p1.setName("Giancarlo");
//        p1.setSurname("Furlacchio");
//        p1.addFreeSlot(1, 1, 18, 26);
//        p1.addFreeSlot(1, 1, 30, 42);
//        p1.addSkillValue(new SkillValue(s1, 2));
//        p1.addSkillValue(new SkillValue(s2, 8));
//        p1.addSkillValue(new SkillValue(s3, 11));
//        p1.addSkillValue(new SkillValue(s4, 3));
//        p1.addSkillValue(new SkillValue(s5, 13));
//
//        Person p2 = new Person();
//        p2.setName("Simona");
//        p2.setSurname("Sventura");
//
//        p2.addFreeSlot(1, 1, 38, 40);
//        p2.addFreeSlot(1, 1, 42, 44);
//        p2.addFreeSlot(1, 1, 22, 26);
//        p2.addFreeSlot(1, 1, 27, 33);
//        p2.addSkillValue(new SkillValue(s1, 12));
//        p2.addSkillValue(new SkillValue(s2, 3));
//        p2.addSkillValue(new SkillValue(s3, 1));
//        p2.addSkillValue(new SkillValue(s4, 3));
//        p2.addSkillValue(new SkillValue(s5, 5));
//
//        Person p3 = new Person();
//        p3.setName("Selvaggia");
//        p3.setSurname("Ciondolante");
//        p3.addFreeSlot(1, 1, 10, 20);
//        p3.addFreeSlot(1, 1, 30, 34);
//        p3.addSkillValue(new SkillValue(s1, 10));
//        p3.addSkillValue(new SkillValue(s2, 5));
//        p3.addSkillValue(new SkillValue(s3, 5));
//        p3.addSkillValue(new SkillValue(s4, 5));
//        p3.addSkillValue(new SkillValue(s5, 7));
//
//        Person p4 = new Person();
//        p4.setName("Adolfo");
//        p4.setSurname("Murato");
//        p4.addFreeSlot(1, 1, 2, 8);
//        p4.addFreeSlot(1, 1, 16, 20);
//        p4.addFreeSlot(1, 1, 21, 30);
//        p4.addSkillValue(new SkillValue(s1, 9));
//        p4.addSkillValue(new SkillValue(s2, 8));
//        p4.addSkillValue(new SkillValue(s3, 7));
//        p4.addSkillValue(new SkillValue(s4, 15));
//        p4.addSkillValue(new SkillValue(s5, 6));
//
//        Person p5 = new Person();
//        p5.setName("Ignazio");
//        p5.setSurname("Valfrutta");
//        p5.addFreeSlot(1, 1, 10, 24);
//        p5.addFreeSlot(1, 1, 36, 44);
//        p5.addSkillValue(new SkillValue(s1, 12));
//        p5.addSkillValue(new SkillValue(s2, 12));
//        p5.addSkillValue(new SkillValue(s3, 6));
//        p5.addSkillValue(new SkillValue(s4, 3));
//        p5.addSkillValue(new SkillValue(s5, 1));
//
//        people.add(p1);
//        people.add(p2);
//        people.add(p3);
//        people.add(p4);
////        people.add(p5);
//
//        activity = new Activity();
//        activity.setName("Teleassistenza");
//
//        ActivityTurn turn1 = new ActivityTurn(1, 1, 18, 22);
//        turn1.addRequiredProfile(new Job("Help", s1));
//        turn1.addRequiredProfile(new Job("Help", s2));
////        turn1.addOtherProfile(job_supporto_spirituale);
//
//        ActivityTurn turn2 = new ActivityTurn(1, 1, 22, 30);
//        turn2.addRequiredProfile(new Job("Telesoccorso", s3));
//        turn2.addRequiredProfile(new Job("Help", s4));
//
//        activity.addActivityTurn(turn1);
//        activity.addActivityTurn(turn2);
//
//        //FINDING CANDIDATES
//        for (Person person : people) {
//            for (ActivityTurn activityTurn : activity.getActivityTurns()) {
//
//                System.out.println("TURN: " + activityTurn);
//
//                for (AvailableTimeToken availableTime : person.getAvailableTimes()) {
//
//                    System.out.println("\tTEMPO LIBERO: " + availableTime);
//
//                    if (activityTurn.getStartTime() >= availableTime.getStartTime() && activityTurn.getEndTime() <= availableTime.getEndTime()) {
//                        activityTurn.getCandidates().add(person);
//                    }
//
//                }
//                System.out.println("------------------------------");
//            }
//            System.out.println(" ==================================");
//        }
//
//        //CANDIDATES ARE FOUND:
//        //SHOW CANDIDATES :
//        for (ActivityTurn activityTurn : activity.getActivityTurns()) {
//            System.out.println("TURN FROM: " + activityTurn.getStartTime());
//            System.out.println("       TO: " + activityTurn.getEndTime());
//            List<Job> requiredProfiles = activityTurn.getRequiredProfiles();
//            for (Job requiredProfile : requiredProfiles) {
//                System.out.println("REQUIRED PROFILES: ");
//                System.out.println("\t" + requiredProfile.getName());
//                System.out.println("\twith skills:");
//                for (Skill s : requiredProfile.getSkills()) {
//                    System.out.println("\t" + s.getName());
//                }
//                System.out.println("------------------------------------");
//            }
//            System.out.println("PERSONE CANDIDATE:");
//            for (Person candidate : activityTurn.getCandidates()) {
//                System.out.println("\t - " + candidate.getName() + " " + candidate.getSurname());
//                System.out.println("\t\tSKILLS:");
//                List<SkillValue> skillValues = candidate.getSkillValues();
//                for (SkillValue skillValue : skillValues) {
//                    System.out.print("[" + skillValue.getSkill().getName() + " " + skillValue.getValue() + "]");
//                }
//                System.out.println("");
//            }
//            System.out.println("\n================================================");
//        }
//    }

//    public void test1() {
//        System.out.println(saluto);
//
//        System.out.println("****************************************************");
//        System.out.println("                   TEST SOLVER");
//        System.out.println("****************************************************");
//
//        System.out.println("ANALYZING ACTIVITY: " + this.activity.getName());
//
//        HashMap<String, String> cfg = new HashMap<String, String>();
//        cfg.put("model", "true");
//        Context ctx = new Context(cfg);
//        Optimize optimizer = ctx.mkOptimize();
//
//        //F IntExpr[][] solutionMatrix = new IntExpr[people.size()][activity.getActivityTurns().size()];
//        IntExpr[][] solutionMatrix = new IntExpr[people.size()][activity.getTotalJobs()];
//
//        List<ArithExpr> matchingList = new ArrayList<>();
//
//        Map<String, IntExpr> directMap = new HashMap<>();
//        Map<Person, Integer> peopleIdMap = new IdentityHashMap<>();
//        Map<Job, Integer> jobsIdMap = new IdentityHashMap<>();
//        int i = 0;
//        for (Person person : people) {
//            peopleIdMap.put(person, i);
//            i++;
//        }
//
//        int j = 0;
//        for (ActivityTurn activityTurn : activity.getActivityTurns()) {
//            for (Job job : activityTurn.getRequiredProfiles()) {
//                jobsIdMap.put(job, j);
//                j++;
//            }
//        }
//
//        
//        i = 0;
//        
//        for (Person person : people) {
//            j = 0;
//            for (ActivityTurn activityTurn : activity.getActivityTurns()) {
//                for (Job job : activityTurn.getRequiredProfiles()) {
//                    IntExpr x = ctx.mkIntConst(person.getName() + "->" + activity.getName() + "->" + job.getName() + j + "->" + activityTurn.toString());
//
//                    directMap.put(person.getName() + "->" + activity.getName() + "->" + job.getName() + j + "->" + activityTurn.toString(), x);
//                    BoolExpr vincoloFalse = ctx.mkLe(x, ctx.mkInt(1));
//                    BoolExpr vincoloTrue = ctx.mkGe(x, ctx.mkInt(0));
//                    optimizer.Add(vincoloTrue, vincoloFalse);
//                    solutionMatrix[i][j] = x;
////                if(la persona non appartiene ai candidati){
//                    // non la inseriamo e poniamo x = 0
////            }
////
//                    IntExpr k = ctx.mkInt(Integer.toString(person.calculateActivityValue(job)));
//                    matchingList.add(ctx.mkMul(k, x));
//
////                    if((person.getName() + "->" + activity.getName() + "->" + job.getName() + j + "->" + activityTurn.toString()).equals("Giancarlo->Teleassistenza->Telesoccorso2->TURN: M: 1 - D: 1 - START: 22 - END: 30")){
////                        System.out.println("ANDIAMO A COMANDAREEEEEEEEEEEEEE");
////                        BoolExpr falsissimo  = ctx.mkEq(x, ctx.mkInt(0));
////                        optimizer.Add(falsissimo);
////                    }
//                    j++;
//                }
//
//            }
//            i++;
//        }
//
//        Optimize.Handle handle = optimizer.MkMaximize(ctx.mkAdd(matchingList.toArray(new ArithExpr[matchingList.size()])));
//
////        System.out.println("H A  N D LE "+handle);
////        for (j = 0; j < solutionMatrix.length; j++) {
////            BoolExpr b1 = ctx.mkGe(ctx.mkAdd(solutionMatrix[j]), ctx.mkInt("1"));
////            optimizer.Add(b1);
////            IntExpr[] colonna = new IntExpr[people.size()];
////            for (int k = 0; k < activity.getActivityTurns().size(); k++) {
////                colonna[k] = solutionMatrix[j][k];
////            }
////
////            BoolExpr b2 = ctx.mkEq(ctx.mkAdd(colonna), ctx.mkInt("1"));
////            optimizer.Add(b2);
////
////        }
//        for (j = 0; j < solutionMatrix.length; j++) {
//            BoolExpr b1 = ctx.mkGe(ctx.mkAdd(solutionMatrix[j]), ctx.mkInt("1"));
//            optimizer.Add(b1);
//        }
//
//        for (int k = 0; k < activity.getTotalJobs(); k++) {
//            IntExpr[] colonna = new IntExpr[people.size()];
//            for (int l = 0; l < solutionMatrix.length; l++) {
//                colonna[l] = solutionMatrix[l][k];
//            }
//            BoolExpr b2 = ctx.mkEq(ctx.mkAdd(colonna), ctx.mkInt("1"));
//            optimizer.Add(b2);
//        }
//
//        //------------------ SOVRAPPOSIZIONI
//        // For each pulse the atoms starting at that pulse
//        int totalJobs = activity.getTotalJobs();
//        Map<Long, Collection<Job>> starting_values = new HashMap<>(totalJobs);
//        // For each pulse the atoms ending at that pulse
//        Map<Long, Collection<Job>> ending_values = new HashMap<>(totalJobs);
//        // The pulses of the timeline
//        Set<Long> c_pulses = new HashSet<>(totalJobs * 2);
//
//        for (ActivityTurn activityTurn : activity.getActivityTurns()) {
//            long start_pulse = activityTurn.getStartTime().getTime();
//            long end_pulse = activityTurn.getEndTime().getTime();
//
//            if (!starting_values.containsKey(start_pulse)) {
//                starting_values.put(start_pulse, new ArrayList<>(activityTurn.getRequiredProfiles().size()));
//            }
//            starting_values.get(start_pulse).addAll(activityTurn.getRequiredProfiles());
//
//            if (!ending_values.containsKey(end_pulse)) {
//                ending_values.put(end_pulse, new ArrayList<>(activityTurn.getRequiredProfiles().size()));
//            }
//            ending_values.get(end_pulse).addAll(activityTurn.getRequiredProfiles());
//            c_pulses.add(start_pulse);
//            c_pulses.add(end_pulse);
//        }
//
//        // Sort current pulses
//        Long[] c_pulses_array = c_pulses.toArray(new Long[c_pulses.size()]);
//        Arrays.sort(c_pulses_array);
//
//        // Push values to timeline according to pulses...
//        List<Job> overlapping_formulas = new ArrayList<>();
//        if (starting_values.containsKey(c_pulses_array[0])) {
//            overlapping_formulas.addAll(starting_values.get(c_pulses_array[0]));
//        }
//        if (ending_values.containsKey(c_pulses_array[0])) {
//            overlapping_formulas.removeAll(ending_values.get(c_pulses_array[0]));
//        }
//        for (int m = 1; m < c_pulses_array.length; m++) {
//            if (overlapping_formulas.size() > 1) {
//
//                for (Person person : people) {
//                    List<ArithExpr> overLappingJobs = new ArrayList<>();
//                    for (Job job : overlapping_formulas) {
//                        IntExpr overlappingJob = solutionMatrix[peopleIdMap.get(person)][jobsIdMap.get(job)];
//                        overLappingJobs.add(overlappingJob);
//                    }
//                    BoolExpr vinc = ctx.mkLe(ctx.mkAdd(overLappingJobs.toArray(new ArithExpr[overLappingJobs.size()])), ctx.mkInt("1"));
//                    optimizer.Add(vinc);
//
//                }
//
//            }
//            if (starting_values.containsKey(c_pulses_array[m])) {
//                overlapping_formulas.addAll(starting_values.get(c_pulses_array[m]));
//            }
//            if (ending_values.containsKey(c_pulses_array[m])) {
//                overlapping_formulas.removeAll(ending_values.get(c_pulses_array[m]));
//            }
//        }
//
//        //-------------------------------
//        System.out.println("sto per trovare una soluzione");
//
//        Status status = optimizer.Check(); //trova una soluzione
//
//        System.out.println(
//                "ho trovato una soluzione");
//
//        System.out.println(
//                "STATUS = " + status);
//
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
//                for (Person candidate : people) {
//                    j = 0;
//                    for (ActivityTurn activityTurn : activity.getActivityTurns()) {
//                        
//                        for (Job job : activityTurn.getRequiredProfiles()) {
////                            IntExpr x = ctx.mkIntConst(person.getName() + "->" + activity.getName() + "->" + job.getName() + j + "->" + activityTurn.toString());
//                            //person.getName() + "->" + activity.getName() + "->" + job.getName() + j + "->" + activityTurn.toString()
//                            IntExpr var = directMap.get(candidate.getName() + "->" + activity.getName() + "->" + job.getName() + j + "->" + activityTurn.toString());
//                            Expr eval = model.eval(var, true);
//                            System.out.println("VAR: "+var +" ->  VALORE: " + eval);
////                            System.out.println("VAR: " + var.);
//                            if(eval.toString().equals("1")){
////                                System.out.println("TOGLIERE");
//                                activityTurn.addComfirmedTurn(new ComfirmedTurn(candidate, job));
//                            }
//                            j++;
//                        }
//                    }
//                }
//                System.out.println("***************************************************************");
//                System.out.println("                       HIGH LEVEL ");
//                System.out.println("***************************************************************");
//                   for (ActivityTurn activityTurn : activity.getActivityTurns()) {
//                       System.out.println("TURNO: "+activityTurn);
//                       for (ComfirmedTurn ct : activityTurn.getComfirmedTurns()) {
//                            System.out.println("A QUESTO TURNO SARANNO PRESENTI: "+ct.getPerson().getName()+ " at "+ct.getJob().getName());
//                       }
//                       System.out.println("---------------------------------");
//                   }
//                
//                break;
//            }
//            case UNKNOWN:
//                System.out.println("boooh");
//                break;
//            case UNSATISFIABLE:
//                Model model = optimizer.getModel();
//                System.out.println("OPTIMIZER: \n" + optimizer);
//                System.out.println("NOT BUONO");
//                break;
//        }

//    }

}
