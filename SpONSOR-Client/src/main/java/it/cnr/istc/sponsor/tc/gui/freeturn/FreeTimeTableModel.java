/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tc.gui.freeturn;

import it.cnr.istc.sponsor.tc.gui.abstracts.AbstractLCTableModel;
import it.cnr.istc.sponsor.tc.logic.Interval;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class FreeTimeTableModel extends AbstractLCTableModel<FreeTimeTableRow> {

    private Map<Integer, List<SingleFreeTimePanel>> dayMap = new HashMap<>();

    public static boolean changingActive = true;

    public FreeTimeTableModel() {
        super(new String[]{"day", "day", "day", "day", "day", "day", "day", "day"}, null);
        for (int i = 0; i < 7; i++) {
            dayMap.put(i, new ArrayList<>());
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return this.datas.get(rowIndex).getLabel();
        } else {
            return this.datas.get(rowIndex).getWeekPanel()[columnIndex - 1];
        }
    }

    public void addTimeRow(Date today, int time) {
        FreeTimeTableRow ftt = new FreeTimeTableRow(today, time);
        this.addRowElement(ftt);
        int tw = today.getDay() == 0 ? 6 : today.getDay() - 1;
        System.out.println("TW: "+tw);
        for (int i = 0; i < 7; i++) {
            this.dayMap.get(i).add(ftt.getWeekPanel()[i]);
        }
//        this.dayMap.get(tw).add(ftt.getWeekPanel()[tw]);

    }

//    @Override
//    public boolean isCellEditable(int row, int col) {
//        return true;
//    }
    public List<Interval> getFreeIntervals() {
        List<Interval> intervals = new ArrayList<>();
        System.out.println("DAY MAP - > "+this.dayMap.size());
        for (int i = 0; i < this.dayMap.size(); i++) {
            System.out.println(i+") panel size: "+this.dayMap.get(i).size());
        }

        for (int i = 0; i < 7; i++) {
            List<SingleFreeTimePanel> dayPanels = this.dayMap.get(i);
            boolean intervalStarted = false;
            for (SingleFreeTimePanel dp : dayPanels) {
                if(dp.isFree() && !intervalStarted){
                    intervalStarted =  true;
                    intervals.add(new Interval(i, dp.getTime(), -1));
                }else if (!dp.isFree() && intervalStarted){
                    intervals.get(intervals.size()-1).setEndHourOfDay(dp.getTime());
                    intervalStarted = false;
                }
            }
        }

        return intervals;
    }

}
