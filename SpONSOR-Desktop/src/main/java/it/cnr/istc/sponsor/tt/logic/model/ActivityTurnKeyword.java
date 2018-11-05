/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.logic.model;

import java.io.Serializable;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */

public class ActivityTurnKeyword implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    
    private Long turnId;
    
    private Keyword keyword;

    public ActivityTurnKeyword() {
    }

    public ActivityTurnKeyword( Long turnId, Keyword keyword) {
        this.turnId = turnId;
        this.keyword = keyword;
    }
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTurnId() {
        return turnId;
    }

    public void setTurnId(Long turnId) {
        this.turnId = turnId;
    }

    

    public Keyword getKeyword() {
        return keyword;
    }

    public void setKeyword(Keyword keyword) {
        this.keyword = keyword;
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
        if (!(object instanceof ActivityTurnKeyword)) {
            return false;
        }
        ActivityTurnKeyword other = (ActivityTurnKeyword) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.cnr.istc.st.server.entity.ActivityTurnKeyword[ id=" + id + " ]";
    }
    
}
