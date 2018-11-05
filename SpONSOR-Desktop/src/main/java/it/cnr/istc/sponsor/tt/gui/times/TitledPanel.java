/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.times;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class TitledPanel extends javax.swing.JPanel {

    /**
     * Creates new form SingleTimeIntervalPanel
     */
    public TitledPanel() {
        initComponents();
    }
    
    
    public TitledPanel(String title) {
        initComponents();
        this.jLabel1.setText(title);
    }
    
    public void setLabelColor(Color color){
        this.jLabel1.setForeground(color);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new JLabel();

        setBackground(new Color(0, 51, 255));
        setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                formMouseEntered(evt);
            }
        });

        jLabel1.setFont(new Font("Tahoma", 0, 10)); // NOI18N
        jLabel1.setForeground(new Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel1.setText("jLabel1");
        jLabel1.setVerticalAlignment(SwingConstants.TOP);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseEntered(MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
        
    }//GEN-LAST:event_formMouseEntered


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
