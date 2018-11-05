/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.logic.model;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class Account implements Comparable<Account>{

    private Long id;
    private transient static final String NOT_QUESTIONED = "NOT_QUESTIONED";
    private String name;
    private String surname;
    private Gender gender = Gender.MALE;
    private Date bornDate;
    private boolean teleAssistent;
    private boolean animation;
    private boolean liveClose;
    private String email;
    private String phone;
    private String note;
    private boolean admin;
    private Map<String, Map<String, Integer>> perceptionQuestionnary = new HashMap<>();
    private List<Interval> intervals = new ArrayList<>();
    private boolean sleeping = false;
    private boolean oneTurnPerDay = true;
    private int maxTurnPerWeek = 1;
    private List<ActivityName> onlyTheseActivities = new ArrayList<>();
    private long dateOfBegin; // data di inizio lavori

    @Override
    public int compareTo(Account o) {
        return (this.name+this.surname).compareTo(o.getName()+o.getSurname());
    }
    

    public enum Gender {

        MALE,
        FEMALE
    }

    public boolean isAdmin() {
        return admin;
    }

    public boolean isOneTurnPerDay() {
        return oneTurnPerDay;
    }

    public void setOneTurnPerDay(boolean oneTurnPerDay) {
        this.oneTurnPerDay = oneTurnPerDay;
    }

    public int getMaxTurnPerWeek() {
        return maxTurnPerWeek;
    }

    public void setMaxTurnPerWeek(int maxTurnPerWeek) {
        this.maxTurnPerWeek = maxTurnPerWeek;
    }

    public List<ActivityName> getOnlyTheseActivities() {
        return onlyTheseActivities;
    }

    public void setOnlyTheseActivities(List<ActivityName> onlyTheseActivities) {
        this.onlyTheseActivities = onlyTheseActivities;
    }

    public long getDateOfBegin() {
        return dateOfBegin;
    }

    public void setDateOfBegin(long dateOfBegin) {
        this.dateOfBegin = dateOfBegin;
    }
    
    
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSleeping(boolean sleeping) {
        this.sleeping = sleeping;
    }

    public boolean isSleeping() {
        return sleeping;
    }
    
     

    public void setDirectSkillValues(int C, int P, int S, int G, int E, int V, int L, int O) {
        Map<String, Map<String, Integer>> qData = this.getPerceptionQuestionnary();
        qData.put(NOT_QUESTIONED, new HashMap<>());
        qData.get(NOT_QUESTIONED).put("C", C);
        qData.get(NOT_QUESTIONED).put("P", P);
        qData.get(NOT_QUESTIONED).put("S", S);
        qData.get(NOT_QUESTIONED).put("G", G);
        qData.get(NOT_QUESTIONED).put("E", E);
        qData.get(NOT_QUESTIONED).put("V", V);
        qData.get(NOT_QUESTIONED).put("L", L);
        qData.get(NOT_QUESTIONED).put("O", O);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public List<Interval> getIntervals() {
        return intervals;
    }

    public void setIntervals(List<Interval> intervals) {
        this.intervals = intervals;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Date getBornDate() {
        return bornDate;
    }

    public void setBornDate(Date bornDate) {
        this.bornDate = bornDate;
    }

    public boolean isTeleAssistent() {
        return teleAssistent;
    }

    public void setTeleAssistent(boolean teleAssistent) {
        this.teleAssistent = teleAssistent;
    }

    public boolean isAnimation() {
        return animation;
    }

    public void setAnimation(boolean animation) {
        this.animation = animation;
    }

    public boolean isLiveClose() {
        return liveClose;
    }

    public void setLiveClose(boolean liveClose) {
        this.liveClose = liveClose;
    }

    public Map<String, Map<String, Integer>> getPerceptionQuestionnary() {
        return perceptionQuestionnary;
    }

    public void setPerceptionQuestionnary(Map<String, Map<String, Integer>> perceptionQuestionnary) {
        this.perceptionQuestionnary = perceptionQuestionnary;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        String jsonMessage = gson.toJson(this);
//        System.out.println(jsonMessage);
        return jsonMessage;
    }

}
