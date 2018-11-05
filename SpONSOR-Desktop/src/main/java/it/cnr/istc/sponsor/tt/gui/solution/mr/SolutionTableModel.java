/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.solution.mr;

import it.cnr.istc.sponsor.tt.gui.solution.mr.*;
import it.cnr.istc.sponsor.tt.abstracts.AbstractLCTableModel;
import it.cnr.istc.sponsor.tt.gui.solution.SingleActivitySolutionPanel;
import it.cnr.istc.sponsor.tt.logic.TrainerManager;
import it.cnr.istc.sponsor.tt.logic.model.Activity;
import it.cnr.istc.sponsor.tt.logic.model.ActivityTurn;
import it.cnr.istc.sponsor.tt.logic.model.Person;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class SolutionTableModel extends AbstractLCTableModel<ActivityTurn> {

    private Map<Integer, List<ActivityTurn>> turnColumnMap = new HashMap<>();
    private boolean personMode = false;

    public SolutionTableModel() {
        super(new String[]{"Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato", "Domenica"}, null);
        for (int i = 0; i < 7; i++) {
            turnColumnMap.put(i, new ArrayList<ActivityTurn>());
        }
    }

//    public void showActivityByPerson(Person person) {
//        System.out.println("SHOW ACTIVITY PERSON");
//        this.clear();
//        personMode = true;
//        List<ActivityTurn> allTurns = new ArrayList<>();
//        List<Activity> activities = TrainerManager.getInstance().getActivities();
//
//        for (Activity activity : activities) {
//            allTurns.addAll(activity.getActivityTurns());
//        }
//        Collections.sort(allTurns);
//        System.out.println("ALL TURNS -> " + allTurns.size());
//        for (ActivityTurn turn : allTurns) {
//            if (turn.isThisPersonConfirmed(person)) {
//                System.out.println("ADDING TURN -> " + turn);
//                this.addRowElement(turn);
//            }
//        }
//    }

    public boolean isPersonMode() {
        return personMode;
    }

    public void setPersonMode(boolean personMode) {
        this.personMode = personMode;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        List<ActivityTurn> list = this.turnColumnMap.get(columnIndex);
        if(list==null){
            return null;
        }
        if (list.isEmpty()) {
            return null;
        }
        if (rowIndex < list.size()) {
            return list.get(rowIndex);
        } else {
            return null;
        }
    }

    @Override
    public int getRowCount() {
        int size = 0;
        for (List<ActivityTurn> list : turnColumnMap.values()) {
            if (list.size() > size) {
                size = list.size();
            }
        }
        return size;
    }

    public void clear() {
//        personMode = false;
        this.turnColumnMap.clear();
        for (int i = 0; i < 7; i++) {
            turnColumnMap.put(i, new ArrayList<ActivityTurn>());
        }
        this.fireTableDataChanged();
    }

    @Override
    public void addRowElement(ActivityTurn element) {
        int day = new Date(element.getStartTime()).getDay();
        day = day == 0 ? 6 : day - 1;
        this.turnColumnMap.get(day).add(element);
    }

    public void addAllRows(List<ActivityTurn> elements) {
//        personMode = false;
//        System.out.println(" -------- !!!!!!!!!!!!!!!!!!!!! ------------");
        System.out.println("ADD ALL ROWS -> elements size -> " + elements.size());
        Collections.sort(elements);
        System.out.println("ADD ALL ROWS -> elements size -> " + elements.size() + " after sort");
        for (ActivityTurn element : elements) {
            System.out.println("SOLUTION PANEL ELEMENT > " + element.toString());
            int day = new Date(element.getStartTime()).getDay();
            day = day == 0 ? 6 : day - 1;
            this.turnColumnMap.get(day).add(element);
//            System.out.println("ADDED <<<<<<<");
        }
    }

}
