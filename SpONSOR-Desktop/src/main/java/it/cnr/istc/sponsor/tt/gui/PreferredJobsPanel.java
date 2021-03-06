/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui;


import it.cnr.istc.sponsor.tt.logic.ParsedAccount;
import it.cnr.istc.sponsor.tt.logic.RegistrationListener;
import it.cnr.istc.sponsor.tt.logic.TrainerManager;
import it.cnr.istc.sponsor.tt.logic.model.Account;
import it.cnr.istc.sponsor.tt.logic.model.Person;
import java.util.List;
import javax.swing.JComponent;

/**
 *
 * @author user
 */
public class PreferredJobsPanel extends javax.swing.JPanel implements RegistrationListener {

    float[][] matrice_coefficienti;

    /**
     * Creates new form PreferredJobsPanel
     */
    public PreferredJobsPanel() {
        initComponents();
        ((JComponent) jTable1.getDefaultRenderer(Boolean.class)).setOpaque(true);
        jTable1.getColumnModel().getColumn(0).setMinWidth(150);
        jTable3.getColumnModel().getColumn(0).setMinWidth(150);
        TrainerManager.getInstance().addRegistrationListener(this);
        initMatrix();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        preferredJobsTableRenderer1 = new it.cnr.istc.sponsor.tt.gui.PreferredJobsTableRenderer();
        accountTableModel1 = new it.cnr.istc.sponsor.tt.gui.AccountTableModel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Help-Line",  new Boolean(true), null, null, null,  new Boolean(true), null,  new Boolean(true),  new Boolean(true)},
                {"TeleServizi", null,  new Boolean(true), null,  new Boolean(true), null,  new Boolean(true),  new Boolean(true), null},
                {"Ascolto e Assistenza",  new Boolean(true), null,  new Boolean(true),  new Boolean(true),  new Boolean(false), null,  new Boolean(true), null},
                {"Banca del Cibo", null,  new Boolean(true), null, null,  new Boolean(true), null,  new Boolean(true),  new Boolean(true)},
                {"Servizio Consulenza",  new Boolean(true),  new Boolean(true),  new Boolean(true), null, null,  new Boolean(true), null,  new Boolean(true)},
                {"Pranzi & Merende", null,  new Boolean(true), null,  new Boolean(true),  new Boolean(true), null,  new Boolean(true), null},
                {"Animazione", null,  new Boolean(true),  new Boolean(true), null, null,  new Boolean(true), null,  new Boolean(true)},
                {"Guida Viaggi Organizzati",  new Boolean(true),  new Boolean(true), null, null,  new Boolean(true), null,  new Boolean(true), null},
                {"Supporto Spirituale",  new Boolean(true), null,  new Boolean(true),  new Boolean(true), null, null, null,  new Boolean(true)}
            },
            new String [] {
                "Attività / Profilo", "Leader", "Pianificatore", "Brillante", "Valutatore", "Concreto", "Esploratore", "Lavoratore", "Oggettivo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setRowHeight(32);
        jTable1.setSelectionBackground(new java.awt.Color(102, 255, 0));
        jTable1.setSelectionForeground(new java.awt.Color(0, 0, 204));
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(1).setCellRenderer(null);
            jTable1.getColumnModel().getColumn(2).setCellRenderer(null);
            jTable1.getColumnModel().getColumn(3).setCellRenderer(null);
            jTable1.getColumnModel().getColumn(4).setCellRenderer(null);
            jTable1.getColumnModel().getColumn(5).setCellRenderer(null);
            jTable1.getColumnModel().getColumn(6).setCellRenderer(null);
            jTable1.getColumnModel().getColumn(7).setCellRenderer(null);
            jTable1.getColumnModel().getColumn(8).setCellRenderer(null);
        }

        jTable2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Score"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.setRowHeight(32);
        jTable2.setSelectionBackground(new java.awt.Color(51, 255, 0));
        jTable2.setSelectionForeground(new java.awt.Color(0, 51, 204));
        jScrollPane2.setViewportView(jTable2);

        jTable3.setModel(accountTableModel1);
        jTable3.setRowHeight(22);
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable3MouseReleased(evt);
            }
        });
        jScrollPane3.setViewportView(jTable3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 817, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTable3MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseReleased
        ParsedAccount pa = this.accountTableModel1.getDatas().get(this.jTable3.getSelectedRow());
        float maxScore = 0;
        int maxIndex = 0;
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            float score = 0f;
            for (int j = 0; j < 8; j++) {
                score +=(Float.parseFloat(""+this.jTable3.getValueAt(this.jTable3.getSelectedRow(), j+1))
                        *matrice_coefficienti[i][j]
                        
                        
                        );
                
            }
            if(score > maxScore){
                    maxScore = score;
                    maxIndex = i;
                }
            jTable2.getModel().setValueAt(score, i, 0);
        }
        this.jTable1.setRowSelectionInterval(maxIndex, maxIndex);
        this.jTable2.setRowSelectionInterval(maxIndex, maxIndex);

    }//GEN-LAST:event_jTable3MouseReleased

    private void printMatrix() {
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(matrice_coefficienti[i][j] + ", ");
            }
            System.out.println("");
        }
        System.out.println("=======================================");
    }

    private void initMatrix() {
        matrice_coefficienti = new float[jTable1.getRowCount()][8];
        printMatrix();
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            int trueCount = 0;
            for (int j = 1; j < 9; j++) {
                if (jTable1.getValueAt(i, j) instanceof Boolean) {
                    if ((Boolean) jTable1.getValueAt(i, j)) {
                        trueCount++;
                    }
                }
            }
            System.out.println("tc  =" + trueCount);
            float c = 1f/trueCount;
            System.out.println("fixing constants -> "+c);
            
            for (int j = 1; j < 9; j++) {
                if (jTable1.getValueAt(i, j) instanceof Boolean) {
                    if ((Boolean) jTable1.getValueAt(i, j)) {
                        matrice_coefficienti[i][j-1] = c;
                    }
                }
            }
        }
        printMatrix();
                

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private it.cnr.istc.sponsor.tt.gui.AccountTableModel accountTableModel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private it.cnr.istc.sponsor.tt.gui.PreferredJobsTableRenderer preferredJobsTableRenderer1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void newAccountDetected(Account account) {
        this.accountTableModel1.addRowElement(new ParsedAccount(account));
    }

    @Override
    public void peopleDataArrived(List<Person> people) {
        
    }
}
