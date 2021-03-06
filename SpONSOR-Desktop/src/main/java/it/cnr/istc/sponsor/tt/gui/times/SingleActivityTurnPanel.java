/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.times;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.TransferHandler;
import static jdk.nashorn.internal.objects.NativeDebug.getClass;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class SingleActivityTurnPanel extends javax.swing.JPanel {

    private boolean free = false;
    private static Icon icon = new javax.swing.ImageIcon(SingleActivityTurnPanel.class.getResource("/it/cnr/istc/sponsor/tt/gui/icons/call16.png"));
    
    /**
     * Creates new form SingleTimeIntervalPanel
     */
    public SingleActivityTurnPanel() {
        initComponents();
        this.setTransferHandler(new ValueImportTransferHandler());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new Color(51, 255, 51));
        setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                formMouseEntered(evt);
            }
            public void mouseReleased(MouseEvent evt) {
                formMouseReleased(evt);
            }
        });
        setLayout(new GridLayout(1, 10));
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseEntered(MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
//        this.setBackground(Color.red);
    }//GEN-LAST:event_formMouseEntered

    private void formMouseReleased(MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        free = !free;
        this.setBackground(free ? Color.GREEN : Color.RED);
    }//GEN-LAST:event_formMouseReleased

    public static class ValueImportTransferHandler extends TransferHandler {

        public static final DataFlavor SUPPORTED_DATE_FLAVOR = DataFlavor.stringFlavor;

        public ValueImportTransferHandler() {
        }

        @Override
        public boolean canImport(TransferHandler.TransferSupport support) {
            System.out.println("can import ?");
            return support.isDataFlavorSupported(SUPPORTED_DATE_FLAVOR);
        }

        @Override
        public boolean importData(TransferHandler.TransferSupport support) {
            boolean accept = false;
            System.out.println("IMPORT");
            if (canImport(support)) {
                try {
                    Transferable t = support.getTransferable();
                    Object value = t.getTransferData(SUPPORTED_DATE_FLAVOR);
                    if (value instanceof String) {
                        Component component = support.getComponent();
                        if (component instanceof JPanel) {
                            ((JPanel) component).add(new JLabel(icon));
                             ((JPanel) component).invalidate();
                             ((JPanel) component).revalidate();
                            accept = true;
                        }
                    }
                    
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
            }
            return accept;
        }
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
