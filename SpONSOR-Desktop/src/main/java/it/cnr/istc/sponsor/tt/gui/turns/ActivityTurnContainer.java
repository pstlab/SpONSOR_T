/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.turns;

import it.cnr.istc.sponsor.tt.gui.times.SingleActivityTurnPanel;
import it.cnr.istc.sponsor.tt.gui.activities.SingleTimeActivityIntervalPanel;
import it.cnr.istc.sponsor.tt.gui.times.TitledPanel;
import it.cnr.istc.sponsor.tt.logic.model.Activity;
import java.awt.Color;
import java.awt.GridLayout;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class ActivityTurnContainer extends javax.swing.JPanel {

    public ActivityTurnContainer( ) {
        initComponents();
    }
    /**
     * Creates new form TimeIntervalContainer
     * @param activity
     */
    public ActivityTurnContainer(Activity activity) {
        initComponents();
        TitledPanel orariLabelPanel = new TitledPanel("Orari");
        orariLabelPanel.setBackground(Color.WHITE);
        orariLabelPanel.setLabelColor(Color.BLACK);
        this.add(orariLabelPanel);
        for (int i = 0; i < 7; i++) {
            this.add(new TitledPanel("DAY " + i));

        }
        int startTime = 7;
        for (int i = 0; i < 24; i++) {
            TitledPanel timePanel = new TitledPanel(startTime <10 ? "0"+startTime+":00" : startTime+":00");
            timePanel.setBackground(Color.ORANGE);
            timePanel.setLabelColor(Color.BLACK);
            this.add(timePanel);
            for (int j = 0; j < 7; j++) {
                this.add(new SingleActivityTurnPanel());
            }
            startTime++;
            if(startTime == 24){
                startTime = 0;
            }
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new GridLayout(25, 8));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
