/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.profiles;

import it.cnr.istc.sponsor.tt.logic.model.Job;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it>
 */
 public class JobEditingManager {

    private static JobEditingManager _instance;
    private List<JobEditingListener> listeners = new ArrayList<>();

    public static JobEditingManager getInstance() {
        if (_instance == null) {
            _instance = new JobEditingManager();
            return _instance;
        } else {
            return _instance;
        }
    }
    
    public void addJobEditingListener(JobEditingListener listener){
        this.listeners.add(listener);
    }
    
    public void addJob(Job job){
        for (JobEditingListener listener : listeners) {
            listener.addJob(job);
        }
    }
    
    public void removeJob(Job job){
        for (JobEditingListener listener : listeners) {
            listener.removeJob(job);
        }
    }
    
    
    
}
