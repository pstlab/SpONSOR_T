/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.st.server.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
@NamedQueries(
        {
            @NamedQuery(name = "AppTurnIndex.findAllByCodePerson", query = "SELECT c FROM AppTurnIndex c WHERE c.personCode = :x")
        }
)
@Entity
public class AppTurnIndex implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String personCode;
    @OneToMany
    private List<AppTurno> turns = new ArrayList<>();

    public AppTurnIndex() {
    }

    
    public AppTurnIndex(String personCode) {
        this.personCode = personCode;
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPersonCode() {
        return personCode;
    }

    public void setPersonCode(String personCode) {
        this.personCode = personCode;
    }

    public List<AppTurno> getTurns() {
        return turns;
    }

    public void setTurns(List<AppTurno> turns) {
        this.turns = turns;
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
        if (!(object instanceof AppTurnIndex)) {
            return false;
        }
        AppTurnIndex other = (AppTurnIndex) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.cnr.istc.st.server.app.AppTurnIndex[ id=" + id + " ]";
    }
    
}
