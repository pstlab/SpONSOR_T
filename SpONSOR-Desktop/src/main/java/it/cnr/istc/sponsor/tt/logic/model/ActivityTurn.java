/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.logic.model;

import it.cnr.istc.sponsor.tt.logic.TrainerManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class ActivityTurn implements Comparable<ActivityTurn> {

    //1 = 30 minuti
//    private int month;
//    private int day;
//    private int startTime;
//    private int endTime;
    private List<JobTurn> requiredProfiles = new ArrayList<>();
//    private List<Job> otherProfiles = new ArrayList<>();

    private List<Person> candidates = new ArrayList<>();
    private List<ComfirmedTurn> comfirmedTurns = new ArrayList<>();

    private List<ActivityTurnKeyword> wantedKeywords = new ArrayList<>();
//    private List<Keyword> unwantedKeywords = new ArrayList<>();

    private transient List<Person> wantedPerson = new ArrayList<>();

    private Long startTime;
    private Long endTime;

    private transient long uniqueCode;
    private long id;

    public ActivityTurn(long id, Long startTime, Long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.id = id;
        uniqueCode = new Date().getTime();
    }

    public ActivityTurn(long startTime, long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        uniqueCode = new Date().getTime();

    }

    public ActivityTurn() {
        try {
            Thread.sleep(30);
        } catch (InterruptedException ex) {
            Logger.getLogger(ActivityTurn.class.getName()).log(Level.SEVERE, null, ex);
        }
        uniqueCode = new Date().getTime();
    }
    
    

    public boolean containsKeywords() {
        return !wantedKeywords.isEmpty();
    }

    public List<ComfirmedTurn> getComfirmedTurns() {
        return comfirmedTurns;
    }

    public List<Person> getWantedPerson() {
        return wantedPerson;
    }

    public long getUniqueCode() {
        return uniqueCode;
    }
    
    

    public void setWantedPerson(List<Person> wantedPerson) {
        this.wantedPerson = wantedPerson;
    }

    public void addWantedPerson(Person person) {
        this.wantedPerson.add(person);
    }

    public boolean isThisPersonOk(Person person) {
//        List<Keyword> keywords = person.getKeywords();
        if (this.containsKeywords()) {

            for (ActivityTurnKeyword keyword : wantedKeywords) {
                if (!person.getKeywords().contains(keyword.getKeyword())) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }

    }

    public long getId() {
        return id;
    }

    public List<ActivityTurnKeyword> getWantedKeywords() {
        return wantedKeywords;
    }

    public void setWantedKeywords(List<ActivityTurnKeyword> wantedKeywords) {
        this.wantedKeywords = wantedKeywords;
    }
    
    
    

    public boolean isThisPersonConteined(Person person) {
        for (Person candidate : candidates) {
            if (person.getName().equals(candidate.getName()) && person.getSurname().equals(candidate.getSurname())) {
                return true;
            }
        }
        return false;
    }

    public boolean isThisPersonConfirmed(Person person) {
        for (ComfirmedTurn ct : comfirmedTurns) {
            if (person.getName().equals(ct.getPerson().getName()) && person.getSurname().equals(ct.getPerson().getSurname())) {
                return true;
            }
        }
        return false;
    }

    public void setComfirmedTurns(List<ComfirmedTurn> comfirmedTurns) {
        this.comfirmedTurns = comfirmedTurns;
    }

    public void addComfirmedTurn(ComfirmedTurn comfirmedTurn) {
        this.comfirmedTurns.add(comfirmedTurn);
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public List<JobTurn> getRequiredProfiles() {
        return requiredProfiles;
    }

    public void setRequiredProfiles(List<JobTurn> requiredProfiles) {
        this.requiredProfiles = requiredProfiles;
    }

    public void addRequiredProfile(JobTurn profile) {
        this.requiredProfiles.add(profile);
    }

    public List<Person> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<Person> candidates) {
        this.candidates = candidates;
    }

    public void addCandidate(Person person) {
        this.candidates.add(person);
    }
    
    public void addWantedKeyword(ActivityTurnKeyword atk){
        this.wantedKeywords.add(atk);  
        
    }

    @Override
    public int compareTo(ActivityTurn o) {
//        return (new Integer(this.month * 1000 + this.day * 100 + this.startTime).compareTo(o.month * 1000 + o.day * 100 + o.startTime));
        return this.startTime.compareTo(o.getStartTime());
    }

    @Override
    public String toString() {
        return "TURN:  - START: " + new Date(this.startTime) + " - END: " + new Date(this.endTime);
    }

    public String toPagePDF() {
        String result = "";
        SimpleDateFormat dt = new SimpleDateFormat("HH:mm");
        result += (dt.format(getStartTime())) + " - ";
        result += (dt.format(getEndTime())) + ";";
        Activity a = TrainerManager.getInstance().getActivityByTurn(this);
        result += a.getActivityName().getName();
//         #14:00 - 18:00;Telepresenza!18:30 - 20:00;Servizio Mensa
        return result;
    }

}
