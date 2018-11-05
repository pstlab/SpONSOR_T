/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.profiles.mr;

import it.cnr.istc.sponsor.tt.abstracts.AbstractLCComboModel;
import it.cnr.istc.sponsor.tt.logic.model.Job;
import java.util.List;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class ProfileComboBoxModel extends AbstractLCComboModel<Job>{
    
    public ProfileComboBoxModel(List<Job> d) {
        super(d);
    }
    
}
