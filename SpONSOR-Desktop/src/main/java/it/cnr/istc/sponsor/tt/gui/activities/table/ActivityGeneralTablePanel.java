/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.activities.table;

import it.cnr.istc.i18n.annotations.I18N;
import it.cnr.istc.i18n.annotations.I18NUpdater;
import it.cnr.istc.sponsor.tt.gui.activities.glass.ActivityGeneralTurnsLayer;
import it.cnr.istc.sponsor.tt.gui.activities.table.mr.ActivityComboBoxModel;
import it.cnr.istc.sponsor.tt.gui.activities.table.mr.ActivityComboBoxRenderer;
import it.cnr.istc.sponsor.tt.gui.engine.GuiEventListener;
import it.cnr.istc.sponsor.tt.gui.engine.GuiEventManager;
import it.cnr.istc.sponsor.tt.gui.profiles.JobSelectorDialog;
import it.cnr.istc.sponsor.tt.logic.SettingsManager;
import it.cnr.istc.sponsor.tt.logic.TrainerManager;
import it.cnr.istc.sponsor.tt.logic.Utils;
import it.cnr.istc.sponsor.tt.logic.Week;
import it.cnr.istc.sponsor.tt.logic.model.Activity;
import it.cnr.istc.sponsor.tt.logic.model.ActivityTurn;
import it.cnr.istc.sponsor.tt.logic.model.Job;
import it.cnr.istc.sponsor.tt.logic.model.JobTurn;
import it.cnr.istc.sponsor.tt.logic.model.ModelManager;
import it.cnr.istc.sponsor.tt.logic.model.Skill;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayer;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class ActivityGeneralTablePanel extends javax.swing.JPanel implements GuiEventListener {

    @I18N(key = "label.time")
    private String labelTime;
    @I18N(key = "day.monday")
    private String labelMonday;
    @I18N(key = "day.tuesday")
    private String labelTuesday;
    @I18N(key = "day.wednesday")
    private String labelWednesday;
    @I18N(key = "day.thursday")
    private String labelThursday;
    @I18N(key = "day.friday")
    private String labelFriday;
    @I18N(key = "day.saturday")
    private String labelSaturday;
    @I18N(key = "day.sunday")
    private String labelSunday;

    private Map<String, Component> labelMap = new HashMap<>();

    private int tab;

    private ActivityComboBoxModel activityComboBoxModel = null;
    private ActivityComboBoxRenderer activityComboBoxRenderer = null;
    private Date today;
    private ActivityGeneralTurnsLayer glassPane = null;
//    private List<Date> week = new ArrayList<>();
    private static Week week = null;
    private List<ActivityTurn> copiedTurns = new ArrayList<>();

    private Activity singleActivity = null;

    private boolean createSingle = false;
    private int delta = 0;
//    private List<String> activeKeys = new ArrayList<>();

    /**
     * Creates new form ActivityTablePanel
     */
    public ActivityGeneralTablePanel() {
        initComponents();

        GuiEventManager.getInstance().addGuiEventListener(this);
        JPanel tableWrapper = new JPanel();
        tableWrapper.setLayout(new GridLayout(0, 1));
        tableWrapper.add(jPanel_tableContainer);
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new GridLayout(0, 1));
//        TimeIntervalContainer panel = new TimeIntervalContainer();
        glassPane = new ActivityGeneralTurnsLayer();
        if (week == null) {
            this.today = Utils.getCurrentMonday();
            week = new Week(this.today);
        }
        jTable1.getColumnModel().getColumn(0).setMaxWidth(120);
        glassPane.setContext(jTable1, today);
//        ActivityCaldendarContainer acc = new ActivityCaldendarContainer((ActivityTurnsLayer)glassPane);
        JLayer<JPanel> jlayer = new JLayer<JPanel>(tableWrapper, glassPane);
        wrapper.add(jlayer);
        this.jScrollPane2.setViewportView(wrapper);

        for (int i = 0; i < 24; i++) {
            if (i < SettingsManager.getInstance().getOpenTime()) {
                continue;
            }
            this.activityTableModel2.addTimeRow(today, i, false);
            this.activityTableModel2.addTimeRow(today, i, true);
            if (i + 1 > SettingsManager.getInstance().getCloseTime()) {
//                this.activityTableModel2.addTimeRow(today, i, false);
                break;
            }

        }
        updateLabels(today);

        activityComboBoxModel = new ActivityComboBoxModel(ModelManager.getInstance().getAllActivities());
        System.out.println("SIZE OF MODEL: " + activityComboBoxModel.getSize());
        activityComboBoxRenderer = new ActivityComboBoxRenderer();
        this.jComboBox_Turni.setModel(activityComboBoxModel);
        this.jComboBox_Turni.setRenderer(activityComboBoxRenderer);
        Set<String> activityKeys = GuiEventManager.getInstance().getActivityKeys();
//        this.jScrollPane1.setTransferHandler(new ValueMainImportTransferHandler());

//        List<Job> jobs = TrainerManager.getInstance().getJobs();
//        for (Job job : jobs) {
//            JLabel q = new JLabel("   ");
//            q.setMinimumSize(new Dimension(24, 24));
//            q.setOpaque(true);
//            q.setBackground(job.getColor());
//            q.setToolTipText(labelize(job));
//            this.jToolBar_buttons.add(q);
//            JLabel label = new JLabel(" " + job.getName() + "  ");
//            this.jToolBar_buttons.add(label);
//        }
        jTable1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_C) {
                    System.out.println("                                COPIA !!");

                    copiedTurns.clear();
                    copiedTurns = getSelectedTurns();

                } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V) {
                    System.out.println("                                INCOLLA !!!!");
                    int selectedColumn = jTable1.getSelectedColumn();
                    List<ActivityTurn> toPasteList = new ArrayList<>();
                    System.out.println("copied turns: " + copiedTurns.size());
                    toPasteList.addAll(copiedTurns);
                    System.out.println("size of paste: " + toPasteList.size());
                    for (ActivityTurn activityTurn : toPasteList) {
                        System.out.println("ACTUNG !!");
                        Date startTime = new Date(activityTurn.getStartTime());
                        Date endTime = new Date(activityTurn.getEndTime());
                        ActivityTurn pasteTurn = new ActivityTurn(startTime.getTime(), endTime.getTime());

                        Date pasteDate = week.getDays().get(selectedColumn - 1);
                        startTime.setMonth(pasteDate.getMonth());
                        startTime.setDate(pasteDate.getDate());
                        endTime.setMonth(pasteDate.getMonth());
                        endTime.setDate(pasteDate.getDate());
                        List<JobTurn> requiredProfiles = activityTurn.getRequiredProfiles();
                        pasteTurn.getRequiredProfiles().addAll(requiredProfiles);
                        if (canAddThisTurn(pasteTurn)) {
                            System.out.println("can add ok");
                            singleActivity.addActivityTurn(pasteTurn);

//                            activityTableModel2.changeTurnActivity(row, column, index);
//             this.jTable1.getSelectionModel().clearSelection();
                        } else {
                            System.out.println("sorry cant add");
                        }
                    }
                    GuiEventManager.getInstance().newActivityTurn(null);
                    invalidate();
                    repaint();
                }

            }
        }
        );
    }

    public List<ActivityTurn> getSelectedTurns() {
        List<ActivityTurn> result = new ArrayList<>();
        int[] selectedRows = this.jTable1.getSelectedRows();
        int[] selectedColumns = this.jTable1.getSelectedColumns();
        if (selectedRows.length > 0 && selectedColumns.length > 0) {
            System.out.println("START SELECTION ROW: " + selectedRows[0]);
            System.out.println("END SELECTION ROW: " + selectedRows[selectedRows.length - 1]);
            System.out.println("START SELECTION COLUMN: " + selectedColumns[0]);
            System.out.println("END SELECTION COLUMN: " + selectedColumns[selectedColumns.length - 1]);
            Date startDate = new Date(week.getDays().get(selectedColumns[0] - 1).getTime());
            Date endDate = new Date(week.getDays().get(selectedColumns[selectedColumns.length - 1] - 1).getTime());
            startDate.setHours(selectedRows[0] + SettingsManager.getInstance().getOpenTime());
            endDate.setHours(selectedRows[selectedRows.length - 1] + 1 + SettingsManager.getInstance().getOpenTime());
            startDate.setMinutes(0);
            startDate.setSeconds(0);
            endDate.setMinutes(0);
            endDate.setSeconds(0);

            System.out.println("  COPYING START: " + startDate);
            System.out.println("  COPYING END:   " + endDate);
//            for (int i = selectedColumns[0]; i <= selectedColumns[selectedColumns.length - 1]; i++) {
            Collections.sort(singleActivity.getActivityTurns());
            List<ActivityTurn> activityTurns = this.singleActivity.getActivityTurns();
            for (ActivityTurn activityTurn : activityTurns) {
                if (activityTurn.getStartTime() > (startDate.getTime()) && activityTurn.getEndTime() < (endDate.getTime())) {
                    System.out.println("                --->     ADDING ACT");
                    result.add(activityTurn);
                }
            }
            GuiEventManager.getInstance().newActivityTurn(null);
            System.out.println("afer copy -> " + result.size());
//            }
        }
        return result;
    }

    public boolean canAddThisTurn(ActivityTurn turn) {
        Collections.sort(singleActivity.getActivityTurns());
        List<ActivityTurn> activityTurns = this.singleActivity.getActivityTurns();
        for (ActivityTurn activityTurn : activityTurns) {
            if (turn.getStartTime() > (activityTurn.getStartTime()) && turn.getStartTime() < (activityTurn.getEndTime())) {
                return false;
            }

            if (turn.getEndTime() > (activityTurn.getStartTime()) && turn.getStartTime() < (activityTurn.getEndTime())) {
                return false;
            }
        }
        return true;
    }

    public String labelize(Job job) {
        String tooltip = "<html><ul>";
        List<Skill> skills = job.getSkills();
        for (Skill skill : skills) {
            tooltip += "<li>" + skill.getName() + "</li>";
        }
        tooltip += "</ul>";
        return tooltip;
    }

    public void setActivity(Activity activity) {
        this.singleActivity = activity;
        glassPane.setContext(jTable1, today);
    }

    public String getLabelDate(Date date) {
        if (date == null) {
            return "null :)";
        }
        String result = " ";
        result += date.getDate() + " ";
        result += new SimpleDateFormat("MMMMM").format(date);
        return result;
    }

    @I18NUpdater
    public final void updateLabels(Date startDay) {

        if (startDay == null) {
            return;
        }
        String mainLabel = "dal " + getLabelDate(startDay);
        jTable1.getColumnModel().getColumn(0).setHeaderValue(labelTime);
        jTable1.getColumnModel().getColumn(1).setHeaderValue(labelMonday + getLabelDate(startDay));
        Date d2 = new Date(startDay.getTime() + (1000l * 60l * 60 * 24l));
        jTable1.getColumnModel().getColumn(2).setHeaderValue(labelTuesday + getLabelDate(d2));
        Date d3 = new Date(d2.getTime() + (1000l * 60l * 60 * 24l));
        jTable1.getColumnModel().getColumn(3).setHeaderValue(labelWednesday + getLabelDate(d3));
        Date d4 = new Date(d3.getTime() + (1000l * 60l * 60 * 24l));
        jTable1.getColumnModel().getColumn(4).setHeaderValue(labelThursday + getLabelDate(d4));
        Date d5 = new Date(d4.getTime() + (1000l * 60l * 60 * 24l));
        jTable1.getColumnModel().getColumn(5).setHeaderValue(labelFriday + getLabelDate(d5));
        Date d6 = new Date(d5.getTime() + (1000l * 60l * 60 * 24l));
        jTable1.getColumnModel().getColumn(6).setHeaderValue(labelSaturday + getLabelDate(d6));
        Date d7 = new Date(d6.getTime() + (1000l * 60l * 60 * 24l));
        jTable1.getColumnModel().getColumn(7).setHeaderValue(labelSunday + getLabelDate(d7));
        mainLabel += " al " + getLabelDate(d7);
//        week.clear();
//        week.add(today);
//        week.add(d2);
//        week.add(d3);
//        week.add(d4);
//        week.add(d5);
//        week.add(d6);
//        week.add(d7);

        this.jLabel_date.setText(mainLabel);
        jTable1.getTableHeader().repaint();
        jTable1.repaint();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        activityTableRenderer1 = new it.cnr.istc.sponsor.tt.gui.activities.table.mr.ActivityTableRenderer();
        activityTableModel2 = new it.cnr.istc.sponsor.tt.gui.activities.table.mr.ActivityTableModel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jComboBox_Turni = new javax.swing.JComboBox<>();
        jPanel_tableContainer = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jToggleButton_creaSingolo = new javax.swing.JToggleButton();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jToolBar_buttons = new javax.swing.JToolBar();
        jButton_WeekBackward = new javax.swing.JButton();
        jLabel_date = new javax.swing.JLabel();
        jButton_WeekForward = new javax.swing.JButton();

        activityTableRenderer1.setText("activityTableRenderer1");

        jToggleButton1.setText("Turni");
        jToggleButton1.setFocusable(false);
        jToggleButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jComboBox_Turni.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Turni.setEnabled(false);

        jPanel_tableContainer.setBackground(new java.awt.Color(255, 0, 0));

        jScrollPane1.setBorder(null);

        jTable1.setModel(activityTableModel2);
        jTable1.setCellSelectionEnabled(true);
        jTable1.setRowHeight(24);
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable1MouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setHeaderValue("day");
            jTable1.getColumnModel().getColumn(0).setCellRenderer(activityTableRenderer1);
            jTable1.getColumnModel().getColumn(1).setHeaderValue("day");
            jTable1.getColumnModel().getColumn(1).setCellRenderer(activityTableRenderer1);
            jTable1.getColumnModel().getColumn(2).setHeaderValue("day");
            jTable1.getColumnModel().getColumn(2).setCellRenderer(activityTableRenderer1);
            jTable1.getColumnModel().getColumn(3).setHeaderValue("day");
            jTable1.getColumnModel().getColumn(3).setCellRenderer(activityTableRenderer1);
            jTable1.getColumnModel().getColumn(4).setHeaderValue("day");
            jTable1.getColumnModel().getColumn(4).setCellRenderer(activityTableRenderer1);
            jTable1.getColumnModel().getColumn(5).setHeaderValue("day");
            jTable1.getColumnModel().getColumn(5).setCellRenderer(activityTableRenderer1);
            jTable1.getColumnModel().getColumn(6).setHeaderValue("day");
            jTable1.getColumnModel().getColumn(6).setCellRenderer(activityTableRenderer1);
            jTable1.getColumnModel().getColumn(7).setHeaderValue("day");
            jTable1.getColumnModel().getColumn(7).setCellRenderer(activityTableRenderer1);
        }

        javax.swing.GroupLayout jPanel_tableContainerLayout = new javax.swing.GroupLayout(jPanel_tableContainer);
        jPanel_tableContainer.setLayout(jPanel_tableContainerLayout);
        jPanel_tableContainerLayout.setHorizontalGroup(
            jPanel_tableContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE)
        );
        jPanel_tableContainerLayout.setVerticalGroup(
            jPanel_tableContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
        );

        jButton1.setText("NEW ACTIVITY");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/CopiaEavanza.png"))); // NOI18N
        jButton4.setText("Copia questa settimana nella prossima");
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        jToggleButton_creaSingolo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/add.png"))); // NOI18N
        jToggleButton_creaSingolo.setSelected(true);
        jToggleButton_creaSingolo.setFocusable(false);
        jToggleButton_creaSingolo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton_creaSingolo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton_creaSingolo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton_creaSingoloActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/backwards-back32.png"))); // NOI18N
        jButton3.setEnabled(false);
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/forward-for32.png"))); // NOI18N
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        jScrollPane2.setBorder(null);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
        );

        jToolBar_buttons.setFloatable(false);
        jToolBar_buttons.setRollover(true);

        jButton_WeekBackward.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/arrow_sx.png"))); // NOI18N
        jButton_WeekBackward.setEnabled(false);
        jButton_WeekBackward.setFocusable(false);
        jButton_WeekBackward.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_WeekBackward.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton_WeekBackward.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_WeekBackwardActionPerformed(evt);
            }
        });
        jToolBar_buttons.add(jButton_WeekBackward);

        jLabel_date.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_date.setText("dal 1 ottobre all' 8 ottobre 2016");
        jToolBar_buttons.add(jLabel_date);

        jButton_WeekForward.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/arrow_dx.png"))); // NOI18N
        jButton_WeekForward.setFocusable(false);
        jButton_WeekForward.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_WeekForward.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton_WeekForward.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_WeekForwardActionPerformed(evt);
            }
        });
        jToolBar_buttons.add(jButton_WeekForward);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar_buttons, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jToolBar_buttons, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jToggleButton1ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        this.jComboBox_Turni.setEnabled(this.jToggleButton1.isSelected());
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jTable1MouseReleased(MouseEvent evt) {//GEN-FIRST:event_jTable1MouseReleased

        if (createSingle) {
//            LRJobSelectorDialog d = new LRJobSelectorDialog(new JFrame(), true);
//            d.setVisible(true);

            JTable source = (JTable) evt.getSource();
            int row = source.rowAtPoint(evt.getPoint());
            int column = source.columnAtPoint(evt.getPoint());
//        System.out.println("row -> " + row);
//        System.out.println("column -> " + column);
//        System.out.println("point x " + evt.getPoint().x);
            int cw = 0;
            for (int i = 0; i < column; i++) {
                cw += jTable1.getColumnModel().getColumn(i).getWidth();
            }
//        System.out.println("cw = " + cw);
            int index = evt.getPoint().x - cw;
//        System.out.println("index -> " + index);

            if (evt.isPopupTrigger()) {
                this.jTable1.getSelectionModel().clearSelection();
                this.activityTableModel2.deleteActivity(row, column, index);
            } else {
//            System.out.println("CHANGING");

//                int start = this.jTable1.getSelectionModel().getMinSelectionIndex();
//                int end = this.jTable1.getSelectionModel().getMaxSelectionIndex();
                int start = this.jTable1.getSelectionModel().getMinSelectionIndex() + SettingsManager.getInstance().getOpenTime();
                int end = this.jTable1.getSelectionModel().getMaxSelectionIndex() + SettingsManager.getInstance().getOpenTime();

//            System.out.println("SELECTION FROM " + start + " TO " + end);
                Date td1 = new Date(week.getDays().get(column - 1).getTime());
                td1.setHours(start);
                td1.setSeconds(0);
                td1.setMinutes(0);

                Date td2 = new Date(week.getDays().get(column - 1).getTime());
                td2.setHours(end + 1);
                td2.setSeconds(0);
                td2.setMinutes(0);
                ActivityTurn activityTurn = new ActivityTurn(td1.getTime(), td2.getTime());
                if (canAddThisTurn(activityTurn)) {
                    JobSelectorDialog dialog = new JobSelectorDialog(new JFrame());
                    dialog.setLocationRelativeTo(null);
                    dialog.setTurn(activityTurn);
                    dialog.setVisible(true);

                    ActivityTurn turn = dialog.getTurn();
                    singleActivity.addActivityTurn(turn);
                    GuiEventManager.getInstance().newActivityTurn(turn);

                    this.activityTableModel2.changeTurnActivity(row, column, index);
                    GuiEventManager.getInstance().newActivityTurn(activityTurn);
//             this.jTable1.getSelectionModel().clearSelection();
                    this.invalidate();
                    this.repaint();
                } else {
                    JOptionPane.showMessageDialog(null, "Non posso aggiungere");
                }

//            this.glassPane.getActivity().addActivityTurn(new ActivityTurn(td1, td2));
            }
        } else {
            //SELEZIONA
            int[] selectedRows = this.jTable1.getSelectedRows();
            int[] selectedColumns = this.jTable1.getSelectedColumns();
            if (selectedRows.length > 0 && selectedColumns.length > 0) {
                System.out.println("START SELECTION ROW: " + selectedRows[0]);
                System.out.println("END SELECTION ROW: " + selectedRows[selectedRows.length - 1]);
                System.out.println("START SELECTION COLUMN: " + selectedColumns[0]);
                System.out.println("END SELECTION COLUMN: " + selectedColumns[selectedColumns.length - 1]);
            }

        }

//        System.out.println("--------------------------------------------------------");

    }//GEN-LAST:event_jTable1MouseReleased

    private void jButton_WeekForwardActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton_WeekForwardActionPerformed
//        this.today = Utils.getNextWeekMonday(today);
//        updateLabels(this.today);
//        this.glassPane.updateTableDate(today);
//        GuiEventManager.getInstance().changeDate(this.today);
        week = week.getFurtherWeek();
        this.today = week.getDays().get(0);
        TrainerManager.getInstance().setEndPlanningDate(week.getDays().get(6));
        updateLabels(this.today);
        this.glassPane.updateTableDate(today);

        delta++;
        if (delta > 0) {
            jButton_WeekBackward.setEnabled(true);
        }
        GuiEventManager.getInstance().changeDate(this.today);
    }//GEN-LAST:event_jButton_WeekForwardActionPerformed

    private void jButton_WeekBackwardActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton_WeekBackwardActionPerformed
//        this.today = Utils.getPreviousWeekMonday(today);
//        updateLabels(this.today);
//        this.glassPane.updateTableDate(today);
        week = week.getPreviousWeek();
        this.today = week.getDays().get(0);
        updateLabels(this.today);
        this.glassPane.updateTableDate(today);

        delta--;
        if (delta == 0) {
            jButton_WeekBackward.setEnabled(false);
        }
        GuiEventManager.getInstance().changeDate(this.today);
    }//GEN-LAST:event_jButton_WeekBackwardActionPerformed

    private void jButton1ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
//        NewActivityFrame frame = new NewActivityFrame();
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
//        Activity activity = new Activity();
//        activity.setName("Astrolabio");
//        Date td1 = new Date(today.getTime());
//        td1.setHours(10);
//        td1.setSeconds(0);
//        td1.setMinutes(0);
//
//        Date td2 = new Date(today.getTime());
//        td2.setHours(12);
//        td2.setSeconds(0);
//        td2.setMinutes(0);
//
//        Date td3 = new Date(today.getTime());
//        td3.setHours(7);
//        td3.setSeconds(0);
//        td3.setMinutes(0);
//
//        Date td4 = new Date(today.getTime());
//        td4.setHours(8);
//        td4.setSeconds(0);
//        td4.setMinutes(0);
//
//        Date td5 = new Date(today.getTime() + 1000l * 60l * 60l * 24 * 3);
//        td5.setHours(15);
//        td5.setSeconds(0);
//        td5.setMinutes(0);
//
//        Date td6 = new Date(td5.getTime());
//        td6.setHours(20);
//        td6.setSeconds(20);
//        td6.setMinutes(20);
//
//        Date td7 = new Date(today.getTime() + 1000l * 60l * 60l * 24 * 9);
//        td7.setHours(4);
//        td7.setSeconds(0);
//        td7.setMinutes(0);
//
//        Date td8 = new Date(td7.getTime());
//        td8.setHours(17);
//        td8.setSeconds(0);
//        td8.setMinutes(0);
//
//        singleActivity.addActivityTurn(new ActivityTurn(21l, td1, td2));
//        singleActivity.addActivityTurn(new ActivityTurn(22l, td3, td4));
//        singleActivity.addActivityTurn(new ActivityTurn(23l, td5, td6));
//        singleActivity.addActivityTurn(new ActivityTurn(24l, td7, td8));

//    activity.addActivityTurn(new ActivityTurn(new Date(today.getTime()+ (3600l*10)), new Date(today.getTime()+ (3600l*17))));
//    activity.addActivityTurn(new ActivityTurn(new Date(today.getTime()+ (3600l*28)), new Date(today.getTime()+ (3600l*32))));
        glassPane.setContext(jTable1, today);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jToggleButton_creaSingoloActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jToggleButton_creaSingoloActionPerformed
        createSingle = this.jToggleButton_creaSingolo.isSelected();
    }//GEN-LAST:event_jToggleButton_creaSingoloActionPerformed

    private void jButton3ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private it.cnr.istc.sponsor.tt.gui.activities.table.mr.ActivityTableModel activityTableModel2;
    private it.cnr.istc.sponsor.tt.gui.activities.table.mr.ActivityTableRenderer activityTableRenderer1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton_WeekBackward;
    private javax.swing.JButton jButton_WeekForward;
    private javax.swing.JComboBox<String> jComboBox_Turni;
    private javax.swing.JLabel jLabel_date;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel_tableContainer;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton_creaSingolo;
    private javax.swing.JToolBar jToolBar_buttons;
    // End of variables declaration//GEN-END:variables

    @Override
    public void newActivityEvent(Activity activity) {
        JLabel q = new JLabel("   ");
        q.setMinimumSize(new Dimension(24, 24));
        q.setOpaque(true);
        q.setBackground(activity.getGeneralColor());
        q.setToolTipText(activity.getActivityName().getName());
        Component q_added = this.jToolBar_buttons.add(q);
        JLabel label = new JLabel(" " + activity.getActivityName().getName() + "  ");
        Component label_added = this.jToolBar_buttons.add(label);
        this.labelMap.put(activity.getActivityName().getName(), q_added);
        this.labelMap.put(activity.getActivityName().getName() + "label", label_added);

    }

    @Override
    public void newProfileEvent(Job profile) {

    }

    @Override
    public void newActivityTurn(ActivityTurn turn) {

    }

    @Override
    public void changeDate(Date date) {
        this.week = new Week(date);
        this.today = week.getDays().get(0);
        updateLabels(this.today);
        this.glassPane.updateTableDate(today);
        delta = 0;

//        System.out.println("date.getTime() --> "+date);
//        System.out.println("WEEK --> "+week.getDays().get(week.getDays().size() - 1));
//        if (date.getTime() < week.getDays().get(week.getDays().size() - 1).getTime()) {
//            System.out.println("FIRST IF");
//            while (true) {
//                System.out.println("tru 1");
//                Week week2 = week.getFurtherWeek();
//                if (date.getTime() < week2.getDays().get(week2.getDays().size() - 1).getTime()) {
//                    break;
//                }
//                week = week2;
//                this.today = week.getDays().get(0);
//                updateLabels(this.today);
//                this.glassPane.updateTableDate(today);
//
//            }
//        }
//        if (date.getTime() > week.getDays().get(week.getDays().size() - 1).getTime()) {
//            System.out.println("SECOND IF");
//            while (true) {
//                System.out.println("tru 2");
//                Week week2 = week.getFurtherWeek();
//                if (date.getTime() < week2.getDays().get(week2.getDays().size() - 1).getTime()) {
//                    System.out.println("break");
//                    break;
//                }
//                week = week2;
//                this.today = week.getDays().get(0);
//                updateLabels(this.today);
//                this.glassPane.updateTableDate(today);
//
//            }
//        }
    }

    @Override
    public void changeTab(int t) {
        System.out.println("TABBONE CAMBIATO -> " + t + " mentre this.tab = " + this.tab);
        if (t == this.tab) {
            System.out.println("CAMBIONE !!" + this.today);
            GuiEventManager.getInstance().changeDate(this.today);
        }
    }

    void setTab(int i) {
        this.tab = i;
    }

    public int getTab() {
        return tab;
    }

    @Override
    public void removeActivityEvent(Activity activity) {
        this.jToolBar_buttons.remove(this.labelMap.get(activity.getActivityName().getName()));
        this.jToolBar_buttons.remove(this.labelMap.get(activity.getActivityName().getName() + "label"));
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
