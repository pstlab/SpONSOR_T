/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.st.server.xml;

import java.util.Date;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class Interval {
    
    private int dayOfWeek;
    private int startHourOfDay;
    private int endHourOfDay;

    public Interval() {
    }

    public Interval(int dayOfWeek, int startHourOfDay, int endHourOfDay) {
        this.dayOfWeek = dayOfWeek;
        this.startHourOfDay = startHourOfDay;
        this.endHourOfDay = endHourOfDay;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getStartHourOfDay() {
        return startHourOfDay;
    }

    public void setStartHourOfDay(int startHourOfDay) {
        this.startHourOfDay = startHourOfDay;
    }

    public int getEndHourOfDay() {
        return endHourOfDay;
    }

    public void setEndHourOfDay(int endHourOfDay) {
        this.endHourOfDay = endHourOfDay;
    }

    @Override
    public String toString() {
        
        return "DAY: "+dayOfWeek+ " from "+startHourOfDay+ " to "+endHourOfDay;
    }
    
    
    
    
    
    
}
