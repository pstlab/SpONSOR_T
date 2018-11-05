/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.solver;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it>
 */
public interface SolverListener {
    
    public void solutionFound();
    
    public void temporarySolutionFound(int value);
    
    public void messageFromSolver(String message);
    
    public void error(String message);
    
}
