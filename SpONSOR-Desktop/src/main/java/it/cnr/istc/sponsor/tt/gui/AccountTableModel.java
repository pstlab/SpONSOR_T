/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui;

import it.cnr.istc.sponsor.tt.abstracts.AbstractLCTableModel;
import it.cnr.istc.sponsor.tt.logic.ParsedAccount;
import it.cnr.istc.sponsor.tt.logic.model.Account;



/**
 *
 * @author user
 */
public class AccountTableModel extends AbstractLCTableModel<ParsedAccount>{

    public AccountTableModel() {
        super(new String[]{"Volontari","Concreto","Presidente","Strutturatore","Geniale","Esploratore","Valutatore","Lavoratore","Obiettivista"}, null);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ParsedAccount pa = this.datas.get(rowIndex);
        Account a = pa.getAccount();
        switch(columnIndex){
            case 0: return a.getName()+" "+a.getSurname();
            case 1: return pa.getLeader();
            case 2: return pa.getPianificatore();
            case 3: return pa.getBrillante();
            case 4: return pa.getValutatore();
            case 5: return pa.getConcreto();
            case 6: return pa.getEsploratore();
            case 7: return pa.getLavoratore();
            case 8: return pa.getOggettivo();
            default: return 0;
        }
    }
    
}
