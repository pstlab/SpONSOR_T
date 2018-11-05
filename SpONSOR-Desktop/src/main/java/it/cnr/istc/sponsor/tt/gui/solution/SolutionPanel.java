/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.solution;

import it.cnr.istc.i18n.annotations.I18N;
import it.cnr.istc.i18n.annotations.I18NUpdater;
import it.cnr.istc.sponsor.tt.abstracts.SubMainPanel;
import it.cnr.istc.sponsor.tt.gui.activities.table.mr.ActivityComboBoxModel;
import it.cnr.istc.sponsor.tt.gui.activities.table.mr.ActivityComboBoxRenderer;
import it.cnr.istc.sponsor.tt.gui.engine.GuiEventListener;
import it.cnr.istc.sponsor.tt.gui.solution.edit.EditTurnFrame;
import it.cnr.istc.sponsor.tt.gui.solution.mr.PeopleComboBoxModel;
import it.cnr.istc.sponsor.tt.gui.solver.SolverManager;
import it.cnr.istc.sponsor.tt.gui.solution.mr.SolutionTableModel;
import it.cnr.istc.sponsor.tt.gui.solution.mr.SolutionTableRenderer;
import it.cnr.istc.sponsor.tt.gui.solver.SolutionCustomerFrame;
import it.cnr.istc.sponsor.tt.gui.solver.SolverListener;
import it.cnr.istc.sponsor.tt.gui.solver.SolverManager2;
import it.cnr.istc.sponsor.tt.gui.solver.SolverManager4;
import it.cnr.istc.sponsor.tt.logic.MQTTClient;
import it.cnr.istc.sponsor.tt.logic.TrainerManager;
import it.cnr.istc.sponsor.tt.logic.Utils;
import it.cnr.istc.sponsor.tt.logic.Week;
import it.cnr.istc.sponsor.tt.logic.excel.ExcelWriter;
import it.cnr.istc.sponsor.tt.logic.model.Activity;
import it.cnr.istc.sponsor.tt.logic.model.ActivityTurn;
import it.cnr.istc.sponsor.tt.logic.model.ComfirmedTurn;
import it.cnr.istc.sponsor.tt.logic.model.Job;
import it.cnr.istc.sponsor.tt.logic.model.JobTurn;
import it.cnr.istc.sponsor.tt.logic.model.ModelManager;
import it.cnr.istc.sponsor.tt.logic.model.Person;
import it.cnr.istc.sponsor.tt.logic.model.app.AppCollegue;
import it.cnr.istc.sponsor.tt.logic.model.app.AppTurno;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class SolutionPanel extends javax.swing.JPanel implements SolverListener, SubMainPanel, GuiEventListener {

    private ActivityComboBoxModel activityComboBoxModel = null;
    private ActivityComboBoxRenderer activityComboBoxRenderer = null;
//    private PDFPreviewFrame frame = null;

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
    private Date todayActivity = new Date();
    private Date todayPeople = new Date();
//    private List<Date> week = new ArrayList<>();
    private PeopleComboBoxModel peopleComboBoxModel = null;
    private List<ActivityTurn> solution = null;
    private List<ActivityTurn> globalSolution = new ArrayList<>();

    private static Week weekActivities = null;
    private static Week weekPeople = null;

    private int delta = 0;
    private int gamma = 0;

    /**
     * Creates new form SolutionPanel
     */
    public SolutionPanel() {
        initComponents();
//        this.jPanel2.setVisible(false);
        this.solutionTableModel1.setPersonMode(false);
        this.solutionTableModel2.setPersonMode(true);
        activityComboBoxModel = new ActivityComboBoxModel(ModelManager.getInstance().getAllActivities());
        activityComboBoxRenderer = new ActivityComboBoxRenderer();
        this.jComboBox_activities.setModel(activityComboBoxModel);
        this.jComboBox_activities.setRenderer(activityComboBoxRenderer);
        SolverManager2.getInstance().addSolverListener(this);
        SolverManager4.getInstance().addSolverListener(this);
        SolverManager.getInstance().addSolverListener(this);
        this.todayActivity = Utils.getCurrentMonday();
        if (weekActivities == null) {
            this.todayActivity = Utils.getCurrentMonday();
            weekActivities = new Week(this.todayActivity);
        }

        this.todayPeople = Utils.getCurrentMonday();
        if (weekPeople == null) {
            this.todayPeople = Utils.getCurrentMonday();
            weekPeople = new Week(this.todayPeople);
        }
        updateLabelsActivity(todayActivity);
        updateLabelsPeople(todayPeople);
        peopleComboBoxModel = new PeopleComboBoxModel(TrainerManager.getInstance().getPeople());
        this.jComboBox_persons.setModel(peopleComboBoxModel);
//        Date today = new Date();
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
//        List<ActivityTurn> list = new ArrayList<>();
//        ActivityTurn t1 = new ActivityTurn(1l,td1, td2);
//        t1.addComfirmedTurn(new ComfirmedTurn(new Person("Luca", "Coraci"), null));
//        t1.addComfirmedTurn(new ComfirmedTurn(new Person("Fabio", "Pulcinello"), null));
//        
//        ActivityTurn t2 = new ActivityTurn(2l,td3, td4);
//        t2.addComfirmedTurn(new ComfirmedTurn(new Person("Luca", "Coraci"), null));
//        t2.addComfirmedTurn(new ComfirmedTurn(new Person("Fabio", "Pulcinello"), null));
//        t2.addComfirmedTurn(new ComfirmedTurn(new Person("Simona", "Cornacchia"), null));
//        
//        ActivityTurn t3 = new ActivityTurn(3l,td5, td6);
//        t3.addComfirmedTurn(new ComfirmedTurn(new Person("Luca", "Coraci"), null));
//        t3.addComfirmedTurn(new ComfirmedTurn(new Person("Fabio", "Pulcinello"), null));
//        
//        ActivityTurn t4 = new ActivityTurn(4l,td7, td8);
//        t4.addComfirmedTurn(new ComfirmedTurn(new Person("Gina", "Strado"), null));
//        t4.addComfirmedTurn(new ComfirmedTurn(new Person("Fabio", "Pulcinello"), null));
//
//        
//        list.add(t1);
//        list.add(t2);
//        list.add(t3);
//        list.add(t4);

    }

    public String getLabelDate(Date date) {
        String result = " ";
        result += date.getDate() + " ";
        result += new SimpleDateFormat("MMMMM").format(date);
        return result;
    }

    @I18NUpdater
    public void updateLabels() {
        updateLabelsActivity(todayActivity);
        updateLabelsPeople(todayPeople);
    }

    public final void updateLabelsActivity(Date startDay) {

        String mainLabel = "dal " + getLabelDate(startDay);
        jTable_activity.getColumnModel().getColumn(0).setHeaderValue(labelMonday + getLabelDate(startDay));
        Date d2 = new Date(startDay.getTime() + (1000l * 60l * 60 * 24l));
        jTable_activity.getColumnModel().getColumn(1).setHeaderValue(labelTuesday + getLabelDate(d2));
        Date d3 = new Date(d2.getTime() + (1000l * 60l * 60 * 24l));
        jTable_activity.getColumnModel().getColumn(2).setHeaderValue(labelWednesday + getLabelDate(d3));
        Date d4 = new Date(d3.getTime() + (1000l * 60l * 60 * 24l));
        jTable_activity.getColumnModel().getColumn(3).setHeaderValue(labelThursday + getLabelDate(d4));
        Date d5 = new Date(d4.getTime() + (1000l * 60l * 60 * 24l));
        jTable_activity.getColumnModel().getColumn(4).setHeaderValue(labelFriday + getLabelDate(d5));
        Date d6 = new Date(d5.getTime() + (1000l * 60l * 60 * 24l));
        jTable_activity.getColumnModel().getColumn(5).setHeaderValue(labelSaturday + getLabelDate(d6));
        Date d7 = new Date(d6.getTime() + (1000l * 60l * 60 * 24l));
        jTable_activity.getColumnModel().getColumn(6).setHeaderValue(labelSunday + getLabelDate(d7));
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
        jTable_activity.getTableHeader().repaint();
        jTable_activity.repaint();

    }

    public final void updateLabelsPeople(Date startDay) {

        String mainLabel = "dal " + getLabelDate(startDay);
        jTable2.getColumnModel().getColumn(0).setHeaderValue(labelMonday + getLabelDate(startDay));
        Date d2 = new Date(startDay.getTime() + (1000l * 60l * 60 * 24l));
        jTable2.getColumnModel().getColumn(1).setHeaderValue(labelTuesday + getLabelDate(d2));
        Date d3 = new Date(d2.getTime() + (1000l * 60l * 60 * 24l));
        jTable2.getColumnModel().getColumn(2).setHeaderValue(labelWednesday + getLabelDate(d3));
        Date d4 = new Date(d3.getTime() + (1000l * 60l * 60 * 24l));
        jTable2.getColumnModel().getColumn(3).setHeaderValue(labelThursday + getLabelDate(d4));
        Date d5 = new Date(d4.getTime() + (1000l * 60l * 60 * 24l));
        jTable2.getColumnModel().getColumn(4).setHeaderValue(labelFriday + getLabelDate(d5));
        Date d6 = new Date(d5.getTime() + (1000l * 60l * 60 * 24l));
        jTable2.getColumnModel().getColumn(5).setHeaderValue(labelSaturday + getLabelDate(d6));
        Date d7 = new Date(d6.getTime() + (1000l * 60l * 60 * 24l));
        jTable2.getColumnModel().getColumn(6).setHeaderValue(labelSunday + getLabelDate(d7));
        mainLabel += " al " + getLabelDate(d7);
//        week.clear();
//        week.add(today);
//        week.add(d2);
//        week.add(d3);
//        week.add(d4);
//        week.add(d5);
//        week.add(d6);
//        week.add(d7);

        this.jLabel_date_people.setText(mainLabel);
        jTable2.getTableHeader().repaint();
        jTable2.repaint();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        solutionTableRenderer2 = new SolutionTableRenderer();
        solutionTableModel1 = new SolutionTableModel();
        solutionTableModel2 = new SolutionTableModel();
        solutionTableRenderer1 = new SolutionTableRenderer();
        jButton_Calculate = new JButton();
        jSeparator1 = new JToolBar.Separator();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        filler1 = new Box.Filler(new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20));
        jToolBar1 = new JToolBar();
        jButton_TEST = new JButton();
        jButton3 = new JButton();
        jButton2 = new JButton();
        jButton4 = new JButton();
        jButton5 = new JButton();
        jButton_pdf = new JButton();
        jButton_Save = new JButton();
        jButton_Excel = new JButton();
        jPanel1 = new JPanel();
        jTabbedPane1 = new JTabbedPane();
        jPanel_activities = new JPanel();
        jScrollPane2 = new JScrollPane();
        jTable_activity = new JTable();
        jToolBar2 = new JToolBar();
        jButton_WeekBackward = new JButton();
        jLabel_date = new JLabel();
        jButton_WeekForward = new JButton();
        jLabel3 = new JLabel();
        jComboBox_activities = new JComboBox<>();
        jPanel_activities1 = new JPanel();
        jScrollPane3 = new JScrollPane();
        jTable2 = new JTable();
        jToolBar3 = new JToolBar();
        jButton_WeekBackward1 = new JButton();
        jLabel_date_people = new JLabel();
        jButton_WeekForward1 = new JButton();
        jLabel4 = new JLabel();
        jComboBox_persons = new JComboBox<>();

        solutionTableRenderer2.setText("solutionTableRenderer1");

        solutionTableRenderer1.setText("solutionTableRenderer1");

        jButton_Calculate.setIcon(new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/play32.png"))); // NOI18N
        jButton_Calculate.setText("Calcola");
        jButton_Calculate.setFocusable(false);
        jButton_Calculate.setHorizontalTextPosition(SwingConstants.CENTER);
        jButton_Calculate.setVerticalTextPosition(SwingConstants.BOTTOM);
        jButton_Calculate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton_CalculateActionPerformed(evt);
            }
        });

        jLabel1.setText("visualizza per attività:");

        jLabel2.setText("visualizza per persona:");

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jButton_TEST.setText("jButton1");
        jButton_TEST.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton_TESTActionPerformed(evt);
            }
        });

        jButton3.setIcon(new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/backwards-back32.png"))); // NOI18N
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(SwingConstants.CENTER);
        jButton3.setOpaque(false);
        jButton3.setVerticalTextPosition(SwingConstants.BOTTOM);

        jButton2.setIcon(new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/forward-for32.png"))); // NOI18N
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(SwingConstants.CENTER);
        jButton2.setOpaque(false);
        jButton2.setVerticalTextPosition(SwingConstants.BOTTOM);

        jButton4.setIcon(new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/backwards-back32.png"))); // NOI18N
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(SwingConstants.CENTER);
        jButton4.setOpaque(false);
        jButton4.setVerticalTextPosition(SwingConstants.BOTTOM);

        jButton5.setIcon(new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/forward-for32.png"))); // NOI18N
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(SwingConstants.CENTER);
        jButton5.setOpaque(false);
        jButton5.setVerticalTextPosition(SwingConstants.BOTTOM);
        jButton5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton_pdf.setIcon(new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/pdf32.png"))); // NOI18N
        jButton_pdf.setText("Stampa PDF Volontari");
        jButton_pdf.setHorizontalTextPosition(SwingConstants.CENTER);
        jButton_pdf.setVerticalTextPosition(SwingConstants.BOTTOM);
        jButton_pdf.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton_pdfActionPerformed(evt);
            }
        });

        jButton_Save.setIcon(new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/load32.png"))); // NOI18N
        jButton_Save.setText("Pubblica Turnazione");
        jButton_Save.setHorizontalTextPosition(SwingConstants.CENTER);
        jButton_Save.setVerticalTextPosition(SwingConstants.BOTTOM);
        jButton_Save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton_SaveActionPerformed(evt);
            }
        });

        jButton_Excel.setIcon(new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/excel_32.png"))); // NOI18N
        jButton_Excel.setText("Genera Excel");
        jButton_Excel.setHorizontalTextPosition(SwingConstants.CENTER);
        jButton_Excel.setVerticalTextPosition(SwingConstants.BOTTOM);
        jButton_Excel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton_ExcelActionPerformed(evt);
            }
        });

        setBackground(new Color(255, 255, 255));

        jPanel1.setBackground(new Color(255, 255, 255));

        jPanel_activities.setBackground(new Color(255, 255, 255));

        jTable_activity.setModel(solutionTableModel1);
        jTable_activity.setRowHeight(100);
        jTable_activity.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent evt) {
                jTable_activityMouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jTable_activity);
        if (jTable_activity.getColumnModel().getColumnCount() > 0) {
            jTable_activity.getColumnModel().getColumn(0).setHeaderValue("Lunedì");
            jTable_activity.getColumnModel().getColumn(0).setCellRenderer(solutionTableRenderer2);
            jTable_activity.getColumnModel().getColumn(1).setHeaderValue("Martedì");
            jTable_activity.getColumnModel().getColumn(1).setCellRenderer(solutionTableRenderer2);
            jTable_activity.getColumnModel().getColumn(2).setHeaderValue("Mercoledì");
            jTable_activity.getColumnModel().getColumn(2).setCellRenderer(solutionTableRenderer2);
            jTable_activity.getColumnModel().getColumn(3).setHeaderValue("Giovedì");
            jTable_activity.getColumnModel().getColumn(3).setCellRenderer(solutionTableRenderer2);
            jTable_activity.getColumnModel().getColumn(4).setHeaderValue("Venerdì");
            jTable_activity.getColumnModel().getColumn(4).setCellRenderer(solutionTableRenderer2);
            jTable_activity.getColumnModel().getColumn(5).setHeaderValue("Sabato");
            jTable_activity.getColumnModel().getColumn(5).setCellRenderer(solutionTableRenderer2);
            jTable_activity.getColumnModel().getColumn(6).setHeaderValue("Domenica");
            jTable_activity.getColumnModel().getColumn(6).setCellRenderer(solutionTableRenderer2);
        }

        jToolBar2.setBackground(new Color(255, 255, 255));
        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        jButton_WeekBackward.setIcon(new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/arrow_sx.png"))); // NOI18N
        jButton_WeekBackward.setEnabled(false);
        jButton_WeekBackward.setFocusable(false);
        jButton_WeekBackward.setHorizontalTextPosition(SwingConstants.CENTER);
        jButton_WeekBackward.setOpaque(false);
        jButton_WeekBackward.setVerticalTextPosition(SwingConstants.BOTTOM);
        jButton_WeekBackward.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton_WeekBackwardActionPerformed(evt);
            }
        });
        jToolBar2.add(jButton_WeekBackward);

        jLabel_date.setFont(new Font("Tahoma", 1, 12)); // NOI18N
        jLabel_date.setText("dal 1 ottobre all' 8 ottobre 2016");
        jToolBar2.add(jLabel_date);

        jButton_WeekForward.setIcon(new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/arrow_dx.png"))); // NOI18N
        jButton_WeekForward.setFocusable(false);
        jButton_WeekForward.setHorizontalTextPosition(SwingConstants.CENTER);
        jButton_WeekForward.setOpaque(false);
        jButton_WeekForward.setVerticalTextPosition(SwingConstants.BOTTOM);
        jButton_WeekForward.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton_WeekForwardActionPerformed(evt);
            }
        });
        jToolBar2.add(jButton_WeekForward);

        jLabel3.setText("Attività:  ");
        jToolBar2.add(jLabel3);

        jComboBox_activities.setModel(new DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_activities.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jComboBox_activitiesActionPerformed(evt);
            }
        });
        jToolBar2.add(jComboBox_activities);

        GroupLayout jPanel_activitiesLayout = new GroupLayout(jPanel_activities);
        jPanel_activities.setLayout(jPanel_activitiesLayout);
        jPanel_activitiesLayout.setHorizontalGroup(jPanel_activitiesLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_activitiesLayout.createSequentialGroup()
                .addGroup(jPanel_activitiesLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(jToolBar2, GroupLayout.DEFAULT_SIZE, 720, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
        jPanel_activitiesLayout.setVerticalGroup(jPanel_activitiesLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, jPanel_activitiesLayout.createSequentialGroup()
                .addComponent(jToolBar2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Distribuzione Personale per Attività", jPanel_activities);

        jPanel_activities1.setBackground(new Color(255, 255, 255));

        jTable2.setModel(solutionTableModel2);
        jTable2.setRowHeight(100);
        jScrollPane3.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setHeaderValue("Lunedì");
            jTable2.getColumnModel().getColumn(0).setCellRenderer(solutionTableRenderer2);
            jTable2.getColumnModel().getColumn(1).setHeaderValue("Martedì");
            jTable2.getColumnModel().getColumn(1).setCellRenderer(solutionTableRenderer2);
            jTable2.getColumnModel().getColumn(2).setHeaderValue("Mercoledì");
            jTable2.getColumnModel().getColumn(2).setCellRenderer(solutionTableRenderer2);
            jTable2.getColumnModel().getColumn(3).setHeaderValue("Giovedì");
            jTable2.getColumnModel().getColumn(3).setCellRenderer(solutionTableRenderer2);
            jTable2.getColumnModel().getColumn(4).setHeaderValue("Venerdì");
            jTable2.getColumnModel().getColumn(4).setCellRenderer(solutionTableRenderer2);
            jTable2.getColumnModel().getColumn(5).setHeaderValue("Sabato");
            jTable2.getColumnModel().getColumn(5).setCellRenderer(solutionTableRenderer2);
            jTable2.getColumnModel().getColumn(6).setHeaderValue("Domenica");
            jTable2.getColumnModel().getColumn(6).setCellRenderer(solutionTableRenderer2);
        }

        jToolBar3.setBackground(new Color(255, 255, 255));
        jToolBar3.setFloatable(false);
        jToolBar3.setRollover(true);

        jButton_WeekBackward1.setIcon(new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/arrow_sx.png"))); // NOI18N
        jButton_WeekBackward1.setEnabled(false);
        jButton_WeekBackward1.setFocusable(false);
        jButton_WeekBackward1.setHorizontalTextPosition(SwingConstants.CENTER);
        jButton_WeekBackward1.setOpaque(false);
        jButton_WeekBackward1.setVerticalTextPosition(SwingConstants.BOTTOM);
        jButton_WeekBackward1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton_WeekBackward1ActionPerformed(evt);
            }
        });
        jToolBar3.add(jButton_WeekBackward1);

        jLabel_date_people.setFont(new Font("Tahoma", 1, 12)); // NOI18N
        jLabel_date_people.setText("dal 1 ottobre all' 8 ottobre 2016");
        jToolBar3.add(jLabel_date_people);

        jButton_WeekForward1.setIcon(new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/arrow_dx.png"))); // NOI18N
        jButton_WeekForward1.setFocusable(false);
        jButton_WeekForward1.setHorizontalTextPosition(SwingConstants.CENTER);
        jButton_WeekForward1.setOpaque(false);
        jButton_WeekForward1.setVerticalTextPosition(SwingConstants.BOTTOM);
        jButton_WeekForward1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton_WeekForward1ActionPerformed(evt);
            }
        });
        jToolBar3.add(jButton_WeekForward1);

        jLabel4.setText("Volontario:  ");
        jToolBar3.add(jLabel4);

        jComboBox_persons.setModel(new DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_persons.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jComboBox_personsActionPerformed(evt);
            }
        });
        jToolBar3.add(jComboBox_persons);

        GroupLayout jPanel_activities1Layout = new GroupLayout(jPanel_activities1);
        jPanel_activities1.setLayout(jPanel_activities1Layout);
        jPanel_activities1Layout.setHorizontalGroup(jPanel_activities1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_activities1Layout.createSequentialGroup()
                .addGroup(jPanel_activities1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(jToolBar3, GroupLayout.DEFAULT_SIZE, 720, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
        jPanel_activities1Layout.setVerticalGroup(jPanel_activities1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, jPanel_activities1Layout.createSequentialGroup()
                .addComponent(jToolBar3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Turnazione Personale", jPanel_activities1);

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, GroupLayout.Alignment.TRAILING)
        );

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_WeekBackwardActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton_WeekBackwardActionPerformed
        weekActivities = weekActivities.getPreviousWeek();
        this.todayActivity = weekActivities.getDays().get(0);
        updateLabelsActivity(this.todayActivity);
        updateSolutionTableActivities();
        delta--;
        if (delta == 0) {
            jButton_WeekBackward.setEnabled(false);
        }
    }//GEN-LAST:event_jButton_WeekBackwardActionPerformed

    private void jButton_WeekForwardActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton_WeekForwardActionPerformed
        delta++;
        if (delta > 0) {
            jButton_WeekBackward.setEnabled(true);
        }
        weekActivities = weekActivities.getFurtherWeek();
        this.todayActivity = weekActivities.getDays().get(0);
        updateLabelsActivity(this.todayActivity);
        updateSolutionTableActivities();
    }//GEN-LAST:event_jButton_WeekForwardActionPerformed

    private void updateSolutionTableActivities() {
        List<ActivityTurn> page = new ArrayList<>();

        Date startDate = new Date(weekActivities.getDays().get(0).getTime());
        Date endDate = new Date(weekActivities.getDays().get(6).getTime());
        startDate.setHours(0);
        endDate.setHours(23);
        startDate.setMinutes(0);
        startDate.setSeconds(0);
        endDate.setMinutes(59);
        endDate.setSeconds(59);

        System.out.println("SOLUTION SIZE -> " + solution.size());
        for (ActivityTurn turn : solution) {
//            if (turn.getStartTime().before(today) && turn.getEndTime().before(week.get(6))) {
            if (turn.getStartTime() + 1000 >= startDate.getTime() && turn.getEndTime() - 2000 <= endDate.getTime()) {
                System.out.println("this is OK");
                page.add(turn);
            } else {
                System.out.println("OH NOOO");
            }
        }
        this.solutionTableModel1.clear();
        this.solutionTableModel1.addAllRows(page);
        this.updateLabelsActivity(startDate);
    }

    private void updateSolutionTablePeople(Person person) {
        List<ActivityTurn> page = new ArrayList<>();

        Date startDate = new Date(weekPeople.getDays().get(0).getTime());
        Date endDate = new Date(weekPeople.getDays().get(6).getTime());
        startDate.setHours(0);
        endDate.setHours(23);
        startDate.setMinutes(0);
        startDate.setSeconds(0);
        endDate.setMinutes(59);
        endDate.setSeconds(59);

        List<ActivityTurn> allTurns = new ArrayList<>();
        List<Activity> activities = TrainerManager.getInstance().getActivities();

        for (Activity activity : activities) {
            allTurns.addAll(activity.getActivityTurns());
        }
        Collections.sort(allTurns);
        System.out.println("SOLUTION SIZE -> " + allTurns.size());
        for (ActivityTurn turn : allTurns) {
//            if (turn.getStartTime().before(today) && turn.getEndTime().before(week.get(6))) {
            if (turn.getStartTime() + 1000 >= startDate.getTime() && turn.getEndTime() - 2000 <= endDate.getTime()) {
                System.out.println("this is OK");
                if (turn.isThisPersonConfirmed(person)) {
                    page.add(turn);
                }
            } else {
                System.out.println("OH NOOO");
            }
        }
        this.solutionTableModel2.clear();
        this.solutionTableModel2.addAllRows(page);
//        if (this.frame != null) {
//            this.frame.setPage(allTurns, page.get(0).getStartTime().getMonth());
//        }
    }

    private void jButton_CalculateActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton_CalculateActionPerformed
        System.out.println("solving.. ");
//        setCursor(new Cursor(Cursor.WAIT_CURSOR));
//        SolverManager.getInstance().solve();
//        setCursor(Cursor.getDefaultCursor());
        SolutionCustomerFrame frame = new SolutionCustomerFrame();
        // frame.setAlwaysOnTop(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
//        System.out.println("solved. [OK]");
    }//GEN-LAST:event_jButton_CalculateActionPerformed

    private void jComboBox_activitiesActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jComboBox_activitiesActionPerformed

        //VISUALIZZA TROPPE ACTIVITIES
        int index = this.jComboBox_activities.getSelectedIndex();
        if (index != -1) {
            Activity activity = this.activityComboBoxModel.getElementAt(index);
            solution = activity.getActivityTurns();
            updateSolutionTableActivities();
//            if (activity != null) {
//                this.solutionTableModel1.clear();
//                System.out.println("<<<<<<<<<<<<<< PER QUESTA ATTIVITA' HO "+activity.getActivityTurns()+" TURNI! >>>>>>>>>>>");
//                this.solutionTableModel1.addAllRows(activity.getActivityTurns());
//            } else {
//                System.out.println("NULLONE COMBO FASULLO");
//            }
        }
    }//GEN-LAST:event_jComboBox_activitiesActionPerformed

    private void jComboBox_personsActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jComboBox_personsActionPerformed
        int selectedIndex = this.jComboBox_persons.getSelectedIndex();
        if (selectedIndex != -1) {
            Person person = this.peopleComboBoxModel.getElementAt(selectedIndex);
//            solutionTableModel1.showActivityByPerson(person);
            updateSolutionTablePeople(person);
        }
    }//GEN-LAST:event_jComboBox_personsActionPerformed

    private void jButton_TESTActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton_TESTActionPerformed
        System.out.println("PRINT TIME");

        List<Activity> activities = TrainerManager.getInstance().getActivities();
        for (Activity activity : activities) {
            List<ActivityTurn> activityTurns = activity.getActivityTurns();
            for (ActivityTurn activityTurn : activityTurns) {
                System.out.println("TURN: " + activityTurn);
            }
        }
    }//GEN-LAST:event_jButton_TESTActionPerformed

    private void jButton_WeekBackward1ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton_WeekBackward1ActionPerformed
        gamma--;
        if (gamma == 0) {
            jButton_WeekBackward1.setEnabled(false);
        }

        weekPeople = weekPeople.getPreviousWeek();
        this.todayPeople = weekPeople.getDays().get(0);
        int selectedIndex = this.jComboBox_persons.getSelectedIndex();
        if (selectedIndex != -1) {
            Person person = this.peopleComboBoxModel.getElementAt(selectedIndex);
//            solutionTableModel1.showActivityByPerson(person);
            updateSolutionTablePeople(person);
        }
        updateLabelsPeople(this.todayPeople);
    }//GEN-LAST:event_jButton_WeekBackward1ActionPerformed

    private void jButton_WeekForward1ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton_WeekForward1ActionPerformed
        gamma++;
        if (gamma > 0) {
            jButton_WeekBackward1.setEnabled(true);
        }
        weekPeople = weekPeople.getFurtherWeek();
        this.todayPeople = weekPeople.getDays().get(0);
        int selectedIndex = this.jComboBox_persons.getSelectedIndex();
        if (selectedIndex != -1) {
            Person person = this.peopleComboBoxModel.getElementAt(selectedIndex);
//            solutionTableModel1.showActivityByPerson(person);
            updateSolutionTablePeople(person);
        }
        updateLabelsPeople(this.todayPeople);
    }//GEN-LAST:event_jButton_WeekForward1ActionPerformed

    private void jButton5ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
//       PDFManager.getInstance().printPDF(null);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton_pdfActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton_pdfActionPerformed
//        PDFManager.getInstance().printPDF(null);

//        if (frame == null) {
//            frame = new PDFPreviewFrame();
//        } else {
//            frame.clear();
//        }
        PDFPreviewFrame frame = new PDFPreviewFrame();

        List<ActivityTurn> page = new ArrayList<>();
        Person person = null;

        int selectedIndex = this.jComboBox_persons.getSelectedIndex();
        if (selectedIndex != -1) {
            person = this.peopleComboBoxModel.getElementAt(selectedIndex);
        } else {
            System.out.println("ERR 273638278");
            return;
        }

//        Date startDate = new Date(weekPeople.getDays().get(0).getTime());
//        Date endDate = new Date(weekPeople.getDays().get(6).getTime());
//        startDate.setHours(0);
//        endDate.setHours(23);
//        startDate.setMinutes(0);
//        startDate.setSeconds(0);
//        endDate.setMinutes(59);
//        endDate.setSeconds(59);
        List<ActivityTurn> allTurns = new ArrayList<>();
        List<Activity> activities = TrainerManager.getInstance().getActivities();

        for (Activity activity : activities) {
            allTurns.addAll(activity.getActivityTurns());
        }
        Collections.sort(allTurns);
        System.out.println("SOLUTION SIZE -> " + allTurns.size());

        if (allTurns.isEmpty()) {
            System.out.println("VUOTONE");
            return;
        }

        Map<Integer, List<ActivityTurn>> monthlyMap = new HashMap<>();
        int startMonth = new Date(allTurns.get(0).getStartTime()).getMonth();
        int endMonth = new Date(allTurns.get(allTurns.size() - 1).getStartTime()).getMonth();
        for (int i = 0; i < 12; i++) {
            monthlyMap.put(i, new ArrayList<>());
        }
//        if (endMonth - startMonth == 0) {
//            monthlyMap.put(startMonth, new ArrayList<>());
//        }
        for (ActivityTurn turn : allTurns) {
//            if (turn.getStartTime().before(today) && turn.getEndTime().before(week.get(6))) {
            if (true) {
                System.out.println("this is OK");
                if (turn.isThisPersonConfirmed(person)) {
                    page.add(turn);
                    monthlyMap.get(new Date(turn.getStartTime()).getMonth()).add(turn);
                }
            } else {
                System.out.println("OH NOOO");
            }
        }
        for (Integer m : monthlyMap.keySet()) {
            if (!monthlyMap.get(m).isEmpty()) {
                frame.addPage(monthlyMap.get(m), m);
            }
        }
        frame.setPerson(person);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }//GEN-LAST:event_jButton_pdfActionPerformed

    private void jButton_SaveActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton_SaveActionPerformed
//        List<Activity> allActivities = ModelManager.getInstance().getAllActivities();
//        List<AppTurn> turns = new ArrayList<>();
//        for (Activity act : allActivities) {
//            for (ActivityTurn activityTurn : act.getActivityTurns()) {
//                for (ComfirmedTurn comfirmedTurn : activityTurn.getComfirmedTurns()) {
//                    AppTurn appTurn = new AppTurn(
//                            null,
//                            comfirmedTurn.getPerson().getCode(),
//                            "" + activityTurn.getId(),
//                            TrainerManager.getInstance().getActivityByTurn(activityTurn).getName(),
//                            activityTurn.getStartTime(),
//                            activityTurn.getEndTime(),
//                            null,
//                            0
//                    );
//                    turns.add(appTurn);
//                }
//
//            }
//        }

        List<Activity> allActivities = ModelManager.getInstance().getAllActivities();
        List<AppTurno> turns = new ArrayList<>();
        for (Activity act : allActivities) {
            for (ActivityTurn activityTurn : act.getActivityTurns()) {
                AppTurno appTurno = new AppTurno();
                appTurno.setActivity(TrainerManager.getInstance().getActivityByTurn(activityTurn).getActivityName().getName());
                appTurno.setStartTime(activityTurn.getStartTime());
                appTurno.setEndTime(activityTurn.getEndTime());

                for (ComfirmedTurn comfirmedTurn : activityTurn.getComfirmedTurns()) {
                    AppCollegue collegue = new AppCollegue();
                    JobTurn jobTurn = comfirmedTurn.getJobTurn();;
                    collegue.setPersonCode(comfirmedTurn.getPerson().getCode());
                    collegue.setIdWantedKeys(jobTurn.getWantedKeywords().stream().map(i -> i.getKeyword()).collect(Collectors.joining(":")));
                    collegue.setIdUnwantedKeys(jobTurn.getUnwantedKeywords().stream().map(i -> i.getKeyword()).collect(Collectors.joining(":")));
                    appTurno.getCollegues().add(collegue);

                }
                turns.add(appTurno);
            }
        }

        //salva
        MQTTClient.getInstance().saveAppTurni(turns);

    }//GEN-LAST:event_jButton_SaveActionPerformed

    private void jTable_activityMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable_activityMouseReleased
        int selectedRow = jTable_activity.getSelectedRow();
        int selectedColumn = jTable_activity.getSelectedColumn();
        ActivityTurn turn = (ActivityTurn) solutionTableModel1.getValueAt(selectedRow, selectedColumn);
        if (turn != null) {
            EditTurnFrame frame = new EditTurnFrame();
            frame.setTurn(turn);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }


    }//GEN-LAST:event_jTable_activityMouseReleased

    private void jButton_ExcelActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton_ExcelActionPerformed
        try {
            File file = ExcelWriter.writeSolution(globalSolution, true);
            Desktop.getDesktop().open(file);
        } catch (IOException ex) {
            Logger.getLogger(SolutionPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton_ExcelActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Box.Filler filler1;
    private JButton jButton2;
    private JButton jButton3;
    private JButton jButton4;
    private JButton jButton5;
    private JButton jButton_Calculate;
    private JButton jButton_Excel;
    private JButton jButton_Save;
    private JButton jButton_TEST;
    private JButton jButton_WeekBackward;
    private JButton jButton_WeekBackward1;
    private JButton jButton_WeekForward;
    private JButton jButton_WeekForward1;
    private JButton jButton_pdf;
    private JComboBox<String> jComboBox_activities;
    private JComboBox<String> jComboBox_persons;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel_date;
    private JLabel jLabel_date_people;
    private JPanel jPanel1;
    private JPanel jPanel_activities;
    private JPanel jPanel_activities1;
    private JScrollPane jScrollPane2;
    private JScrollPane jScrollPane3;
    private JToolBar.Separator jSeparator1;
    private JTabbedPane jTabbedPane1;
    private JTable jTable2;
    private JTable jTable_activity;
    private JToolBar jToolBar1;
    private JToolBar jToolBar2;
    private JToolBar jToolBar3;
    private SolutionTableModel solutionTableModel1;
    private SolutionTableModel solutionTableModel2;
    private SolutionTableRenderer solutionTableRenderer1;
    private SolutionTableRenderer solutionTableRenderer2;
    // End of variables declaration//GEN-END:variables

    @Override
    public void solutionFound() {
        this.activityComboBoxModel.clear();
        this.solutionTableModel1.clear();
        List<ActivityTurn> list = new ArrayList<>();
        List<Activity> activities = TrainerManager.getInstance().getActivities();

        for (Activity act : activities) {
            this.activityComboBoxModel.addItem(act);
        }

        System.out.println("SOLUTION PANEL");
        System.out.println("TURNS: " + list.size());

        Activity selectedItem = this.activityComboBoxModel.getElementAt(0);
        System.out.println("<<<<<<<<<<<<<< PER QUESTA ATTIVITA' HO " + selectedItem.getActivityTurns().size() + " TURNI! >>>>>>>>>>>");
        solution = selectedItem.getActivityTurns();

        for (Activity activity : activities) {
            globalSolution.addAll(activity.getActivityTurns());
        }

        this.jComboBox_persons.setSelectedIndex(0);
//        List<ActivityTurn> page = new ArrayList<>();
//        for (ActivityTurn turn : solution) {
////            if (turn.getStartTime().before(today) && turn.getEndTime().before(week.get(6))) {
//            page.add(turn);
////            } else {
////                break;
////            }
//        }
//        this.solutionTableModel1.clear();
//        this.solutionTableModel1.addAllRows(page);
        updateSolutionTableActivities();
    }

    @Override
    public JComponent[] getToolbarButtons() {
        JComponent[] buttons = new JComponent[4];
        buttons[0] = this.jButton_Calculate;
        buttons[1] = this.jButton_pdf;
        buttons[2] = this.jButton_Save;
        buttons[3] = this.jButton_Excel;
//        buttons[1] = this.jSeparator1;
//        buttons[2] = this.jLabel1;
//        buttons[3] = this.jComboBox_activities;
//        buttons[4] = this.filler1;
//        buttons[5] = this.jLabel2;
//        buttons[6] = this.jComboBox_persons;
//        buttons[7] = this.jButton_TEST;
        return buttons;
    }

    @Override
    public void newActivityEvent(Activity activity) {

    }

    @Override
    public void removeActivityEvent(Activity activity) {
        Activity selectedItem = this.activityComboBoxModel.getElementAt(0);
        System.out.println("<<<<<<<<<<<<<< PER QUESTA ATTIVITA' HO " + selectedItem.getActivityTurns().size() + " TURNI! >>>>>>>>>>>");
        solution = selectedItem.getActivityTurns();
        this.jComboBox_persons.setSelectedIndex(0);
        updateSolutionTableActivities();
    }

    @Override
    public void newProfileEvent(Job profile) {

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
    public void temporarySolutionFound(int value) {
        this.activityComboBoxModel.clear();
        this.solutionTableModel1.clear();
        List<ActivityTurn> list = new ArrayList<>();
        List<Activity> activities = TrainerManager.getInstance().getActivities();
        globalSolution.clear();
        for (Activity activity : activities) {
            globalSolution.addAll(activity.getActivityTurns());
        }
        for (Activity act : activities) {
            this.activityComboBoxModel.addItem(act);
        }

        System.out.println("SOLUTION PANEL");
        System.out.println("TURNS: " + list.size());

        Activity selectedItem = this.activityComboBoxModel.getElementAt(0);
        System.out.println("<<<<<<<<<<<<<< PER QUESTA ATTIVITA' HO " + selectedItem.getActivityTurns().size() + " TURNI! >>>>>>>>>>>");
        solution = selectedItem.getActivityTurns();
        this.jComboBox_persons.setSelectedIndex(0);
//        List<ActivityTurn> page = new ArrayList<>();
//        for (ActivityTurn turn : solution) {
////            if (turn.getStartTime().before(today) && turn.getEndTime().before(week.get(6))) {
//            page.add(turn);
////            } else {
////                break;
////            }
//        }
//        this.solutionTableModel1.clear();
//        this.solutionTableModel1.addAllRows(page);
        updateSolutionTableActivities();
    }

    @Override
    public void messageFromSolver(String message) {

    }

    @Override
    public void newDormient(long id) {
    }

    @Override
    public void dormientWokeup(long id) {
    }

    @Override
    public void error(String message) {

    }

    @Override
    public void ghostNumberUpdated() {
    }

    @Override
    public void timeAvailableChanged() {
    }

}
