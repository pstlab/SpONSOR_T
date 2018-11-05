/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.profiles.mr;

import it.cnr.istc.sponsor.tt.abstracts.AbstractLCTableModel;
import it.cnr.istc.sponsor.tt.logic.model.Job;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class ProfileLRTableModel extends AbstractLCTableModel<Job> {

    private String[] header;
    private Map<Integer, String> indexedHeader = new HashMap<>();

    public ProfileLRTableModel(String[] columns) {
        super(columns, null);
        this.header = columns;
        int i = 0;
        for (String column : columns) {
            indexedHeader.put(i, column);
            i++;
        }

    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        if(value instanceof Boolean){
            String skillName = indexedHeader.get(col);
            if((Boolean)value){
                this.datas.get(row).addSkill(skillName);
            }else{
                this.datas.get(row).removeSkill(skillName);
            }
        }
    }

    
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        Job profile = this.datas.get(rowIndex);
        return profile.hasSkillWithName(indexedHeader.get(columnIndex));
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        return Boolean.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

}
