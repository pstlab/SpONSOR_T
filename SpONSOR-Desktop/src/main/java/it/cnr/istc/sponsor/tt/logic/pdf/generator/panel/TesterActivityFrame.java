/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.logic.pdf.generator.panel;

import javax.swing.GroupLayout;
import javax.swing.WindowConstants;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class TesterActivityFrame extends javax.swing.JFrame {

    /**
     * Creates new form TesterActivityFrame
     */
    public TesterActivityFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tableActivityGeneratorPanel1 = new TableActivityGeneratorPanel();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        GroupLayout tableActivityGeneratorPanel1Layout = new GroupLayout(tableActivityGeneratorPanel1);
        tableActivityGeneratorPanel1.setLayout(tableActivityGeneratorPanel1Layout);
        tableActivityGeneratorPanel1Layout.setHorizontalGroup(tableActivityGeneratorPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 876, Short.MAX_VALUE)
        );
        tableActivityGeneratorPanel1Layout.setVerticalGroup(tableActivityGeneratorPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 507, Short.MAX_VALUE)
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(tableActivityGeneratorPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(tableActivityGeneratorPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(TesterActivityFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TesterActivityFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TesterActivityFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TesterActivityFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TesterActivityFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private TableActivityGeneratorPanel tableActivityGeneratorPanel1;
    // End of variables declaration//GEN-END:variables
}
