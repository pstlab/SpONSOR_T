/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tc.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class LoginTestManager {
    
    private static LoginTestManager _instance = null;
    private Map<Integer, Integer> consumedPointsMap = new HashMap<>();
    private List<LoginEventListener> loginEventListeners = new ArrayList<>();
    private Account account = null;
    
    public static LoginTestManager getInstance() {
        if (_instance == null) {
            _instance = new LoginTestManager();
            return _instance;
        } else {
            return _instance;
        }
    }
    
    private LoginTestManager() {
        super();
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }
    
    
    public void addLoginEventListener(LoginEventListener loginEventListener){
        this.loginEventListeners.add(loginEventListener);
    }
    
    public void cleanup(){
        for (LoginEventListener loginEventListener : loginEventListeners) {
            loginEventListener.cleanup();
        }
    }
    
    public void backHome(){
        for (LoginEventListener loginEventListener : loginEventListeners) {
            loginEventListener.backHome();
        }
    }
    
}
