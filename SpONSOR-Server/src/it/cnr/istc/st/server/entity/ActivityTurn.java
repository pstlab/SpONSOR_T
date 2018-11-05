/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.st.server.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
@Entity
public class ActivityTurn implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany()
    private List<JobTurn> requiredProfiles = new ArrayList<>();
    
//    @OneToMany(fetch=FetchType.EAGER)
//    @JoinColumn(nullable = true)
//    private List<Keyword> wantedKeywords = new ArrayList<>();
//    @JoinColumn(nullable = true)
//    @OneToMany(fetch=FetchType.EAGER)
//    private List<Keyword> unwantedKeywords = new ArrayList<>();
    
    @OneToMany
    private List<ActivityTurnKeyword> wantedKeywords = new ArrayList<>();
    
//    @OneToMany
//    @JoinColumn(nullable = true)
//    private List<Job> otherProfiles = new ArrayList<>();

    @OneToMany
    private List<Person> candidates = new ArrayList<>();
    @OneToMany 
    @JoinColumn(nullable = true)
    private List<ComfirmedTurn> comfirmedTurns = new ArrayList<>();

//    @Temporal(javax.persistence.TemporalType.DATE)
    private Long startTime;
//    @Temporal(javax.persistence.TemporalType.DATE)
    private Long endTime;

    public ActivityTurn() {
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ActivityTurnKeyword> getWantedKeywords() {
        return wantedKeywords;
    }

    public void setWantedKeywords(List<ActivityTurnKeyword> wantedKeywords) {
        this.wantedKeywords = wantedKeywords;
    }
    
    public void addWantedKeyword(ActivityTurnKeyword atk){
        this.wantedKeywords.add(atk);  
        
    }

//    public List<Keyword> getWantedKeywords() {
//        return wantedKeywords;
//    }
//
//    public void setWantedKeywords(List<Keyword> wantedKeywords) {
//        this.wantedKeywords = wantedKeywords;
//    }
//
//    public List<Keyword> getUnwantedKeywords() {
//        return unwantedKeywords;
//    }
//
//    public void setUnwantedKeywords(List<Keyword> unwantedKeywords) {
//        this.unwantedKeywords = unwantedKeywords;
//    }

    public List<JobTurn> getRequiredProfiles() {
        return requiredProfiles;
    }

    public void setRequiredProfiles(List<JobTurn> requiredProfiles) {
        this.requiredProfiles = requiredProfiles;
    }

    

//    public List<Job> getOtherProfiles() {
//        return otherProfiles;
//    }
//
//    public void setOtherProfiles(List<Job> otherProfiles) {
//        this.otherProfiles = otherProfiles;
//    }

    public List<Person> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<Person> candidates) {
        this.candidates = candidates;
    }

    public List<ComfirmedTurn> getComfirmedTurns() {
        return comfirmedTurns;
    }

    public void setComfirmedTurns(List<ComfirmedTurn> comfirmedTurns) {
        this.comfirmedTurns = comfirmedTurns;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ActivityTurn)) {
            return false;
        }
        ActivityTurn other = (ActivityTurn) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "id: "+id+" startTime: "+new Date(this.startTime)+ ", endTime: "+new Date(this.endTime);
    }
    
}
