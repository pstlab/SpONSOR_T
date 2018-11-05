/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.logic.model.app;

import java.io.Serializable;
/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class AppCollegue implements Serializable {

    private Long id;
    private String personCode;
    private boolean aborted = false;
    private int delay = 0;
    private boolean subsitute = false;
    private String idWantedKeys;
    private String idUnwantedKeys;

    public AppCollegue() {
    }

    public AppCollegue(Long id, String personCode, boolean aborted, int delay, boolean subsitute) {
        this.id = id;
        this.personCode = personCode;
        this.aborted = aborted;
        this.delay = delay;
        this.subsitute = subsitute;
    }

    public void setSubsitute(boolean subsitute) {
        this.subsitute = subsitute;
    }

    public boolean isSubsitute() {
        return subsitute;
    }

    public String getPersonCode() {
        return personCode;
    }

    public void setPersonCode(String personCode) {
        this.personCode = personCode;
    }

    public boolean isAborted() {
        return aborted;
    }

    public void setAborted(boolean aborted) {
        this.aborted = aborted;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public String getIdWantedKeys() {
        return idWantedKeys;
    }

    public void setIdWantedKeys(String idWantedKeys) {
        this.idWantedKeys = idWantedKeys;
    }

    public String getIdUnwantedKeys() {
        return idUnwantedKeys;
    }

    public void setIdUnwantedKeys(String idUnwantedKeys) {
        this.idUnwantedKeys = idUnwantedKeys;
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
        if (!(object instanceof AppCollegue)) {
            return false;
        }
        AppCollegue other = (AppCollegue) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.cnr.istc.st.server.app.AppCollegue[ id=" + id + " ]";
    }
    
}
