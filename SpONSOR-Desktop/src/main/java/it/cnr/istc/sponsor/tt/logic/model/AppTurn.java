/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.logic.model;

import java.util.Date;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class AppTurn {

    private Long id;
    private String codePerson;
    private String codeTurn;
    private String activity;
    private long startTime;
    private long endTime;
    private Date abortTime;
    private long time; //delay

    public AppTurn() {
    }

    public AppTurn(Long id, String codePerson, String codeTurn, String activity, long startTime, long endTime, Date abortTime, long time) {
        this.id = id;
        this.codePerson = codePerson;
        this.codeTurn = codeTurn;
        this.activity = activity;
        this.startTime = startTime;
        this.endTime = endTime;
        this.abortTime = abortTime;
        this.time = time;
    }
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodePerson() {
        return codePerson;
    }

    public void setCodePerson(String codePerson) {
        this.codePerson = codePerson;
    }

    public String getCodeTurn() {
        return codeTurn;
    }

    public void setCodeTurn(String codeTurn) {
        this.codeTurn = codeTurn;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
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

    public Date getAbortTime() {
        return abortTime;
    }

    public void setAbortTime(Date abortTime) {
        this.abortTime = abortTime;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
    
    
}
