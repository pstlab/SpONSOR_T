/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.st.server.entity;

import it.cnr.istc.st.server.app.AppCollegue;
import it.cnr.istc.st.server.app.AppPerson;
import it.cnr.istc.st.server.app.AppTurno;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it>
 */
@Entity
public class TurnCancellation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private AppTurno appTurno;
    @OneToOne
    private AppCollegue collegue;
    private long timeActivation;
    @OneToMany
    @JoinColumn(nullable = true)
    private List<Person> candidatePeople;
//    @OneToMany
//    private List<Person> ackPeople       = new ArrayList<>();
    @OneToMany
    @JoinColumn(nullable = true)
    private List<Person> yesPeople;

    public TurnCancellation() {
    }

    public TurnCancellation(Long id, AppTurno appTurno, AppCollegue collegue, long timeActivation) {
        this.id = id;
        this.appTurno = appTurno;
        this.collegue = collegue;
        this.timeActivation = timeActivation;
    }

    

    
    public AppTurno getAppTurno() {
        return appTurno;
    }

    public void setAppTurno(AppTurno appTurno) {
        this.appTurno = appTurno;
    }

    public AppCollegue getCollegue() {
        return collegue;
    }

    public void setCollegue(AppCollegue collegue) {
        this.collegue = collegue;
    }

    public long getTimeActivation() {
        return timeActivation;
    }

    public void setTimeActivation(long timeActivation) {
        this.timeActivation = timeActivation;
    }

    public List<Person> getCandidatePeople() {
        return candidatePeople;
    }

    public void setCandidatePeople(List<Person> candidatePeople) {
        this.candidatePeople = candidatePeople;
    }

//    public List<Person> getAckPeople() {
//        return ackPeople;
//    }
//
//    public void setAckPeople(List<Person> ackPeople) {
//        this.ackPeople = ackPeople;
//    }

    public List<Person> getYesPeople() {
        return yesPeople;
    }

    public void setYesPeople(List<Person> yesPeople) {
        this.yesPeople = yesPeople;
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
        if (!(object instanceof TurnCancellation)) {
            return false;
        }
        TurnCancellation other = (TurnCancellation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.cnr.istc.st.server.entity.TurnCancellation[ id=" + id + " ]";
    }
    
}
