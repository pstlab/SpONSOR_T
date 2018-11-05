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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
@Entity
@NamedQueries(
        {
            @NamedQuery(name = "AppTurn.findAllByCodePerson", query = "SELECT c FROM AppTurn c WHERE c.codePerson = :x"),
            @NamedQuery(name = "AppTurn.findAllByCodeTurn", query = "SELECT c FROM AppTurn c WHERE c.codeTurn = :x"),
            @NamedQuery(name = "AppTurn.findAllByCodeTurnANDcodePersone", query = "SELECT c FROM AppTurn c WHERE c.codeTurn = :x AND c.codePerson = :y")
        }
)
public class AppTurn implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String codePerson;
    private String codeTurn;
    private String activity;
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date startTime;
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date endTime;
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date abortTime;
    private long time;
    //private String otherDelay; //codePerson:minDelay;codePerson:minDelay .. 

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodePerson() {
        return codePerson;
    }

    public void setCodePerson(String codePerson) {
        this.codePerson = codePerson;
    }

    public String getCodeTurn() {
        return codeTurn;
    }

    public void setCodeTurn(String codeTurn) {
        this.codeTurn = codeTurn;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getAbortTime() {
        return abortTime;
    }

    public void setAbortTime(Date abortTime) {
        this.abortTime = abortTime;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

//    public String getOtherDelay() {
//        return otherDelay;
//    }
//
//    public void setOtherDelay(String otherDelay) {
//        this.otherDelay = otherDelay;
//    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AppTurn)) {
            return false;
        }
        AppTurn other = (AppTurn) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.cnr.istc.st.server.entity.AppTurn[ id=" + id + " ]";
    }

}
