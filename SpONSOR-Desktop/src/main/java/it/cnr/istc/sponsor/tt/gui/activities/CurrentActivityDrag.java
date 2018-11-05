/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.activities;

import java.util.Date;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class CurrentActivityDrag {

    private int startTime;
    private String activityName;
    private Date when;

    public CurrentActivityDrag() {
    }

    public CurrentActivityDrag(int startTime, String activityName, Date when) {
        this.startTime = startTime;
        this.activityName = activityName;
        this.when = when;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }

}
