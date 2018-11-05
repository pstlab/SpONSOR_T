/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.st.server.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
@Entity
public class ComfirmedTurn implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private Person person;
    @OneToOne
    private JobTurn jobTurn;
    @OneToOne
    private AppTurn appturn;
    
    public ComfirmedTurn() {
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public AppTurn getAppturn() {
        return appturn;
    }

    public void setAppturn(AppTurn appturn) {
        this.appturn = appturn;
    }

    public JobTurn getJobTurn() {
        return jobTurn;
    }

    public void setJobTurn(JobTurn jobTurn) {
        this.jobTurn = jobTurn;
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComfirmedTurn)) {
            return false;
        }
        ComfirmedTurn other = (ComfirmedTurn) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.cnr.istc.st.server.entity.ComfirmedTurn[ id=" + id + " ]";
    }

}
