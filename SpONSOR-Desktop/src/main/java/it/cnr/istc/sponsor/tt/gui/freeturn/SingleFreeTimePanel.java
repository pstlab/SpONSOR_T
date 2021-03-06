/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.cnr.istc.sponsor.tt.gui.freeturn;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import javax.swing.GroupLayout;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class SingleFreeTimePanel extends javax.swing.JPanel {

    private Date date;
    /** Creates new form SingleFreeTimePanel */
    private boolean free = false;
    private int time;
    public SingleFreeTimePanel() {
        initComponents();
    }
    
    public SingleFreeTimePanel(Date date, int time) {
        initComponents();
        this.date = date;
        this.time = time;
        this.setBackground(Color.red);
    }

    public Date getDate() {
        return date;
    }

    public int getTime() {
        return time;
    }
    
    

    public void invert(){
        this.free = !this.free;
        if(free){
            this.setBackground(Color.green);
        }else{
            this.setBackground(Color.red);
        }
    }
    
    public void setFree(boolean free) {
        this.free = free;
        if(free){
            this.setBackground(Color.green);
        }else{
            this.setBackground(Color.red);
        }
        
    }

    public boolean isFree() {
        return free;
    }
    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new Color(255, 255, 255));
        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                formMouseEntered(evt);
            }
        });

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 112, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 17, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseEntered(MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseEntered


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
