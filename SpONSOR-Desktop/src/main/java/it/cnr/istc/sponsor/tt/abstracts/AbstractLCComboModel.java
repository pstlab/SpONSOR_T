/*
 *   Copyright (C) 2010-2011 Planning and Scheduling Team (PST) @ ISTC-CNR
 *                         http://pst.istc.cnr.it
 *
 *                  Project PANDORA - FP7-ICT-SEC-2007-1
 *          (Advanced Training Environment for Crisis Scenarios)
 *                       Grant agreement no.: 225387
 *
 *                       http://pandora.eupm.net/
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.
 */
package it.cnr.istc.sponsor.tt.abstracts;

import java.util.LinkedList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Luca Coraci
 * @param <T>
 */
public abstract class AbstractLCComboModel<T> extends DefaultComboBoxModel {

    protected List<T> datas = new LinkedList<T>();

    public AbstractLCComboModel(List<T> d) {
        if(d != null){
            this.datas = d;
        }
    }

    @Override
    public T getElementAt(int index) {
        System.out.println("index: "+index);
        T element = index == -1 ? null : datas.get(index);
        System.out.println("element -> "+element.toString());
        return element;
        // return dati.get(index);

    }
    
    public void removeElementAt(int index){
        this.datas.remove(index);
        fireIntervalRemoved(this, index, index);
    }
    
    public void addItem(T element){
        this.datas.add(element);
        this.fireIntervalAdded(this, 0, datas.size()-1);
    }
    
    public void clear(){
        this.datas.clear();
        this.fireContentsChanged(this, 0, 0);
    }
    
    

    @Override
    public int getSize() {
        return datas.size();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getSelectedItem() {
        return (T)super.getSelectedItem();
    }

    
    public List<T> getDatas()
    {
        return this.datas;
    }



}
