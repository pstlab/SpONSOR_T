/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.activities.table.mr;

import it.cnr.istc.sponsor.tt.gui.activities.ActivityCalendarManager;
import it.cnr.istc.sponsor.tt.gui.activities.ActivityTurnOverlapPanel;
import it.cnr.istc.sponsor.tt.gui.activities.CurrentActivityDrag;
import it.cnr.istc.sponsor.tt.gui.activities.SingleTimeActivityIntervalPanel;
import it.cnr.istc.sponsor.tt.gui.engine.GuiEventManager;
import java.awt.Color;
import java.awt.Component;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class ActivityTableRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); //To change body of generated methods, choose Tools | Templates.
        if (value != null) {
            if (column != 0) {
                if (isSelected) {
                    ((JComponent) ((ActivityTableModel) table.getModel()).getValueAt(row, column)).setBackground((row%2 ==0 ) ? new Color(186,255,198) :Color.green);
                    List<String> activeKeys = ((ActivityTableModel) table.getModel()).getActiveKeys();
                    for (String activeKey : activeKeys) {
                        Icon activityIcon = GuiEventManager.getInstance().getActivityIcon(activeKey);
                        ((SingleTimeActivityIntervalPanel) ((ActivityTableModel) table.getModel()).getValueAt(row, column)).add(activeKey, new ActivityTurnOverlapPanel(activityIcon));
                    }
//                SingleTimeActivityIntervalPanel.this.invalidate();
                } else {
                    ((JComponent) ((ActivityTableModel) table.getModel()).getValueAt(row, column)).setBackground((row%2 ==0 ) ?  Color.white : new Color(240,240,240));
                }
            }else if(column==0){
                  ((JComponent) ((ActivityTableModel) table.getModel()).getValueAt(row, column)).setBackground((row%2 ==0 ) ? new Color(253,226,170) :  Color.orange );
            }
            if(row%2==1 && column >0){
                 ((JComponent) ((ActivityTableModel) table.getModel()).getValueAt(row, column)).setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK));
            }
            if(row%2==1 && column ==0){
                 ((JComponent) ((ActivityTableModel) table.getModel()).getValueAt(row, column)).setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
            }
            return (JComponent) value;
        }

        return c;
    }

}
