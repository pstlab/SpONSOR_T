/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.logic.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it>
 */
public class JobTurn {
    
    private Job job;
    private List<Keyword> wantedKeywords = new ArrayList<>();
    private List<Keyword> unwantedKeywords = new ArrayList<>();
    private transient ActivityTurn turn;
    private Person wantedPerson;
    

    public JobTurn() {
    }

    public JobTurn(Job job, ActivityTurn turn) {
        this.job = job;
        this.turn = turn;
        
    }
    
     public JobTurn(Job job, ActivityTurn turn, List<Keyword> wantedKeywords, List<Keyword> unwantedKeywords, Person wantedPerson) {
        this.job = job;
        this.turn = turn;
        this.wantedKeywords = wantedKeywords;
        this.unwantedKeywords = unwantedKeywords;
        this.wantedPerson = wantedPerson;
    }

    public void setWantedPerson(Person wantedPerson) {
        this.wantedPerson = wantedPerson;
    }

    public Person getWantedPerson() {
        return wantedPerson;
    }
     
     
    
    public boolean containsKeywords(){
        return !wantedKeywords.isEmpty() || !unwantedKeywords.isEmpty();
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public List<Keyword> getWantedKeywords() {
        return wantedKeywords;
    }

    public void setWantedKeywords(List<Keyword> wantedKeywords) {
        this.wantedKeywords = wantedKeywords;
    }

    
    public void addWantedKeyword(Keyword wantedKeyword) {
        this.wantedKeywords.add(wantedKeyword);
    }
    
    public void addUnwantedKeyword(Keyword unwantedKeyword) {
        this.unwantedKeywords.add(unwantedKeyword);
    }
    
    public List<Keyword> getUnwantedKeywords() {
        return unwantedKeywords;
    }

    public void setUnwantedKeywords(List<Keyword> unwantedKeywords) {
        this.unwantedKeywords = unwantedKeywords;
    }

    public ActivityTurn getTurn() {
        return turn;
    }

    public void setTurn(ActivityTurn turn) {
        this.turn = turn;
    }
    
    public boolean isThisPersonOk(Person person){
        List<Keyword> keywords = person.getKeywords();
        if(this.containsKeywords()){
            
            for (Keyword keyword : wantedKeywords) {
                if(!person.getKeywords().contains(keyword)){
                    return false;
                }
            }
            for (Keyword keyword : unwantedKeywords) {
                if(person.getKeywords().contains(keyword)){
                    return false;
                }
            }
            return true;
        }else{
            return true;
        }
        
    }
    
    
}
