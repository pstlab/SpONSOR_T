/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.activities.table.mr;

import it.cnr.istc.sponsor.tt.abstracts.AbstractLCComboModel;
import it.cnr.istc.sponsor.tt.gui.engine.GuiEventListener;
import it.cnr.istc.sponsor.tt.logic.model.Activity;
import it.cnr.istc.sponsor.tt.logic.model.ActivityTurn;
import it.cnr.istc.sponsor.tt.logic.model.Job;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class ActivityComboBoxModel extends AbstractLCComboModel<Activity> implements GuiEventListener{
    
    public ActivityComboBoxModel(List<Activity> d) {
        super(d);
    }
    
    @Override
    public void newActivityEvent(Activity activity) {

    }

    @Override
    public void removeActivityEvent(Activity activity) {
        this.datas.remove(activity);
        int index = this.datas.indexOf(activity);
        if (index != -1) {
            this.fireIntervalRemoved(this, index, index);
        }
    }

    @Override
    public void newProfileEvent(Job profile) {

    }

    @Override
    public void newActivityTurn(ActivityTurn turn) {

    }

    @Override
    public void changeDate(Date date) {

    }

    @Override
    public void changeTab(int tab) {

    }

    @Override
    public void newDormient(long id) {
    }

    @Override
    public void dormientWokeup(long id) {
    }

    @Override
    public void ghostNumberUpdated() {
    }

    @Override
    public void timeAvailableChanged() {
    }
    
}
