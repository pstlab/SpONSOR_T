/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.people.mr;

import it.cnr.istc.sponsor.tt.abstracts.AbstractLCTableModel;
import it.cnr.istc.sponsor.tt.gui.engine.GuiEventManager;
import it.cnr.istc.sponsor.tt.gui.people.PeopleStatManager;
import it.cnr.istc.sponsor.tt.logic.MQTTClient;
import it.cnr.istc.sponsor.tt.logic.ParsedAccount;
import it.cnr.istc.sponsor.tt.logic.TrainerManager;
import it.cnr.istc.sponsor.tt.logic.model.Account;
import it.cnr.istc.sponsor.tt.logic.model.Keyword;
import it.cnr.istc.sponsor.tt.logic.model.Person;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class PeopleTableModel extends AbstractLCTableModel<ParsedAccount> {

    public PeopleTableModel() {
        super(new String[]{"Admin", "Volontari", "p", "p", "p", "p", "p", "p", "p", "p", "Dormiente", "Keywords"}, null);
    }

    public ParsedAccount getAccountAtRow(int row) {
        if (row < datas.size() && row >= 0) {
            return this.datas.get(row);
        }
        return null;

    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (this.datas.isEmpty()) {
            return null;
        }
        ParsedAccount pa = this.datas.get(rowIndex);
        Account a = pa.getAccount();
        switch (columnIndex) {
            case 0:
                return a.isAdmin();
            case 1:
                return a.getName() + " " + a.getSurname();
            case 2:
                return pa.getConcreto();
            case 3:
                return pa.getLeader();
            case 4:
                return pa.getPianificatore();
            case 5:
                return pa.getBrillante();
            case 6:
                return pa.getEsploratore();
            case 7:
                return pa.getValutatore();
            case 8:
                return pa.getLavoratore();
            case 9:
                return pa.getOggettivo();
            case 10:
                return pa.isSleeping();
            case 11: {
                Person personByID = TrainerManager.getInstance().getPersonByID(pa.getAccount().getId());
                if (personByID != null) {
                    List<Keyword> keywords = personByID.getKeywords();
                    return keywords.stream().map(i -> i.getKeyword()).collect(Collectors.joining(", "));
                }
                return "";
            }
            default:
                return 0;
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        if (col == 10) {
            return true;
        }
        return super.isCellEditable(row, col); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        if (col == 10) {
            System.out.println("VALUE -> " + value);
            ParsedAccount pa = this.datas.get(row);
            Account a = pa.getAccount();
            pa.setSleeping((Boolean) value);
            a.setSleeping((Boolean) value);
            if (((Boolean) value)) {
                TrainerManager.getInstance().getDormientsIds().add(a.getId());
                GuiEventManager.getInstance().newDormient(a.getId());
            } else {
                TrainerManager.getInstance().getDormientsIds().remove(a.getId());
                GuiEventManager.getInstance().dormientWokeup(a.getId());
            }
            MQTTClient.getInstance().setSleeping(a, ((Boolean) value));
        }
    }

    public void updateCircleSquare() {
        PeopleStatManager.getInstance().clear();

        int maxLeaderRow = 0;
        int maxLeader = -1;
        int maxPianificatoreRow = 0;
        int maxPianificatore = -1;
        int maxBrillanteRow = 0;
        int maxBrillante = -1;
        int maxValutatoreRow = 0;
        int maxValutatore = -1;
        int maxConcretoRow = 0;
        int maxConcreto = -1;
        int maxEsploratoreRow = 0;
        int maxEsploratore = -1;
        int maxLavoratoreRow = 0;
        int maxLavoratore = -1;
        int maxOggettivoRow = 0;
        int maxOggettivo = -1;

        for (int i = 0; i < this.datas.size(); i++) {
            ParsedAccount pa = this.datas.get(i);
            int[] skills = new int[8];
            skills[0] = pa.getConcreto();
            skills[1] = pa.getLeader();
            skills[2] = pa.getPianificatore();
            skills[3] = pa.getBrillante();
            skills[4] = pa.getEsploratore();
            skills[5] = pa.getValutatore();
            skills[6] = pa.getLavoratore();
            skills[7] = pa.getOggettivo();

            int maxIndex = 0;
            int maxValue = -1;
            for (int z = 0; z < skills.length; z++) {
                if (skills[z] > maxValue) {
                    maxValue = skills[z];
                    maxIndex = z;
                }
            }

            PeopleStatManager.getInstance().addCircleCoords(i, maxIndex + 2);
            
             for (int z = 0; z < skills.length; z++) {
                if (skills[z] == maxValue) {
                    PeopleStatManager.getInstance().addCircleCoords(i, z + 2);
                }
            }

            if (pa.getLeader() > maxLeader) {
                maxLeader = pa.getLeader();
                maxLeaderRow = i;
            }
            if (pa.getPianificatore() > maxPianificatore) {
                maxPianificatore = pa.getPianificatore();
                maxPianificatoreRow = i;
            }

            if (pa.getBrillante() > maxBrillante) {
                maxBrillante = pa.getBrillante();
                maxBrillanteRow = i;
            }
            if (pa.getValutatore() > maxValutatore) {
                maxValutatore = pa.getValutatore();
                maxValutatoreRow = i;
            }
            if (pa.getConcreto() > maxConcreto) {
                maxConcreto = pa.getConcreto();
                maxConcretoRow = i;
            }
            if (pa.getEsploratore() > maxEsploratore) {
                maxEsploratore = pa.getEsploratore();
                maxEsploratoreRow = i;
            }
            if (pa.getLavoratore() > maxLavoratore) {
                maxLavoratore = pa.getLavoratore();
                maxLavoratoreRow = i;
            }
            if (pa.getOggettivo() > maxOggettivo) {
                maxOggettivo = pa.getOggettivo();
                maxOggettivoRow = i;
            }
        }

//        skills[0] = pa.getConcreto();
//        skills[1] = pa.getLeader();
//        skills[2] = pa.getPianificatore();
//        skills[3] = pa.getBrillante();
//        skills[4] = pa.getEsploratore();
//        skills[5] = pa.getValutatore();
//        skills[6] = pa.getLavoratore();
//        skills[7] = pa.getOggettivo();
        PeopleStatManager.getInstance().addSquareCoords(maxConcretoRow, 2);
        PeopleStatManager.getInstance().addSquareCoords(maxLeaderRow, 3);
        PeopleStatManager.getInstance().addSquareCoords(maxPianificatoreRow, 4);
        PeopleStatManager.getInstance().addSquareCoords(maxBrillanteRow, 5);
        PeopleStatManager.getInstance().addSquareCoords(maxEsploratoreRow, 6);
        PeopleStatManager.getInstance().addSquareCoords(maxValutatoreRow, 7);
        PeopleStatManager.getInstance().addSquareCoords(maxLavoratoreRow, 8);
        PeopleStatManager.getInstance().addSquareCoords(maxOggettivoRow, 9);

        for (int i = 0; i < this.datas.size(); i++) {
            ParsedAccount pa = this.datas.get(i);
            int[] skills = new int[8];
            skills[0] = pa.getConcreto();
            skills[1] = pa.getLeader();
            skills[2] = pa.getPianificatore();
            skills[3] = pa.getBrillante();
            skills[4] = pa.getEsploratore();
            skills[5] = pa.getValutatore();
            skills[6] = pa.getLavoratore();
            skills[7] = pa.getOggettivo();         
            
            if(skills[0] == maxConcreto){
                 PeopleStatManager.getInstance().addSquareCoords(i, 2);
            }
            if(skills[1] == maxLeader){
                 PeopleStatManager.getInstance().addSquareCoords(i, 3);
            }
            if(skills[2] == maxPianificatore){
                 PeopleStatManager.getInstance().addSquareCoords(i, 4);
            }
            if(skills[3] == maxBrillante){
                 PeopleStatManager.getInstance().addSquareCoords(i, 5);
            }
            if(skills[4] == maxValutatore){
                 PeopleStatManager.getInstance().addSquareCoords(i, 6);
            }
            if(skills[5] == maxEsploratore){
                 PeopleStatManager.getInstance().addSquareCoords(i, 7);
            }
            if(skills[6] == maxLavoratore){
                 PeopleStatManager.getInstance().addSquareCoords(i, 8);
            }
            if(skills[7] == maxOggettivo){
                 PeopleStatManager.getInstance().addSquareCoords(i, 9);
            }
            
            
            
            
        }
    }

    @Override
    public Class getColumnClass(int column) {
        return getValueAt(0, column).getClass();
    }

    @Override
    public void addRowElement(ParsedAccount element) {
        super.addRowElement(element); //To change body of generated methods, choose Tools | Templates.   
        updateCircleSquare();
    }

    public Person getPersonByRow(int row) {
        ParsedAccount pa = this.datas.get(row);
        Person personByID = TrainerManager.getInstance().getPersonByID(pa.getAccount().getId());
        return personByID;
    }

    public void replacePerson(int row, Person newPerson) {
        this.datas.set(row, new ParsedAccount(newPerson.getAccount()));
        updateCircleSquare();
    }

    public void addRowElement(Account account) {
        System.out.println("\t\t\t ------------------------------------>  " + account.getName());
        super.addRowElement(new ParsedAccount(account));
        updateCircleSquare();
    }

    public void deleteUser(Long id) {
        int i = 0;
        for (ParsedAccount data : datas) {
            if (data.getAccount().getId().equals(id)) {
                this.removeRowElement(i);
                break;
            }
            i++;
        }
        updateCircleSquare();
    }

    public void update() {
        fireTableDataChanged();
    }
}
