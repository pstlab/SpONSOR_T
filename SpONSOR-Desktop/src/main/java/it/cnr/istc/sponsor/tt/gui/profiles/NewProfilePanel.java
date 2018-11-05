/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.profiles;

import it.cnr.istc.sponsor.tt.logic.model.ModelManager;
import it.cnr.istc.sponsor.tt.logic.model.Skill;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class NewProfilePanel extends javax.swing.JPanel {

    List<SingleCheckBoxPropertyPanel> checkPanels = new ArrayList<>();
    /**
     * Creates new form NewActivityPanel
     */
    public NewProfilePanel() {
        initComponents();
        List<Skill> skills = ModelManager.getInstance().getAllSkills();
        for (Skill skill : skills) {
            System.out.println("MANNACC U BOZZ: "+skill.getId());
            checkPanels.add(new SingleCheckBoxPropertyPanel(skill));
        }
        for (SingleCheckBoxPropertyPanel ccc : checkPanels) {
            this.jPanel_Container.add(ccc);
        }
        
        
    }

    public List<SingleCheckBoxPropertyPanel> getCheckPanels() {
        return checkPanels;
    }
    
    
    public String getProfileName(){
        return jTextField_Name.getText();
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
        jTextField_Name = new JTextField();
        jPanel_Container = new JPanel();

        setBackground(new Color(255, 255, 255));

        jLabel1.setText("Profilo:");

        jPanel_Container.setBackground(new Color(255, 255, 255));
        jPanel_Container.setLayout(new GridLayout(4, 2));

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_Container, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField_Name, GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField_Name, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel_Container, GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JLabel jLabel1;
    private JPanel jPanel_Container;
    private JTextField jTextField_Name;
    // End of variables declaration//GEN-END:variables
}