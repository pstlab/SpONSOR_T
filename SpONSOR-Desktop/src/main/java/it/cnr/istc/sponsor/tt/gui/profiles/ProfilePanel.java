/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.profiles;

import it.cnr.istc.sponsor.tt.abstracts.SubMainPanel;
import it.cnr.istc.sponsor.tt.gui.engine.GuiEventListener;
import it.cnr.istc.sponsor.tt.gui.engine.GuiEventManager;
import it.cnr.istc.sponsor.tt.gui.profiles.mr.ProfileTableModel;
import it.cnr.istc.sponsor.tt.logic.MQTTClient;
import it.cnr.istc.sponsor.tt.logic.MQTTListener;
import it.cnr.istc.sponsor.tt.logic.model.Activity;
import it.cnr.istc.sponsor.tt.logic.model.ActivityName;
import it.cnr.istc.sponsor.tt.logic.model.ActivityTurn;
import it.cnr.istc.sponsor.tt.logic.model.ModelManager;
import it.cnr.istc.sponsor.tt.logic.model.Job;
import it.cnr.istc.sponsor.tt.logic.model.Keyword;
import it.cnr.istc.sponsor.tt.logic.model.Person;
import it.cnr.istc.sponsor.tt.logic.model.Project;
import it.cnr.istc.sponsor.tt.logic.model.Skill;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class ProfilePanel extends javax.swing.JPanel implements GuiEventListener, MQTTListener, SubMainPanel{

    private ProfileTableModel model = null;
    private String profileColumn0Label = "Ruoli";

    /**
     * Creates new form ActivityPanel
     */
    public ProfilePanel() {
        initComponents();
        String[] skillNames = ModelManager.getInstance().getAllSkills().stream().map(skill -> skill.getName()).toArray(String[]::new);
        String[] columns = new String[skillNames.length + 1];
        columns[0] = profileColumn0Label;
        for (int i = 0; i < skillNames.length; i++) {

            columns[i + 1] = skillNames[i];
        }
        model = new ProfileTableModel(columns);
        this.jTable1.setModel(model);
        this.jTable1.getColumnModel().getColumn(0).setMinWidth(120);
        jTable1.setRowHeight(32);
        jTable1.setSelectionBackground(new java.awt.Color(102, 255, 0));
        jTable1.setSelectionForeground(new java.awt.Color(0, 0, 204));
        ((JComponent) jTable1.getDefaultRenderer(Boolean.class)).setOpaque(true);
        GuiEventManager.getInstance().addGuiEventListener(this);
        MQTTClient.getInstance().addMQTTListener(this);
        System.out.println("---------- > how many queries ?");
        MQTTClient.getInstance().sendQueryToGetSkills();
        MQTTClient.getInstance().sendQueryToGetJobs();
        System.out.println("---------- > query sent");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton_create = new JButton();
        jButton_edit = new JButton();
        jButton_delete = new JButton();
        jPanel1 = new JPanel();
        jScrollPane1 = new JScrollPane();
        jTable1 = new JTable();

        jButton_create.setIcon(new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/linePlus32.png"))); // NOI18N
        jButton_create.setText("Nuovo");
        jButton_create.setFocusable(false);
        jButton_create.setHorizontalTextPosition(SwingConstants.CENTER);
        jButton_create.setVerticalTextPosition(SwingConstants.BOTTOM);
        jButton_create.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton_createActionPerformed(evt);
            }
        });

        jButton_edit.setText("EDIT");
        jButton_edit.setFocusable(false);
        jButton_edit.setHorizontalTextPosition(SwingConstants.CENTER);
        jButton_edit.setVerticalTextPosition(SwingConstants.BOTTOM);

        jButton_delete.setIcon(new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/lineRemove32.png"))); // NOI18N
        jButton_delete.setText("Cancella");
        jButton_delete.setHorizontalTextPosition(SwingConstants.CENTER);
        jButton_delete.setVerticalTextPosition(SwingConstants.BOTTOM);
        jButton_delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton_deleteActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new Color(255, 255, 204));

        jTable1.setModel(new DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 683, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
        );

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_createActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton_createActionPerformed
        NewProfileFrame frame = new NewProfileFrame();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }//GEN-LAST:event_jButton_createActionPerformed

    private void jButton_deleteActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton_deleteActionPerformed
        int selectedRow = this.jTable1.getSelectedRow();
        if(selectedRow != -1){
            Job selectedJob = this.model.getDatas().get(selectedRow);
            MQTTClient.getInstance().deleteJob(selectedJob.getId());
            
        }
        //NONFUNGE!!!
    }//GEN-LAST:event_jButton_deleteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton jButton_create;
    private JButton jButton_delete;
    private JButton jButton_edit;
    private JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private JTable jTable1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void newActivityEvent(Activity activity) {

    }

    @Override
    public void newProfileEvent(Job profile) {
        this.model.addRowElement(profile);
    }

    @Override
    public void peopleDataArrived(List<Person> person) {

    }

    @Override
    public void jobsDataArrived(List<Job> jobs) {
        for (Job job : jobs) {
            this.model.addRowElement(job);
        }
    }

    @Override
    public void skillsDataArrived(List<Skill> skills) {
        ModelManager.getInstance().fixSkillIds(skills);
    }

    @Override
    public void userDeleted(Long id) {
        
    }

    @Override
    public JButton[] getToolbarButtons() {
        JButton[] buttons = new JButton[2];
        buttons[0] = this.jButton_create;
        buttons[1] = this.jButton_delete;
//        buttons[1] = this.jButton_edit;
        return buttons;
    }

    @Override
    public void jobDeleted(Long id) {
        this.model.deleteJob(id);
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
    public void jobCreated(String nameJob, Long id) {
        
    }

    @Override
    public void newDormient(long id) {
    }

    @Override
    public void dormientWokeup(long id) {
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
    public void ghostNumberUpdated() {
    }

    @Override
    public void userCreated(Person person) {
    }

    @Override
    public void timeAvailableChanged() {
    }
}