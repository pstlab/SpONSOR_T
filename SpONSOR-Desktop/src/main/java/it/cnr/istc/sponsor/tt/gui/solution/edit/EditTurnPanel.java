/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.solution.edit;

import com.google.gson.Gson;
import it.cnr.istc.i18n.annotations.I18N;
import it.cnr.istc.i18n.annotations.I18NUpdater;
import it.cnr.istc.sponsor.tt.gui.people.mr.PeopleTableModel;
import it.cnr.istc.sponsor.tt.gui.people.mr.PeopleTableRenderer;
import it.cnr.istc.sponsor.tt.logic.ParsedAccount;
import it.cnr.istc.sponsor.tt.logic.TrainerManager;
import it.cnr.istc.sponsor.tt.logic.model.Account;
import it.cnr.istc.sponsor.tt.logic.model.Activity;
import it.cnr.istc.sponsor.tt.logic.model.ActivityTurn;
import it.cnr.istc.sponsor.tt.logic.model.ComfirmedTurn;
import it.cnr.istc.sponsor.tt.logic.model.FreeTimeToken;
import it.cnr.istc.sponsor.tt.logic.model.Job;
import it.cnr.istc.sponsor.tt.logic.model.JobTurn;
import it.cnr.istc.sponsor.tt.logic.model.ModelManager;
import it.cnr.istc.sponsor.tt.logic.model.Person;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class EditTurnPanel extends javax.swing.JPanel {

    @I18N(key = "label.registered.profiles")
    private String profiles;
    @I18N(key = "people.leader")
    private String peopleLeader;
    @I18N(key = "people.planner")
    private String peoplePlanner;
    @I18N(key = "people.brilliant")
    private String peopleBrilliant;
    @I18N(key = "people.evaluator")
    private String peopleEvaluator;
    @I18N(key = "people.concrete")
    private String peopleConcrete;
    @I18N(key = "people.explorer")
    private String peopleExplorer;
    @I18N(key = "people.worker")
    private String peopleWorker;
    @I18N(key = "people.objective")
    private String peopleObjective;

    private ActivityTurn turn;
    List<Person> availablePeople;
    List<Person> allPeople;

    /**
     * Creates new form EditTurnPanel
     */
    public EditTurnPanel() {
        initComponents();
        updateLabels();
        jTable1.getColumnModel().getColumn(0).setMinWidth(55);
        jTable1.getColumnModel().getColumn(0).setMaxWidth(55);
        jTable1.getColumnModel().getColumn(1).setMinWidth(150);
        jTable2.getColumnModel().getColumn(0).setMinWidth(55);
        jTable2.getColumnModel().getColumn(0).setMaxWidth(55);
        jTable2.getColumnModel().getColumn(1).setMinWidth(150);
        this.peopleTableRenderer1.setOpaque(true);
        ((JComponent) jTable1.getDefaultRenderer(Boolean.class)).setOpaque(true);
        this.peopleTableRenderer2.setOpaque(true);
        ((JComponent) jTable2.getDefaultRenderer(Boolean.class)).setOpaque(true);
    }

    @I18NUpdater
    public final void updateLabels() {
        jTable1.getColumnModel().getColumn(1).setHeaderValue(profiles);
        jTable1.getColumnModel().getColumn(2).setHeaderValue(peopleLeader);
        jTable1.getColumnModel().getColumn(3).setHeaderValue(peoplePlanner);
        jTable1.getColumnModel().getColumn(4).setHeaderValue(peopleBrilliant);
        jTable1.getColumnModel().getColumn(5).setHeaderValue(peopleEvaluator);
        jTable1.getColumnModel().getColumn(6).setHeaderValue(peopleConcrete);
        jTable1.getColumnModel().getColumn(7).setHeaderValue(peopleExplorer);
        jTable1.getColumnModel().getColumn(8).setHeaderValue(peopleWorker);
        jTable1.getColumnModel().getColumn(9).setHeaderValue(peopleObjective);
        jTable1.getTableHeader().repaint();
        jTable1.repaint();
        jTable2.getColumnModel().getColumn(1).setHeaderValue(profiles);
        jTable2.getColumnModel().getColumn(2).setHeaderValue(peopleLeader);
        jTable2.getColumnModel().getColumn(3).setHeaderValue(peoplePlanner);
        jTable2.getColumnModel().getColumn(4).setHeaderValue(peopleBrilliant);
        jTable2.getColumnModel().getColumn(5).setHeaderValue(peopleEvaluator);
        jTable2.getColumnModel().getColumn(6).setHeaderValue(peopleConcrete);
        jTable2.getColumnModel().getColumn(7).setHeaderValue(peopleExplorer);
        jTable2.getColumnModel().getColumn(8).setHeaderValue(peopleWorker);
        jTable2.getColumnModel().getColumn(9).setHeaderValue(peopleObjective);
        jTable2.getTableHeader().repaint();
        jTable2.repaint();
    }

    public void setTurn(ActivityTurn turn) {
        this.turn = turn;
        Activity act = TrainerManager.getInstance().getActivityByTurn(turn);
        if (act != null) {
            this.jLabel_activityName.setText(act.getActivityName().getName());
        }
        List<Person> peopleOfTurn = new ArrayList<>();
        List<ComfirmedTurn> comfirmedTurns = turn.getComfirmedTurns();
        for (ComfirmedTurn comfirmedTurn : comfirmedTurns) {
            peopleOfTurn.add(comfirmedTurn.getPerson());
        }
        System.out.println("PERSON LIST ARRIVED: " + peopleOfTurn.size());
        try {
            for (Person p : peopleOfTurn) {
                p.fix();
                Account account = p.getAccount();
                account.setId(p.getId());
                System.out.println("account: " + account.getName() + " " + account.getSurname());
                if (account.getId() == null) {
                    JOptionPane.showMessageDialog(null, "NO ID!!! at " + account.getName());
                }
                this.peopleTableModel1.addRowElement(account);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        allPeople = TrainerManager.getInstance().getPeople();
        availablePeople = new ArrayList<>();

        for (Person person : allPeople) {
            person.fix();

            System.out.println("size of availableTimes is: " + person.getName() + " ha " + person.getExpandedFreeTimes().size() + " turni liberi");
            for (FreeTimeToken availableTime : person.getExpandedFreeTimes()) {

                if (turn.getStartTime() + 1000 >= availableTime.getStarTime() && turn.getEndTime()- 1000 <= availableTime.getEndTime()) {

                    System.out.println("PERSON -> " + person);
                    if (!turn.isThisPersonConfirmed(person)) {
                        availablePeople.add(person);
                        System.out.println(">>> CANDIDATE FOUND");
                    } else {
                        System.out.println("NO BUONO");
                    }

                }

            }

        }
        for (Person p : availablePeople) {
            p.fix();
            Account account = p.getAccount();
            account.setId(p.getId());
            System.out.println("ADDING TO TABLE: " + p);
            this.peopleTableModel2.addRowElement(account);
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

        peopleTableModel1 = new PeopleTableModel();
        peopleTableRenderer1 = new PeopleTableRenderer();
        peopleTableModel2 = new PeopleTableModel();
        peopleTableRenderer2 = new PeopleTableRenderer();
        jButton2 = new JButton();
        jLabel1 = new JLabel();
        jLabel_activityName = new JLabel();
        jSeparator1 = new JSeparator();
        jSplitPane1 = new JSplitPane();
        jPanel1 = new JPanel();
        jScrollPane1 = new JScrollPane();
        jTable1 = new JTable();
        jPanel2 = new JPanel();
        jToolBar2 = new JToolBar();
        jPanel3 = new JPanel();
        jScrollPane2 = new JScrollPane();
        jTable2 = new JTable();
        jToolBar1 = new JToolBar();
        jButton1 = new JButton();

        peopleTableRenderer1.setText("peopleTableRenderer3");

        peopleTableRenderer2.setText("peopleTableRenderer3");

        jButton2.setText("jButton2");
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(SwingConstants.BOTTOM);

        setBackground(new Color(255, 255, 255));

        jLabel1.setFont(new Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Attività:");

        jLabel_activityName.setFont(new Font("Tahoma", 1, 14)); // NOI18N
        jLabel_activityName.setForeground(new Color(51, 51, 255));
        jLabel_activityName.setText("jLabel2");

        jSplitPane1.setBorder(null);
        jSplitPane1.setDividerLocation(160);
        jSplitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT);

        jPanel1.setBackground(new Color(255, 255, 255));
        jPanel1.setBorder(BorderFactory.createTitledBorder(null, "Volontari del turno secondo la Soluzione", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Tahoma", 1, 12), new Color(51, 51, 255))); // NOI18N

        jTable1.setModel(peopleTableModel1);
        jTable1.setRowHeight(24);
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setHeaderValue("Admin");
            jTable1.getColumnModel().getColumn(0).setCellRenderer(peopleTableRenderer1);
            jTable1.getColumnModel().getColumn(1).setHeaderValue("Volontari");
            jTable1.getColumnModel().getColumn(2).setHeaderValue("p");
            jTable1.getColumnModel().getColumn(2).setCellRenderer(peopleTableRenderer1);
            jTable1.getColumnModel().getColumn(3).setHeaderValue("p");
            jTable1.getColumnModel().getColumn(3).setCellRenderer(peopleTableRenderer1);
            jTable1.getColumnModel().getColumn(4).setHeaderValue("p");
            jTable1.getColumnModel().getColumn(4).setCellRenderer(peopleTableRenderer1);
            jTable1.getColumnModel().getColumn(5).setHeaderValue("p");
            jTable1.getColumnModel().getColumn(5).setCellRenderer(peopleTableRenderer1);
            jTable1.getColumnModel().getColumn(6).setHeaderValue("p");
            jTable1.getColumnModel().getColumn(6).setCellRenderer(peopleTableRenderer1);
            jTable1.getColumnModel().getColumn(7).setHeaderValue("p");
            jTable1.getColumnModel().getColumn(7).setCellRenderer(peopleTableRenderer1);
            jTable1.getColumnModel().getColumn(8).setHeaderValue("p");
            jTable1.getColumnModel().getColumn(8).setCellRenderer(peopleTableRenderer1);
            jTable1.getColumnModel().getColumn(9).setHeaderValue("p");
            jTable1.getColumnModel().getColumn(9).setCellRenderer(peopleTableRenderer1);
            jTable1.getColumnModel().getColumn(10).setHeaderValue("Dormiente");
            jTable1.getColumnModel().getColumn(11).setHeaderValue("Keywords");
            jTable1.getColumnModel().getColumn(11).setCellRenderer(peopleTableRenderer1);
        }

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 835, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                .addContainerGap())
        );

        jSplitPane1.setTopComponent(jPanel1);

        jPanel2.setBackground(new Color(255, 255, 255));

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        jPanel3.setBackground(new Color(255, 255, 255));
        jPanel3.setBorder(BorderFactory.createTitledBorder(null, "Altri volontari disponibili", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Tahoma", 1, 12), new Color(0, 153, 51))); // NOI18N

        jTable2.setModel(peopleTableModel2);
        jTable2.setRowHeight(24);
        jScrollPane2.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setHeaderValue("Admin");
            jTable2.getColumnModel().getColumn(0).setCellRenderer(peopleTableRenderer2);
            jTable2.getColumnModel().getColumn(1).setHeaderValue("Volontari");
            jTable2.getColumnModel().getColumn(2).setHeaderValue("p");
            jTable2.getColumnModel().getColumn(2).setCellRenderer(peopleTableRenderer2);
            jTable2.getColumnModel().getColumn(3).setHeaderValue("p");
            jTable2.getColumnModel().getColumn(3).setCellRenderer(peopleTableRenderer2);
            jTable2.getColumnModel().getColumn(4).setHeaderValue("p");
            jTable2.getColumnModel().getColumn(4).setCellRenderer(peopleTableRenderer2);
            jTable2.getColumnModel().getColumn(5).setHeaderValue("p");
            jTable2.getColumnModel().getColumn(5).setCellRenderer(peopleTableRenderer2);
            jTable2.getColumnModel().getColumn(6).setHeaderValue("p");
            jTable2.getColumnModel().getColumn(6).setCellRenderer(peopleTableRenderer2);
            jTable2.getColumnModel().getColumn(7).setHeaderValue("p");
            jTable2.getColumnModel().getColumn(7).setCellRenderer(peopleTableRenderer2);
            jTable2.getColumnModel().getColumn(8).setHeaderValue("p");
            jTable2.getColumnModel().getColumn(8).setCellRenderer(peopleTableRenderer2);
            jTable2.getColumnModel().getColumn(9).setHeaderValue("p");
            jTable2.getColumnModel().getColumn(9).setCellRenderer(peopleTableRenderer2);
            jTable2.getColumnModel().getColumn(10).setHeaderValue("Dormiente");
            jTable2.getColumnModel().getColumn(11).setHeaderValue("Keywords");
            jTable2.getColumnModel().getColumn(11).setCellRenderer(peopleTableRenderer2);
        }

        GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, GroupLayout.DEFAULT_SIZE, 835, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane2, GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
                .addContainerGap())
        );

        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jToolBar2, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jSplitPane1.setRightComponent(jPanel2);

        jToolBar1.setBackground(new Color(255, 255, 255));
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jButton1.setBackground(new Color(255, 255, 255));
        jButton1.setIcon(new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/updateee32.png"))); // NOI18N
        jButton1.setText("Sostituisci");
        jButton1.setFocusable(false);
        jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel_activityName, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(246, 246, 246)
                        .addComponent(jToolBar1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSplitPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel_activityName)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jToolBar1, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Person person1 = this.peopleTableModel1.getPersonByRow(jTable1.getSelectedRow());
        Person person2 = this.peopleTableModel2.getPersonByRow(jTable2.getSelectedRow());
        this.peopleTableModel1.replacePerson(jTable1.getSelectedRow(), person2);
        this.peopleTableModel2.replacePerson(jTable2.getSelectedRow(), person1);
        this.jTable1.repaint();
        this.jTable2.repaint();
        this.jSplitPane1.invalidate();
        this.jSplitPane1.revalidate();
        this.turn.getComfirmedTurns().clear();
        List<ParsedAccount> datas = this.peopleTableModel1.getDatas();
        for (ParsedAccount pa : datas) {
            Person p = TrainerManager.getInstance().getPersonByID(pa.getAccount().getId());
            this.turn.getComfirmedTurns().add(new ComfirmedTurn(p, new JobTurn()));
        }

        this.repaint();
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton jButton1;
    private JButton jButton2;
    private JLabel jLabel1;
    private JLabel jLabel_activityName;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JPanel jPanel3;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JSeparator jSeparator1;
    private JSplitPane jSplitPane1;
    private JTable jTable1;
    private JTable jTable2;
    private JToolBar jToolBar1;
    private JToolBar jToolBar2;
    private PeopleTableModel peopleTableModel1;
    private PeopleTableModel peopleTableModel2;
    private PeopleTableRenderer peopleTableRenderer1;
    private PeopleTableRenderer peopleTableRenderer2;
    // End of variables declaration//GEN-END:variables

}
