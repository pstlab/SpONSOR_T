/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.activities;

import it.cnr.istc.sponsor.tt.gui.times.SingleActivityTurnPanel;
import it.cnr.istc.sponsor.tt.logic.model.Activity;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class ActivityCalendarManager {

    private static ActivityCalendarManager _instance = null;
    private Map<Date, List<SingleActivityTurnPanel>> activityPanelsDayMap = new HashMap<>();
    private Map<Date, Map<String, Integer>> indexActivityDayMap = new HashMap<>();
    private CurrentActivityDrag currentActivityDrag = null;

    public static ActivityCalendarManager getInstance() {
        if (_instance == null) {
            _instance = new ActivityCalendarManager();
            return _instance;
        } else {
            return _instance;
        }
    }

    private ActivityCalendarManager() {
        super();
    }

    public CurrentActivityDrag getCurrentActivityDrag() {
        return currentActivityDrag;
    }

    public void setCurrentActivityDrag(CurrentActivityDrag currentActivityDrag) {
        this.currentActivityDrag = currentActivityDrag;
    }
    
    

    public int addActivityTurn(Date when, int timeSlot, String activityName) {
        System.out.println("adding on when: "+when.toString());
        if (!indexActivityDayMap.containsKey(when)) {
            indexActivityDayMap.put(when, new HashMap<>());
            indexActivityDayMap.get(when).put(activityName, 0);
//            System.out.println("INDEX = 0");
            return 0;
        } else {
            if (!indexActivityDayMap.get(when).containsKey(activityName)) {
                 indexActivityDayMap.get(when).put(activityName, indexActivityDayMap.get(when).size());
            }
            System.out.println("INDEX = "+indexActivityDayMap.get(when).get(activityName));
            return indexActivityDayMap.get(when).get(activityName);
        }
    }
    
    public int getTotalActivityCountPerDay(Date when){
        if (!indexActivityDayMap.containsKey(when)) {
            return 0;
        }else{
            return indexActivityDayMap.get(when).size(); 
        }
    }
    
    

}
