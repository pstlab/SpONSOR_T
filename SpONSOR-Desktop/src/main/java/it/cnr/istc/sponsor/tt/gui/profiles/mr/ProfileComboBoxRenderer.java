/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.profiles.mr;

import it.cnr.istc.sponsor.tt.logic.model.Job;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class ProfileComboBoxRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component c =  super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus); 
        if(c !=null){
            if(value!=null && value instanceof Job){
                 ((JLabel)c).setText(((Job)value).getName());
                 System.out.println("nn - > "+((Job)value).getName());
            }
        }else{
            System.out.println("NULLONE");
        }
        return c;
    }

    
}
