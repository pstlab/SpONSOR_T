/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.engine;

import it.cnr.istc.sponsor.tt.gui.activities.SingleTimeActivityIntervalPanel;
import it.cnr.istc.sponsor.tt.logic.MQTTClient;
import it.cnr.istc.sponsor.tt.logic.model.Activity;
import it.cnr.istc.sponsor.tt.logic.model.ActivityTurn;
import it.cnr.istc.sponsor.tt.logic.model.Job;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.Icon;
import javax.swing.JOptionPane;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class GuiEventManager {
    
    private static GuiEventManager _instance = null;
    private List<GuiEventListener> listeners = new ArrayList<>();
    private Map<String,Icon> activityIconMap = new HashMap<>();
    
    public static GuiEventManager getInstance() {
        if (_instance == null) {
            _instance = new GuiEventManager();
            return _instance;
        } else {
            return _instance;
        }
    }
    
    private GuiEventManager() {
        super();
        //DEMO
//        this.activityIconMap.put("Teleassistenza", new javax.swing.ImageIcon(SingleTimeActivityIntervalPanel.class.getResource("/it/cnr/istc/sponsor/tt/gui/icons/call16.png")));
//        this.activityIconMap.put("Emergenze", new javax.swing.ImageIcon(SingleTimeActivityIntervalPanel.class.getResource("/it/cnr/istc/sponsor/tt/gui/icons/alarm16.png")));
//        this.activityIconMap.put("Cucina", new javax.swing.ImageIcon(SingleTimeActivityIntervalPanel.class.getResource("/it/cnr/istc/sponsor/tt/gui/icons/full_screen16.png")));
    }
    
    public void putActivityIcon(String key, Icon icon){
        this.activityIconMap.put(key, icon);
    }
    
    public Set<String> getActivityKeys(){
        return this.activityIconMap.keySet();
    }
    
    public Icon getActivityIcon(String key){
        return this.activityIconMap.get(key);
    }
    
    public void addGuiEventListener(GuiEventListener listener){
        this.listeners.add(listener);
    }
    
    public void newProfile(Job profile){
        MQTTClient.getInstance().createJob(profile);
        for (GuiEventListener listener : listeners) {
            listener.newProfileEvent(profile);
        }
    }
    
    public void timeAvailableChanged(){
        for (GuiEventListener listener : listeners) {
            listener.timeAvailableChanged();
        }
    }
    
    public void newActivity(Activity activity){
        activity.assignColor();
        for (GuiEventListener listener : listeners) {
            listener.newActivityEvent(activity);
        }
    }
    
    public void newDormient(long id){
        for (GuiEventListener listener : listeners) {
            listener.newDormient(id);
        }
    }
    
    public void dormientWokeup(long id){
        for (GuiEventListener listener : listeners) {
            listener.dormientWokeup(id);
        }
    }
    
    public void ghostNumberUpdated(){
        for (GuiEventListener listener : listeners) {
            listener.ghostNumberUpdated();
        }
    }
    
    
    public void removeActivity(Activity activity){
        for (GuiEventListener listener : listeners) {
            listener.removeActivityEvent(activity);
            listener.ghostNumberUpdated();
        }
    }
    
    public void newActivityTurn(ActivityTurn turn){
        for (GuiEventListener listener : listeners) {
            listener.newActivityTurn(turn);
        }
    }
    
    public void changeDate(Date date){
        for (GuiEventListener listener : listeners) {
            listener.changeDate(date);
        }
    }
    
    public void changeTab(int tab){
        for (GuiEventListener listener : listeners) {
            listener.changeTab(tab);
        }
    }
}
