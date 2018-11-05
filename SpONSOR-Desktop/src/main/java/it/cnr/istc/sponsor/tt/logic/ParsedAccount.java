/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.logic;

import it.cnr.istc.sponsor.tt.logic.model.Account;
import it.cnr.istc.sponsor.tt.logic.model.ModelManager;
import it.cnr.istc.sponsor.tt.logic.model.Skill;
import it.cnr.istc.sponsor.tt.logic.model.SkillValue;
import java.util.Map;

/**
 *
 * @author user
 */
public class ParsedAccount implements Comparable<ParsedAccount>{

    private Account account;
    private transient static final String NOT_QUESTIONED = "NOT_QUESTIONED";

    private int leader;
    private int pianificatore;
    private int brillante;
    private int valutatore;
    private int concreto;
    private int esploratore;
    private int lavoratore;
    private int oggettivo;
    
    private boolean sleeping = false;

    public ParsedAccount(Account account) {
        this.account = account;
        parseData(account);
    }

    public boolean isSleeping() {
        return sleeping;
    }

    public void setSleeping(boolean sleeping) {
        this.sleeping = sleeping;
    }
    
    

    private void parseData(Account account) {
        try {

            this.sleeping = account.isSleeping();
            Map<String, Map<String, Integer>> qData = account.getPerceptionQuestionnary();

            if (qData.containsKey(NOT_QUESTIONED)) {
                concreto = qData.get(NOT_QUESTIONED).get("C");
                leader = qData.get(NOT_QUESTIONED).get("P");
                pianificatore = qData.get(NOT_QUESTIONED).get("S");
                brillante = qData.get(NOT_QUESTIONED).get("G");
                esploratore = qData.get(NOT_QUESTIONED).get("E");
                valutatore = qData.get(NOT_QUESTIONED).get("V");
                lavoratore = qData.get(NOT_QUESTIONED).get("L");
                oggettivo = qData.get(NOT_QUESTIONED).get("O");
                return;
            }

            concreto = qData.get("Sezione I").get("g")
                    + qData.get("Sezione II").get("a")
                    + qData.get("Sezione III").get("h")
                    + qData.get("Sezione IV").get("d")
                    + qData.get("Sezione V").get("b")
                    + qData.get("Sezione VI").get("f")
                    + qData.get("Sezione VII").get("e");

            leader = qData.get("Sezione I").get("d")
                    + qData.get("Sezione II").get("b")
                    + qData.get("Sezione III").get("a")
                    + qData.get("Sezione IV").get("h")
                    + qData.get("Sezione V").get("f")
                    + qData.get("Sezione VI").get("c")
                    + qData.get("Sezione VII").get("g");

            pianificatore = qData.get("Sezione I").get("f")
                    + qData.get("Sezione II").get("e")
                    + qData.get("Sezione III").get("c")
                    + qData.get("Sezione IV").get("b")
                    + qData.get("Sezione V").get("d")
                    + qData.get("Sezione VI").get("g")
                    + qData.get("Sezione VII").get("a");

            brillante = qData.get("Sezione I").get("c")
                    + //                qData.get("Sezione II").get("g") +
                    qData.get("Sezione III").get("d")
                    + qData.get("Sezione IV").get("e")
                    + qData.get("Sezione V").get("h")
                    + qData.get("Sezione VI").get("a")
                    + qData.get("Sezione VII").get("f");

            esploratore = qData.get("Sezione I").get("a")
                    + qData.get("Sezione II").get("c")
                    + qData.get("Sezione III").get("f")
                    + qData.get("Sezione IV").get("g")
                    + qData.get("Sezione V").get("e")
                    + qData.get("Sezione VI").get("h")
                    + qData.get("Sezione VII").get("d");

            valutatore = qData.get("Sezione I").get("h")
                    + qData.get("Sezione II").get("d")
                    + qData.get("Sezione III").get("g")
                    + qData.get("Sezione IV").get("c")
                    + qData.get("Sezione V").get("a")
                    + qData.get("Sezione VI").get("e")
                    + qData.get("Sezione VII").get("b");

            lavoratore = qData.get("Sezione I").get("b")
                    + qData.get("Sezione II").get("f")
                    + qData.get("Sezione III").get("e")
                    + qData.get("Sezione IV").get("a")
                    + qData.get("Sezione V").get("c")
                    + qData.get("Sezione VI").get("b")
                    + qData.get("Sezione VII").get("h");

            oggettivo = qData.get("Sezione I").get("e")
                    + //                qData.get("Sezione II").get("h") +
                    qData.get("Sezione III").get("b")
                    + qData.get("Sezione IV").get("f")
                    + qData.get("Sezione V").get("g")
                    + qData.get("Sezione VI").get("d")
                    + qData.get("Sezione VII").get("c");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Account getAccount() {
        return account;
    }

    public int getLeader() {
        return leader;
    }

    public int getPianificatore() {
        return pianificatore;
    }

    public int getBrillante() {
        return brillante;
    }

    public int getValutatore() {
        return valutatore;
    }

    public int getConcreto() {
        return concreto;
    }

    public int getEsploratore() {
        return esploratore;
    }

    public int getLavoratore() {
        return lavoratore;
    }

    public int getOggettivo() {
        return oggettivo;
    }

    @Override
    public int compareTo(ParsedAccount o) {
           return this.account.compareTo(o.getAccount());
    }

}
