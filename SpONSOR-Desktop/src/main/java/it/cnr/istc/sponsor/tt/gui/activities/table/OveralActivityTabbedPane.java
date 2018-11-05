/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.activities.table;

import it.cnr.istc.sponsor.tt.abstracts.SubMainPanel;
import it.cnr.istc.sponsor.tt.gui.activities.components.GhostViewerPanel;
import it.cnr.istc.sponsor.tt.gui.activities.components.PeoplePerWeekPanel;
import it.cnr.istc.sponsor.tt.gui.activities.components.PeopleWorkingViewerPanel;
import it.cnr.istc.sponsor.tt.gui.activities.edit.ActivityEditDialog;
import it.cnr.istc.sponsor.tt.gui.engine.GuiEventListener;
import it.cnr.istc.sponsor.tt.gui.engine.GuiEventManager;
import it.cnr.istc.sponsor.tt.logic.MQTTClient;
import it.cnr.istc.sponsor.tt.logic.MQTTListener;
import it.cnr.istc.sponsor.tt.logic.TrainerManager;
import it.cnr.istc.sponsor.tt.logic.Utils;
import it.cnr.istc.sponsor.tt.logic.Week;
import it.cnr.istc.sponsor.tt.logic.model.Activity;
import it.cnr.istc.sponsor.tt.logic.model.ActivityName;
import it.cnr.istc.sponsor.tt.logic.model.ActivityTurn;
import it.cnr.istc.sponsor.tt.logic.model.Job;
import it.cnr.istc.sponsor.tt.logic.model.Keyword;
import it.cnr.istc.sponsor.tt.logic.model.Person;
import it.cnr.istc.sponsor.tt.logic.model.Project;
import it.cnr.istc.sponsor.tt.logic.model.Skill;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.Box;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class OveralActivityTabbedPane extends javax.swing.JPanel implements SubMainPanel {

    private boolean changing = false;
    private Map<String, Activity> titleActivityMap = new HashMap<>();

    /**
     * Creates new form OveralActivityTabbedPane
     */
    public OveralActivityTabbedPane() {
        initComponents();
        this.peoplePerWeekPanel1.updateDate(Utils.getCurrentMonday());
        this.activityGeneralTablePanel1.setTab(0);

        this.jTabbedPane1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                if (changing) {
                    return;
                }

                System.out.println("E -> " + e.toString());
                System.out.println("Tab: " + jTabbedPane1.getSelectedIndex());
                GuiEventManager.getInstance().changeTab(jTabbedPane1.getSelectedIndex());
                if (jTabbedPane1.getSelectedIndex() == jTabbedPane1.getTabCount() - 1) {
                    changing = true;
                    //String activityName = JOptionPane.showInputDialog(null, "Nome Attività: ");
                    ActivityEditDialog dialog = new ActivityEditDialog(new JFrame(), true);
                    dialog.setLocationRelativeTo(null);
                    dialog.setVisible(true);
                    ActivityName activityName = dialog.getActivityName();
                    if (activityName == null) {
                        jTabbedPane1.setSelectedIndex(0);
                        changing = false;
                        return;
                    }
                    if (activityName.getName().isEmpty()) {
                        jTabbedPane1.setSelectedIndex(0);
                        changing = false;
                        return;
                    }
                    System.out.println("ACT NAME: " + activityName);
                    ActivityTablePanel atp = new ActivityTablePanel(null);
                    atp.init();
                    atp.setTab(jTabbedPane1.getTabCount() - 1);
                    Activity activity = new Activity();
                    activity.setActivityName(activityName);
                    TrainerManager.getInstance().addActivity(activity);
                    atp.setActivity(activity);
                    titleActivityMap.put("<html><b>" + activityName.getName(), activity);
                    jTabbedPane1.insertTab("<html><b>" + activityName.getName(), null, atp, activityName.getName(), jTabbedPane1.getTabCount() - 1);
                    JPanel pnlTab = new JPanel(new GridBagLayout());
                    pnlTab.setOpaque(false);
                    JLabel lblTitle = new JLabel("<html><b>" + activityName.getName());
                    JToolBar bar = new JToolBar();
                    bar.setFloatable(false);
                    bar.setBorderPainted(false);
                    bar.setBorder(null);
                    bar.setMaximumSize(new Dimension(32, 8));
                    JButton btnClose = new JButton();
                    btnClose.setIcon(new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/delete8.png")));
                    btnClose.setMargin(new Insets(2, 2, 2, 2));
                    btnClose.addActionListener(new RemoveTabAction("<html><b>" + activityName.getName()));

                    GridBagConstraints gbc = new GridBagConstraints();
                    gbc.gridx = 0;
                    gbc.gridy = 0;
                    gbc.weightx = 1;
                    bar.add(btnClose);

                    pnlTab.add(lblTitle, gbc);

                    gbc.gridx++;
                    gbc.weightx = 0;
                    pnlTab.add(bar, gbc);

                    jTabbedPane1.setTabComponentAt(jTabbedPane1.getTabCount() - 2, pnlTab);

                    // btnClose.addActionListener(myCloseActionHandler);
                    jTabbedPane1.setSelectedComponent(atp);
                    changing = false;
                }
            }
        });
        TrainerManager.getInstance().setOveralPanel(this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        peopleWorkingViewerPanel1 = new PeopleWorkingViewerPanel();
        peoplePerWeekPanel1 = new PeoplePerWeekPanel();
        filler1 = new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20));
        jButton_SalvaTodoMundo = new JButton();
        jButton_Carica = new JButton();
        jButton_ManageActivity = new JButton();
        ghostViewerPanel1 = new GhostViewerPanel();
        jTabbedPane1 = new JTabbedPane();
        jPanel1 = new JPanel();
        activityGeneralTablePanel1 = new ActivityGeneralTablePanel();
        jPanel2 = new JPanel();

        jButton_SalvaTodoMundo.setIcon(new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/save32.png"))); // NOI18N
        jButton_SalvaTodoMundo.setText("Salva Progetto");
        jButton_SalvaTodoMundo.setHorizontalTextPosition(SwingConstants.CENTER);
        jButton_SalvaTodoMundo.setVerticalTextPosition(SwingConstants.BOTTOM);
        jButton_SalvaTodoMundo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton_SalvaTodoMundoActionPerformed(evt);
            }
        });

        jButton_Carica.setIcon(new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/openfile32.png"))); // NOI18N
        jButton_Carica.setText("Carica Progetto");
        jButton_Carica.setHorizontalTextPosition(SwingConstants.CENTER);
        jButton_Carica.setVerticalTextPosition(SwingConstants.BOTTOM);
        jButton_Carica.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton_CaricaActionPerformed(evt);
            }
        });

        jButton_ManageActivity.setIcon(new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/actsacts32.png"))); // NOI18N
        jButton_ManageActivity.setText("Gestisci Gruppi");
        jButton_ManageActivity.setHorizontalTextPosition(SwingConstants.CENTER);
        jButton_ManageActivity.setVerticalTextPosition(SwingConstants.BOTTOM);

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(activityGeneralTablePanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(activityGeneralTablePanel1, GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Vista Generale", jPanel1);

        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 678, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 319, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab(" + ", jPanel2);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_SalvaTodoMundoActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton_SalvaTodoMundoActionPerformed
        //SALVA TUTTO
        String nomeProgetto = JOptionPane.showInputDialog(null, "Salva con nome:", "Salva progetto", JOptionPane.INFORMATION_MESSAGE);
        if (nomeProgetto == null) {
            return;
        }
        if (nomeProgetto.isEmpty()) {
            return;
        }
        if (TrainerManager.getInstance().isNameProjectExisting(nomeProgetto)) {
            JOptionPane.showMessageDialog(null, "Spiacente, nome già esistente!", "Errore", JOptionPane.ERROR_MESSAGE);
        } else {
            MQTTClient.getInstance().saveProject(nomeProgetto);
        }


    }//GEN-LAST:event_jButton_SalvaTodoMundoActionPerformed

    private void jButton_CaricaActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton_CaricaActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                LoadProjectDialog dialog = new LoadProjectDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {

                    }
                });
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
            }
        });
    }//GEN-LAST:event_jButton_CaricaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ActivityGeneralTablePanel activityGeneralTablePanel1;
    private Box.Filler filler1;
    private GhostViewerPanel ghostViewerPanel1;
    private JButton jButton_Carica;
    private JButton jButton_ManageActivity;
    private JButton jButton_SalvaTodoMundo;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JTabbedPane jTabbedPane1;
    private PeoplePerWeekPanel peoplePerWeekPanel1;
    private PeopleWorkingViewerPanel peopleWorkingViewerPanel1;
    // End of variables declaration//GEN-END:variables

    @Override
    public JComponent[] getToolbarButtons() {
        jButton_ManageActivity.setVisible(false);
        JComponent[] bb = new JComponent[6];
        bb[0] = this.jButton_ManageActivity;
        bb[1] = this.peopleWorkingViewerPanel1;
        bb[2] = this.ghostViewerPanel1;
        bb[3] = filler1;
        bb[4] = jButton_SalvaTodoMundo;
        bb[5] = jButton_Carica;
        //   bb[2] = this.peoplePerWeekPanel1;
        return bb;
    }

    public void projectHasBeenLoaded() {

//        JOptionPane.showMessageDialog(null, "ora dovrei caricare tutte le attività");
//        JOptionPane.showMessageDialog(null, "ora dovrei caricare tutte le attività");
        int tabCount = this.jTabbedPane1.getTabCount();
        changing = true;
        for (int i = 1; i < tabCount - 1; i++) {
            this.jTabbedPane1.remove(1);
        }
        this.titleActivityMap.clear();

//        JOptionPane.showMessageDialog(null, "ora dovrei caricare tutte le attivit 2");
        Project currentProject = TrainerManager.getInstance().getCurrentProject();
        Long idate = currentProject.getInitialDate();
        Date initialDate = new Date(idate);
        Date thisMonday = Utils.getCurrentMonday();
        Date selectedDate = null;
        if (idate < thisMonday.getTime()) {

            int answer = JOptionPane.showConfirmDialog(null, "Questo piano inizia nel passato, precisamente il :" + initialDate + "\nVuoi traslare il piano a questa settimana ? ", "Piano nel passato", JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {

                Date oldMonday = Utils.getCurrentMonday(initialDate);

                GregorianCalendar gc_oldMonday = new GregorianCalendar();
                gc_oldMonday.setTime(oldMonday);
                gc_oldMonday.set(Calendar.HOUR, 0);
                gc_oldMonday.set(Calendar.HOUR_OF_DAY, 0);
                gc_oldMonday.set(Calendar.MINUTE, 0);
                gc_oldMonday.set(Calendar.SECOND, 0);
                gc_oldMonday.set(Calendar.MILLISECOND, 0);
                GregorianCalendar gc_thisMonday = new GregorianCalendar();
                gc_thisMonday.setTime(thisMonday);
                gc_thisMonday.set(Calendar.HOUR, 0);
                gc_thisMonday.set(Calendar.HOUR_OF_DAY, 0);
                gc_thisMonday.set(Calendar.MINUTE, 0);
                gc_thisMonday.set(Calendar.SECOND, 0);
                gc_thisMonday.set(Calendar.MILLISECOND, 0);
                long difference = gc_thisMonday.getTime().getTime() - gc_oldMonday.getTime().getTime();
                for (Activity activity : currentProject.getActivities()) {
                    for (ActivityTurn activityTurn : activity.getActivityTurns()) {
                        System.out.println("OLD START TIME -> " + new Date(activityTurn.getStartTime()));
                        activityTurn.setStartTime(activityTurn.getStartTime() + difference);
                        activityTurn.setEndTime(activityTurn.getEndTime() + difference);
                        System.out.println("NEW START TIME -> " + new Date(activityTurn.getStartTime()));
                        System.out.println("-------------------------------");
                    }
                }
            } else if (answer == JOptionPane.NO_OPTION) {
                SelectDayDialog dialog = new SelectDayDialog(new JFrame(), true);
                dialog.setVisible(true);
                selectedDate = dialog.getSelectedDate();
                System.out.println("DATA SELEZIONATA = " + selectedDate);
                Date futureMonday = Utils.getCurrentMonday(selectedDate);
                GregorianCalendar futureDate = new GregorianCalendar();
                futureDate.setTime(futureMonday);
                futureDate.set(Calendar.HOUR, 0);
                futureDate.set(Calendar.HOUR_OF_DAY, 0);
                futureDate.set(Calendar.MINUTE, 0);
                futureDate.set(Calendar.SECOND, 0);
                futureDate.set(Calendar.MILLISECOND, 0);
                
                Date oldMonday = Utils.getCurrentMonday(initialDate);
                GregorianCalendar gc_oldMonday = new GregorianCalendar();
                gc_oldMonday.setTime(oldMonday);
                gc_oldMonday.set(Calendar.HOUR, 0);
                gc_oldMonday.set(Calendar.HOUR_OF_DAY, 0);
                gc_oldMonday.set(Calendar.MINUTE, 0);
                gc_oldMonday.set(Calendar.SECOND, 0);
                gc_oldMonday.set(Calendar.MILLISECOND, 0);
                
                System.out.println("FUTURE DATE -> "+futureDate.getTime());
                System.out.println("OLD MO DATE -> "+gc_oldMonday.getTime());
                long difference = futureDate.getTime().getTime() - gc_oldMonday.getTime().getTime();
                for (Activity activity : currentProject.getActivities()) {
                    for (ActivityTurn activityTurn : activity.getActivityTurns()) {
                        System.out.println("OLD START TIME -> " + new Date(activityTurn.getStartTime()));
                        activityTurn.setStartTime(activityTurn.getStartTime() + difference);
                        activityTurn.setEndTime(activityTurn.getEndTime() + difference);
                        System.out.println("NEW START TIME -> " + new Date(activityTurn.getStartTime()));
                        System.out.println("-------------------------------");
                    }
                }

                GuiEventManager.getInstance().changeDate(selectedDate);
            }
        }

        List<ActivityTablePanel> lap = new ArrayList<>();
        for (Activity activity : currentProject.getActivities()) {
            this.activityGeneralTablePanel1.newActivityEvent(activity);
//            this.titleActivityMap.put(activity.getName(), activity);

            ActivityTablePanel atp = new ActivityTablePanel(new Week(selectedDate));
            atp.setTab(jTabbedPane1.getTabCount() - 2);
            lap.add(atp);
//            TrainerManager.getInstance().addActivity(activity);
            String activityName = activity.getActivityName().getName();
            atp.setActivity(activity);
            titleActivityMap.put("<html><b>" + activityName, activity);
            jTabbedPane1.insertTab("<html><b>" + activityName, null, atp, activityName, jTabbedPane1.getTabCount() - 1);
            JPanel pnlTab = new JPanel(new GridBagLayout());
            pnlTab.setOpaque(false);
            JLabel lblTitle = new JLabel("<html><b>" + activityName);
            JToolBar bar = new JToolBar();
            bar.setFloatable(false);
            bar.setBorderPainted(false);
            bar.setBorder(null);
            bar.setMaximumSize(new Dimension(32, 8));
            JButton btnClose = new JButton();
            btnClose.setIcon(new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/delete8.png")));
            btnClose.setMargin(new Insets(2, 2, 2, 2));
            btnClose.addActionListener(new RemoveTabAction("<html><b>" + activityName));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1;
            bar.add(btnClose);

            pnlTab.add(lblTitle, gbc);

            gbc.gridx++;
            gbc.weightx = 0;
            pnlTab.add(bar, gbc);

            jTabbedPane1.setTabComponentAt(jTabbedPane1.getTabCount() - 2, pnlTab);

            // btnClose.addActionListener(myCloseActionHandler);
//            jTabbedPane1.setSelectedComponent(atp);
//            changing = false;PeopleWorkingViewerPanel
        }
        changing = false;
        for (ActivityTablePanel activityTablePanel : lap) {
            activityTablePanel.init();
        }

    }

    class RemoveTabAction implements ActionListener {

        String tabName;

        public RemoveTabAction(String tabName) {
            this.tabName = tabName;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int response = JOptionPane.showConfirmDialog(null, "Vuoi veramente cancellare questa attività con tutti i turni dichiarati ?", "Avviso", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (response != JOptionPane.YES_OPTION) {
                return;
            }
            int index = jTabbedPane1.indexOfTab(tabName);
            System.out.println("INDEX: " + index);
            if (index >= 0) {

                jTabbedPane1.setSelectedIndex(0);
                jTabbedPane1.remove(index);
                // It would probably be worthwhile getting the source
                // casting it back to a JButton and removing
                // the action handler reference ;)
                TrainerManager.getInstance().removeActivity(titleActivityMap.get(tabName));
                titleActivityMap.remove(tabName);

            }
            //jTabbedPane1.remove(index);
        }

    }

}
