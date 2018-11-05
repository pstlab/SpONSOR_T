/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.engine;

import it.cnr.istc.sponsor.tt.logic.model.Activity;
import it.cnr.istc.sponsor.tt.logic.model.ActivityTurn;
import it.cnr.istc.sponsor.tt.logic.model.Job;
import it.cnr.istc.sponsor.tt.logic.model.Person;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public interface GuiEventListener {
    
    public void newActivityEvent(Activity activity);
    
    public void removeActivityEvent(Activity activity);
    
    public void newProfileEvent(Job profile);
    
    public void newActivityTurn(ActivityTurn turn);
    
    public void changeDate(Date date);
    
    public void changeTab(int tab);
    
    public void newDormient(long id);
    
    public void dormientWokeup(long id);
    
    public void ghostNumberUpdated();
    
    public void timeAvailableChanged();
    
    
}
