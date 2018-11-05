/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.logic.model;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class ComfirmedTurn {
    
    private Person person;
    private JobTurn job;

    public ComfirmedTurn(Person person, JobTurn job) {
        this.person = person;
        this.job = job;
    }

    public ComfirmedTurn() {
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public JobTurn getJobTurn() {
        return job;
    }

    public void setJobTurn(JobTurn job) {
        this.job = job;
    }
    
    
    
}
