/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.profiles;

import com.google.gson.Gson;
import it.cnr.istc.i18n.annotations.I18N;
import it.cnr.istc.i18n.annotations.I18NUpdater;
import it.cnr.istc.i18n.translator.TranslatorManager;
import it.cnr.istc.sponsor.tt.gui.people.mr.PeopleTableModel;
import it.cnr.istc.sponsor.tt.gui.people.mr.PeopleTableRenderer;
import it.cnr.istc.sponsor.tt.logic.TrainerManager;
import it.cnr.istc.sponsor.tt.logic.model.Account;
import it.cnr.istc.sponsor.tt.logic.model.ActivityTurn;
import it.cnr.istc.sponsor.tt.logic.model.JobTurn;
import it.cnr.istc.sponsor.tt.logic.model.Person;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class DirectPersonSelectorDialog extends javax.swing.JDialog {

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
    private Person selectedPerson = null;
    private JobTurn jobTurn = null;
    
    /**
     * Creates new form DirectPersonSelectorFrame
     * @param turn
     * @param jobTurn
     */
    public DirectPersonSelectorDialog() {
        super(new JFrame(),true);
        initComponents();
        updateLabels();
        
        this.peopleTableRenderer1.setOpaque(true);
        ((JComponent) jTable1.getDefaultRenderer(Boolean.class)).setOpaque(true);
        jTable1.getColumnModel().getColumn(0).setMinWidth(55);
        jTable1.getColumnModel().getColumn(0).setMaxWidth(55);
        jTable1.getColumnModel().getColumn(1).setMinWidth(150);
       
        
    }
    
    public void init(ActivityTurn turn, JobTurn jobTurn){
        this.turn = turn;
        this.jobTurn = jobTurn;
         List<Person> people = TrainerManager.getInstance().getPeople();
        for (Person p : people) {
            boolean turnDoable = p.isTurnDoable(turn);
            if(!turnDoable){
                continue;
            }
            if(this.turn.getWantedPerson().contains(p)){
                continue;
            }
            p.fix();
            Account account = p.getAccount();
            account.setId(p.getId());
            this.peopleTableModel1.addRowElement(account);
        }
        System.out.println("                                            AAAAAAAAAAA: "+TranslatorManager.getInstance().getTranslation(peopleBrilliant));
        System.out.println("                                            AAAAAAAAAAA: "+peoplePlanner);
        System.out.println("                                            AAAAAAAAAAA: "+peopleBrilliant);
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
        System.out.println("UPDATED");
    }

    public JobTurn getJobTurn() {
        return jobTurn;
    }

    public Person getSelectedPerson() {
        return selectedPerson;
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
        jPanel1 = new JPanel();
        jScrollPane1 = new JScrollPane();
        jTable1 = new JTable();
        jLabel1 = new JLabel();
        jTextField1 = new JTextField();
        jButton_Seleziona = new JButton();
        jButton_Annulla = new JButton();

        peopleTableRenderer1.setText("peopleTableRenderer1");

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new Color(255, 255, 255));

        jTable1.setModel(peopleTableModel1);
        jTable1.setRowHeight(24);
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable1.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent evt) {
                jTable1MouseReleased(evt);
            }
        });
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

        jLabel1.setFont(new Font("Gill Sans MT Condensed", 0, 16)); // NOI18N
        jLabel1.setText("<html><span style='font-size: 18px;'>C</span>ERCA");

        jButton_Seleziona.setBackground(new Color(255, 51, 51));
        jButton_Seleziona.setFont(new Font("Gill Sans MT Condensed", 0, 16)); // NOI18N
        jButton_Seleziona.setForeground(new Color(255, 255, 255));
        jButton_Seleziona.setText("<html><span style='font-size: 18px;'>S</span>ELEZIONA");
        jButton_Seleziona.setEnabled(false);
        jButton_Seleziona.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton_SelezionaActionPerformed(evt);
            }
        });

        jButton_Annulla.setBackground(new Color(255, 51, 51));
        jButton_Annulla.setFont(new Font("Gill Sans MT Condensed", 0, 16)); // NOI18N
        jButton_Annulla.setForeground(new Color(255, 255, 255));
        jButton_Annulla.setText("<html><span style='font-size: 18px;'>A</span>NNULLA");
        jButton_Annulla.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton_AnnullaActionPerformed(evt);
            }
        });

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 613, Short.MAX_VALUE)
                    .addGroup(GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton_Annulla, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton_Seleziona, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_Seleziona, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_Annulla, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_AnnullaActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton_AnnullaActionPerformed
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_jButton_AnnullaActionPerformed

    private void jButton_SelezionaActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton_SelezionaActionPerformed
        this.selectedPerson = this.peopleTableModel1.getPersonByRow(this.jTable1.getSelectedRow());
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_jButton_SelezionaActionPerformed

    private void jTable1MouseReleased(MouseEvent evt) {//GEN-FIRST:event_jTable1MouseReleased
        this.jButton_Seleziona.setEnabled(true);
    }//GEN-LAST:event_jTable1MouseReleased

 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton jButton_Annulla;
    private JButton jButton_Seleziona;
    private JLabel jLabel1;
    private JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private JTable jTable1;
    private JTextField jTextField1;
    private PeopleTableModel peopleTableModel1;
    private PeopleTableRenderer peopleTableRenderer1;
    // End of variables declaration//GEN-END:variables
}
