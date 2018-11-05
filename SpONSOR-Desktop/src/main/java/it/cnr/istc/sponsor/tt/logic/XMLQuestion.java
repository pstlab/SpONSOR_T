/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.logic;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
@XmlRootElement(name = "question")
public class XMLQuestion {
    
    private String title;
    private String subtitle;
    
    private List<XMLAnswer> answers = new ArrayList<>();

    @XmlElement
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @XmlElement
    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    @XmlElementWrapper(name = "answers")
    @XmlElement(name = "answer")
    public List<XMLAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<XMLAnswer> answers) {
        this.answers = answers;
    }
    
}
