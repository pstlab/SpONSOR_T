/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.activities.edit;

import it.cnr.istc.sponsor.tt.logic.model.ActivityName;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.DefaultListModel;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class ActivityNameListModel extends DefaultListModel<ActivityName>{
    
    public List<ActivityName> values(){
        Enumeration<ActivityName> elements = this.elements();
        List<ActivityName> result = new ArrayList<>();
        while(elements.hasMoreElements()){
            result.add(elements.nextElement());
        }
        return result;
    }
}
