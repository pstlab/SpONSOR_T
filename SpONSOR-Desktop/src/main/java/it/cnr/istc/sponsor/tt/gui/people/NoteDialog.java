/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.people;

import it.cnr.istc.sponsor.tt.logic.MQTTClient;
import it.cnr.istc.sponsor.tt.logic.TrainerManager;
import it.cnr.istc.sponsor.tt.logic.model.Person;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.WindowConstants;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class NoteDialog extends javax.swing.JDialog {

    private Person person;

    /**
     * Creates new form NoteDialog
     */
    public NoteDialog(java.awt.Frame parent, boolean modal, Person person) {
        super(parent, modal);
        initComponents();
        this.person = person;
        if(this.person != null){
            
            this.jTextArea1.setText(this.person.getNote());
            this.setTitle(person.toString());
        }
        
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    System.out.println("set");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Object o = UIManager.get("TextArea[Enabled+NotInScrollPane].borderPainter");

        UIDefaults paneDefaults = new UIDefaults();
        paneDefaults.put("TextPane.borderPainter", o);

//        jScrollPane1.setMargin(new Insets(10, 10, 10, 10));
        jScrollPane1.putClientProperty("Nimbus.Overrides", paneDefaults);
        jScrollPane1.putClientProperty("Nimbus.Overrides.InheritDefaults", false);

//        this.jTextPane1.setBorder(null);
//        this.jTextPane1.setBackground(Color.red);
//
//        this.jTextArea1.setBackground(null);
//        JViewport viewport = new JViewport();
//
//        //Component that need to be added in Scroll pane//
//        viewport.setView(jTextPane1);
//
//        viewport.setOpaque(false);
//
//        jScrollPane2.setViewport(viewport);
        jTextArea1.setOpaque(false); // added by OP
        jTextArea1.setBorder(null);
        jTextArea1.setBackground(new Color(0, 0, 0, 0));

        jScrollPane1.getViewport().setOpaque(false);
//
        jScrollPane1.setOpaque(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextPane1 = new JTextPane();
        jPanel1 = new JPanel();
        jButton_Salva = new JButton();
        jButton_Annulla = new JButton();
        notePanel1 = new NotePanel();
        jScrollPane1 = new JScrollPane();
        jTextArea1 = new JTextArea();

        jTextPane1.setBackground(UIManager.getDefaults().getColor("InternalFrame.activeBorderColor"));
        jTextPane1.setOpaque(false);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new Color(255, 255, 255));

        jButton_Salva.setText("Salva");
        jButton_Salva.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton_SalvaActionPerformed(evt);
            }
        });

        jButton_Annulla.setText("Annulla");
        jButton_Annulla.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton_AnnullaActionPerformed(evt);
            }
        });

        notePanel1.setOpaque(false);

        jScrollPane1.setBorder(null);
        jScrollPane1.setOpaque(false);

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new Font("Lucida Calligraphy", 0, 18)); // NOI18N
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        GroupLayout notePanel1Layout = new GroupLayout(notePanel1);
        notePanel1.setLayout(notePanel1Layout);
        notePanel1Layout.setHorizontalGroup(notePanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(notePanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 372, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );
        notePanel1Layout.setVerticalGroup(notePanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(notePanel1Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 340, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(57, Short.MAX_VALUE))
        );

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(notePanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton_Annulla)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton_Salva)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(notePanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_Salva)
                    .addComponent(jButton_Annulla))
                .addContainerGap())
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_SalvaActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton_SalvaActionPerformed
        this.person.setNote(this.jTextArea1.getText());
//        MQTTClient.getInstance().editPerson(person);
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_jButton_SalvaActionPerformed

    private void jButton_AnnullaActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton_AnnullaActionPerformed
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_jButton_AnnullaActionPerformed

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
            java.util.logging.Logger.getLogger(NoteDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NoteDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NoteDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NoteDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                NoteDialog dialog = new NoteDialog(new javax.swing.JFrame(), true, null);
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
    private JButton jButton_Annulla;
    private JButton jButton_Salva;
    private JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private JTextArea jTextArea1;
    private JTextPane jTextPane1;
    private NotePanel notePanel1;
    // End of variables declaration//GEN-END:variables
}
