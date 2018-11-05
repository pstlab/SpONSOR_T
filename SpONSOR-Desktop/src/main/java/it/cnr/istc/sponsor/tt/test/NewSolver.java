/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.test;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class NewSolver {
    
    public static void main(String[] args) {
//                 per la creazione di variabili e vincoli..
        Model model = new Model("Televita model");

        BoolVar[] vars = new BoolVar[10];
        for (int i = 0; i < vars.length; i++) {
            // crea una variabile booleana (estende IntVar, lb=0 ub=1)..
            vars[i] = model.boolVar("b" + i);
        }
        // aggiunge una clausola (sum vars >= 1)
        model.addClauses(vars, new BoolVar[0]);
        // variabile obiettivo..
        IntVar obj_var = model.intVar(0, 10000, true);
        // espressione lineare uguale a variabile obiettivo..
        model.scalar(vars, new int[]{-3, 4, -2, 5, -1, 3, 2, 4, 2, 4}, "=", obj_var).post();
        // massimizzare la variabile obiettivo..
        model.setObjective(Model.MAXIMIZE, obj_var);

        System.out.println(model);
        Solver solver = model.getSolver();
       Solution record = null;
        while (solver.solve()) {
            record = new Solution(model).record();
            System.out.println(model);
            System.out.println(obj_var.getLB() + ", " + obj_var.getUB() + " -> " + obj_var.getValue());
        }
        System.out.println("---------------------------------------");
        System.out.println("SOLUZIONE");
        for (int i = 0; i < 10; i++) {
            System.out.println("VAR "+i+" = "+ (record.getIntVal(vars[i])==1));
        }
        
        System.out.println(record);
        
//         solve ha restituito false.. il dominio delle variabili non è più valido!!
    }
    
}
