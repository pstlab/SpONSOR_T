/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.logic.model;

import it.cnr.istc.i18n.translator.TranslatorManager;
import it.cnr.istc.sponsor.tt.logic.TrainerManager;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class Job {
    
    private Long id;
    private String name;
    private List<Skill> skills = new ArrayList<>();
    private Color color;

    public Job() {
    }

   
    
    
    public Job(String name, Skill ... skills){
        this.name = name;
        for (Skill skill : skills) {
            this.skills.add(skill);
        }
    }
    
    public Job(String name, Color color, Skill ... skills){
        this.name = name;
        for (Skill skill : skills) {
            this.skills.add(skill);
        }
        this.color = color;
    }
    
    public Job(long id, String name, Color color, List<Skill> skills){
        this.name = name;
        this.skills = skills;
        this.color = color;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
    
    
    
    

    public Job(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }
    
    public void addSkill(Skill skill){
        this.skills.add(skill);
    }
    
    public boolean hasSkillWithName(String skillName){
        if(skillName == null){
            return false;
        }
        for (Skill skill : skills) {
            if(skillName.equals(skill.getName())){
                return true;
            }
            String translation = TranslatorManager.getInstance().getTranslation(skill.getName());
            if(translation != null && translation.equals(skillName)){
                return true;
            }
        }
        return false;
    }
    
    public void removeSkill(String skillName){
        int indexToRemove = -1;
        int i = 0;
        for (Skill skill : skills) {
            if(skill.getName().equals(skillName)){
                indexToRemove = i;
                break;
            }
            i++;
        }
        if(indexToRemove != -1){
            skills.remove(indexToRemove);
        }
    }
    
    public void addSkill(String skillName){
        if(!hasSkillWithName(skillName)){
            List<Skill> allSkills = ModelManager.getInstance().getAllSkills();
            for (Skill skill : allSkills) {
                if(skillName.equals(skill.getName())){
                    this.skills.add(skill);
                }
            }
        }
    }

    @Override
    public String toString() {
        String res = this.name + " {";
        for (Skill skill : skills) {
            res+= skill.getName()+", ";
        }
        res = res.substring(0, res.length()-1);
        return res + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(obj instanceof Job){
            System.out.println("I AM "+this.getName()+" with ID: "+this.getId()+" & OBJ IS "+((Job)obj).getName()+" with ID: "+((Job)obj).getId());
            return this.getId().equals(((Job)obj).getId());
        }else{
            return false;
        }
    }
    
    
    
    
    
    
}
