/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.activities.glass;

import it.cnr.istc.sponsor.tt.abstracts.PaintSupplier;
import it.cnr.istc.sponsor.tt.logic.SettingsManager;
import it.cnr.istc.sponsor.tt.logic.TrainerManager;
import it.cnr.istc.sponsor.tt.logic.model.Activity;
import it.cnr.istc.sponsor.tt.logic.model.ActivityTurn;
import it.cnr.istc.sponsor.tt.logic.model.Job;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JLayer;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.plaf.LayerUI;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class ActivityGeneralTurnsLayer extends LayerUI<JPanel> {

//    private List<ActivityTurnOverlapPanel> turnPanels = new ArrayList<>();
    private boolean squareActive = false;
    private JTable underTable;
    private Date tableDate;
    private Date finalDate;
    private Map<Integer, Integer> dayColumnMap = new HashMap<>();
    private Map<Integer, List<String>> howManyRectPerDayMap = new HashMap<>(); //String = activity name
    private Map<Integer, Map<String, Integer>> activityOrderMap = new HashMap<>();
    private List<Long> managedTurns = new ArrayList<>();
    private List<String> assignedColorActivities = new ArrayList<>();

    public ActivityGeneralTurnsLayer() {
//          this.setTransferHandler(null);

    }

    public void setContext(JTable table, Date tableDate) {
        if (tableDate != null) {
            this.tableDate = tableDate;
            this.tableDate.setHours(0);
            this.tableDate.setMinutes(0);
            this.tableDate.setSeconds(0);
            this.underTable = table;
            this.finalDate = new Date(this.tableDate.getTime() + (1000l * 60l * 60l * 24l * 7l));

            Calendar c = Calendar.getInstance();
            c.setTime(tableDate);

            for (int i = 1; i < 8; i++) {
                this.dayColumnMap.put(c.getTime().getDate(), i);
                c.add(Calendar.DATE, 1);
            }
        }
    }

    public void updateTableDate(Date startDate) {
        this.dayColumnMap.clear();
        this.tableDate = startDate;
        this.tableDate.setHours(0);
        this.tableDate.setMinutes(0);
        this.tableDate.setSeconds(0);
        this.finalDate = new Date(this.tableDate.getTime() + (1000l * 60l * 60l * 24l * 7l));
        assignedColorActivities.clear();
        PaintSupplier.reset();
//        howManyRectPerDayMap.clear();

        Calendar c = Calendar.getInstance();
        c.setTime(tableDate);

        for (int i = 1; i < 8; i++) {
            this.dayColumnMap.put(c.getTime().getDate(), i);
            c.add(Calendar.DATE, 1);
        }
    }

//    public void addActivityTurnOverlapPanel(ActivityTurnOverlapPanel turnPanel, int x, int y) {
//        this.turnPanels.add(turnPanel);
//    }
    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        JLayer jlayer = (JLayer) c;
        jlayer.setLayerEventMask(
                AWTEvent.MOUSE_EVENT_MASK
                | AWTEvent.MOUSE_MOTION_EVENT_MASK
        );
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(g, c); //To change body of generated methods, choose Tools | Templates.
        Graphics2D g2 = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        RenderingHints rh2 = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(rh);
        g2.setRenderingHints(rh2);
//        g2.fillRect(100, 100, 200, 200);
        if (squareActive) {
            g2.setColor(Color.BLUE);
            g2.fillRect(0, 0, 200, 300);
        }

        if (underTable == null) {
            return;
        }
        int x = this.underTable.getColumnModel().getColumn(0).getWidth();

        List<Activity> activities = TrainerManager.getInstance().getActivities();

        for (Activity activity : activities) {
            if (!assignedColorActivities.contains(activity.getActivityName().getName())) {
                activity.assignColor();
                assignedColorActivities.add(activity.getActivityName().getName());
            }
//            System.out.println("ACTIVITY -> " + activity.getName());

            if (this.underTable != null && this.tableDate != null) {
                List<ActivityTurn> activityTurns = activity.getActivityTurns();

//                System.out.println("  --------------------- x- > " + x);
                int ccc = 0;
                for (ActivityTurn turn : activityTurns) {

//                    System.out.println("turn start -> " + turn.getStartTime());
//                    System.out.println("turn end -> " + turn.getEndTime());
//                    System.out.println("tabletime -> " + tableDate);
//                    System.out.println("final -> " + finalDate);
                    if (turn.getStartTime() < (tableDate.getTime())) {
//                        System.out.println("CONTINUE");
                        continue;
                    } else if (turn.getStartTime()>(tableDate.getTime()) && turn.getStartTime()<(finalDate.getTime())) {
                        //CALCOLO
//                            System.out.println("DRAW");
                        int nDay = this.dayColumnMap.get(new Date(turn.getStartTime()).getDate());
                        System.out.println("valuto il giorno: "+nDay);
                        if (!activityOrderMap.containsKey(nDay)) {
                            activityOrderMap.put(nDay, new HashMap<>());
//                                System.out.println("INCREASING");
                        }
//else {
                        if (!activityOrderMap.get(nDay).containsKey(activity.getActivityName().getName())) {
                            activityOrderMap.get(nDay).put(activity.getActivityName().getName(), activityOrderMap.get(nDay).size() + 1);
                        }
//                            }
                        if (!this.managedTurns.contains(turn.getUniqueCode())) {
                            if (!howManyRectPerDayMap.containsKey(nDay)) {
                                howManyRectPerDayMap.put(nDay, new ArrayList<>());
                                howManyRectPerDayMap.get(nDay).add(activity.getActivityName().getName());
                            } else if (!howManyRectPerDayMap.get(nDay).contains(activity.getActivityName().getName())) {
                                howManyRectPerDayMap.get(nDay).add(activity.getActivityName().getName());
                            } //                                    howManyRectPerDayMap.put(nDay, howManyRectPerDayMap.get(nDay) + 1);
                            this.managedTurns.add(turn.getUniqueCode());
                        }
                        // F int barWidth = this.underTable.getColumnModel().getColumn(nDay).getWidth() / activities.size();
                        if (!howManyRectPerDayMap.containsKey(nDay)) {
                            System.out.println("ER1: "+nDay);
                            continue;
                        }
                        Integer barCount = howManyRectPerDayMap.get(nDay).size();
//                            System.out.println("BARCOUNT -> -> " + barCount);
//                            System.out.println("nDay -> " + nDay);
                        int tableRowHeight = this.underTable.getRowHeight();
                        int hours = (new Date(turn.getEndTime()).getHours() - new Date(turn.getStartTime()).getHours()) * 2;
                        int h_scart = 0;
                        if (new Date(turn.getStartTime()).getMinutes() == 30 && new Date(turn.getEndTime()).getMinutes() == 0) {
                            hours--;
                        }
                        if (new Date(turn.getStartTime()).getMinutes() == 0 && new Date(turn.getEndTime()).getMinutes() == 30) {
                            hours++;
                        }
                        if(new Date(turn.getStartTime()).getMinutes()==30){
                            h_scart=1;
                        }
                        int scart = this.underTable.getTableHeader().getHeight();
                        int sumColumnWidths = 0;
                        for (int i = 1; i < nDay; i++) {
                            sumColumnWidths += this.underTable.getColumnModel().getColumn(i).getWidth();
                        }
//                            g2.setColor(ccc % 2 == 0 ? new Color(200, 50, 21, 190) : new Color(50, 21, 200, 190));
                        g2.setColor(activity.getGeneralColor());
//                            List<Job> requiredProfiles = turn.getRequiredProfiles();
                        int d = 5;
                        int orderActivity = activityOrderMap.get(nDay).get(activity.getActivityName().getName());
//                            System.out.println("ORDER -> " + orderActivity);
                        g2.fillRoundRect(
                                x + sumColumnWidths + d + ((this.underTable.getColumnModel().getColumn(nDay).getWidth() / barCount)) * (orderActivity - 1),
                                tableRowHeight * (((new Date(turn.getStartTime()).getHours() - SettingsManager.getInstance().getOpenTime())*2)+h_scart) + scart,
                                this.underTable.getColumnModel().getColumn(nDay).getWidth() / barCount - (d * 2), // - (d * (barCount + 1))
                                tableRowHeight * hours,
                                15,
                                15
                        );
//                            int puppi = 7;
//                            for (Job requiredProfile : requiredProfiles) {
//                                g2.setColor(TrainerManager.getInstance().getColorByJob(requiredProfile));
////                                System.out.println("COLOR: " + TrainerManager.getInstance().getColorByJob(requiredProfile));
//                                g2.fillOval(
//                                        x + sumColumnWidths + puppi,
//                                        tableRowHeight * turn.getStartTime().getHours() + scart + 5,
//                                        10,
//                                        10);
//                                puppi += 12;
//                            }

                        ccc++;
                    } else {
//                            System.out.println("BREAK");
                        break;
                    }
                }
            }
        }

    }

    @Override
    protected void processMouseEvent(MouseEvent e, JLayer<? extends JPanel> l
    ) {
        super.processMouseEvent(e, l); //To change body of generated methods, choose Tools | Templates.

        Point p = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), l);
        int mX = p.x;
        int mY = p.y;

    }

}
