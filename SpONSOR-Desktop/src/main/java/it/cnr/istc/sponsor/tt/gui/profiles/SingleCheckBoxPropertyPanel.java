/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.profiles;

import it.cnr.istc.sponsor.tt.logic.model.Skill;
import java.awt.Color;
import javax.swing.GroupLayout;
import javax.swing.JCheckBox;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class SingleCheckBoxPropertyPanel extends javax.swing.JPanel {

    private Skill skill;
    /**
     * Creates new form SingleCheckBoxPropertyPanel
     */
    public SingleCheckBoxPropertyPanel() {
        initComponents();
    }
    
    public SingleCheckBoxPropertyPanel(Skill skill) {
        initComponents();
        this.skill = skill;
        this.jCheckBox1.setText(skill.getName());
    }

    public Skill getSkill() {
        return skill;
    }
    
    
    public boolean isSelected(){
        return this.jCheckBox1.isSelected();
    }
    

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBox1 = new JCheckBox();

        setBackground(new Color(255, 255, 255));

        jCheckBox1.setText("jCheckBox1");
        jCheckBox1.setOpaque(false);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jCheckBox1, GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jCheckBox1)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JCheckBox jCheckBox1;
    // End of variables declaration//GEN-END:variables
}
