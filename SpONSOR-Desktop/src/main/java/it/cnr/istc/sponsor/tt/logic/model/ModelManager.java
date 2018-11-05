/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.logic.model;

import it.cnr.istc.i18n.annotations.I18N;
import it.cnr.istc.i18n.annotations.I18NUpdater;
import it.cnr.istc.i18n.translator.TranslatorManager;
import it.cnr.istc.sponsor.tt.gui.engine.GuiEventListener;
import it.cnr.istc.sponsor.tt.gui.engine.GuiEventManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class ModelManager implements GuiEventListener {

    private static ModelManager _instance = null;
    private List<Skill> allSkills = new ArrayList<>();
    @I18N(key = "label.skill1")
    private String skill1;
    @I18N(key = "label.skill2")
    private String skill2;
    @I18N(key = "label.skill3")
    private String skill3;
    @I18N(key = "label.skill4")
    private String skill4;
    @I18N(key = "label.skill5")
    private String skill5;
    @I18N(key = "label.skill6")
    private String skill6;
    @I18N(key = "label.skill7")
    private String skill7;
    @I18N(key = "label.skill8")
    private String skill8;
    private List<Activity> activities = new ArrayList<>();
    private List<Job> profiles = new ArrayList<>();

    public static ModelManager getInstance() {
        if (_instance == null) {
            _instance = new ModelManager();
            return _instance;
        } else {
            return _instance;
        }
    }

    private ModelManager() {
        super();
        GuiEventManager.getInstance().addGuiEventListener(this);

//        "Teleassistenza", new javax.swing.ImageIcon(SingleTimeActivityIntervalPanel.class.getResource("/it/cnr/istc/sponsor/tt/gui/icons/call16.png")));
//        this.activityIconMap.put("Emergenze", new javax.swing.ImageIcon(SingleTimeActivityIntervalPanel.class.getResource("/it/cnr/istc/sponsor/tt/gui/icons/alarm16.png")));
//        this.activityIconMap.put("Cucina"
//        this.addActivity(new Activity(1, "Teleassistenza"));
//        this.addActivity(new Activity(2, "Emergenze"));
//        this.addActivity(new Activity(3, "Cucina"));
        initSkill();
    }

    @I18NUpdater
    private void initSkill() {
        System.out.println(" - > init skill");
        allSkills.add(new Skill(skill5)); // CONCRETO
        allSkills.add(new Skill(skill1)); // PRESIDENTE
        allSkills.add(new Skill(skill2)); // STRUTTURATORE
        allSkills.add(new Skill(skill3)); // GENIALE
        allSkills.add(new Skill(skill6)); // ESPLORATORE
        allSkills.add(new Skill(skill4)); // VALUTATORE
        allSkills.add(new Skill(skill7)); // LAVORATORE
        allSkills.add(new Skill(skill8)); // OBIETTIVISTA
        System.out.println(" - > end init skill");
        System.out.println("SKIL >>>>>>>>>>> ");
        for (Skill allSkill : allSkills) {
            System.out.println("\t\t\t\t\t\t\t\tSKILL >> " + allSkill.getName());
        }
    }

    public List<Skill> getAllSkills() {
        return allSkills;
    }

    public List<Activity> getAllActivities() {
        return activities;
    }

    public void addActivity(Activity activity) {
        this.activities.add(activity);
    }

    public void removeActivity(Activity activity) {
        this.activities.remove(activity);
    }

    @Override
    public void newActivityEvent(Activity activity) {
        this.activities.add(activity);
    }

    @Override
    public void newProfileEvent(Job profile) {
        profiles.add(profile);
    }

    public List<Job> getProfiles() {
        return profiles;
    }

    public void fixSkillIds(List<Skill> skills) {
        System.out.println(" FIXING SKILLS");
        
        for (Skill allSkill : allSkills) {
            System.out.println("\t\t\t\t\t\t\t\tSKILL  2 >> " + allSkill.getName());
        }
        
//        this.allSkills = skills;
        for (Skill skill : skills) {
            for (Skill s : allSkills) {
                System.out.println("\t\t\t\t S1<" + s.getName());
                System.out.println("\t\t\t\t S2<" + skill.getName());
                System.out.println("\t\t\t\t T per S2<" + TranslatorManager.getInstance().getTranslation(skill.getName()));
                if (s.getName().equals(TranslatorManager.getInstance().getTranslation(skill.getName()))) {
                    System.out.println("\t\tfixing skills -> " + s.getName() + " con ID -> "+skill.getId());
                    s.setId(skill.getId());
                }
                if (s.getName().equals(skill.getName())) {
                    System.out.println("\t\tfixing skills -> " + s.getName() + " con ID -> "+skill.getId());
                    s.setId(skill.getId());
                }
            }
        }
        
        for (Skill allSkill : allSkills) {
            System.out.println("\t\t\t\t\t\t\t\tSKILL 3 >> " + allSkill.getName() + " and ID: "+allSkill.getId());
        }
    }

    @Override
    public void newActivityTurn(ActivityTurn turn) {

    }

    @Override
    public void changeDate(Date date) {

    }

    @Override
    public void changeTab(int t) {

    }

    @Override
    public void removeActivityEvent(Activity activity) {
        this.removeActivity(activity);
    }

    @Override
    public void newDormient(long id) {
        
    }

    @Override
    public void dormientWokeup(long id) {
        
    }

    @Override
    public void ghostNumberUpdated() {
    }

    @Override
    public void timeAvailableChanged() {
    }

}
