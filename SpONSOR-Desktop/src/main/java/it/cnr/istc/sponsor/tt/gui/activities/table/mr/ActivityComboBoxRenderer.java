/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.activities.table.mr;

import it.cnr.istc.sponsor.tt.gui.engine.GuiEventManager;
import it.cnr.istc.sponsor.tt.logic.model.Activity;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class ActivityComboBoxRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component c =  super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus); 
        if(c !=null){
            if(value!=null && value instanceof Activity){
                 Icon activityIcon = GuiEventManager.getInstance().getActivityIcon(((Activity)value).getActivityName().getName());
                 ((JLabel)c).setIcon(activityIcon);
                 ((JLabel)c).setText(((Activity)value).getActivityName().getName());
            }
        }
        return c;
    }

    
}
