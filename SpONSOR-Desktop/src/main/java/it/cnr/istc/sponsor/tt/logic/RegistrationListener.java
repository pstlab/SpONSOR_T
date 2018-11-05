/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.logic;

import it.cnr.istc.sponsor.tt.logic.model.Account;
import it.cnr.istc.sponsor.tt.logic.model.Person;
import java.util.List;



/**
 *
 * @author user
 */
public interface RegistrationListener {
    
    public void newAccountDetected(Account account);
    
    public void peopleDataArrived(List<Person> people);
    
}
