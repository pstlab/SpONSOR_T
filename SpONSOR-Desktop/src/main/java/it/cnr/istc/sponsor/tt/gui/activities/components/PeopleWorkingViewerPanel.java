/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.activities.components;

import it.cnr.istc.sponsor.tt.abstracts.RoundedBorderAllWorkers;
import it.cnr.istc.sponsor.tt.gui.engine.GuiEventListener;
import it.cnr.istc.sponsor.tt.gui.engine.GuiEventManager;
import it.cnr.istc.sponsor.tt.logic.MQTTClient;
import it.cnr.istc.sponsor.tt.logic.MQTTListener;
import it.cnr.istc.sponsor.tt.logic.TrainerManager;
import it.cnr.istc.sponsor.tt.logic.model.Activity;
import it.cnr.istc.sponsor.tt.logic.model.ActivityName;
import it.cnr.istc.sponsor.tt.logic.model.ActivityTurn;
import it.cnr.istc.sponsor.tt.logic.model.Job;
import it.cnr.istc.sponsor.tt.logic.model.Keyword;
import it.cnr.istc.sponsor.tt.logic.model.Person;
import it.cnr.istc.sponsor.tt.logic.model.Project;
import it.cnr.istc.sponsor.tt.logic.model.Skill;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class PeopleWorkingViewerPanel extends javax.swing.JPanel implements MQTTListener, GuiEventListener {

    RoundedBorderAllWorkers roundBorder = new RoundedBorderAllWorkers(Color.BLACK, 10);

    /**
     * Creates new form PeopleWorkingViewerPanel
     */
    public PeopleWorkingViewerPanel() {
        initComponents();
        String filename = "./digital-7.ttf";//this is for testing normally we would store the font file in our app (knows as an embedded resource), see this for help on that http://stackoverflow.com/questions/13796331/jar-embedded-resources-nullpointerexception/13797070#13797070

        Font font = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File(filename));
            font = font.deriveFont(Font.BOLD, 28);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            this.jLabel1.setBorder(roundBorder);
            this.jLabel1.setFont(font);
            this.jLabel1.setForeground(Color.GREEN);
            MQTTClient.getInstance().addMQTTListener(this);
            GuiEventManager.getInstance().addGuiEventListener(this);
//            this.jLabel1.setText(""+TrainerManager.getInstance().getPeople().size());
        } catch (FontFormatException ex) {
            Logger.getLogger(PeopleWorkingViewerPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PeopleWorkingViewerPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new JLabel();
        jLabel2 = new JLabel();

        setBackground(new Color(255, 255, 255));

        jLabel1.setBackground(new Color(255, 255, 255));
        jLabel1.setForeground(new Color(0, 204, 0));
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setText("jLabel1");
        jLabel1.setOpaque(true);

        jLabel2.setText("Volontari da Assegnare");

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JLabel jLabel1;
    private JLabel jLabel2;
    // End of variables declaration//GEN-END:variables

    @Override
    public void peopleDataArrived(List<Person> people) {
        int max = people.size();
        for (Person person : people) {
            if(person.isSleeping()){
                max--;
            }
        }
        roundBorder.setPersonAvailable(max);
        this.jLabel1.invalidate();
        this.jLabel1.repaint();
    }

    @Override
    public void jobsDataArrived(List<Job> jobs) {

    }

    @Override
    public void skillsDataArrived(List<Skill> skills) {

    }

    @Override
    public void userDeleted(Long id) {

    }

    @Override
    public void jobDeleted(Long id) {

    }

    @Override
    public void keywordDeleted(Long id) {

    }

    @Override
    public void newActivityEvent(Activity activity) {

    }

    @Override
    public void newProfileEvent(Job profile) {

    }

    @Override
    public void newActivityTurn(ActivityTurn turn) {
        int requiredPeople = 0;
        List<Activity> activities = TrainerManager.getInstance().getActivities();

        for (Activity activity : activities) {
            List<ActivityTurn> activityTurns = activity.getActivityTurns();
            for (ActivityTurn activityTurn : activityTurns) {
                requiredPeople += activityTurn.getRequiredProfiles().size();
            }
        }
        roundBorder.setPersonAvailable(TrainerManager.getInstance().getPeople().size() - requiredPeople);
        this.jLabel1.invalidate();
        this.jLabel1.repaint();
    }

    @Override
    public void changeDate(Date date) {

    }

    @Override
    public void changeTab(int t) {

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
    public void projectsDataArrived(List<Project> projects) {

    }

    @Override
    public void newDormient(long id) {
        int requiredPeople = 0;
        List<Activity> activities = TrainerManager.getInstance().getActivities();

        for (Activity activity : activities) {
            List<ActivityTurn> activityTurns = activity.getActivityTurns();
            for (ActivityTurn activityTurn : activityTurns) {
                requiredPeople += activityTurn.getRequiredProfiles().size();
            }
        }
        roundBorder.setPersonAvailable(TrainerManager.getInstance().getPeople().size() - requiredPeople);
        this.jLabel1.invalidate();
        this.jLabel1.repaint();
    }

    @Override
    public void dormientWokeup(long id) {
        int requiredPeople = 0;
        List<Activity> activities = TrainerManager.getInstance().getActivities();

        for (Activity activity : activities) {
            List<ActivityTurn> activityTurns = activity.getActivityTurns();
            for (ActivityTurn activityTurn : activityTurns) {
                requiredPeople += activityTurn.getRequiredProfiles().size();
            }
        }
        roundBorder.setPersonAvailable(TrainerManager.getInstance().getPeople().size() - requiredPeople);
        this.jLabel1.invalidate();
        this.jLabel1.repaint();
    }

  

    @Override
    public void projectDeleted(long id) {
    }

    @Override
    public void projectCreated(Project project) {
    }

    @Override
    public void jobCreated(String nameJob, Long id) {
    }

    @Override
    public void projectLoaded(Project project) {
        int requiredPeople = 0;
        List<Activity> activities = project.getActivities();

        for (Activity activity : activities) {
            List<ActivityTurn> activityTurns = activity.getActivityTurns();
            for (ActivityTurn activityTurn : activityTurns) {
                requiredPeople += activityTurn.getRequiredProfiles().size();
            }
        }
        roundBorder.setPersonAvailable(TrainerManager.getInstance().getPeople().size() - requiredPeople);
        this.jLabel1.invalidate();
        this.jLabel1.repaint();
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
    public void ghostNumberUpdated() {
    }

    @Override
    public void userCreated(Person person) {
        List<Person> people = TrainerManager.getInstance().getPeople();
        int max = people.size();
        for (Person p : people) {
            if(p.isSleeping()){
                max--;
            }
        }
        roundBorder.setPersonAvailable(max);
        this.jLabel1.invalidate();
        this.jLabel1.repaint();
    }

    @Override
    public void timeAvailableChanged() {
    }
}
