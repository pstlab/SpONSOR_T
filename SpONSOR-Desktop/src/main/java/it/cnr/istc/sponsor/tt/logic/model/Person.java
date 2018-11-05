/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.logic.model;

import com.google.gson.Gson;
import it.cnr.istc.sponsor.tt.logic.SettingsManager;
import it.cnr.istc.sponsor.tt.logic.TrainerManager;
import it.cnr.istc.sponsor.tt.logic.Utils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class Person implements Comparable<Person>{

    private Long id;
    private String nickname;
    private String password;
    private String name;
    private String surname;
    private String otherData;
    private String plan;
    private String email;
    private String phone;
    private String note;
    private String code;
    private boolean admin;
    private boolean oneTurnPerDay = true;
    private int maxTurnPerWeek = 1;
    private boolean sleeping = false;

    private List<AvailableTimeToken> availableTimes = new ArrayList<>();
    private List<SkillValue> skillValues = new ArrayList<>();
    private transient Account account;
    private transient static final String NOT_QUESTIONED = "NOT_QUESTIONED";
    private boolean fixed = false;

    private List<FreeTimeToken> expandedFreeTimes = new ArrayList<>();
    private List<Keyword> keywords = new ArrayList<>();
    private List<ActivityName> onlyTheseActivities = new ArrayList<>();

    public Person() {

    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
        if (this.getAccount() != null) {
            this.getAccount().setAdmin(admin);
        }
    }

    public List<ActivityName> getOnlyTheseActivities() {
        return onlyTheseActivities;
    }

    public void setOnlyTheseActivities(List<ActivityName> onlyTheseActivities) {
        this.onlyTheseActivities = onlyTheseActivities;
        if (this.getAccount() != null) {
            this.getAccount().setOnlyTheseActivities(onlyTheseActivities);
        }
    }

    public void addOnlyThisActivity(ActivityName activityName) {
        this.onlyTheseActivities.add(activityName);
        if (this.getAccount() != null) {
            this.getAccount().getOnlyTheseActivities().add(activityName);
        }
    }

    public boolean isTurnDoable(ActivityTurn turn) {
        if (turn == null) {
            return false;
        } else {
            boolean doable = false;
            List<FreeTimeToken> expandedFreeTimes1 = this.getExpandedFreeTimes();
            for (FreeTimeToken freeTimeToken : expandedFreeTimes1) {
                if (freeTimeToken.getStarTime() - 1000 < turn.getStartTime() && freeTimeToken.getEndTime() + 1000 > turn.getEndTime()) {
                    doable = true;
                    break;
                }
            }

            return doable;
        }
    }

    public void fix() {
        if (fixed) {
            return;
        }
        this.password = "PASS";
        Gson gson = new Gson();
        if (otherData != null) {
            account = gson.fromJson(otherData, Account.class);
            account.setSleeping(sleeping);
          

            GregorianCalendar month1 = new GregorianCalendar();
            month1.setTime(new Date());
            month1.add(Calendar.MONTH, 1);
            List<Interval> intervals = account.getIntervals();
            this.availableTimes.clear();
            this.expandedFreeTimes.clear();
            for (Interval interval : intervals) {
                this.availableTimes.add(new AvailableTimeToken(
                        0,
                        interval.getDayOfWeek(),
                        interval.getStartHourOfDay() < SettingsManager.getInstance().getOpenTime() ? SettingsManager.getInstance().getOpenTime() : interval.getStartHourOfDay(),
                        interval.getEndHourOfDay() > SettingsManager.getInstance().getCloseTime() ? SettingsManager.getInstance().getCloseTime() : interval.getEndHourOfDay()
                ));
                System.out.println("PERSON (" + name + " " + surname + "), FIXING: interval { " + interval + " }");
                GregorianCalendar gcStart = new GregorianCalendar();
                Date startDate = new Date(Utils.getCurrentMonday().getTime());
//                startDate.setDate(interval.getDayOfWeek());
                startDate.setHours(interval.getStartHourOfDay());
                startDate.setMinutes(0);
                startDate.setSeconds(0);
                gcStart.setTime(startDate);
                gcStart.add(GregorianCalendar.DAY_OF_YEAR, interval.getDayOfWeek());

                GregorianCalendar gcEnd = new GregorianCalendar();
                Date endDate = new Date(Utils.getCurrentMonday().getTime());
//                endDate.setDate(interval.getDayOfWeek());
                endDate.setHours(interval.getEndHourOfDay());
                endDate.setMinutes(0);
                endDate.setSeconds(0);
                gcEnd.setTime(endDate);
                gcEnd.add(GregorianCalendar.DAY_OF_YEAR, interval.getDayOfWeek());

                long max = TrainerManager.getInstance().getEndPlanningDate() == null ? month1.getTime().getTime()+(3*1000l*60l*60l*24l*30l) : TrainerManager.getInstance().getEndPlanningDate().getTime();
                while (gcEnd.getTime().getTime() < max) {

                    this.expandedFreeTimes.add(new FreeTimeToken(gcStart.getTime().getTime(), gcEnd.getTime().getTime()));
                    gcStart.add(GregorianCalendar.WEEK_OF_YEAR, 1);
                    gcEnd.add(GregorianCalendar.WEEK_OF_YEAR, 1);
                }
                //this.addFreeSlot(-1, interval.getDayOfWeek(), interval.getStartHourOfDay(), interval.getEndHourOfDay());
            }

            parseData(account);
            account.setSleeping(sleeping);
            System.out.println("PARSED ACCOUNT: " + this.skillValues.size());

        }
        fixed = true;
    }

    public Person(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public boolean isOneTurnPerDay() {
        return oneTurnPerDay;
    }

    public void setOneTurnPerDay(boolean oneTurnPerDay) {
        this.oneTurnPerDay = oneTurnPerDay;
        if (this.getAccount() != null) {
            this.getAccount().setOneTurnPerDay(oneTurnPerDay);
        }
    }

    public int getMaxTurnPerWeek() {
        return maxTurnPerWeek;
    }

    public void setMaxTurnPerWeek(int maxTurnPerWeek) {
        this.maxTurnPerWeek = maxTurnPerWeek;
        if (this.getAccount() != null) {
            this.getAccount().setMaxTurnPerWeek(maxTurnPerWeek);
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        if (this.getAccount() != null) {
            this.getAccount().setEmail(email);
        }
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        if (this.getAccount() != null) {
            this.getAccount().setPhone(phone);
        }
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
        if (this.getAccount() != null) {
            this.getAccount().setNote(note);
        }
        
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<AvailableTimeToken> getAvailableTimes() {
        if (!isFixed()) {
            System.out.println("NOT FIXED");
            this.fix();
        }
        return availableTimes;
    }

    public void setAvailableTimes(List<AvailableTimeToken> availableTimes) {
        this.availableTimes = availableTimes;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<FreeTimeToken> getExpandedFreeTimes() {
        return expandedFreeTimes;
    }

    public Account getAccount() {
        if (account == null && !fixed) {
            fix();
        }
        return account;
    }

    public List<Keyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<Keyword> keywords) {
        this.keywords = keywords;
    }

    public String getNickname() {
        return nickname;
    }

    public boolean isSleeping() {
        return sleeping;
    }

    public void setSleeping(boolean sleeping) {
        this.sleeping = sleeping;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOtherData() {
        return otherData;
    }

    public void setOtherData(String otherData) {
        this.otherData = otherData;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        if (this.getAccount() != null) {
            this.getAccount().setName(name);
        }
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
        if (this.getAccount() != null) {
            this.getAccount().setSurname(surname);
        }
    }

//    public List<AvailableTimeToken> getAvailableTimes() {
//        return availableTimes;
//    }
//    public void addFreeSlot(int month, int day, int startTime, int endTime) {
//        this.availableTimes.add(new AvailableTimeToken(month, day, startTime, endTime));
//        Collections.sort(availableTimes);
//    }
    public List<SkillValue> getSkillValues() {
        return skillValues;
    }

    public void setSkillValues(List<SkillValue> skillValues) {
        this.skillValues = skillValues;
        
    }

    public void addSkillValue(SkillValue sv) {
        this.skillValues.add(sv);
    }

    public int calculateActivityValue(Job job) {
        int result = 0;
        for (Skill skill : job.getSkills()) {
            result += getSkillValue(skill);
        }
        //TODO CALCULATEEEE
        return result;
    }

    private int getSkillValue(Skill skill) {
        for (SkillValue skillValue : skillValues) {
            if (skill.getId().equals(skillValue.getSkill().getId())) {
                return skillValue.getValue();
            }
        }
        return 0;
    }

//    public void setDirectSkillValues(int C, int P, int S, int G, int E, int V, int L, int O){
//        Map<String, Map<String, Integer>> qData = account.getPerceptionQuestionnary();
//        qData.put(NOT_QUESTIONED, new HashMap<>());
//        qData.get(NOT_QUESTIONED).put("C",C);
//        qData.get(NOT_QUESTIONED).put("P",P);
//        qData.get(NOT_QUESTIONED).put("S",S);
//        qData.get(NOT_QUESTIONED).put("G",G);
//        qData.get(NOT_QUESTIONED).put("E",E);
//        qData.get(NOT_QUESTIONED).put("V",V);
//        qData.get(NOT_QUESTIONED).put("L",L);
//        qData.get(NOT_QUESTIONED).put("O",O);
//    }
    public void parseData(Account account) {
//        try{
        Map<String, Map<String, Integer>> qData = account.getPerceptionQuestionnary();
        Skill skill1 = ModelManager.getInstance().getAllSkills().get(0);
        Skill skill2 = ModelManager.getInstance().getAllSkills().get(1);
        Skill skill3 = ModelManager.getInstance().getAllSkills().get(2);
        Skill skill4 = ModelManager.getInstance().getAllSkills().get(3);
        Skill skill5 = ModelManager.getInstance().getAllSkills().get(4);
        Skill skill6 = ModelManager.getInstance().getAllSkills().get(5);
        Skill skill7 = ModelManager.getInstance().getAllSkills().get(6);
        Skill skill8 = ModelManager.getInstance().getAllSkills().get(7);
        System.out.println("SKILL NAME = "+skill1.getName() + " ---------> ");

        if (qData.containsKey(NOT_QUESTIONED)) {
            skillValues.add(new SkillValue(skill1, qData.get(NOT_QUESTIONED).get("P")));
            skillValues.add(new SkillValue(skill2, qData.get(NOT_QUESTIONED).get("S")));
            skillValues.add(new SkillValue(skill3, qData.get(NOT_QUESTIONED).get("G")));
            skillValues.add(new SkillValue(skill4, qData.get(NOT_QUESTIONED).get("V")));
            skillValues.add(new SkillValue(skill5, qData.get(NOT_QUESTIONED).get("C")));
            skillValues.add(new SkillValue(skill6, qData.get(NOT_QUESTIONED).get("E")));
            skillValues.add(new SkillValue(skill7, qData.get(NOT_QUESTIONED).get("L")));
            skillValues.add(new SkillValue(skill8, qData.get(NOT_QUESTIONED).get("O")));

            return;
        }

        int C = qData.get("Sezione I").get("g")
                + qData.get("Sezione II").get("a")
                + qData.get("Sezione III").get("h")
                + qData.get("Sezione IV").get("d")
                + qData.get("Sezione V").get("b")
                + qData.get("Sezione VI").get("f")
                + qData.get("Sezione VII").get("e");

        skillValues.add(new SkillValue(skill5, C));
//        
        int P = qData.get("Sezione I").get("d")
                + qData.get("Sezione II").get("b")
                + qData.get("Sezione III").get("a")
                + qData.get("Sezione IV").get("h")
                + qData.get("Sezione V").get("f")
                + qData.get("Sezione VI").get("c")
                + qData.get("Sezione VII").get("g");
        skillValues.add(new SkillValue(skill1, P));
//        
        int S = qData.get("Sezione I").get("f")
                + qData.get("Sezione II").get("e")
                + qData.get("Sezione III").get("c")
                + qData.get("Sezione IV").get("b")
                + qData.get("Sezione V").get("d")
                + qData.get("Sezione VI").get("g")
                + qData.get("Sezione VII").get("a");
        skillValues.add(new SkillValue(skill2, S));
//        
        int G = qData.get("Sezione I").get("c")
                + //                qData.get("Sezione II").get("g") +
                qData.get("Sezione III").get("d")
                + qData.get("Sezione IV").get("e")
                + qData.get("Sezione V").get("h")
                + qData.get("Sezione VI").get("a")
                + qData.get("Sezione VII").get("f");
        skillValues.add(new SkillValue(skill3, G));

        int E = qData.get("Sezione I").get("a")
                + qData.get("Sezione II").get("c")
                + qData.get("Sezione III").get("f")
                + qData.get("Sezione IV").get("g")
                + qData.get("Sezione V").get("e")
                + qData.get("Sezione VI").get("h")
                + qData.get("Sezione VII").get("d");
        skillValues.add(new SkillValue(skill6, E));

        int V = qData.get("Sezione I").get("h")
                + qData.get("Sezione II").get("d")
                + qData.get("Sezione III").get("g")
                + qData.get("Sezione IV").get("c")
                + qData.get("Sezione V").get("a")
                + qData.get("Sezione VI").get("e")
                + qData.get("Sezione VII").get("b");
        skillValues.add(new SkillValue(skill4, V));

        int L = qData.get("Sezione I").get("b")
                + qData.get("Sezione II").get("f")
                + qData.get("Sezione III").get("e")
                + qData.get("Sezione IV").get("a")
                + qData.get("Sezione V").get("c")
                + qData.get("Sezione VI").get("b")
                + qData.get("Sezione VII").get("h");
        skillValues.add(new SkillValue(skill7, L));

        int O = qData.get("Sezione I").get("e")
                + //                qData.get("Sezione II").get("h") +
                qData.get("Sezione III").get("b")
                + qData.get("Sezione IV").get("f")
                + qData.get("Sezione V").get("g")
                + qData.get("Sezione VI").get("d")
                + qData.get("Sezione VII").get("c");
        skillValues.add(new SkillValue(skill8, O));
//         //{"Activity/Role", "President", "Structure", "Brilliant", "Evaluator", "Concrete", "Explorer", "Worker", "Objectivist"}
//        System.out.println("PRESIDENT   = "+C);
//        System.out.println("STRUCTURE   = "+P);
//        System.out.println("BRILLIANT   = "+S);
//        System.out.println("EVALUATOR   = "+G);
//        System.out.println("CONCRETE    = "+E);
//        System.out.println("EXPLORER    = "+V);
//        System.out.println("WORKER      = "+L);
//        System.out.println("OBJECTIVIST = "+O);
        
        System.out.println("CONCRETO   = "+C);
        System.out.println("PRESIDENTE   = "+P);
        System.out.println("STRUTTURATORE   = "+S);
        System.out.println("GENIALE   = "+G);
        System.out.println("ESPLORATORE    = "+E);
        System.out.println("VALUTATORE    = "+V);
        System.out.println("LAVORATORE      = "+L);
        System.out.println("OBIETTIVISTA = "+O);
//        }catch(Exception ex){
//            ex.printStackTrace();

        System.out.println("SKILL ====================================");
        System.out.println("SKILL 1 = "+skill1.getName());
        System.out.println("SKILL 2 = "+skill2.getName());
        System.out.println("SKILL 3 = "+skill3.getName());
        System.out.println("SKILL 4 = "+skill4.getName());
        System.out.println("SKILL 5 = "+skill5.getName());
        System.out.println("SKILL 6 = "+skill6.getName());
        System.out.println("SKILL 7 = "+skill7.getName());
        System.out.println("SKILL 8 = "+skill8.getName());
    }

    @Override
    public String toString() {
        String res = this.password + " -> " + this.name + " " + this.surname + " {";
//
//        System.out.println("skill values: " + skillValues.size());
//        System.out.println("avaliale times: " + availableTimes.size());
        for (SkillValue skillValue : skillValues) {
            res += skillValue.getSkill().getName() + " = " + skillValue.getValue() + ", ";
        }
        System.out.println("RES = "+res);
////        res=res.substring(0, res.length()-1);
//        return res + "}";
        return this.name + " " + this.surname;
    }

    @Override
    public int compareTo(Person o) {
        return (this.name+this.surname).compareTo(o.getName()+o.getSurname());
    }

}
