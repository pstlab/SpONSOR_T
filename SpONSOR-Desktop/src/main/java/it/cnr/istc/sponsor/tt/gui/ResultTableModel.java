/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui;


import it.cnr.istc.sponsor.tt.logic.model.Account;
import java.util.Map;

/**
 *
 * @author user
 */
public class ResultTableModel  {

    public ResultTableModel() {
        
    }

    //{"Activity/Role", "President", "Structure", "Brilliant", "Evaluator", "Concrete", "Explorer", "Worker", "Objectivist"}

    
    public void parseData(Account account){
        try{
        Map<String, Map<String, Integer>> qData = account.getPerceptionQuestionnary();
        int C = qData.get("Sezione I").get("g") +
                qData.get("Sezione II").get("a") +
                qData.get("Sezione III").get("h") +
                qData.get("Sezione IV").get("d") +
                qData.get("Sezione V").get("b") +
                qData.get("Sezione VI").get("f") +
                qData.get("Sezione VII").get("e");
        
        int P = qData.get("Sezione I").get("d") +
                qData.get("Sezione II").get("b") +
                qData.get("Sezione III").get("a") +
                qData.get("Sezione IV").get("h") +
                qData.get("Sezione V").get("f") +
                qData.get("Sezione VI").get("c") +
                qData.get("Sezione VII").get("g");
        
        int S = qData.get("Sezione I").get("f") +
                qData.get("Sezione II").get("e") +
                qData.get("Sezione III").get("c") +
                qData.get("Sezione IV").get("b") +
                qData.get("Sezione V").get("d") +
                qData.get("Sezione VI").get("g") +
                qData.get("Sezione VII").get("a");
        
        int G = qData.get("Sezione I").get("c") +
//                qData.get("Sezione II").get("g") +
                qData.get("Sezione III").get("d") +
                qData.get("Sezione IV").get("e") +
                qData.get("Sezione V").get("h") +
                qData.get("Sezione VI").get("a") +
                qData.get("Sezione VII").get("f");
        
        int E = qData.get("Sezione I").get("a") +
                qData.get("Sezione II").get("c") +
                qData.get("Sezione III").get("f") +
                qData.get("Sezione IV").get("g") +
                qData.get("Sezione V").get("e") +
                qData.get("Sezione VI").get("h") +
                qData.get("Sezione VII").get("d");
        
        int V = qData.get("Sezione I").get("h") +
                qData.get("Sezione II").get("d") +
                qData.get("Sezione III").get("g") +
                qData.get("Sezione IV").get("c") +
                qData.get("Sezione V").get("a") +
                qData.get("Sezione VI").get("e") +
                qData.get("Sezione VII").get("b");
        
        int L = qData.get("Sezione I").get("b") +
                qData.get("Sezione II").get("f") +
                qData.get("Sezione III").get("e") +
                qData.get("Sezione IV").get("a") +
                qData.get("Sezione V").get("c") +
                qData.get("Sezione VI").get("b") +
                qData.get("Sezione VII").get("h");
        
        int O = qData.get("Sezione I").get("e") +
//                qData.get("Sezione II").get("h") +
                qData.get("Sezione III").get("b") +
                qData.get("Sezione IV").get("f") +
                qData.get("Sezione V").get("g") +
                qData.get("Sezione VI").get("d") +
                qData.get("Sezione VII").get("c");
         //{"Activity/Role", "President", "Structure", "Brilliant", "Evaluator", "Concrete", "Explorer", "Worker", "Objectivist"}
        System.out.println("PRESIDENT   = "+C);
        System.out.println("STRUCTURE   = "+P);
        System.out.println("BRILLIANT   = "+S);
        System.out.println("EVALUATOR   = "+G);
        System.out.println("CONCRETE    = "+E);
        System.out.println("EXPLORER    = "+V);
        System.out.println("WORKER      = "+L);
        System.out.println("OBJECTIVIST = "+O);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
    }
//     C P S G E V L O
//I    g d f c a h B e
//II   a b e g c d F h
//III  h a c d f g E b
//IV   d h b e g c A f
//V    b f d h e a C g
//VI   f c g a h e B d
//VII  E g a f d b H c

}
