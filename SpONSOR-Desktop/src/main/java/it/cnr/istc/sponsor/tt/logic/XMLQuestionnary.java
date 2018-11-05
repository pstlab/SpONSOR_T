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
@XmlRootElement(name = "questionnary")
public class XMLQuestionnary {

    private String name;
    private List<XMLQuestion> questions = new ArrayList<>();
    private Integer max;

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setMax(Integer max) {
        this.max = max;
    }

    @XmlElement
    public Integer getMax() {
        return max;
    }
   

    @XmlElementWrapper(name = "questions")
    @XmlElement(name = "question")
    public List<XMLQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<XMLQuestion> questions) {
        this.questions = questions;
    }

}
