/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.activities;

import it.cnr.istc.sponsor.tt.gui.activities.glass.ActivityTurnsLayer;
import it.cnr.istc.sponsor.tt.gui.activities.ActivityCalendarPlacerPanel.ValueExportTransferHandler;
import it.cnr.istc.sponsor.tt.gui.engine.GuiEventManager;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.TransferHandler;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class SingleTimeActivityIntervalPanel extends javax.swing.JPanel {

    private boolean free = false;
//    private static Icon icon = new javax.swing.ImageIcon(SingleTimeActivityIntervalPanel.class.getResource("/it/cnr/istc/sponsor/tt/gui/icons/call16.png"));
//    private static Icon iconAlarm = new javax.swing.ImageIcon(SingleTimeActivityIntervalPanel.class.getResource("/it/cnr/istc/sponsor/tt/gui/icons/alarm16.png"));

    private JComponent[] activityLabels = new JComponent[15];
    private int id; //time
    private Date day;
    private ActivityTurnsLayer glassPane = null;

    /**
     * Creates new form SingleTimeIntervalPanel
     */
    public SingleTimeActivityIntervalPanel(Date when) {
        initComponents();
        this.day = when;
    }

//    public SingleTimeActivityIntervalPanel(int id) {
//        initComponents();
//        this.id = id;
//        this.setTransferHandler(new ValueImportTransferHandler(id));
//    }
    public SingleTimeActivityIntervalPanel(ActivityTurnsLayer glassPane, Date when, int id) {
        initComponents();
        this.day = when;
        this.id = id;
        this.glassPane = glassPane;
//        this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK));
        this.setTransferHandler(new ValueImportTransferHandler(when, id));

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e); //To change body of generated methods, choose Tools | Templates.
                System.out.println("<<<<<<< MOUSE IS MOVED OVER ME: " + id);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e); //To change body of generated methods, choose Tools | Templates.
//                System.out.println("ENTER THE TIME: " + id);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e); //To change body of generated methods, choose Tools | Templates.
                System.out.println("DRAAG");
            }

        });
        this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseDragged(java.awt.event.MouseEvent evt) {

                System.out.println("MOUSE IS DRAGGED OVER ME: " + id);

            }

            @Override
            public void mouseMoved(MouseEvent e) {
//                System.out.println("MOUSE IS MOVED OVER ME: " + id);
            }

        });
    }

    public int getId() {
        return id;
    }

    public void changeActicityTurn(int index){
        int i = index / 32;
        System.out.println("<<<<<<<<<<<<< i to delete -> " + i);
        if(activityLabels[i] instanceof ActivityTurnOverlapPanel){
            ((ActivityTurnOverlapPanel)activityLabels[i]).changeTurn();
        }
        this.removeAll();
        int maxSlot = ActivityCalendarManager.getInstance().getTotalActivityCountPerDay(day);
        for (int q = 0; q < maxSlot; q++) {
            if (activityLabels[q] == null) {
                this.add(new Box.Filler(new Dimension(32, 5), new Dimension(32, 5), new Dimension(32, 5)));
            } else {
                this.add(activityLabels[q]);
            }
        }
    }
    
    public void deleteActivity(int index) {
        int i = index / 32;
        System.out.println("i to delete -> " + i);
        activityLabels[i] = null;
        this.removeAll();
        int maxSlot = ActivityCalendarManager.getInstance().getTotalActivityCountPerDay(day);
        for (int q = 0; q < maxSlot; q++) {
            if (activityLabels[q] == null) {
                this.add(new Box.Filler(new Dimension(32, 5), new Dimension(32, 5), new Dimension(32, 5)));
            } else {
                this.add(activityLabels[q]);
            }
        }
    }

    public void add(String actName, ActivityTurnOverlapPanel panel) {
        int slot = ActivityCalendarManager.getInstance().addActivityTurn(day, id, actName);
        int maxSlot = ActivityCalendarManager.getInstance().getTotalActivityCountPerDay(day);
        activityLabels[slot] = panel;
        this.removeAll();
        for (int i = 0; i < maxSlot; i++) {
            if (activityLabels[i] == null) {
                this.add(new Box.Filler(new Dimension(32, 5), new Dimension(32, 5), new Dimension(32, 5)));
            } else {
                this.add(activityLabels[i]);
            }
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

        filler1 = new Box.Filler(new Dimension(10, 10), new Dimension(10, 10), new Dimension(10, 10));

        setBackground(new Color(255, 255, 255));
        setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                formMouseEntered(evt);
            }
            public void mouseReleased(MouseEvent evt) {
                formMouseReleased(evt);
            }
        });
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(filler1);
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseEntered(MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
//        this.setBackground(Color.red);
    }//GEN-LAST:event_formMouseEntered

    private void formMouseReleased(MouseEvent evt) {//GEN-FIRST:event_formMouseReleased

    }//GEN-LAST:event_formMouseReleased

    public class ValueImportTransferHandler extends TransferHandler {

        public final DataFlavor SUPPORTED_DATE_FLAVOR = DataFlavor.stringFlavor;

        private int time;
        private Date when;

        public ValueImportTransferHandler(Date when, int time) {
            this.time = time;
            this.when = when;
        }

        @Override
        public boolean canImport(TransferHandler.TransferSupport support) {
            boolean canImport = support.isDataFlavorSupported(SUPPORTED_DATE_FLAVOR);
//            System.out.println("CAN IMPORT ? "+canImport);
            if (canImport && ActivityCalendarManager.getInstance().getCurrentActivityDrag() != null) {
                CurrentActivityDrag cad = ActivityCalendarManager.getInstance().getCurrentActivityDrag();
                Icon activityIcon = GuiEventManager.getInstance().getActivityIcon(cad.getActivityName());
//                if (!ActivityCaldendarContainer.colorIndexedMap.containsKey(when)) {
//                    System.out.println("NO DATA!!");
//                }
//                if (!ActivityCaldendarContainer.colorIndexedMap.get(when).containsKey(cad.getActivityName())) {
//                    ActivityCaldendarContainer.colorIndexedMap.get(when).put(cad.getActivityName(), 0);
//                } else {
//                    ActivityCaldendarContainer.colorIndexedMap.get(when).put(cad.getActivityName(), ActivityCaldendarContainer.colorIndexedMap.get(when).get(cad.getActivityName()) + 1);
//                }
//                int turn = ActivityCaldendarContainer.colorIndexedMap.get(when).get((String) cad.getActivityName());
//                Color background = turn % 2 == 0 ? Color.GREEN : Color.CYAN;
                SingleTimeActivityIntervalPanel.this.add(cad.getActivityName(), new ActivityTurnOverlapPanel(when, activityIcon, cad.getActivityName(), time));
//                SingleTimeActivityIntervalPanel.this.invalidate();
                SingleTimeActivityIntervalPanel.this.revalidate();
            }
//            System.out.println("can import at " + time);
            return canImport;
        }

        @Override
        public boolean importData(TransferHandler.TransferSupport support) {
            boolean accept = false;
//            System.out.println("IMPORT");
            ActivityCalendarManager.getInstance().setCurrentActivityDrag(null);
            if (canImport(support)) {
                try {
                    Transferable t = support.getTransferable();
                    Object value = t.getTransferData(SUPPORTED_DATE_FLAVOR);
                    if (value instanceof String) {
                        Component component = support.getComponent();
                        if (component instanceof JPanel) {
                            Icon activityIcon = GuiEventManager.getInstance().getActivityIcon((String) value);
                            if (activityIcon != null) {
                                //((JPanel) component).add(new JLabel(activityIcon));
//                                ActivityCaldendarContainer.colorIndexedMap.get(when)

//                                if (!ActivityCaldendarContainer.colorIndexedMap.containsKey(when)) {
//                                    System.out.println("NO DATA!!");
//                                }
//                                if (!ActivityCaldendarContainer.colorIndexedMap.get(when).containsKey((String) value)) {
//                                    ActivityCaldendarContainer.colorIndexedMap.get(when).put((String) value, 0);
//                                } else {
//                                    ActivityCaldendarContainer.colorIndexedMap.get(when).put((String) value, ActivityCaldendarContainer.colorIndexedMap.get(when).get((String) value) + 1);
//                                }
//                                int turn = ActivityCaldendarContainer.colorIndexedMap.get(when).get((String) value);
//                                System.out.println("TURN -> " + turn);
//                                Color background = (turn % 2) == 0 ? Color.GREEN : Color.CYAN;
                                ((SingleTimeActivityIntervalPanel) component).add((String) value, new ActivityTurnOverlapPanel(when, activityIcon, (String) value, time));
                            }
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
    private Box.Filler filler1;
    // End of variables declaration//GEN-END:variables
}
