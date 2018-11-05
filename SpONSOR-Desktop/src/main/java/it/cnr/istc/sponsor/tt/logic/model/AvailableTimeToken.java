/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.logic.model;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class AvailableTimeToken implements Comparable<AvailableTimeToken>{
    
    //1 = 30 minuti
    private int startTime;
    private int endTime;
    private int month;
    private int day;

    public AvailableTimeToken(int month, int day, int startTime, int endTime){
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
        this.month = month;
    }

    
    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public int compareTo(AvailableTimeToken o) {
        return (new Integer(this.month * 1000 + this.day*100 + this.startTime).compareTo(o.month * 1000 + o.day*100 + o.startTime));
    }
    
    
    @Override
    public String toString() {
        return "AVAILABLE TOKEN: M: "+this.month+" - D: "+this.day+" - START: "+this.startTime+" - END: "+this.endTime;
    }
    
    
}
