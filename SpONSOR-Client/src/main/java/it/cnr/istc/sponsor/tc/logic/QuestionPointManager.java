/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tc.logic;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it>
 */
public class QuestionPointManager {
    private static QuestionPointManager _instance = null;
    private List<QuestionPointListener> listeners = new ArrayList<>();
    
    public static QuestionPointManager getInstance() {
        if (_instance == null) {
            _instance = new QuestionPointManager();
            return _instance;
        } else {
            return _instance;
        }
    }
    
    private QuestionPointManager() {
        super();
    }
    
    public void addQuestionPointListener(QuestionPointListener listener) {
        this.listeners.add(listener);
    }
    
    public void pointChanged() {
        for (QuestionPointListener listener : listeners) {
            listener.pointChanged();
        }
    }
    
}
