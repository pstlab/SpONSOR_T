/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.logic.model;

import it.cnr.istc.sponsor.tt.abstracts.PaintSupplier;
import it.cnr.istc.sponsor.tt.gui.engine.GuiEventManager;
import it.cnr.istc.sponsor.tt.logic.TrainerManager;
import it.cnr.istc.sponsor.tt.logic.Utils;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class Activity {

    private long id;
    private ActivityName activityName;
    private List<ActivityTurn> activityTurns = new ArrayList<>();
    private Color generalColor = null;
    private static PaintSupplier ps = new PaintSupplier();

    public Activity() {

    }

    public Activity(long id, ActivityName name) {
        this.id = id;
        this.activityName = name;

    }

    public void assignColor() {
        if (generalColor == null) {
            generalColor = ps.getNextPaint();
           
        }
//         JOptionPane.showMessageDialog(null, "Color is: "+generalColor.toString());
    }

    public Color getGeneralColor() {
        return generalColor;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ActivityName getActivityName() {
        return activityName;
    }

    public void setActivityName(ActivityName activityName) {
        this.activityName = activityName;
    }

    

    public int getTotalJobs() {
        int result = 0;
        for (ActivityTurn activityTurn : activityTurns) {
            result += activityTurn.getRequiredProfiles().size();
        }
        return result;
    }

    public List<JobTurn> getAllJobTurns(){
        List<JobTurn> result = new ArrayList<>();
        for (ActivityTurn activityTurn : activityTurns) {
            result.addAll(activityTurn.getRequiredProfiles());
        }
        return result;
    }
    
    public List<ActivityTurn> getActivityTurns() {
        return activityTurns;
    }

    public void setActivityTurns(List<ActivityTurn> activityTurns) {
        this.activityTurns = activityTurns;
    }

    public void addActivityTurn(ActivityTurn turn) {
        TrainerManager.getInstance().mapTurn(turn, this);
        this.activityTurns.add(turn);
        Collections.sort(activityTurns);
        GuiEventManager.getInstance().newActivityTurn(turn);
    }

    @Override
    public String toString() {
        return "ACTIVITY: " + this.getActivityName().getName();
    }

}
