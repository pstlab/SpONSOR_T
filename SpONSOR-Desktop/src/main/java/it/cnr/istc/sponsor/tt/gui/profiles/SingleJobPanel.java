/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.profiles;

import it.cnr.istc.sponsor.tt.logic.model.Job;
import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.LayoutStyle;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class SingleJobPanel extends javax.swing.JPanel {

    private Job job;
    private int qnt = 0;

    /**
     * Creates new form SingleJobPanel
     */
    public SingleJobPanel() {
        initComponents();
    }

    public SingleJobPanel(Job job) {
        initComponents();

//        this.job = job_2;
        this.job = job;
        this.jLabel_jobName.setText(job.getName());
    }

    public Job getJob() {
        return job;
    }

    public int getJobCount() {
        return (Integer) this.jSpinner1.getValue();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSpinner1 = new JSpinner();
        jLabel_jobName = new JLabel();

        setBackground(new Color(255, 255, 255));

        jSpinner1.setModel(new SpinnerNumberModel(0, 0, null, 1));
        jSpinner1.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                jSpinner1StateChanged(evt);
            }
        });

        jLabel_jobName.setText("jLabel1");

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSpinner1, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel_jobName, GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinner1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_jobName))
                .addGap(4, 4, 4))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jSpinner1StateChanged(ChangeEvent evt) {//GEN-FIRST:event_jSpinner1StateChanged
        int current = getJobCount();

        if (current > qnt) {
            JobEditingManager.getInstance().addJob(job);
        } else {
            System.out.println("DIMISH");
            JobEditingManager.getInstance().removeJob(job);
        }
        qnt = getJobCount();
    }//GEN-LAST:event_jSpinner1StateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JLabel jLabel_jobName;
    private JSpinner jSpinner1;
    // End of variables declaration//GEN-END:variables
}
