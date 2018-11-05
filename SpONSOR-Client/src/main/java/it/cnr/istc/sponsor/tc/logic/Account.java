/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tc.logic;

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
public class Account {

    private String name;
    private String surname;
    private Gender gender = Gender.MALE;
    private Date bornDate;
    private boolean teleAssistent;
    private boolean animation;
    private boolean liveClose;
    private Map<String, Map<String,Integer>> perceptionQuestionnary = new HashMap<>();
    private List<Interval> intervals = new ArrayList<>();
    
    public enum Gender {

        MALE,
        FEMALE
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
    
    public void addSingleResult(String question, String answerCode, int point) {
        if (perceptionQuestionnary.containsKey(question)) {
            perceptionQuestionnary.get(question).put(answerCode, point);
        } else {
            perceptionQuestionnary.put(question, new HashMap<String, Integer>());
        }
    }

    public List<Interval> getIntervals() {
        return intervals;
    }

    public void setIntervals(List<Interval> intervals) {
        this.intervals = intervals;
    }
    
    public void addInterval(Interval interval){
        this.intervals.add(interval);
    }
    

    @Override
    public String toString() {
        Gson gson = new Gson();
        String jsonMessage = gson.toJson(this);
//        System.out.println(jsonMessage);
        return jsonMessage;
    }

}
