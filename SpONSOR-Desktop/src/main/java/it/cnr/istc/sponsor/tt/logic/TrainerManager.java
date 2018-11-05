/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.logic;

import it.cnr.istc.sponsor.tt.abstracts.PaintSupplier;
import it.cnr.istc.sponsor.tt.gui.activities.table.OveralActivityTabbedPane;
import it.cnr.istc.sponsor.tt.gui.engine.GuiEventListener;
import it.cnr.istc.sponsor.tt.gui.engine.GuiEventManager;
import it.cnr.istc.sponsor.tt.logic.model.Account;
import it.cnr.istc.sponsor.tt.logic.model.Activity;
import it.cnr.istc.sponsor.tt.logic.model.ActivityName;
import it.cnr.istc.sponsor.tt.logic.model.ActivityTurn;
import it.cnr.istc.sponsor.tt.logic.model.Job;
import it.cnr.istc.sponsor.tt.logic.model.Keyword;
import it.cnr.istc.sponsor.tt.logic.model.Person;
import it.cnr.istc.sponsor.tt.logic.model.Project;
import it.cnr.istc.sponsor.tt.logic.model.Skill;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author user
 */
public class TrainerManager implements GuiEventListener, MQTTListener {

    private static TrainerManager _instance = null;
//    private Map<Integer, Integer> consumedPointsMap = new HashMap<>();
    private List<RegistrationListener> registrationListeners = new ArrayList<>();
    private List<Activity> activities = new ArrayList<>();
    private List<Job> jobs = new ArrayList<>();
    private PaintSupplier paintSupplier = new PaintSupplier();
    private List<Person> people = new ArrayList<>();
    private Map<ActivityTurn, Activity> turnMap = new HashMap<>();
    private List<Long> dormientsIds = new ArrayList<>();
    private List<Project> projects = new ArrayList<>();
    private Project currentProject = null;
    private OveralActivityTabbedPane overalPanel = null;
    private int monthToPlan = 1;
    private Date startPlanningDate = null;
    private Date endPlanningDate = null;
    private List<ActivityName> activityNames = null;
    private int ghostNumber = 0;
    

    public static TrainerManager getInstance() {
        if (_instance == null) {
            _instance = new TrainerManager();
            GuiEventManager.getInstance().addGuiEventListener(_instance);
            MQTTClient.getInstance().addMQTTListener(_instance);
            return _instance;
        } else {
            return _instance;
        }
    }

    private TrainerManager() {
        super();

    }

    public int getGhostNumber() {
        return ghostNumber;
    }

    public void setMonthToPlan(int monthToPlan) {
        this.monthToPlan = monthToPlan;
    }

    public int getMonthToPlan() {
        return monthToPlan;
    }

    public Date getStartPlanningDate() {
        return startPlanningDate;
    }

    public void setStartPlanningDate(Date startPlanningDate) {
        this.startPlanningDate = startPlanningDate;
    }

    public Date getEndPlanningDate() {
        return endPlanningDate;
    }

    public void setEndPlanningDate(Date endPlanningDate) {
        if (this.endPlanningDate == null) {
            this.endPlanningDate = endPlanningDate;
            return;
        }
        if (endPlanningDate.after(this.endPlanningDate)) {
            this.endPlanningDate = endPlanningDate;
        }
    }

    public void setOveralPanel(OveralActivityTabbedPane overalPanel) {
        this.overalPanel = overalPanel;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public List<Long> getDormientsIds() {
        return dormientsIds;
    }

    public Person getPersonByID(long id) {
        for (Person person : people) {
            if (person.getId() == id) {
                return person;
            }
        }

        return null;
    }

    public void mapTurn(ActivityTurn turn, Activity activity) {
        this.turnMap.put(turn, activity);
    }

    public Activity getActivityByTurn(ActivityTurn turn) {
        return this.turnMap.get(turn);
    }

    public Color getNexColor() {
        return this.paintSupplier.getNextPaint();
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public Color getColorByJob(Job job) {
        for (Job j : jobs) {
            if (j.getName().equals(job.getName())) {
                return j.getColor();
            }
        }
        return null;
    }

    public void addActivity(Activity activity) {
        this.activities.add(activity);
        GuiEventManager.getInstance().newActivity(activity);
    }

    public void removeActivity(Activity activity) {
        this.activities.remove(activity);
        for (ActivityName activityName : activityNames) {
            if (activityName.equals(activity.getActivityName())) {
                activityName.setUsed(false);
            }
        }
        Iterator<ActivityTurn> listIterator = turnMap.keySet().iterator();
        //tolgo la persona dalla lista dei candidati
        while (listIterator.hasNext()) {
            if (listIterator.next().getId() == (activity.getId())) {
                listIterator.remove();
            }
        }
        GuiEventManager.getInstance().removeActivity(activity);
    }

    public void addRegistrationListener(RegistrationListener listener) {
        this.registrationListeners.add(listener);
    }

    public void newAccountDetected(Account account) {
        for (RegistrationListener registrationListener : registrationListeners) {
            registrationListener.newAccountDetected(account);
        }
    }

    public void updateGhosts() {
        int ghosts = 0;
        for (Person person : people) {
            if (!person.isSleeping()) {
                ghosts += person.getMaxTurnPerWeek();
            }
        }
        this.ghostNumber = ghosts;
        GuiEventManager.getInstance().ghostNumberUpdated();
    }

    public List<Person> getPeople() {
        List<Person> notDormients = new ArrayList<>();
        for (Person person : people) {
            if (!this.dormientsIds.contains(person.getId())) {
//                System.out.println(person.getName()+ "is not dormient!");
                notDormients.add(person);
            }
        }
        return notDormients;
    }

    @Override
    public void newActivityEvent(Activity activity) {

    }

//    public void addNewPerson(Person person) {
//        this.people.add(person);
//    }

    @Override
    public void newProfileEvent(Job profile) {
        profile.setColor(this.getNexColor());
        this.jobs.add(profile);
    }

    @Override
    public void peopleDataArrived(List<Person> person) {
        people.clear();
        people = person;
        for (Person p : person) {
            if (p.isSleeping()) {
                this.dormientsIds.add(p.getId());
            }
        }
        updateGhosts();
    }

    @Override
    public void jobsDataArrived(List<Job> jobs) {
        for (Job job : jobs) {
            if (this.jobs.contains(job)) {
                continue;
            }
            job.setColor(this.getNexColor());
            System.out.println("arrived job, changing color: " + job.getName());
            this.jobs.add(job);
        }

    }

    @Override
    public void skillsDataArrived(List<Skill> skills) {

    }

    @Override
    public void userDeleted(Long id) {
        int indexToRemove = -1;
        int i = 0;
        for (Person person : people) {
            if (person.getId().equals(id)) {
                indexToRemove = i;
                break;
            }
            i++;
        }
        if (indexToRemove != -1) {
            people.remove(indexToRemove);
        }
        updateGhosts();
    }

    public boolean isNameProjectExisting(String name) {
        for (Project project : projects) {
            if (project.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void jobDeleted(Long id) {

    }

    @Override
    public void keywordDeleted(Long id) {

    }

    @Override
    public void newActivityTurn(ActivityTurn turn) {

    }

    @Override
    public void changeDate(Date date) {

    }

    @Override
    public void changeTab(int tab) {

    }

    @Override
    public void keywordsDataArrived(List<Keyword> skills) {

    }

    @Override
    public void keywordCreated(Long id) {

    }

    @Override
    public void removeActivityEvent(Activity activity) {

    }

    @Override
    public void jobCreated(String jobName, Long id) {
        for (Job job : jobs) {
            if (job.getName().equals(jobName)) {
                job.setId(id);
            }
        }
    }

    @Override
    public void projectsDataArrived(List<Project> projects) {
        this.projects = projects;
    }

    @Override
    public void newDormient(long id) {
        for (Person person : people) {
            if (person.getId() == id) {
                person.setSleeping(true);
            }
        }
        updateGhosts();
    }

    @Override
    public void dormientWokeup(long id) {
        for (Person person : people) {
            if (person.getId() == id) {
                person.setSleeping(false);
            }
        }
        updateGhosts();
    }

    @Override
    public void projectCreated(Project project) {
        this.projects.add(project);
    }

    @Override
    public void projectDeleted(long id) {
    }

    public Project getCurrentProject() {
        return currentProject;
    }

    @Override
    public void projectLoaded(Project project) {
        for (Activity activity : activities) {
            Iterator<ActivityTurn> listIterator = turnMap.keySet().iterator();
            //tolgo la persona dalla lista dei candidati
            while (listIterator.hasNext()) {
                if (listIterator.next().getId() == (activity.getId())) {
                    listIterator.remove();
                }
            }
            GuiEventManager.getInstance().removeActivity(activity);
        }
        this.activities.clear();
        this.currentProject = project;
        this.activities = project.getActivities();
        this.turnMap.clear();
        for (Activity activity : activities) {
            for (ActivityTurn activityTurn : activity.getActivityTurns()) {
                this.mapTurn(activityTurn, activity);
            }
        }
        for (ActivityName activityName : this.activityNames) {
            for (Activity activity : project.getActivities()) {
                if (activityName.getId().equals(activity.getActivityName().getId())) {
                    activityName.setUsed(true);
                }
            }
        }
        this.overalPanel.projectHasBeenLoaded();
    }

    @Override
    public void activityNamesDataArrived(List<ActivityName> activities) {
        this.activityNames = activities;
    }

    public List<ActivityName> getActivityNames() {
        return activityNames;
    }

    @Override
    public void activityNameCreated(ActivityName activity) {
        this.activityNames.add(activity);
    }

    @Override
    public void activityNameDeleted(long id) {
    }

    @Override
    public void ghostNumberUpdated() {
    }

    @Override
    public void userCreated(Person person) {
        this.people.add(person);
        updateGhosts();
    }

    @Override
    public void timeAvailableChanged() {
    }

}
