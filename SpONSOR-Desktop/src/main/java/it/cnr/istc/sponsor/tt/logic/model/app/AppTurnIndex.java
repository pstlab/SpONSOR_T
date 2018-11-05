/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.logic.model.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class AppTurnIndex implements Serializable {

    private static final long serialVersionUID = 1L;
    private String personCode;
    private List<AppTurno> turns = new ArrayList<>();


    public String getPersonCode() {
        return personCode;
    }

    public void setPersonCode(String personCode) {
        this.personCode = personCode;
    }

    public List<AppTurno> getTurns() {
        return turns;
    }

    public void setTurns(List<AppTurno> turns) {
        this.turns = turns;
    }
    

    
}
