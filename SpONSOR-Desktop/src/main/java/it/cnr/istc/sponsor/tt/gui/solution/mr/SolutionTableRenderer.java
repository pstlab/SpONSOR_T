/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.solution.mr;

import it.cnr.istc.sponsor.tt.gui.solution.mr.*;
import it.cnr.istc.sponsor.tt.gui.activities.ActivityTurnOverlapPanel;
import it.cnr.istc.sponsor.tt.gui.activities.SingleTimeActivityIntervalPanel;
import it.cnr.istc.sponsor.tt.gui.activities.table.mr.ActivityTableModel;
import it.cnr.istc.sponsor.tt.gui.engine.GuiEventManager;
import it.cnr.istc.sponsor.tt.gui.solution.SingleActivitySolutionPanel;
import it.cnr.istc.sponsor.tt.gui.solution.SinglePersonSolutionPanel;
import it.cnr.istc.sponsor.tt.logic.model.ActivityTurn;
import java.awt.Color;
import java.awt.Component;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class SolutionTableRenderer extends DefaultTableCellRenderer {

    private boolean pm = false;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        TableModel model = table.getModel();
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); //To change body of generated methods, choose Tools | Templates.
        if (value != null) {
//            System.out.println("value is diverso da null and "+value.getClass().getCanonicalName());
            if (value instanceof ActivityTurn) {
                if (model instanceof SolutionTableModel) {
                    pm = ((SolutionTableModel) model).isPersonMode();
                }
                if (pm) {
                    System.out.println(" ----------------------------- PERSON MODE ACTIVE -------------------------");
                    SinglePersonSolutionPanel singlePersonPanel = new SinglePersonSolutionPanel((ActivityTurn) value);
                    if (row % 2 == 0) {
                        singlePersonPanel.setBackground(new Color(220, 232, 239));
                    } else {
                        singlePersonPanel.setBackground(new Color(188, 232, 239));
                    }
                    return singlePersonPanel;
                    
                } else {
                    SingleActivitySolutionPanel singleActivitySolutionPanel = new SingleActivitySolutionPanel((ActivityTurn) value);
                    if (row % 2 == 0) {
                        singleActivitySolutionPanel.setBackground(new Color(220, 232, 239));
                    } else {
                        singleActivitySolutionPanel.setBackground(new Color(188, 232, 239));
                    }
                    return singleActivitySolutionPanel;
                }

            } else {
                return new JLabel();
            }
        } else {
//            System.out.println("value is NULLONE");
            return new JLabel();
        }

    }

}
