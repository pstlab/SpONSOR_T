/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.profiles.mr;

import it.cnr.istc.i18n.translator.TranslatorManager;
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
public class ProfileTableModel extends AbstractLCTableModel<Job> {

    private String[] header;
    private Map<Integer, String> indexedHeader = new HashMap<>();
    private List<Integer> editableIndex = new ArrayList<>();

    public ProfileTableModel(String[] columns) {
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
        super.setValueAt(value, row, col); //To change body of generated methods, choose Tools | Templates.
    }

    public void deleteJob(Long id) {
        int index = -1;
        int i = 0;
        for (Job data : datas) {
            if (data.getId().equals(id)) {
                index = i;
                break;
            }
            i++;
        }
        if (index != -1) {
            datas.remove(index);
        }
        this.fireTableRowsDeleted(index, index);

    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        Job profile = this.datas.get(rowIndex);
        if (columnIndex == 0) {
            return "<html><b><font color=red>" + profile.getName();
        }
        return profile.hasSkillWithName(indexedHeader.get(columnIndex));
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        return columnIndex == 0 ? String.class : Boolean.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

}
