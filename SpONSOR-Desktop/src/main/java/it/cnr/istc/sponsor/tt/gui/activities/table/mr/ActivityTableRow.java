/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.activities.table.mr;

import it.cnr.istc.sponsor.tt.gui.activities.SingleTimeActivityIntervalPanel;
import it.cnr.istc.sponsor.tt.gui.times.TitledPanel;
import java.awt.Color;
import java.util.Date;
import javax.swing.BorderFactory;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class ActivityTableRow {

    private SingleTimeActivityIntervalPanel[] weekPanel = new SingleTimeActivityIntervalPanel[7];
    private TitledPanel label;
    private int time;
    private boolean half = false;

    public ActivityTableRow(Date today, int time, boolean half) {
        this.time = time;
        this.half = half;
        String labelId = time < 10 ? "0" + time + ":00" : time + ":00";
        if(half){
            labelId = labelId.replace("00", "30");
        }
        label = new TitledPanel(labelId);
        label.setBackground(Color.ORANGE);
        label.setLabelColor(Color.BLACK);
        label.setBorder(null);
        if (today != null) {
            Date days[] = new Date[7];
            for (int i = 0; i < 7; i++) {
                days[i] = today;
                today = new Date(today.getTime() + (1000l * 60 * 60l * 24l));
            }

            for (int i = 0; i < 7; i++) {
                weekPanel[i] = new SingleTimeActivityIntervalPanel(days[i]);
                weekPanel[i].setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));
            }
        }
    }

    public void changeItemTurn(int day, int index) {
        weekPanel[day - 1].changeActicityTurn(index);
    }

    public void deleteItem(int day, int index) {
        weekPanel[day - 1].deleteActivity(index);
    }

    public SingleTimeActivityIntervalPanel[] getWeekPanel() {
        return weekPanel;
    }

    public TitledPanel getLabel() {
        return label;
    }

    public int getTime() {
        return time;
    }

}
