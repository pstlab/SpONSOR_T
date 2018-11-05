/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.logic.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */

public class Solution implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private List<SolutionToken> solutionTokens = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SolutionToken> getSolutionTokens() {
        return solutionTokens;
    }

    public void setSolutionTokens(List<SolutionToken> solutionTokens) {
        this.solutionTokens = solutionTokens;
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
        if (!(object instanceof Solution)) {
            return false;
        }
        Solution other = (Solution) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.cnr.istc.st.server.entity.Solution[ id=" + id + " ]";
    }
    
}
