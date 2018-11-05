/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author user
 */
public class PreferredJobsTableRenderer extends DefaultTableCellRenderer{

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c =  super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); //To change body of generated methods, choose Tools | Templates.
        Color rightColor = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, 0).getBackground();
        
        if(value!=null && value instanceof Boolean){
            JCheckBox check = new JCheckBox();
            check.setSelected((Boolean)value);
            check.setBackground(rightColor);
            return check;
        }
        return c;
    }
    
    
    
}
