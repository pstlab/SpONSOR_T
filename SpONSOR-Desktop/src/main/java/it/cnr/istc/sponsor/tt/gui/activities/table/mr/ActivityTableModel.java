/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.activities.table.mr;

import it.cnr.istc.i18n.annotations.I18N;
import it.cnr.istc.sponsor.tt.abstracts.AbstractLCTableModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class ActivityTableModel extends AbstractLCTableModel<ActivityTableRow> {

    private List<String> activeKeys = new ArrayList<>();
    
    public ActivityTableModel() {
        super(new String[]{"day","day","day","day","day","day","day","day"}, null);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return this.datas.get(rowIndex).getLabel();
        } else {
            return this.datas.get(rowIndex).getWeekPanel()[columnIndex -1];
        }
    }
    
    public void deleteActivity(int row, int column, int pixIndex){
        this.datas.get(row).deleteItem(column, pixIndex);
         fireTableRowsUpdated(row, row);
    }
    
    public void changeTurnActivity(int row, int column, int pixIndex){
        this.datas.get(row).changeItemTurn(column, pixIndex);
         fireTableRowsUpdated(row, row);
    }
    
    public void addTimeRow(Date today, int time, boolean half){
        this.addRowElement(new ActivityTableRow(today, time,half));
    }

    public List<String> getActiveKeys() {
        return activeKeys;
    }

    public void addKey(String key){
        this.activeKeys.add(key);
    }
    
    public void removeKey(String key){
        this.activeKeys.remove(key);
    }
}
