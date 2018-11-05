/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.solution.mr;

import it.cnr.istc.sponsor.tt.abstracts.AbstractLCComboModel;
import it.cnr.istc.sponsor.tt.logic.MQTTClient;
import it.cnr.istc.sponsor.tt.logic.MQTTListener;
import it.cnr.istc.sponsor.tt.logic.model.ActivityName;
import it.cnr.istc.sponsor.tt.logic.model.Job;
import it.cnr.istc.sponsor.tt.logic.model.Keyword;
import it.cnr.istc.sponsor.tt.logic.model.Person;
import it.cnr.istc.sponsor.tt.logic.model.Project;
import it.cnr.istc.sponsor.tt.logic.model.Skill;
import java.util.List;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it>
 */
public class PeopleComboBoxModel extends AbstractLCComboModel<Person> implements MQTTListener {

    public PeopleComboBoxModel(List<Person> d) {
        super(d);
        MQTTClient.getInstance().addMQTTListener(this);
    }


    @Override
    public void peopleDataArrived(List<Person> people) {
        for (Person person : people) {
            this.addItem(person);
        }

    }

    @Override
    public void jobsDataArrived(List<Job> jobs) {
        
    }

    @Override
    public void skillsDataArrived(List<Skill> skills) {
        
    }

    @Override
    public void userDeleted(Long id) {
        int index = -1;
        for (int i = 0; i < this.datas.size(); i++) {
//            System.out.println("id -> "+this.datas.get(i).getId());
            if(this.datas.get(i).getId().equals(id)){
                index = i;
                break;
            }
        }
        System.out.println("UTENTE DA CANCELLARE ALLA POSIZIONE "+index);
        if(index!=-1){
            this.removeElementAt(index);
        }
    }

    @Override
    public void jobDeleted(Long id) {
        
    }

    @Override
    public void keywordDeleted(Long id) {
       
    }

    @Override
    public void keywordsDataArrived(List<Keyword> skills) {
        
    }

    @Override
    public void keywordCreated(Long id) {
        
    }

    @Override
    public void jobCreated(String nameJob, Long id) {
        
    }

    @Override
    public void projectsDataArrived(List<Project> projects) {
    }

    @Override
    public void projectCreated(Project project) {
    }

    @Override
    public void projectDeleted(long id) {
    }

    @Override
    public void projectLoaded(Project project) {
    }

    @Override
    public void activityNamesDataArrived(List<ActivityName> activities) {
    }

    @Override
    public void activityNameCreated(ActivityName activity) {
    }

    @Override
    public void activityNameDeleted(long id) {
    }

    @Override
    public void userCreated(Person person) {
        this.addItem(person);
    }

}
