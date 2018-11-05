/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.st.server.model;


import java.util.Date;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
  public class FreeTimeToken implements Comparable<FreeTimeToken>{
    
    //1 = 30 minuti
    private Long starTime;
    private Long endTime;

    public FreeTimeToken() {
    }

    public FreeTimeToken(Long starTime, Long endTime) {
        this.starTime = starTime;
        this.endTime = endTime;
    }

    public Long getStarTime() {
        return starTime;
    }

    public void setStarTime(Long starTime) {
        this.starTime = starTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    
    @Override
    public int compareTo(FreeTimeToken o) {
        return starTime.compareTo(o.getStarTime());
    }
    
    
    @Override
    public String toString() {
        return "AVAILABLE TOKEN: StartTime: "+this.starTime+", EndTime: "+this.endTime;
    }
    
    
}

