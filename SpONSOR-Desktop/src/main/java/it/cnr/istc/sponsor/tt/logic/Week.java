/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class Week {

    private List<Date> days = null;
    private Week previousWeek = null;
    private Week furtherWeek  = null;

    public Week(Date startDay) {
        if(startDay == null){
            startDay = new Date();
        }
        Date d2 = new Date(startDay.getTime() + (1000l * 60l * 60 * 24l));
        Date d3 = new Date(d2.getTime() + (1000l * 60l * 60 * 24l));
        Date d4 = new Date(d3.getTime() + (1000l * 60l * 60 * 24l));
        Date d5 = new Date(d4.getTime() + (1000l * 60l * 60 * 24l));
        Date d6 = new Date(d5.getTime() + (1000l * 60l * 60 * 24l));
        Date d7 = new Date(d6.getTime() + (1000l * 60l * 60 * 24l));
        if (days == null) {
            days = new ArrayList<>();
            days.clear();
            days.add(startDay);
            days.add(d2);
            days.add(d3);
            days.add(d4);
            days.add(d5);
            days.add(d6);
            days.add(d7);
        }
    }

    public List<Date> getDays() {
        return days;
    }

    
    public Week getFurtherWeek() {
        if(furtherWeek==null){
            furtherWeek = new Week(Utils.getNextWeekMonday(days.get(0)));
        }
        return furtherWeek;
    }

    public Week getPreviousWeek() {
        if(previousWeek==null){
            previousWeek = new Week(Utils.getPreviousWeekMonday(days.get(0)));
        }
        return previousWeek;
    }
    
    
    
    

}
