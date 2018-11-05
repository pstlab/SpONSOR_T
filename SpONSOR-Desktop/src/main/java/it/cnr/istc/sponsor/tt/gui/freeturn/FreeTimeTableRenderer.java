/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.freeturn;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class FreeTimeTableRenderer extends DefaultTableCellRenderer {

    private List<Integer> rowChanged = new ArrayList<>();
    private List<Integer> columnChanged = new ArrayList<>();
    private boolean freeActive = true;
    private List<Integer> cellChanged = new ArrayList<>();
    private boolean editable = true;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); //To change body of generated methods, choose Tools | Templates.
        
        if (value != null) {
            if (column != 0) {
//                if (isSelected && freeActive) {
////                    ((JComponent) ((FreeTimeTableModel) table.getModel()).getValueAt(row, column)).setBackground((row % 2 == 0) ? new Color(186, 255, 198) : Color.green);
//                    ((SingleFreeTimePanel) ((FreeTimeTableModel) table.getModel()).getValueAt(row, column)).setFree(true);
//                } 
//                else if(isSelected && freeActive){
//                     ((SingleFreeTimePanel) ((FreeTimeTableModel) table.getModel()).getValueAt(row, column)).setFree(false);
//                }
//                if (isSelected && freeActive && !(rowChanged.contains(row) && columnChanged.contains(column))) {
                if (isSelected && freeActive && !cellChanged.contains((row * 1000 + column)) && editable) {
                    ((SingleFreeTimePanel) ((FreeTimeTableModel) table.getModel()).getValueAt(row, column)).invert();
//                    this.rowChanged.add(row);
//                    this.columnChanged.add(column);
                    cellChanged.add(row * 1000 + column);
                }
                if (((SingleFreeTimePanel) ((FreeTimeTableModel) table.getModel()).getValueAt(row, column)).isFree()) {
                    ((JComponent) ((FreeTimeTableModel) table.getModel()).getValueAt(row, column)).setBackground((row % 2 == 0) ? new Color(8, 170, 8) : Color.green);
                } else {
                    ((JComponent) ((FreeTimeTableModel) table.getModel()).getValueAt(row, column)).setBackground((row % 2 == 0) ? new Color(255, 50, 50) : new Color(168, 8, 8));
                }
//                else if (isSelected && !freeActive) {
//                    ((SingleFreeTimePanel) ((FreeTimeTableModel) table.getModel()).getValueAt(row, column)).setFree(false);
//                }
//                else {
//                    ((JComponent) ((FreeTimeTableModel) table.getModel()).getValueAt(row, column)).setBackground((row % 2 == 0) ? Color.white : new Color(240, 240, 240));
//                }
            } else if (column == 0) {
                ((JComponent) ((FreeTimeTableModel) table.getModel()).getValueAt(row, column)).setBackground((row % 2 == 0) ? new Color(253, 226, 170) : Color.orange);
            }
            return (JComponent) value;
        }
        return c;
    }

    public void setFreeActive(boolean freeActive) {
        this.freeActive = freeActive;
        if (!freeActive) {
            this.rowChanged.clear();
            this.columnChanged.clear();
            cellChanged.clear();
        }
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }
    
    

}
