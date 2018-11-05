/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.activities.table;

import it.cnr.istc.i18n.annotations.I18N;
import it.cnr.istc.i18n.annotations.I18NUpdater;
import it.cnr.istc.i18n.translator.TranslatorManager;
import it.cnr.istc.sponsor.tt.abstracts.SubMainPanel;
import it.cnr.istc.sponsor.tt.gui.activities.glass.ActivityTurnsLayer;
import it.cnr.istc.sponsor.tt.gui.activities.table.mr.ActivityComboBoxModel;
import it.cnr.istc.sponsor.tt.gui.activities.table.mr.ActivityComboBoxRenderer;
import it.cnr.istc.sponsor.tt.gui.activities.table.mr.ActivityTableModel;
import it.cnr.istc.sponsor.tt.gui.activities.table.mr.ActivityTableRenderer;
import it.cnr.istc.sponsor.tt.gui.engine.GuiEventListener;
import it.cnr.istc.sponsor.tt.gui.engine.GuiEventManager;
import it.cnr.istc.sponsor.tt.gui.profiles.JobSelectorDialog;
import it.cnr.istc.sponsor.tt.gui.solver.SolverManager;
import it.cnr.istc.sponsor.tt.gui.solver.SolverManager2;
import it.cnr.istc.sponsor.tt.gui.solver.SolverManager4;
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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayer;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class ActivityTablePanel extends javax.swing.JPanel implements SubMainPanel, GuiEventListener {

    

    private ActivityComboBoxModel activityComboBoxModel = null;
    private ActivityComboBoxRenderer activityComboBoxRenderer = null;
    private Date today;
    private ActivityTurnsLayer glassPane = null;
//    private static List<Date> week = null;
    private Week week = null;
    private List<ActivityTurn> copiedTurns = new ArrayList<>();

    private Activity singleActivity = null;

    private boolean createSingle = true;
    private int tab;
    private int delta = 0;
    private DayNames names;
//    private List<String> activeKeys = new ArrayList<>();

    /**
     * Creates new form ActivityTablePanel
     */
    public ActivityTablePanel(Week uic) {
        initComponents();
        names = new DayNames();
        
        this.week = uic;
        JPanel tableWrapper = new JPanel();
        tableWrapper.setLayout(new GridLayout(0, 1));
        tableWrapper.add(jPanel_tableContainer);
        tableWrapper.setBackground(Color.ORANGE);
        JPanel wrapper = new JPanel();

//        jTable1.setMinimumSize(new Dimension(3000, 3000));
//        wrapper.setMinimumSize(new Dimension(3000, 3000));
//        jPanel_tableContainer.setMinimumSize(new Dimension(3000, 3000));
//        tableWrapper.setMinimumSize(new Dimension(3000, 3000));
        wrapper.setBackground(Color.PINK);
        wrapper.setLayout(new GridLayout(0, 1));
//        TimeIntervalContainer panel = new TimeIntervalContainer();
        glassPane = new ActivityTurnsLayer();
//        ActivityCaldendarContainer acc = new ActivityCaldendarContainer((ActivityTurnsLayer)glassPane);
        JLayer<JPanel> jlayer = new JLayer<JPanel>(tableWrapper, glassPane);
        wrapper.add(jlayer);
        this.jScrollPane2.setViewportView(wrapper);
        this.jScrollPane1.getViewport().setOpaque(true);
        this.jScrollPane2.getViewport().setOpaque(true);
        this.jScrollPane2.getViewport().setBackground(Color.RED);
        this.jScrollPane1.getViewport().setBackground(Color.BLUE);

        if (week == null) {
            this.today = Utils.getCurrentMonday();
            week = new Week(this.today);
        }else{
             this.today = Utils.getCurrentMonday(week.getDays().get(0));
             this.week = new Week(today);
        }

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
        jTable1.getColumnModel().getColumn(0).setMaxWidth(120);

        List<Job> jobs = TrainerManager.getInstance().getJobs();
        for (Job job : jobs) {
            JLabel q = new JLabel("   ");
            q.setMinimumSize(new Dimension(24, 24));
            q.setOpaque(true);
            q.setBackground(job.getColor());
            q.setToolTipText(labelize(job));
            this.jToolBar_buttons.add(q);
//            JLabel label = new JLabel(" " + job.getName() + "  ");
            JLabel label = new JLabel("  ");
            this.jToolBar_buttons.add(label);
        }
        jTable1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_C) {
                    System.out.println("                                COPIA !!");

                    copiedTurns.clear();
                    copiedTurns = getSelectedTurns();
                    JOptionPane.showMessageDialog(null, "Turni Copiati");

                } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V) {
                    System.out.println("                                INCOLLA !!!!");
                    int selectedColumn = jTable1.getSelectedColumn();
                    List<ActivityTurn> toPasteList = new ArrayList<>();
                    System.out.println("copied turns: " + copiedTurns.size());
                    toPasteList.addAll(copiedTurns);
                    System.out.println("size of paste: " + toPasteList.size());
                    for (ActivityTurn activityTurn : toPasteList) {
                        System.out.println("ACTUNG !!");
//                        

                        Date startTime = new Date(activityTurn.getStartTime());
                        Date endTime = new Date(activityTurn.getEndTime());

                        Date pasteDate = week.getDays().get(selectedColumn - 1);
                        startTime.setMonth(pasteDate.getMonth());
                        startTime.setDate(pasteDate.getDate());
                        endTime.setMonth(pasteDate.getMonth());
                        endTime.setDate(pasteDate.getDate());

                        ActivityTurn pasteTurn = new ActivityTurn(startTime.getTime(), endTime.getTime());

                        List<JobTurn> requiredProfiles = activityTurn.getRequiredProfiles();
                        List<JobTurn> toPasteProfiles = new ArrayList<>();
                        for (JobTurn rp : requiredProfiles) {
                            //toPasteProfiles.add(new Job(rp.getId(),rp.getName(), rp.getColor(), rp.getWantedKeywords(), rp.getUnwantedKeywords(), rp.getSkills()));
                            toPasteProfiles.add(new JobTurn(rp.getJob(), pasteTurn, rp.getWantedKeywords(), rp.getUnwantedKeywords(), rp.getWantedPerson()));
                        }
                        pasteTurn.getRequiredProfiles().clear();
                        pasteTurn.getRequiredProfiles().addAll(toPasteProfiles);
                        pasteTurn.setWantedPerson(activityTurn.getWantedPerson());
                        pasteTurn.setWantedKeywords(activityTurn.getWantedKeywords());
//                        pasteTurn.setUnwantedKeywords(activityTurn.getUnwantedKeywords());
                        if (canAddThisTurn(pasteTurn)) {
                            System.out.println("can add ok");
                            singleActivity.addActivityTurn(pasteTurn);

//                            activityTableModel2.changeTurnActivity(row, column, index);
//             this.jTable1.getSelectionModel().clearSelection();
                        } else {
                            System.out.println("sorry cant add");
                        }
                    }
                    invalidate();
                    repaint();
                }

            }
        }
        );
    }

    public void init() {
        GuiEventManager.getInstance().addGuiEventListener(this);
    }

    public void setTab(int tab) {
        this.tab = tab;
    }

    public int getTab() {
        return tab;
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

            int start = this.jTable1.getSelectionModel().getMinSelectionIndex() / 2 + SettingsManager.getInstance().getOpenTime();
            int end = this.jTable1.getSelectionModel().getMaxSelectionIndex() / 2 + SettingsManager.getInstance().getOpenTime();
            end += (this.jTable1.getSelectionModel().getMaxSelectionIndex() % 2 != 0 ? 1 : 0);

            startDate.setHours(start);
            startDate.setSeconds(0);
            startDate.setMinutes(this.jTable1.getSelectionModel().getMinSelectionIndex() % 2 == 0 ? 0 : 30);

            endDate.setHours(end);
            endDate.setSeconds(0);
            endDate.setMinutes(this.jTable1.getSelectionModel().getMaxSelectionIndex() % 2 != 0 ? 0 : 30);

//            startDate.setHours(selectedRows[0] / 2 + SettingsManager.getInstance().getOpenTime());
//            endDate.setHours((selectedRows[selectedRows.length - 1] + 1) / 2 + SettingsManager.getInstance().getOpenTime());
//            startDate.setMinutes(selectedRows[0] / 2 == 0 ? 0 : 30);
//            startDate.setSeconds(0);
//            endDate.setMinutes(0);
//            endDate.setSeconds(0);
            System.out.println("  COPYING START: " + startDate);
            System.out.println("  COPYING END:   " + endDate);
//            for (int i = selectedColumns[0]; i <= selectedColumns[selectedColumns.length - 1]; i++) {
            Collections.sort(singleActivity.getActivityTurns());
            List<ActivityTurn> activityTurns = this.singleActivity.getActivityTurns();
            for (ActivityTurn activityTurn : activityTurns) {
                if (activityTurn.getStartTime() > startDate.getTime() - 1000 && activityTurn.getEndTime() < endDate.getTime() + 1000) {
                    System.out.println("                --->     ADDING ACT");
                    result.add(activityTurn);
                }
            }
            System.out.println("afer copy -> " + result.size());
//            }
        }
        return result;
    }

    public List<ActivityTurn> getAllCurrentTurns() {
        List<ActivityTurn> result = new ArrayList<>();
        int rowsCount = this.jTable1.getRowCount();
        int columnsCount = this.jTable1.getColumnCount();
        Date startDate = new Date(week.getDays().get(0).getTime());
        Date endDate = new Date(week.getDays().get(6).getTime());
        startDate.setHours(0);
        endDate.setHours(23);
        startDate.setMinutes(0);
        startDate.setSeconds(0);
        endDate.setMinutes(59);
        endDate.setSeconds(59);

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
        System.out.println("afer copy -> " + result.size());
//            }

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
        String tooltip = "<html><b>" + job.getName() + "</b><ul>";
        List<Skill> skills = job.getSkills();
        for (Skill skill : skills) {
            tooltip += "<li>" + TranslatorManager.getInstance().getTranslation(skill.getName()) + "</li>";
        }
        tooltip += "</ul>";
        return tooltip;
    }

    public void setActivity(Activity activity) {
        this.singleActivity = activity;
        glassPane.setContext(singleActivity, jTable1, today);
    }

    public String getLabelDate(Date date) {
        String result = " ";
        result += date.getDate() + " ";
        result += new SimpleDateFormat("MMMMM").format(date);
        return result;
    }

    @I18NUpdater
    public final void updateLabels(Date startDay) {

        String mainLabel = "dal " + getLabelDate(startDay);
        jTable1.getColumnModel().getColumn(0).setHeaderValue(names.labelTime);
        jTable1.getColumnModel().getColumn(1).setHeaderValue(names.labelMonday + getLabelDate(startDay));
        Date d2 = new Date(startDay.getTime() + (1000l * 60l * 60 * 24l));
        jTable1.getColumnModel().getColumn(2).setHeaderValue(names.labelTuesday + getLabelDate(d2));
        Date d3 = new Date(d2.getTime() + (1000l * 60l * 60 * 24l));
        jTable1.getColumnModel().getColumn(3).setHeaderValue(names.labelWednesday + getLabelDate(d3));
        Date d4 = new Date(d3.getTime() + (1000l * 60l * 60 * 24l));
        jTable1.getColumnModel().getColumn(4).setHeaderValue(names.labelThursday + getLabelDate(d4));
        Date d5 = new Date(d4.getTime() + (1000l * 60l * 60 * 24l));
        jTable1.getColumnModel().getColumn(5).setHeaderValue(names.labelFriday + getLabelDate(d5));
        Date d6 = new Date(d5.getTime() + (1000l * 60l * 60 * 24l));
        jTable1.getColumnModel().getColumn(6).setHeaderValue(names.labelSaturday + getLabelDate(d6));
        Date d7 = new Date(d6.getTime() + (1000l * 60l * 60 * 24l));
        jTable1.getColumnModel().getColumn(7).setHeaderValue(names.labelSunday + getLabelDate(d7));
        mainLabel += " al " + getLabelDate(d7);
//        if (week == null) {
//            week = new ArrayList<>();
//            week.clear();
//            week.add(today);
//            week.add(d2);
//            week.add(d3);
//            week.add(d4);
//            week.add(d5);
//            week.add(d6);
//            week.add(d7);
//        }

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

        activityTableRenderer1 = new ActivityTableRenderer();
        activityTableModel2 = new ActivityTableModel();
        jToggleButton1 = new JToggleButton();
        jComboBox_Turni = new JComboBox<>();
        jPanel_tableContainer = new JPanel();
        jScrollPane1 = new JScrollPane();
        jTable1 = new JTable();
        jButton2 = new JButton();
        jButton3 = new JButton();
        jPopupMenu1 = new JPopupMenu();
        jMenuItem1 = new JMenuItem();
        jMenuItem2 = new JMenuItem();
        jPanel1 = new JPanel();
        jScrollPane2 = new JScrollPane();
        jToolBar_buttons = new JToolBar();
        jToggleButton_creaSingolo = new JToggleButton();
        jButton_DeleteActivity = new JButton();
        jButton_WeekBackward = new JButton();
        jLabel_date = new JLabel();
        jButton_WeekForward = new JButton();
        jButton4 = new JButton();
        jSeparator1 = new JToolBar.Separator();
        jCheckBox1 = new JCheckBox();
        jSeparator2 = new JToolBar.Separator();
        filler1 = new Box.Filler(new Dimension(10, 15), new Dimension(10, 15), new Dimension(10, 15));

        activityTableRenderer1.setText("activityTableRenderer1");

        jToggleButton1.setText("Turni");
        jToggleButton1.setFocusable(false);
        jToggleButton1.setHorizontalTextPosition(SwingConstants.CENTER);
        jToggleButton1.setVerticalTextPosition(SwingConstants.BOTTOM);
        jToggleButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jComboBox_Turni.setModel(new DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Turni.setEnabled(false);

        jPanel_tableContainer.setPreferredSize(new Dimension(678, 900));

        jScrollPane1.setBorder(null);

        jTable1.setModel(activityTableModel2);
        jTable1.setCellSelectionEnabled(true);
        jTable1.setRowHeight(24);
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jTable1.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent evt) {
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

        GroupLayout jPanel_tableContainerLayout = new GroupLayout(jPanel_tableContainer);
        jPanel_tableContainer.setLayout(jPanel_tableContainerLayout);
        jPanel_tableContainerLayout.setHorizontalGroup(jPanel_tableContainerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE)
        );
        jPanel_tableContainerLayout.setVerticalGroup(jPanel_tableContainerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_tableContainerLayout.createSequentialGroup()
                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 670, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jButton2.setIcon(new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/forward-for32.png"))); // NOI18N
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(SwingConstants.BOTTOM);
        jButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/backwards-back32.png"))); // NOI18N
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(SwingConstants.BOTTOM);
        jButton3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jMenuItem1.setText("Copia");
        jMenuItem1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        jMenuItem2.setText("Incolla");
        jMenuItem2.setEnabled(false);
        jMenuItem2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem2);

        setBackground(new Color(255, 255, 255));

        jPanel1.setBackground(new Color(102, 255, 0));

        jScrollPane2.setBackground(new Color(0, 255, 153));
        jScrollPane2.setBorder(null);

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );

        jToolBar_buttons.setBackground(new Color(255, 255, 255));
        jToolBar_buttons.setFloatable(false);
        jToolBar_buttons.setRollover(true);

        jToggleButton_creaSingolo.setIcon(new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/add.png"))); // NOI18N
        jToggleButton_creaSingolo.setSelected(true);
        jToggleButton_creaSingolo.setFocusable(false);
        jToggleButton_creaSingolo.setHorizontalTextPosition(SwingConstants.CENTER);
        jToggleButton_creaSingolo.setVerticalTextPosition(SwingConstants.BOTTOM);
        jToggleButton_creaSingolo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jToggleButton_creaSingoloActionPerformed(evt);
            }
        });
        jToolBar_buttons.add(jToggleButton_creaSingolo);

        jButton_DeleteActivity.setBackground(new Color(255, 255, 255));
        jButton_DeleteActivity.setIcon(new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/delete32.png"))); // NOI18N
        jButton_DeleteActivity.setToolTipText("Rimuovi i turni nell'area selezionata");
        jButton_DeleteActivity.setFocusable(false);
        jButton_DeleteActivity.setHorizontalTextPosition(SwingConstants.CENTER);
        jButton_DeleteActivity.setVerticalTextPosition(SwingConstants.BOTTOM);
        jButton_DeleteActivity.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton_DeleteActivityActionPerformed(evt);
            }
        });
        jToolBar_buttons.add(jButton_DeleteActivity);

        jButton_WeekBackward.setBackground(new Color(255, 255, 255));
        jButton_WeekBackward.setIcon(new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/arrow_sx.png"))); // NOI18N
        jButton_WeekBackward.setEnabled(false);
        jButton_WeekBackward.setFocusable(false);
        jButton_WeekBackward.setHorizontalTextPosition(SwingConstants.CENTER);
        jButton_WeekBackward.setVerticalTextPosition(SwingConstants.BOTTOM);
        jButton_WeekBackward.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton_WeekBackwardActionPerformed(evt);
            }
        });
        jToolBar_buttons.add(jButton_WeekBackward);

        jLabel_date.setFont(new Font("Tahoma", 1, 12)); // NOI18N
        jLabel_date.setText("dal 1 ottobre all' 8 ottobre 2016");
        jToolBar_buttons.add(jLabel_date);

        jButton_WeekForward.setBackground(new Color(255, 255, 255));
        jButton_WeekForward.setIcon(new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/arrow_dx.png"))); // NOI18N
        jButton_WeekForward.setFocusable(false);
        jButton_WeekForward.setHorizontalTextPosition(SwingConstants.CENTER);
        jButton_WeekForward.setVerticalTextPosition(SwingConstants.BOTTOM);
        jButton_WeekForward.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton_WeekForwardActionPerformed(evt);
            }
        });
        jToolBar_buttons.add(jButton_WeekForward);

        jButton4.setBackground(new Color(255, 255, 255));
        jButton4.setIcon(new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/CopiaEavanza.png"))); // NOI18N
        jButton4.setText("Copia questa settimana nella prossima");
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(SwingConstants.RIGHT);
        jButton4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar_buttons.add(jButton4);
        jToolBar_buttons.add(jSeparator1);

        jCheckBox1.setBackground(new Color(255, 255, 255));
        jCheckBox1.setText("<html><div>un solo turno a settimana");
        jCheckBox1.setToolTipText("Un volontario può frequentare uno o più turni a settimana a seconda che il riquadro sia spuntato o meno");
        jCheckBox1.setFocusable(false);
        jCheckBox1.setHorizontalTextPosition(SwingConstants.RIGHT);
        jCheckBox1.setMaximumSize(new Dimension(100, 40));
        jCheckBox1.setMinimumSize(new Dimension(100, 40));
        jCheckBox1.setPreferredSize(new Dimension(100, 40));
        jCheckBox1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        jToolBar_buttons.add(jCheckBox1);
        jToolBar_buttons.add(jSeparator2);
        jToolBar_buttons.add(filler1);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar_buttons, GroupLayout.DEFAULT_SIZE, 733, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jToolBar_buttons, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

                int start = this.jTable1.getSelectionModel().getMinSelectionIndex() / 2 + SettingsManager.getInstance().getOpenTime();
                int end = this.jTable1.getSelectionModel().getMaxSelectionIndex() / 2 + SettingsManager.getInstance().getOpenTime();
                end += (this.jTable1.getSelectionModel().getMaxSelectionIndex() % 2 != 0 ? 1 : 0);
//                JOptionPane.showMessageDialog(null, "START: "+start+"\nEND: "+end);
//            System.out.println("SELECTION FROM " + start + " TO " + end);
                Date td1 = new Date(week.getDays().get(column - 1).getTime());
                td1.setHours(start);
                td1.setSeconds(0);
                td1.setMinutes(this.jTable1.getSelectionModel().getMinSelectionIndex() % 2 == 0 ? 0 : 30);

                Date td2 = new Date(week.getDays().get(column - 1).getTime());
                td2.setHours(end);
                td2.setSeconds(0);
                td2.setMinutes(this.jTable1.getSelectionModel().getMaxSelectionIndex() % 2 != 0 ? 0 : 30);

//                JOptionPane.showMessageDialog(null, "TD START: "+td1+"\nTD END: "+td2);
                ActivityTurn activityTurn = new ActivityTurn(td1.getTime(), td2.getTime());
                if (canAddThisTurn(activityTurn)) {
                    JobSelectorDialog dialog = new JobSelectorDialog(new JFrame());
                    dialog.setLocationRelativeTo(null);
                    dialog.setTurn(activityTurn);
                    dialog.setVisible(true);

                    ActivityTurn turn = dialog.getTurn();
                    if (turn != null && !turn.getRequiredProfiles().isEmpty()) {
                        singleActivity.addActivityTurn(turn);
                        this.activityTableModel2.changeTurnActivity(row, column, index);
//             this.jTable1.getSelectionModel().clearSelection();
                        this.invalidate();
                        this.repaint();
                    }

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
            if (evt.isPopupTrigger()) {
                jPopupMenu1.show(jTable1, evt.getX(), evt.getY());
            }

        }

//        System.out.println("--------------------------------------------------------");

    }//GEN-LAST:event_jTable1MouseReleased

    private void jButton_WeekForwardActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton_WeekForwardActionPerformed
//        this.today = Utils.getNextWeekMonday(today);
        delta++;
        if (delta > 0) {
            jButton_WeekBackward.setEnabled(true);
        }
        week = week.getFurtherWeek();
        this.today = week.getDays().get(0);
        updateLabels(this.today);
        this.glassPane.updateTableDate(today);
        TrainerManager.getInstance().setEndPlanningDate(week.getDays().get(6));
        GuiEventManager.getInstance().changeDate(this.today);
    }//GEN-LAST:event_jButton_WeekForwardActionPerformed

    private void jButton_WeekBackwardActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton_WeekBackwardActionPerformed
//        this.today = Utils.getPreviousWeekMonday(today);
        delta--;
        if (delta == 0) {
            jButton_WeekBackward.setEnabled(false);
        }
        week = week.getPreviousWeek();
        this.today = week.getDays().get(0);
        updateLabels(this.today);
        this.glassPane.updateTableDate(today);
        GuiEventManager.getInstance().changeDate(this.today);
    }//GEN-LAST:event_jButton_WeekBackwardActionPerformed

    private void jButton_DeleteActivityActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton_DeleteActivityActionPerformed
        List<ActivityTurn> selectedTurns = getSelectedTurns();
        this.singleActivity.getActivityTurns().removeAll(selectedTurns);
        invalidate();
        repaint();
        GuiEventManager.getInstance().newActivityTurn(null);
    }//GEN-LAST:event_jButton_DeleteActivityActionPerformed

    private void jToggleButton_creaSingoloActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jToggleButton_creaSingoloActionPerformed
        createSingle = this.jToggleButton_creaSingolo.isSelected();
    }//GEN-LAST:event_jToggleButton_creaSingoloActionPerformed

    private void jButton2ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.today = Utils.getNextMonthMonday(today);
        updateLabels(this.today);
        this.glassPane.updateTableDate(today);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        this.today = Utils.getPreviousMonthMonday(today);
        updateLabels(this.today);
        this.glassPane.updateTableDate(today);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        copiedTurns.clear();
        copiedTurns = getAllCurrentTurns();
        jButton_WeekForward.doClick();
        List<ActivityTurn> toPasteList = new ArrayList<>();
        System.out.println("copied turns: " + copiedTurns.size());
        toPasteList.addAll(copiedTurns);
        System.out.println("size of paste: " + toPasteList.size());
        for (ActivityTurn activityTurn : toPasteList) {
            System.out.println("ACTUNG !!");
            System.out.println("ADDING - > " + activityTurn);

            Date startTime = new Date(activityTurn.getStartTime());
            Date endTime = new Date(activityTurn.getEndTime());

            int day = new Date(activityTurn.getStartTime()).getDay() == 0 ? 6 : new Date(activityTurn.getStartTime()).getDay() - 1;
            Date pasteDate = week.getDays().get(day);
            startTime.setMonth(pasteDate.getMonth());
            startTime.setDate(pasteDate.getDate());
            endTime.setMonth(pasteDate.getMonth());
            endTime.setDate(pasteDate.getDate());

            ActivityTurn pasteTurn = new ActivityTurn(startTime.getTime(), endTime.getTime());

            List<JobTurn> requiredProfiles = activityTurn.getRequiredProfiles();

            for (JobTurn originalJobs : requiredProfiles) {
                pasteTurn.getRequiredProfiles().add(new JobTurn(originalJobs.getJob(), pasteTurn, originalJobs.getWantedKeywords(), originalJobs.getUnwantedKeywords(), originalJobs.getWantedPerson()));
            }
            pasteTurn.setWantedPerson(activityTurn.getWantedPerson());
            pasteTurn.setWantedKeywords(activityTurn.getWantedKeywords());
//            pasteTurn.setUnwantedKeywords(activityTurn.getUnwantedKeywords());

            if (canAddThisTurn(pasteTurn)) {
                System.out.println("can add ok");
                singleActivity.addActivityTurn(pasteTurn);

//                            activityTableModel2.changeTurnActivity(row, column, index);
//             this.jTable1.getSelectionModel().clearSelection();
            } else {
                System.out.println("sorry cant add");
            }
        }
        invalidate();
        repaint();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jCheckBox1ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        if (jCheckBox1.isSelected()) {
//            SolverManager2.getInstance().addWeekActivity(this.singleActivity);//questa attività può gingillare 1-n
//            SolverManager.getInstance().addWeekActivity(this.singleActivity);//questa attività può gingillare 1-n
            SolverManager4.getInstance().setActivityWeekConstraint(singleActivity, new Boolean(true));
        } else {
//            SolverManager2.getInstance().removeWeekActivity(this.singleActivity);
//            SolverManager.getInstance().removeWeekActivity(this.singleActivity);
            SolverManager4.getInstance().setActivityWeekConstraint(singleActivity, new Boolean(false));
        }
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jMenuItem1ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        System.out.println("                                COPIA !!");

        copiedTurns.clear();
        copiedTurns = getSelectedTurns();
        JOptionPane.showMessageDialog(null, "Turni Copiati");
        this.jMenuItem2.setEnabled(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        int selectedColumn = jTable1.getSelectedColumn();
        List<ActivityTurn> toPasteList = new ArrayList<>();
        System.out.println("copied turns: " + copiedTurns.size());
        toPasteList.addAll(copiedTurns);
        System.out.println("size of paste: " + toPasteList.size());
        for (ActivityTurn activityTurn : toPasteList) {
            System.out.println("ACTUNG !!");

            Date startTime = new Date(activityTurn.getStartTime());
            Date endTime = new Date(activityTurn.getEndTime());

            Date pasteDate = week.getDays().get(selectedColumn - 1);
            startTime.setMonth(pasteDate.getMonth());
            startTime.setDate(pasteDate.getDate());
            endTime.setMonth(pasteDate.getMonth());
            endTime.setDate(pasteDate.getDate());
            ActivityTurn pasteTurn = new ActivityTurn(startTime.getTime(), endTime.getTime());
            List<JobTurn> requiredProfiles = activityTurn.getRequiredProfiles();
            List<JobTurn> toPasteProfiles = new ArrayList<>();
            for (JobTurn rp : requiredProfiles) {
                //toPasteProfiles.add(new Job(rp.getId(), rp.getName(), rp.getColor(), rp.getWantedKeywords(), rp.getUnwantedKeywords(), rp.getSkills()));
                toPasteProfiles.add(new JobTurn(rp.getJob(), pasteTurn, rp.getWantedKeywords(), rp.getUnwantedKeywords(), rp.getWantedPerson()));
            }
            pasteTurn.getRequiredProfiles().clear();
            pasteTurn.getRequiredProfiles().addAll(toPasteProfiles);
            pasteTurn.setWantedPerson(activityTurn.getWantedPerson());
            pasteTurn.setWantedKeywords(activityTurn.getWantedKeywords());
//            pasteTurn.setUnwantedKeywords(activityTurn.getUnwantedKeywords());
            if (canAddThisTurn(pasteTurn)) {
                System.out.println("can add ok");
                singleActivity.addActivityTurn(pasteTurn);

//                            activityTableModel2.changeTurnActivity(row, column, index);
//             this.jTable1.getSelectionModel().clearSelection();
            } else {
                System.out.println("sorry cant add");
            }
        }
        invalidate();
        repaint();
//        this.jMenuItem2.setEnabled(false);

    }//GEN-LAST:event_jMenuItem2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ActivityTableModel activityTableModel2;
    private ActivityTableRenderer activityTableRenderer1;
    private Box.Filler filler1;
    private JButton jButton2;
    private JButton jButton3;
    private JButton jButton4;
    private JButton jButton_DeleteActivity;
    private JButton jButton_WeekBackward;
    private JButton jButton_WeekForward;
    private JCheckBox jCheckBox1;
    private JComboBox<String> jComboBox_Turni;
    private JLabel jLabel_date;
    private JMenuItem jMenuItem1;
    private JMenuItem jMenuItem2;
    private JPanel jPanel1;
    private JPanel jPanel_tableContainer;
    private JPopupMenu jPopupMenu1;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JToolBar.Separator jSeparator1;
    private JToolBar.Separator jSeparator2;
    private JTable jTable1;
    private JToggleButton jToggleButton1;
    private JToggleButton jToggleButton_creaSingolo;
    private JToolBar jToolBar_buttons;
    // End of variables declaration//GEN-END:variables

    @Override
    public JComponent[] getToolbarButtons() {
        JComponent[] bb = new JComponent[0];
//        bb[0] = this.peopleWorkingViewerPanel1;
        return bb;
    }

    @Override
    public void newActivityEvent(Activity activity) {

    }

    @Override
    public void newProfileEvent(Job profile) {

    }

    @Override
    public void newActivityTurn(ActivityTurn turn) {

    }

    @Override
    public void changeDate(Date date) {
//        if (date.getTime() > week.getDays().get(week.getDays().size() - 1).getTime()) {
//            System.out.println("SECOND IF");
//            while (true) {
//                System.out.println("tru 9999999999999999999999");
////                Week week2 = week.getFurtherWeek();
//                if (date.getTime() < week.getDays().get(week.getDays().size() - 1).getTime()) {
//                    System.out.println("break");
//                    break;
//                }
//                jButton_WeekForward.doClick();
//                week = week.getFurtherWeek();
//                this.today = week.getDays().get(0);
//                updateLabels(this.today);
//                this.glassPane.updateTableDate(today);
//
//            }
//            this.invalidate();
//            this.revalidate();
//            this.repaint();
//        }

    }

    @Override
    public void changeTab(int t) {
        System.out.println("TABBONE CAMBIATO ACT ACT-> " + t + "    ma this.tab = " + this.tab);
        if (t == this.tab) {
            System.out.println("YUPPI PUP");
            GuiEventManager.getInstance().changeDate(this.today);

        }
    }

    @Override
    public void removeActivityEvent(Activity activity) {

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
