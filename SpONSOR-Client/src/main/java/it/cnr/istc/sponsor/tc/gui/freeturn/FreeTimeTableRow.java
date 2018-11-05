/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tc.gui.freeturn;

import java.awt.Color;
import java.util.Date;
import javax.swing.BorderFactory;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class FreeTimeTableRow {

    private SingleFreeTimePanel[] weekPanel = new SingleFreeTimePanel[7];
    private TitledPanel label;
    private int time;

    public FreeTimeTableRow(Date today, int time) {
        this.time = time;
        String labelId = time < 10 ? "0" + time + ":00" : time + ":00";
        label = new TitledPanel(labelId);
        label.setBackground(Color.ORANGE);
        label.setLabelColor(Color.BLACK);
        label.setBorder(null);
        Date days[] = new Date[7];
        for (int i = 0; i < 7; i++) {
            days[i] = today;
            today = new Date(today.getTime()+(1000l*60*60l*24l));
        }
        
        for (int i = 0; i < 7; i++) {
            weekPanel[i] = new SingleFreeTimePanel(days[i], time);
            weekPanel[i].setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));
        }
    }


    public SingleFreeTimePanel[] getWeekPanel() {
        return weekPanel;
    }

    public TitledPanel getLabel() {
        return label;
    }

    public int getTime() {
        return time;
    }
    
    

}
