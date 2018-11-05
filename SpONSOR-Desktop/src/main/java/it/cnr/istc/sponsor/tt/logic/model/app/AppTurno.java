/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.logic.model.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class AppTurno implements Serializable {

    private List<AppCollegue> collegues = new ArrayList<>();
    private String activity;
    private long startTime;
    private long endTime;
    

    public List<AppCollegue> getCollegues() {
        return collegues;
    }

    public void setCollegues(List<AppCollegue> collegues) {
        this.collegues = collegues;
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
    
   
    
}
