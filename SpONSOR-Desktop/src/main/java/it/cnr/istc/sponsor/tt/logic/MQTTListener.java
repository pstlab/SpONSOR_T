/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.logic;

import it.cnr.istc.sponsor.tt.logic.model.Activity;
import it.cnr.istc.sponsor.tt.logic.model.ActivityName;
import it.cnr.istc.sponsor.tt.logic.model.Job;
import it.cnr.istc.sponsor.tt.logic.model.Keyword;
import it.cnr.istc.sponsor.tt.logic.model.Person;
import it.cnr.istc.sponsor.tt.logic.model.Project;
import it.cnr.istc.sponsor.tt.logic.model.Skill;
import java.util.List;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public interface MQTTListener {
    
    public void peopleDataArrived(List<Person> people);
    
    public void userCreated(Person person);
      
    public void jobsDataArrived(List<Job> jobs);
    
    public void projectsDataArrived(List<Project> projects);
    
    public void projectCreated(Project project);
    
    public void projectLoaded(Project project);
    
    public void projectDeleted(long id);
    
    public void skillsDataArrived(List<Skill> skills);
    
    public void keywordsDataArrived(List<Keyword> skills);
    
    public void userDeleted(Long id);
    
    public void jobDeleted(Long id);
    
    public void jobCreated(String nameJob, Long id);
    
    public void keywordDeleted(Long id);
    
    public void keywordCreated(Long id);
    
    public void activityNamesDataArrived(List<ActivityName> activities);
    
    public void activityNameCreated(ActivityName activity);
    
    public void activityNameDeleted(long id);
    
}
