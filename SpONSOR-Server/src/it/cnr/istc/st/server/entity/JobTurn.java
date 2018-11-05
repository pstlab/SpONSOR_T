/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.st.server.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
@Entity
public class JobTurn implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Job job;
    @OneToMany
    @JoinColumn(name = "job_wantedkeyword_id", nullable = true)
    private List<Keyword> wantedKeywords = new ArrayList<>();
    @OneToMany
    @JoinColumn(name = "job_unwantedkeyword_id", nullable = true)
    private List<Keyword> unwantedKeywords = new ArrayList<>();
    @OneToOne
    private Person person;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public List<Keyword> getWantedKeywords() {
        return wantedKeywords;
    }

    public void setWantedKeywords(List<Keyword> wantedKeywords) {
        this.wantedKeywords = wantedKeywords;
    }

    public List<Keyword> getUnwantedKeywords() {
        return unwantedKeywords;
    }

    public void setUnwantedKeywords(List<Keyword> unwantedKeywords) {
        this.unwantedKeywords = unwantedKeywords;
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
        if (!(object instanceof JobTurn)) {
            return false;
        }
        JobTurn other = (JobTurn) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "" + id + " ->job(" + job.getName() + ")";
    }

}
