/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.people;

import it.cnr.istc.sponsor.tt.gui.profiles.MiniSingleKeyPanel;
import it.cnr.istc.sponsor.tt.gui.profiles.mr.KeywordListModel;
import it.cnr.istc.sponsor.tt.logic.MQTTClient;
import it.cnr.istc.sponsor.tt.logic.MQTTListener;
import it.cnr.istc.sponsor.tt.logic.ParsedAccount;
import it.cnr.istc.sponsor.tt.logic.TrainerManager;
import it.cnr.istc.sponsor.tt.logic.model.ActivityName;
import it.cnr.istc.sponsor.tt.logic.model.Job;
import it.cnr.istc.sponsor.tt.logic.model.Keyword;
import it.cnr.istc.sponsor.tt.logic.model.Person;
import it.cnr.istc.sponsor.tt.logic.model.Project;
import it.cnr.istc.sponsor.tt.logic.model.Skill;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class PeopleKeywordDialog extends javax.swing.JDialog implements MQTTListener {

    private Keyword creatingKey = null;
    private Person person = null;
    private List<Keyword> allKeywords = new ArrayList<>();
    private Map<String, MiniSingleKeyPanel> panelMap = new HashMap<>();

    /**
     * Creates new form PeopleKeywordDialog
     */
    public PeopleKeywordDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        MQTTClient.getInstance().addMQTTListener(this);
        MQTTClient.getInstance().sendQueryToGetKeywords();
    }

    public PeopleKeywordDialog(java.awt.Frame parent, boolean modal, ParsedAccount pa) {
        super(parent, modal);
        initComponents();
        person = TrainerManager.getInstance().getPersonByID(pa.getAccount().getId());
        List<Keyword> keywords = person.getKeywords();
        if (keywords != null) {
            for (Keyword keyword : keywords) {
                System.out.println("ADDING PERSON KEY = " + keyword.getKeyword());
                MiniSingleKeyPanel miniSingleKeyPanel = new MiniSingleKeyPanel(false, keyword);
                miniSingleKeyPanel.setParent(this);
                this.panelMap.put(keyword.getKeyword(), miniSingleKeyPanel);
                this.jPanel_container.add(miniSingleKeyPanel);
            }
        }

        MQTTClient.getInstance().addMQTTListener(this);
        MQTTClient.getInstance().sendQueryToGetKeywords();
        this.jLabel_Person.setText(pa.getAccount().getName() + " " + pa.getAccount().getSurname());
    }

    public void removeKeyword(String key) {
        if (this.panelMap.containsKey(key)) {
            this.jPanel_container.remove(this.panelMap.get(key));
            Keyword toRemoveKey = null;
            this.panelMap.remove(key);
            System.out.println("KEY to remove -> " + key);
            for (Keyword k : allKeywords) {
                System.out.println("checking if " + k.getKeyword() + " == " + key);
                if (k.getKeyword().equals(key)) {
                    this.keywordListModel1.addElement(k);
                    toRemoveKey = k;
                    System.out.println("FOUND");
                    System.out.println("ADDED ->" + k.getKeyword());
                    break;

                }
                System.out.println("----------------------------");
            }
//            if(toRemoveKey != null){
//                this.allKeywords.add(toRemoveKey);
//            }
            this.jPanel_container.invalidate();
            this.jPanel_container.revalidate();
            this.repaint();
            System.out.println("NOTHING TO ADD!");

        }
    }

    public Person getPerson() {
        return person;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        keywordListModel1 = new KeywordListModel();
        jPanel1 = new JPanel();
        jPanel_mainKey = new JPanel();
        jScrollPane2 = new JScrollPane();
        jList1 = new JList<>();
        jLabel1 = new JLabel();
        jTextField_key = new JTextField();
        jButton_Inserisci = new JButton();
        jButton_Deve = new JButton();
        jLabel_K = new JLabel();
        jLabel3 = new JLabel();
        jPanel2 = new JPanel();
        jLabel_Person = new JLabel();
        jPanel_container = new JPanel();
        jButton1 = new JButton();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        jPanel_mainKey.setBackground(new Color(255, 255, 255));

        jScrollPane2.setBorder(null);

        jList1.setBorder(BorderFactory.createTitledBorder("Lista Keyword"));
        jList1.setModel(keywordListModel1);
        jScrollPane2.setViewportView(jList1);

        jLabel1.setText("Nuova Keyword:");

        jTextField_key.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        jTextField_key.setForeground(new Color(51, 51, 255));

        jButton_Inserisci.setBackground(new Color(255, 51, 51));
        jButton_Inserisci.setFont(new Font("Gill Sans MT Condensed", 0, 16)); // NOI18N
        jButton_Inserisci.setForeground(new Color(255, 255, 255));
        jButton_Inserisci.setText("<html><span style='font-size: 18px;'>I</span>NSERISCI");
        jButton_Inserisci.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton_Inserisci.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton_InserisciActionPerformed(evt);
            }
        });

        jButton_Deve.setBackground(new Color(255, 51, 51));
        jButton_Deve.setFont(new Font("Gill Sans MT Condensed", 0, 16)); // NOI18N
        jButton_Deve.setForeground(new Color(255, 255, 255));
        jButton_Deve.setText("<html><span style='font-size: 18px;'>A</span>SSEGNA KEYWORD >>");
        jButton_Deve.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton_Deve.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton_DeveActionPerformed(evt);
            }
        });

        jLabel_K.setFont(new Font("Tahoma", 1, 14)); // NOI18N
        jLabel_K.setText("Selezione Keyword");

        jLabel3.setIcon(new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/frciastrana80.png"))); // NOI18N

        GroupLayout jPanel_mainKeyLayout = new GroupLayout(jPanel_mainKey);
        jPanel_mainKey.setLayout(jPanel_mainKeyLayout);
        jPanel_mainKeyLayout.setHorizontalGroup(jPanel_mainKeyLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_mainKeyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_mainKeyLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel_K, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel_mainKeyLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 167, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel_mainKeyLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel_mainKeyLayout.createSequentialGroup()
                                .addGroup(jPanel_mainKeyLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel1)
                                    .addComponent(jTextField_key, GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                                    .addComponent(jButton_Deve, GroupLayout.PREFERRED_SIZE, 169, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton_Inserisci))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel_mainKeyLayout.setVerticalGroup(jPanel_mainKeyLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, jPanel_mainKeyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel_K)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel_mainKeyLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_mainKeyLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField_key, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_Inserisci, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                        .addGap(89, 89, 89)
                        .addComponent(jButton_Deve, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE))
                .addContainerGap())
        );

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_mainKey, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_mainKey, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new Color(255, 255, 255));

        jLabel_Person.setFont(new Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Person.setForeground(new Color(51, 51, 255));
        jLabel_Person.setText("Piero Pierino");

        jPanel_container.setBackground(new Color(255, 255, 255));
        jPanel_container.setBorder(BorderFactory.createTitledBorder("Keyword selezionate"));
        jPanel_container.setLayout(new GridLayout(10, 1));

        jButton1.setBackground(new Color(255, 51, 51));
        jButton1.setFont(new Font("Gill Sans MT Condensed", 0, 16)); // NOI18N
        jButton1.setForeground(new Color(255, 255, 255));
        jButton1.setText("<html><span style='font-size: 18px;'>O</span>K");
        jButton1.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel_container, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel_Person, GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                        .addGap(30, 30, 30))))
            .addGroup(GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel_Person)
                .addGap(18, 18, 18)
                .addComponent(jPanel_container, GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3))
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_InserisciActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton_InserisciActionPerformed

        String text = this.jTextField_key.getText();
        if (text.isEmpty()) {
            System.err.println("ERRORE STRINGA VUOTA");
            return;
        }
        creatingKey = new Keyword();
        creatingKey.setKeyword(text);
        MQTTClient.getInstance().createKeyword(text);
        //        this.keywordListModel1.addElement(key);
    }//GEN-LAST:event_jButton_InserisciActionPerformed

    private void jButton_DeveActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton_DeveActionPerformed
        List<Keyword> selectedValuesList = this.jList1.getSelectedValuesList();

        for (Keyword key : selectedValuesList) {
//            boolean toInstert = true;
//            for (Keyword kappa : allKeywords) {
//                if (kappa.getId().equals(key.getId())) {
//                    toInstert = false;
//                    break;
//                }
//            }
//            if (toInstert) {
            MiniSingleKeyPanel miniSingleKeyPanel = new MiniSingleKeyPanel(false, key);
            miniSingleKeyPanel.setParent(this);
//                allKeywords.remove(key);
            this.panelMap.put(key.getKeyword(), miniSingleKeyPanel);
            this.jPanel_container.add(miniSingleKeyPanel);
            this.keywordListModel1.removeElement(key);

        }
        this.jPanel_container.invalidate();
        this.jPanel_container.revalidate();
        this.repaint();
    }//GEN-LAST:event_jButton_DeveActionPerformed

    private void jButton1ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        List<Keyword> kappakey = new ArrayList<>();

        for (MiniSingleKeyPanel value : panelMap.values()) {
            kappakey.add(value.getKeyword());
        }
//        for (Keyword keyword : kappakey) {
//            if (this.person.getKeywords().contains(keyword)) {
//                this.person.getKeywords().add(keyword);
//            }
//        }
        this.person.getKeywords().clear();
        this.person.getKeywords().addAll(kappakey);

        MQTTClient.getInstance().updatePersonKeyword(this.person.getId(), kappakey);

        this.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    public List<Keyword> getSelectedKeywords() {
        List<Keyword> selectedValuesList = this.jList1.getSelectedValuesList();
        return selectedValuesList;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PeopleKeywordDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PeopleKeywordDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PeopleKeywordDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PeopleKeywordDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PeopleKeywordDialog dialog = new PeopleKeywordDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton jButton1;
    private JButton jButton_Deve;
    private JButton jButton_Inserisci;
    private JLabel jLabel1;
    private JLabel jLabel3;
    private JLabel jLabel_K;
    private JLabel jLabel_Person;
    private JList<Keyword> jList1;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JPanel jPanel_container;
    private JPanel jPanel_mainKey;
    private JScrollPane jScrollPane2;
    private JTextField jTextField_key;
    private KeywordListModel keywordListModel1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void peopleDataArrived(List<Person> people) {

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
    public void keywordCreated(Long id) {
        if (creatingKey == null) {
            System.out.println("not me");
            return;
        }
        creatingKey.setId(id);
        this.keywordListModel1.addElement(creatingKey);
    }

    @Override
    public void keywordsDataArrived(List<Keyword> keys) {
        this.allKeywords = keys;
        for (Keyword k : keys) {
            boolean toInsert = true;
            if (person != null) {
                for (Keyword keyword : person.getKeywords()) {
                    if (k.getId().equals(keyword.getId())) {
                        toInsert = false;
                        break;
                    }
                }
            } else {
                System.err.println("NO PERSON");
            }
            if (toInsert) {
                this.keywordListModel1.addElement(k);
            }
        }
    }

    @Override
    public void jobCreated(String nameJob, Long id) {

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
    public void userCreated(Person person) {
    }

}
