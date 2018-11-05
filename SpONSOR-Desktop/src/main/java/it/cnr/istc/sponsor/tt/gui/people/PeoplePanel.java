/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.people;

import com.google.gson.Gson;
import it.cnr.istc.i18n.annotations.I18N;
import it.cnr.istc.i18n.annotations.I18NUpdater;
import it.cnr.istc.sponsor.tt.abstracts.SubMainPanel;
import it.cnr.istc.sponsor.tt.gui.activities.edit.ActivityNameListModel;
import it.cnr.istc.sponsor.tt.gui.engine.GuiEventListener;
import it.cnr.istc.sponsor.tt.gui.engine.GuiEventManager;
import it.cnr.istc.sponsor.tt.gui.people.mr.PeopleTableModel;
import it.cnr.istc.sponsor.tt.gui.people.mr.PeopleTableRenderer;
import it.cnr.istc.sponsor.tt.logic.MQTTClient;
import it.cnr.istc.sponsor.tt.logic.MQTTListener;
import it.cnr.istc.sponsor.tt.logic.ParsedAccount;
import it.cnr.istc.sponsor.tt.logic.RegistrationListener;
import it.cnr.istc.sponsor.tt.logic.SettingsManager;
import it.cnr.istc.sponsor.tt.logic.TrainerManager;
import it.cnr.istc.sponsor.tt.logic.excel.ExcelWriter;
import it.cnr.istc.sponsor.tt.logic.model.Account;
import it.cnr.istc.sponsor.tt.logic.model.Activity;
import it.cnr.istc.sponsor.tt.logic.model.ActivityName;
import it.cnr.istc.sponsor.tt.logic.model.ActivityTurn;
import it.cnr.istc.sponsor.tt.logic.model.Interval;
import it.cnr.istc.sponsor.tt.logic.model.Job;
import it.cnr.istc.sponsor.tt.logic.model.Keyword;
import it.cnr.istc.sponsor.tt.logic.model.Person;
import it.cnr.istc.sponsor.tt.logic.model.Project;
import it.cnr.istc.sponsor.tt.logic.model.Skill;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.Beans;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.LayoutStyle;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class PeoplePanel extends javax.swing.JPanel implements RegistrationListener, MQTTListener, SubMainPanel, GuiEventListener {

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
    private List<Account> accountFasulli = new ArrayList<>();

    private boolean modifica = false;

    private Icon modifyIcon = new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/iEngrenages24.png"));
    private Icon saveIcon = new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/save24.png"));

    private Border editBorder = null;
    private Border noBorder = null;

    private boolean propertyShown = true;

    /**
     * Creates new form PeoplePanel
     */
    public PeoplePanel() {
        initComponents();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        JOptionPane.showMessageDialog(null, "" + (int) (screenSize.width));
//        JOptionPane.showMessageDialog(null, "" + (int) (screenSize.width - jPanel1.getPreferredSize().getWidth()));
        this.jSplitPane1.setDividerLocation((int) (screenSize.width - jPanel_property.getPreferredSize().getWidth()));

        updateLabels();
        if (!Beans.isDesignTime()) {
            TrainerManager.getInstance().addRegistrationListener(this);
            GuiEventManager.getInstance().addGuiEventListener(this);
        }
        MQTTClient.getInstance().addMQTTListener(this);
        this.peopleTableRenderer1.setOpaque(true);
//        ((JComponent) jTable1.getDefaultRenderer(Boolean.class)).setOpaque(true);
        jTable1.getColumnModel().getColumn(0).setMinWidth(55);
        jTable1.getColumnModel().getColumn(0).setMaxWidth(55);
        jTable1.getColumnModel().getColumn(1).setMinWidth(120);
        int width = (int) this.jSplitPane1.getPreferredSize().getWidth();
        System.out.println("W I D H T  ===============> " + width);
        this.jSplitPane1.setDividerLocation(880);
        this.noBorder = this.jTextField_nome.getBorder();
        this.editBorder = this.jTextField1.getBorder();
        this.jButtonTest.setVisible(SettingsManager.getInstance().isPopola());

        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        ListSelectionModel selectionModel = jTable1.getSelectionModel();

        selectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                handleSelectionEvent(e);
            }
        });

    }

    protected void handleSelectionEvent(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            System.out.println("adjusting ? ");
            return;
        }

        // e.getSource() returns an object like this
        // javax.swing.DefaultListSelectionModel 1052752867 ={11}
        // where 11 is the index of selected element when mouse button is released
        String strSource = e.getSource().toString();
//        int start = strSource.indexOf("{") + 1,
//                stop = strSource.length() - 1;
//        int index = Integer.parseInt(strSource.substring(start, stop));
        int index = jTable1.getSelectedRow();
        System.out.println("index = " + index);
        if (index != -1) {
            this.jToggleButton_modifica.setEnabled(true);
            this.jButton_freeTime.setEnabled(true);
            ParsedAccount pa = this.peopleTableModel1.getDatas().get(index);
            Person person = TrainerManager.getInstance().getPersonByID(pa.getAccount().getId());
            this.jTextField_code.setText(person.getCode());
            this.jTextField_email.setText(person.getEmail());
            this.jTextField_phone.setText(person.getPhone());
            this.jTextField_cognome.setText(person.getSurname());
            this.jTextField_nome.setText(person.getName());
            this.jCheckBox_Admin.setSelected(person.isAdmin());
            this.jCheckBox1.setSelected(person.isOneTurnPerDay());
            this.jSpinner1.setValue(person.getMaxTurnPerWeek());
            List<ActivityName> onlyTheseActivities = person.getOnlyTheseActivities();
            this.activityNameListModel1.clear();
            for (ActivityName onlyTheseActivity : onlyTheseActivities) {
                this.activityNameListModel1.addElement(onlyTheseActivity);
            }
        }
    }

    @I18NUpdater
    public final void updateLabels() {
        jTable1.getColumnModel().getColumn(1).setHeaderValue(profiles);
        jTable1.getColumnModel().getColumn(2).setHeaderValue(peopleConcrete);
        jTable1.getColumnModel().getColumn(3).setHeaderValue(peopleLeader);
        jTable1.getColumnModel().getColumn(4).setHeaderValue(peoplePlanner);
        jTable1.getColumnModel().getColumn(5).setHeaderValue(peopleBrilliant);
        jTable1.getColumnModel().getColumn(6).setHeaderValue(peopleExplorer);
        jTable1.getColumnModel().getColumn(7).setHeaderValue(peopleEvaluator);
        jTable1.getColumnModel().getColumn(8).setHeaderValue(peopleWorker);
        jTable1.getColumnModel().getColumn(9).setHeaderValue(peopleObjective);
        jTable1.getTableHeader().repaint();
        jTable1.repaint();
    }

    public void initPanel() {
        try {
            MQTTClient.getInstance().publish("nonabbozzo", "fai schifo");

        } catch (MqttException ex) {
            Logger.getLogger(PeoplePanel.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        initSearchFild();
        MQTTClient.getInstance().sendQueryToGetPeople();
        MQTTClient.getInstance().sendQueryToGetActivities();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.jSplitPane1.setDividerLocation((int) (screenSize.width - jPanel_property.getPreferredSize().getWidth()) - 30);

//        RowFilter<PeopleTableModel, String> filter = new RowFilter<PeopleTableModel, String>() {
//            @Override
//            public boolean include(RowFilter.Entry<? extends PeopleTableModel, ? extends String> entry) {
//            
//                
//            }
//        }; 
//        {
//            public boolean include(Entry entry) {
//                Integer population = (Integer) entry.getValue(1);
//                return population.intValue() > 3;
//            }
//        };
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
        jButton_removeUser = new JButton();
        jButton_addUser = new JButton();
        peopleTableRenderer1 = new PeopleTableRenderer();
        jButton_keywords = new JButton();
        jTextField1 = new JTextField();
        activityNameListModel1 = new ActivityNameListModel();
        jButtonTest = new JButton();
        jButton_excels = new JButton();
        jPanel_search = new JPanel();
        jTextField_search = new JTextField();
        jLabel7 = new JLabel();
        jSeparator5 = new JSeparator();
        filler1 = new Box.Filler(new Dimension(5, 5), new Dimension(5, 5), new Dimension(5, 5));
        closedPropertiesPanel1 = new ClosedPropertiesPanel();
        jSplitPane1 = new JSplitPane();
        jPanel_property = new JPanel();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jSeparator1 = new JSeparator();
        jLabel3 = new JLabel();
        jLabel4 = new JLabel();
        jTextField_email = new JTextField();
        jTextField_code = new JTextField();
        jLabel5 = new JLabel();
        jTextField_phone = new JTextField();
        jTextField_nome = new JTextField();
        jTextField_cognome = new JTextField();
        jSeparator2 = new JSeparator();
        jButton_freeTime = new JButton();
        jToggleButton_modifica = new JToggleButton();
        jButton_annulla = new JButton();
        jSeparator3 = new JSeparator();
        jCheckBox1 = new JCheckBox();
        jSpinner1 = new JSpinner();
        jLabel6 = new JLabel();
        jScrollPane1 = new JScrollPane();
        jList1 = new JList<>();
        jButton_manageAct = new JButton();
        jSeparator4 = new JSeparator();
        jCheckBox_Admin = new JCheckBox();
        jButton_eliminaAct = new JButton();
        jButton_Note = new JButton();
        jScrollPane2 = new JScrollPane();
        jTable1 = new JTable();

        jButton_removeUser.setIcon(new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/list_remove_user32.png"))); // NOI18N
        jButton_removeUser.setText("Rimuovi Volontario");
        jButton_removeUser.setHorizontalTextPosition(SwingConstants.CENTER);
        jButton_removeUser.setVerticalTextPosition(SwingConstants.BOTTOM);
        jButton_removeUser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton_removeUserActionPerformed(evt);
            }
        });

        jButton_addUser.setIcon(new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/list_add_user32.png"))); // NOI18N
        jButton_addUser.setText("Aggiungi Volontario");
        jButton_addUser.setHorizontalTextPosition(SwingConstants.CENTER);
        jButton_addUser.setVerticalTextPosition(SwingConstants.BOTTOM);
        jButton_addUser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton_addUserActionPerformed(evt);
            }
        });

        peopleTableRenderer1.setText("peopleTableRenderer1");

        jButton_keywords.setIcon(new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/keyword32.png"))); // NOI18N
        jButton_keywords.setText("Keywords");
        jButton_keywords.setHorizontalTextPosition(SwingConstants.CENTER);
        jButton_keywords.setVerticalTextPosition(SwingConstants.BOTTOM);
        jButton_keywords.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton_keywordsActionPerformed(evt);
            }
        });

        jTextField1.setText("jTextField1");

        jButtonTest.setText("Popola");
        jButtonTest.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButtonTestActionPerformed(evt);
            }
        });

        jButton_excels.setIcon(new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/excel_32.png"))); // NOI18N
        jButton_excels.setText("Genera Excel");
        jButton_excels.setHorizontalTextPosition(SwingConstants.CENTER);
        jButton_excels.setVerticalTextPosition(SwingConstants.BOTTOM);
        jButton_excels.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton_excelsActionPerformed(evt);
            }
        });

        jPanel_search.setBackground(new Color(255, 255, 255));
        jPanel_search.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent evt) {
                jPanel_searchMouseReleased(evt);
            }
        });

        jTextField_search.setFont(new Font("Tahoma", 2, 12)); // NOI18N
        jTextField_search.setForeground(new Color(51, 153, 255));
        jTextField_search.setText("<Cerca un volontario>");
        jTextField_search.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                jTextField_searchMousePressed(evt);
            }
        });
        jTextField_search.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                jTextField_searchKeyPressed(evt);
            }
        });

        jLabel7.setIcon(new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/search32.png"))); // NOI18N

        GroupLayout jPanel_searchLayout = new GroupLayout(jPanel_search);
        jPanel_search.setLayout(jPanel_searchLayout);
        jPanel_searchLayout.setHorizontalGroup(jPanel_searchLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, jPanel_searchLayout.createSequentialGroup()
                .addComponent(jLabel7)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField_search, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );
        jPanel_searchLayout.setVerticalGroup(jPanel_searchLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jLabel7, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jTextField_search)
        );

        jSeparator5.setOrientation(SwingConstants.VERTICAL);

        closedPropertiesPanel1.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent evt) {
                closedPropertiesPanel1MouseReleased(evt);
            }
        });

        setBackground(new Color(255, 255, 255));
        addAncestorListener(new AncestorListener() {
            public void ancestorMoved(AncestorEvent evt) {
            }
            public void ancestorAdded(AncestorEvent evt) {
                formAncestorAdded(evt);
            }
            public void ancestorRemoved(AncestorEvent evt) {
            }
        });

        jSplitPane1.setDividerLocation(900);
        jSplitPane1.setResizeWeight(1.0);

        jPanel_property.setBackground(new Color(255, 255, 255));
        jPanel_property.setBorder(BorderFactory.createTitledBorder("Dettagli Persona"));
        jPanel_property.setMaximumSize(new Dimension(383, 32767));
        jPanel_property.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent evt) {
                jPanel_propertyMouseReleased(evt);
            }
        });

        jLabel1.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel1.setText("Nome:");

        jLabel2.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel2.setText("Cognome:");

        jLabel3.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel3.setText("Codice:");

        jLabel4.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel4.setText("Email:");

        jTextField_email.setEditable(false);
        jTextField_email.setBackground(new Color(255, 255, 255));
        jTextField_email.setFont(new Font("Tahoma", 0, 12)); // NOI18N
        jTextField_email.setForeground(new Color(255, 0, 0));
        jTextField_email.setBorder(null);

        jTextField_code.setEditable(false);
        jTextField_code.setBackground(new Color(255, 255, 255));
        jTextField_code.setFont(new Font("Tahoma", 1, 12)); // NOI18N
        jTextField_code.setForeground(new Color(255, 0, 0));
        jTextField_code.setBorder(null);

        jLabel5.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel5.setText("Telefono:");

        jTextField_phone.setEditable(false);
        jTextField_phone.setBackground(new Color(255, 255, 255));
        jTextField_phone.setFont(new Font("Tahoma", 0, 12)); // NOI18N
        jTextField_phone.setForeground(new Color(255, 0, 0));
        jTextField_phone.setBorder(null);

        jTextField_nome.setEditable(false);
        jTextField_nome.setBackground(new Color(255, 255, 255));
        jTextField_nome.setFont(new Font("Tahoma", 1, 12)); // NOI18N
        jTextField_nome.setForeground(new Color(51, 51, 255));
        jTextField_nome.setBorder(null);

        jTextField_cognome.setEditable(false);
        jTextField_cognome.setBackground(new Color(255, 255, 255));
        jTextField_cognome.setFont(new Font("Tahoma", 1, 12)); // NOI18N
        jTextField_cognome.setForeground(new Color(51, 51, 255));
        jTextField_cognome.setBorder(null);

        jButton_freeTime.setBackground(new Color(255, 51, 51));
        jButton_freeTime.setFont(new Font("Gill Sans MT Condensed", 0, 16)); // NOI18N
        jButton_freeTime.setForeground(new Color(255, 255, 255));
        jButton_freeTime.setIcon(new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/weekly24.png"))); // NOI18N
        jButton_freeTime.setText("<html><span style='font-size: 18px;'>D</span>ISPONIBILITA'");
        jButton_freeTime.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton_freeTime.setEnabled(false);
        jButton_freeTime.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton_freeTimeActionPerformed(evt);
            }
        });

        jToggleButton_modifica.setBackground(new Color(255, 51, 51));
        jToggleButton_modifica.setFont(new Font("Gill Sans MT Condensed", 0, 16)); // NOI18N
        jToggleButton_modifica.setForeground(new Color(255, 255, 255));
        jToggleButton_modifica.setIcon(new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/iEngrenages24.png"))); // NOI18N
        jToggleButton_modifica.setText("<html><span style='font-size: 18px;'>M</span>ODIFICA");
        jToggleButton_modifica.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jToggleButton_modifica.setEnabled(false);
        jToggleButton_modifica.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jToggleButton_modificaActionPerformed(evt);
            }
        });

        jButton_annulla.setBackground(new Color(255, 51, 51));
        jButton_annulla.setFont(new Font("Gill Sans MT Condensed", 0, 16)); // NOI18N
        jButton_annulla.setForeground(new Color(255, 255, 255));
        jButton_annulla.setIcon(new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/cancel24.png"))); // NOI18N
        jButton_annulla.setText("<html><span style='font-size: 18px;'>A</span>NNULLA");
        jButton_annulla.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton_annulla.setEnabled(false);
        jButton_annulla.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton_annullaActionPerformed(evt);
            }
        });

        jCheckBox1.setBackground(new Color(255, 255, 255));
        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Un turno al giorno");
        jCheckBox1.setEnabled(false);

        jSpinner1.setModel(new SpinnerNumberModel(1, 0, 50, 1));
        jSpinner1.setEnabled(false);

        jLabel6.setText("Max turni/settimana");

        jScrollPane1.setBorder(null);

        jList1.setBorder(BorderFactory.createTitledBorder("Attività che si vogliono svolgere"));
        jList1.setModel(activityNameListModel1);
        jScrollPane1.setViewportView(jList1);

        jButton_manageAct.setBackground(new Color(255, 51, 51));
        jButton_manageAct.setFont(new Font("Gill Sans MT Condensed", 0, 16)); // NOI18N
        jButton_manageAct.setForeground(new Color(255, 255, 255));
        jButton_manageAct.setText("<html><span style='font-size: 18px;'>A</span>GGIUNGI ATTIVITA'");
        jButton_manageAct.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton_manageAct.setEnabled(false);
        jButton_manageAct.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton_manageActActionPerformed(evt);
            }
        });

        jCheckBox_Admin.setBackground(new Color(255, 255, 255));
        jCheckBox_Admin.setText("Admin");
        jCheckBox_Admin.setEnabled(false);

        jButton_eliminaAct.setBackground(new Color(255, 51, 51));
        jButton_eliminaAct.setFont(new Font("Gill Sans MT Condensed", 0, 16)); // NOI18N
        jButton_eliminaAct.setForeground(new Color(255, 255, 255));
        jButton_eliminaAct.setText("<html><span style='font-size: 18px;'>E</span>LIMINA ATTIVITA'");
        jButton_eliminaAct.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton_eliminaAct.setEnabled(false);
        jButton_eliminaAct.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton_eliminaActActionPerformed(evt);
            }
        });

        jButton_Note.setBackground(new Color(255, 51, 51));
        jButton_Note.setFont(new Font("Gill Sans MT Condensed", 0, 16)); // NOI18N
        jButton_Note.setForeground(new Color(255, 255, 255));
        jButton_Note.setIcon(new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/postitBad24.png"))); // NOI18N
        jButton_Note.setText("<html><span style='font-size: 18px;'>N</span>OTE");
        jButton_Note.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton_Note.setEnabled(false);
        jButton_Note.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton_NoteActionPerformed(evt);
            }
        });

        GroupLayout jPanel_propertyLayout = new GroupLayout(jPanel_property);
        jPanel_property.setLayout(jPanel_propertyLayout);
        jPanel_propertyLayout.setHorizontalGroup(jPanel_propertyLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_propertyLayout.createSequentialGroup()
                .addGroup(jPanel_propertyLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel_propertyLayout.createSequentialGroup()
                        .addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField_code))
                    .addGroup(jPanel_propertyLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel_propertyLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel_propertyLayout.createSequentialGroup()
                                .addGroup(jPanel_propertyLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2, GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel_propertyLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField_cognome, GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                                    .addComponent(jTextField_nome)))
                            .addComponent(jCheckBox1)
                            .addGroup(jPanel_propertyLayout.createSequentialGroup()
                                .addComponent(jSpinner1, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6))
                            .addComponent(jCheckBox_Admin)
                            .addGroup(jPanel_propertyLayout.createSequentialGroup()
                                .addComponent(jButton_freeTime, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_Note, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel_propertyLayout.createSequentialGroup()
                                .addGroup(jPanel_propertyLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel5, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel_propertyLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField_phone, GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                                    .addComponent(jTextField_email)))
                            .addGroup(jPanel_propertyLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jSeparator2, GroupLayout.Alignment.LEADING)
                                .addComponent(jSeparator3, GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane1, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addGroup(jPanel_propertyLayout.createSequentialGroup()
                                    .addComponent(jButton_manageAct, GroupLayout.PREFERRED_SIZE, 111, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButton_eliminaAct, GroupLayout.PREFERRED_SIZE, 111, GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel_propertyLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jSeparator4, GroupLayout.Alignment.LEADING)
                                .addGroup(GroupLayout.Alignment.LEADING, jPanel_propertyLayout.createSequentialGroup()
                                    .addComponent(jButton_annulla, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
                                    .addGap(26, 26, 26)
                                    .addComponent(jToggleButton_modifica, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE, 225, GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 0, 0))
        );
        jPanel_propertyLayout.setVerticalGroup(jPanel_propertyLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_propertyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_propertyLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_propertyLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2))
                    .addGroup(jPanel_propertyLayout.createSequentialGroup()
                        .addComponent(jTextField_nome, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField_cognome, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel_propertyLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_propertyLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5))
                    .addGroup(jPanel_propertyLayout.createSequentialGroup()
                        .addComponent(jTextField_email, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField_phone, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel_propertyLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField_code, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox_Admin)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox1)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel_propertyLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinner1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel_propertyLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_freeTime, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_Note, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel_propertyLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_manageAct, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_eliminaAct, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator4, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel_propertyLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_annulla, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToggleButton_modifica, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)))
        );

        jSplitPane1.setRightComponent(jPanel_property);

        jTable1.setModel(peopleTableModel1);
        jTable1.setRowHeight(24);
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable1.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent evt) {
                jTable1MouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setHeaderValue("Admin");
            jTable1.getColumnModel().getColumn(0).setCellRenderer(peopleTableRenderer1);
            jTable1.getColumnModel().getColumn(1).setHeaderValue("Volontari");
            jTable1.getColumnModel().getColumn(1).setCellRenderer(peopleTableRenderer1);
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
            jTable1.getColumnModel().getColumn(10).setCellRenderer(peopleTableRenderer1);
            jTable1.getColumnModel().getColumn(11).setHeaderValue("Keywords");
            jTable1.getColumnModel().getColumn(11).setCellRenderer(peopleTableRenderer1);
        }

        jSplitPane1.setLeftComponent(jScrollPane2);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, GroupLayout.DEFAULT_SIZE, 869, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 563, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_removeUserActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton_removeUserActionPerformed
        int selectedRow = this.jTable1.getSelectedRow();
        if (selectedRow != -1) {
            ParsedAccount account = this.peopleTableModel1.getDatas().get(selectedRow);
            int answer = JOptionPane.showConfirmDialog(null, "Vuoi veramente cancellare l'account di: " + account.getAccount().getName() + " " + account.getAccount().getSurname(), "Conferma Cancellazione", JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                MQTTClient.getInstance().deleteUser(account.getAccount().getId());
            }
        }
    }//GEN-LAST:event_jButton_removeUserActionPerformed

    private void jButton_addUserActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton_addUserActionPerformed
        NewPeopleFrame npf = new NewPeopleFrame();
        npf.setLocationRelativeTo(null);
        npf.setVisible(true);
    }//GEN-LAST:event_jButton_addUserActionPerformed

    private void jButton_keywordsActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton_keywordsActionPerformed
        int selectedRow = this.jTable1.getSelectedRow();
        if (selectedRow != -1) {
            ParsedAccount person = this.peopleTableModel1.getDatas().get(selectedRow);
            PeopleKeywordDialog dialog = new PeopleKeywordDialog(new JFrame(), true, person);
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
            this.peopleTableModel1.update();
        }


    }//GEN-LAST:event_jButton_keywordsActionPerformed

    private void jTable1MouseReleased(MouseEvent evt) {//GEN-FIRST:event_jTable1MouseReleased
        int selectedRow = this.jTable1.convertRowIndexToModel(this.jTable1.getSelectedRow());

        if (selectedRow != -1) {
            this.jToggleButton_modifica.setEnabled(true);
            this.jButton_freeTime.setEnabled(true);
            ParsedAccount pa = this.peopleTableModel1.getDatas().get(selectedRow);
            Person person = TrainerManager.getInstance().getPersonByID(pa.getAccount().getId());
            this.jTextField_code.setText(person.getCode());
            this.jTextField_email.setText(person.getEmail());
            this.jTextField_phone.setText(person.getPhone());
            this.jTextField_cognome.setText(person.getSurname());
            this.jTextField_nome.setText(person.getName());
            this.jCheckBox_Admin.setSelected(person.isAdmin());
            this.jCheckBox1.setSelected(person.isOneTurnPerDay());
            this.jSpinner1.setValue(person.getMaxTurnPerWeek());
            List<ActivityName> onlyTheseActivities = person.getOnlyTheseActivities();
            this.activityNameListModel1.clear();
            for (ActivityName onlyTheseActivity : onlyTheseActivities) {
                this.activityNameListModel1.addElement(onlyTheseActivity);
            }
        }
    }//GEN-LAST:event_jTable1MouseReleased

    private void jToggleButton_modificaActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jToggleButton_modificaActionPerformed

        if (modifica) {
            int selectedRow = this.jTable1.getSelectedRow();
            if (selectedRow != -1) {
                ParsedAccount pa = this.peopleTableModel1.getDatas().get(selectedRow);
                System.out.println("MODIFICA PA NOTE: "+pa.getAccount().getNote());
                Person person = TrainerManager.getInstance().getPersonByID(pa.getAccount().getId());
                person.setId(pa.getAccount().getId());
                person.getAccount().setId(pa.getAccount().getId());
                person.getAccount().setAdmin(jCheckBox_Admin.isSelected());
                person.setName(this.jTextField_nome.getText());
                person.setSurname(this.jTextField_cognome.getText());
                person.setAdmin(jCheckBox_Admin.isSelected());
//                person.setCode(this.jTextField_code.getText());
                person.setEmail(this.jTextField_email.getText());
                person.setPhone(this.jTextField_phone.getText());
                person.setOneTurnPerDay(this.jCheckBox1.isSelected());
                person.setMaxTurnPerWeek((int) this.jSpinner1.getValue());
                person.setOnlyTheseActivities(this.activityNameListModel1.values());
                person.setNote(pa.getAccount().getNote());
//                person.getAccount().setMaxTurnPerWeek(person.getMaxTurnPerWeek());
                MQTTClient.getInstance().editPerson(person);
            }
        }
        this.modifica = !this.modifica;
        this.jToggleButton_modifica.setText(modifica ? "<html><span style='font-size: 18px;'>S</span>ALVA" : "<html><span style='font-size: 18px;'>M</span>ODIFICA");
        this.jToggleButton_modifica.setIcon(modifica ? saveIcon : modifyIcon);
        this.jButton_annulla.setEnabled(modifica);
        this.jButton_manageAct.setEnabled(modifica);
        this.jCheckBox_Admin.setEnabled(modifica);
        this.jButton_eliminaAct.setEnabled(modifica);
        this.jButton_Note.setEnabled(modifica);

        this.jTextField_nome.setBorder(modifica ? editBorder : noBorder);
        this.jTextField_cognome.setBorder(modifica ? editBorder : noBorder);
//        this.jTextField_code.setBorder(modifica ? editBorder : noBorder);
        this.jTextField_email.setBorder(modifica ? editBorder : noBorder);
        this.jTextField_phone.setBorder(modifica ? editBorder : noBorder);

        this.jTextField_nome.setEditable(modifica);
        this.jTextField_cognome.setEditable(modifica);
//        this.jTextField_code.setEditable(modifica);
        this.jTextField_email.setEditable(modifica);
        this.jTextField_phone.setEditable(modifica);
        this.jCheckBox1.setEnabled(modifica);
        this.jSpinner1.setEnabled(modifica);

    }//GEN-LAST:event_jToggleButton_modificaActionPerformed

    private void jButton_annullaActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton_annullaActionPerformed
        modifica = false;
        this.jButton_annulla.setEnabled(false);
        this.jToggleButton_modifica.setText(modifica ? "<html><span style='font-size: 18px;'>S</span>ALVA" : "<html><span style='font-size: 18px;'>M</span>ODIFICA");
        this.jToggleButton_modifica.setSelected(false);
        this.jButton_eliminaAct.setEnabled(false);

        this.jToggleButton_modifica.setIcon(modifica ? saveIcon : modifyIcon);
        this.jTextField_nome.setBorder(modifica ? editBorder : noBorder);
        this.jTextField_cognome.setBorder(modifica ? editBorder : noBorder);
//        this.jTextField_code.setBorder(modifica ? editBorder : noBorder);
        this.jTextField_email.setBorder(modifica ? editBorder : noBorder);
        this.jTextField_phone.setBorder(modifica ? editBorder : noBorder);

        this.jTextField_nome.setEditable(modifica);
        this.jCheckBox_Admin.setEnabled(modifica);
        this.jTextField_cognome.setEditable(modifica);
//        this.jTextField_code.setEditable(modifica);
        this.jTextField_email.setEditable(modifica);
        this.jTextField_phone.setEditable(modifica);
        this.jCheckBox1.setEnabled(modifica);
        this.jSpinner1.setEnabled(modifica);
        this.jButton_manageAct.setEnabled(modifica);
        this.jButton_Note.setEnabled(modifica);
    }//GEN-LAST:event_jButton_annullaActionPerformed

    private void jButton_freeTimeActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton_freeTimeActionPerformed
        int selectedRow = this.jTable1.getSelectedRow();
        if (selectedRow != -1) {
            ParsedAccount pa = this.peopleTableModel1.getDatas().get(selectedRow);
            Person person = TrainerManager.getInstance().getPersonByID(pa.getAccount().getId());
            FreeTimeViewer vi = new FreeTimeViewer(person);
            vi.setLocationRelativeTo(null);
            vi.setVisible(true);
        }
    }//GEN-LAST:event_jButton_freeTimeActionPerformed

    private void jButton_manageActActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton_manageActActionPerformed
        int selectedRow = this.jTable1.getSelectedRow();
        System.out.println("SELECTED ROW = " + selectedRow);
        if (selectedRow != -1) {

            Enumeration<ActivityName> elements = this.activityNameListModel1.elements();
            List<ActivityName> list = new ArrayList();
            System.out.println("THIS PERSON HAS: ");
            while (elements.hasMoreElements()) {
                ActivityName nextElement = elements.nextElement();
                System.out.println("\tact name: " + nextElement.getName());
                list.add(nextElement);
            }
            System.out.println("LIST SIZE = " + list.size());
            List<ActivityName> remaining = TrainerManager.getInstance().getActivityNames();
            for (ActivityName activityName : list) {
                System.out.println("attempt to remove: " + activityName);
                remaining.remove(activityName);
            }
            for (ActivityName activityName : remaining) {
                System.out.println("remaining act name: " + activityName.getName());
            }
            OnlyTheseActivityEditDialog dialog = new OnlyTheseActivityEditDialog(new JFrame(), true, remaining);
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
            List<ActivityName> activityNames = dialog.getActivityNames();
            if (activityNames != null) {
                for (ActivityName activityName : activityNames) {
//                    if (!list.contains(activityName)) {
                    this.activityNameListModel1.addElement(activityName);
//                    }
                }
            }
        }
    }//GEN-LAST:event_jButton_manageActActionPerformed

    private void formAncestorAdded(AncestorEvent evt) {//GEN-FIRST:event_formAncestorAdded


    }//GEN-LAST:event_formAncestorAdded

    private void jButtonTestActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButtonTestActionPerformed

        createFasullo(false, 14, 19, 0, 2, 21, 0, 2, 6, "Alberico", " ", null, "", "", false, 1, true);
        createFasullo(false, 1, 6, 11, 3, 15, 5, 24, 5, "Flavia", "-", null, "", "", false, 1, true);
        createFasullo(false, 14, 0, 8, 9, 11, 7, 22, 2, "Maria", "1", null, "", "", false, 1, true);
        createFasullo(false, 6, 13, 9, 9, 14, 5, 4, 10, "Ornella", "-", null, "", "", false, 1, true);
        createFasullo(false, 10, 24, 6, 0, 3, 2, 18, 4, "Anna", "P.", null, "", "", false, 1, true);
        createFasullo(false, 6, 7, 3, 5, 10, 4, 33, 2, "Angela", "-", null, "", "", false, 1, true);
        createFasullo(false, 4, 3, 0, 7, 18, 5, 28, 2, "Lia", "-", null, "", "", false, 1, true);
        createFasullo(false, 2, 0, 3, 5, 17, 3, 32, 1, "Carla", "-", null, "", "", false, 1, true);
        createFasullo(false, 15, 12, 0, 6, 7, 7, 9, 0, "Mauro", "-", null, "", "", false, 1, true);
        createFasullo(false, 11, 9, 6, 3, 15, 6, 3, 15, "Donatella", "-", null, "", "", false, 1, true);
        createFasullo(false, 19, 3, 6, 4, 17, 0, 18, 3, "Renato", "-", null, "", "", false, 1, true);
        createFasullo(false, 16, 12, 9, 3, 11, 8, 8, 4, "Enrico", "-", null, "", "", false, 1, true);
        createFasullo(false, 6, 14, 8, 2, 19, 8, 2, 6, "Emanuela", "-", null, "", "", false, 1, true);
        createFasullo(false, 4, 17, 7, 8, 22, 0, 11, 7, "Daniela", "-", null, "", "", false, 1, true);
        createFasullo(false, 6, 10, 2, 7, 21, 2, 7, 11, "Giovanni", "-", null, "", "", false, 1, true);
        createFasullo(false, 20, 0, 0, 9, 26, 0, 10, 5, "Rosella", "-", null, "", "", false, 1, true);
        createFasullo(false, 5, 2, 14, 3, 12, 10, 11, 12, "Maria", "-", null, "", "", false, 1, true);
        createFasullo(false, 9, 15, 3, 2, 18, 5, 15, 2, "Ida", "-", null, "", "", false, 1, true);
        createFasullo(false, 10, 10, 7, 2, 4, 4, 29, 0, "Teresa", "-", null, "", "", false, 1, true);
        createFasullo(false, 4, 8, 0, 8, 22, 0, 8, 15, "Nicoletta", "-", null, "", "", false, 1, true);
        createFasullo(false, 13, 13, 3, 4, 7, 3, 4, 20, "Giovanna", "-", null, "", "", false, 1, true);
        createFasullo(false, 13, 5, 8, 5, 15, 7, 17, 0, "Naudik", "-", null, "", "", false, 1, true);
        createFasullo(false, 8, 11, 5, 3, 13, 4, 13, 13, "Claudia", "-", null, "", "", false, 1, true);
        createFasullo(false, 7, 12, 4, 4, 16, 4, 11, 12, "Franco", "-", null, "", "", false, 1, true);
        createFasullo(false, 2, 16, 0, 8, 17, 1, 13, 13, "Rosa", "-", null, "", "", false, 1, true);
        createFasullo(true, 11, 7, 2, 6, 16, 2, 6, 15, "Caterina", "-", null, "", "", false, 1, true);
        JOptionPane.showMessageDialog(null, "Popolazione Completata");


    }//GEN-LAST:event_jButtonTestActionPerformed

    private void jButton_excelsActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton_excelsActionPerformed
        File file = ExcelWriter.write(peopleTableModel1.getDatas());
        JOptionPane.showMessageDialog(null, "Il file Excel è stato creato con successo!", "Excel", JOptionPane.INFORMATION_MESSAGE);
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException ex) {
            Logger.getLogger(PeoplePanel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButton_excelsActionPerformed

    private void jButton_eliminaActActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton_eliminaActActionPerformed
        List<ActivityName> selectedValuesList = this.jList1.getSelectedValuesList();
        for (ActivityName activityName : selectedValuesList) {
            System.out.println("\t-------------- act name: " + activityName.getName());
            this.activityNameListModel1.removeElement(activityName);
        }
    }//GEN-LAST:event_jButton_eliminaActActionPerformed

    private void jTextField_searchKeyPressed(KeyEvent evt) {//GEN-FIRST:event_jTextField_searchKeyPressed
        initSearch();
    }//GEN-LAST:event_jTextField_searchKeyPressed

    private void jTextField_searchMousePressed(MouseEvent evt) {//GEN-FIRST:event_jTextField_searchMousePressed
        initSearch();
    }//GEN-LAST:event_jTextField_searchMousePressed

    private void jPanel_searchMouseReleased(MouseEvent evt) {//GEN-FIRST:event_jPanel_searchMouseReleased

    }//GEN-LAST:event_jPanel_searchMouseReleased

    private void jPanel_propertyMouseReleased(MouseEvent evt) {//GEN-FIRST:event_jPanel_propertyMouseReleased
        if (!evt.isPopupTrigger() && evt.getClickCount() == 2) {
            hideShowProperties();
        }
    }//GEN-LAST:event_jPanel_propertyMouseReleased

    private void closedPropertiesPanel1MouseReleased(MouseEvent evt) {//GEN-FIRST:event_closedPropertiesPanel1MouseReleased
        if (!evt.isPopupTrigger() && evt.getClickCount() == 2) {
            hideShowProperties();
        }
    }//GEN-LAST:event_closedPropertiesPanel1MouseReleased

    private void jButton_NoteActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton_NoteActionPerformed
        int index = jTable1.getSelectedRow();
        System.out.println("index = " + index);
        if (index != -1) {
            this.jToggleButton_modifica.setEnabled(true);
            this.jButton_freeTime.setEnabled(true);
            ParsedAccount pa = this.peopleTableModel1.getDatas().get(index);
            Person person = TrainerManager.getInstance().getPersonByID(pa.getAccount().getId());
            NoteDialog dialog = new NoteDialog(new JFrame(), true,person);
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
            
            System.out.println("NUOVA NOTA : "+person.getNote());
        }
    }//GEN-LAST:event_jButton_NoteActionPerformed

    private void hideShowProperties() {
        if (propertyShown) {

            this.jSplitPane1.setRightComponent(closedPropertiesPanel1);
            propertyShown = !propertyShown;
        } else {  

            this.jSplitPane1.setRightComponent(jPanel_property);
            propertyShown = !propertyShown;
        }
    }

    private void initSearch() {
        if (this.jTextField_search.getText().equals("<Cerca un volontario>")) {
            this.jTextField_search.setText("");
            jTextField_search.setFont(new Font("Tahoma", 1, 12)); // NOI18N
            jTextField_search.setForeground(new Color(0, 0, 0));
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ActivityNameListModel activityNameListModel1;
    private ClosedPropertiesPanel closedPropertiesPanel1;
    private Box.Filler filler1;
    private JButton jButtonTest;
    private JButton jButton_Note;
    private JButton jButton_addUser;
    private JButton jButton_annulla;
    private JButton jButton_eliminaAct;
    private JButton jButton_excels;
    private JButton jButton_freeTime;
    private JButton jButton_keywords;
    private JButton jButton_manageAct;
    private JButton jButton_removeUser;
    private JCheckBox jCheckBox1;
    private JCheckBox jCheckBox_Admin;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JList<ActivityName> jList1;
    private JPanel jPanel_property;
    private JPanel jPanel_search;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JSeparator jSeparator1;
    private JSeparator jSeparator2;
    private JSeparator jSeparator3;
    private JSeparator jSeparator4;
    private JSeparator jSeparator5;
    private JSpinner jSpinner1;
    private JSplitPane jSplitPane1;
    private JTable jTable1;
    private JTextField jTextField1;
    private JTextField jTextField_code;
    private JTextField jTextField_cognome;
    private JTextField jTextField_email;
    private JTextField jTextField_nome;
    private JTextField jTextField_phone;
    private JTextField jTextField_search;
    private JToggleButton jToggleButton_modifica;
    private PeopleTableModel peopleTableModel1;
    private PeopleTableRenderer peopleTableRenderer1;
    // End of variables declaration//GEN-END:variables

    public void createFasullo(boolean unleashKraken,
            int value_peopleLeader,
            int value_peoplePlanner,
            int value_peopleBrilliant,
            int value_peopleEvaluator,
            int value_peopleConcrete,
            int value_peopleExplorer,
            int value_peopleWorker,
            int value_peopleObjective,
            String name, String surname, Date borndate, String phone, String email, boolean admin, int maxTw, boolean oneTD
    ) {
        Account account = new Account();
        account.setDirectSkillValues(
                value_peopleLeader,
                value_peoplePlanner,
                value_peopleBrilliant,
                value_peopleEvaluator,
                value_peopleConcrete,
                value_peopleExplorer,
                value_peopleWorker,
                value_peopleObjective);

        List<Interval> freeIntervals = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            freeIntervals.add(new Interval(i, 0, 23));
        }
        //account.setOnlyTheseActivities(this.activityNameListModel1.values());

//        for (Interval freeInterval : freeIntervals) {
//            System.out.println("INTERVAL: " + freeInterval.toString());
//        }
        account.setIntervals(freeIntervals);
        account.setName(name);
        account.setSurname(surname);
        account.setBornDate(borndate);
        account.setPhone(phone);
        account.setEmail(email);
        account.setAdmin(admin);
        account.setMaxTurnPerWeek(maxTw);
        account.setOneTurnPerDay(oneTD);

        //account.setPhone(this.jTextField_Cell.getText());
//        this.accountFasulli.add(account);
//        if (unleashKraken) {
        MQTTClient.getInstance().createAccount(account);
        try {
            Thread.sleep(1000);
//        }

        } catch (InterruptedException ex) {
            Logger.getLogger(PeoplePanel.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void newAccountDetected(Account account) {
        peopleTableModel1.addRowElement(account);
    }

    @Override
    public void peopleDataArrived(List<Person> people) {
        System.out.println("PERSON LIST ARRIVED: " + people.size());
        try {
            for (Person p : people) {
                p.fix();
                Gson gson = new Gson();
//                Account account = gson.fromJson(p.getOtherData(), Account.class);
//                account.setId(p.getId());
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
    }

    @Override
    public void jobsDataArrived(List<Job> person) {

    }

    @Override
    public void skillsDataArrived(List<Skill> skills) {

    }

    @Override
    public JComponent[] getToolbarButtons() {
        JComponent[] buttons = new JComponent[9];
        System.out.println("                                buttons side = " + buttons.length);
        buttons[0] = this.jButton_addUser;
        buttons[1] = this.jButton_removeUser;
        buttons[2] = this.jButton_keywords;
        buttons[3] = this.jButton_excels;
        buttons[4] = this.jButtonTest;
        buttons[5] = this.filler1;
        buttons[6] = this.jSeparator5;
        buttons[7] = this.filler1;
        buttons[8] = this.jPanel_search;
        return buttons;
    }

    @Override
    public void userDeleted(Long id) {
        this.peopleTableModel1.deleteUser(id);
    }

    @Override
    public void jobDeleted(Long id) {

    }

    @Override
    public void keywordDeleted(Long id) {

    }

    @Override
    public void keywordsDataArrived(List<Keyword> skills) {

    }

    @Override
    public void keywordCreated(Long id) {

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

    @Override
    public void newActivityEvent(Activity activity) {
    }

    @Override
    public void removeActivityEvent(Activity activity) {
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
        this.jTable1.repaint();
    }

    private void initSearchFild() {

        RowSorter<? extends TableModel> rs = this.jTable1.getRowSorter();
        if (rs == null) {
            jTable1.setAutoCreateRowSorter(true);
            rs = jTable1.getRowSorter();
        }

        TableRowSorter<? extends TableModel> rowSorter
                = (rs instanceof TableRowSorter) ? (TableRowSorter<? extends TableModel>) rs : null;

        if (rowSorter == null) {
            throw new RuntimeException("Cannot find appropriate rowSorter: " + rs);
        }

        this.jTextField_search.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update(e);
            }

            private void update(DocumentEvent e) {
                String text = jTextField_search.getText();
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

    }
}

//int selectedRow = this.jTable1.getSelectedRow();
//        if (selectedRow != -1) {
//            ParsedAccount pa = this.peopleTableModel1.getDatas().get(selectedRow);
//            Person person = TrainerManager.getInstance().getPersonByID(pa.getAccount().getId());
//            this.jTextField_code.setText(person.getCode());
//            this.jTextField_email.setText(person.getEmail());
//            this.jTextField_phone   .setText(person.getPhone());
//            this.jLabel_Cognome.setText(person.getSurname());
//            this.jLabel_Nome.setText(person.getName());
//        }
