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
public class ActivityName implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private transient boolean used = false;

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

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
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
        if (!(object instanceof ActivityName)) {
            return false;
        }
        System.out.println("MY ID = "+this.id);
        System.out.println("MY NAME = "+this.name);
        System.out.println("OTHER ID = "+((ActivityName)object).id);
        System.out.println("NAME NAME = "+((ActivityName)object).name);
        
        ActivityName other = (ActivityName) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.name; 
    }

}
