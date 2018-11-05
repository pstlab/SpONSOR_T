/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.activities.glass;

import it.cnr.istc.sponsor.tt.logic.SettingsManager;
import it.cnr.istc.sponsor.tt.logic.TrainerManager;
import it.cnr.istc.sponsor.tt.logic.model.Activity;
import it.cnr.istc.sponsor.tt.logic.model.ActivityTurn;
import it.cnr.istc.sponsor.tt.logic.model.ActivityTurnKeyword;
import it.cnr.istc.sponsor.tt.logic.model.Job;
import it.cnr.istc.sponsor.tt.logic.model.JobTurn;
import it.cnr.istc.sponsor.tt.logic.model.Keyword;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Font;
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
import java.util.stream.Collectors;
import javax.swing.ImageIcon;
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
public class ActivityTurnsLayer extends LayerUI<JPanel> {

//    private List<ActivityTurnOverlapPanel> turnPanels = new ArrayList<>();
    private boolean squareActive = false;
    private JTable underTable;
    private Activity activity;
    private Date tableDate;
    private Date finalDate;
    private Map<Integer, Integer> dayColumnMap = new HashMap<>();
    private ImageIcon keyIcon = new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/key12.png"));

    public ActivityTurnsLayer() {
//          this.setTransferHandler(null);

    }

    public void setContext(Activity activity, JTable table, Date tableDate) {
        this.activity = activity;
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

    public Activity getActivity() {
        return activity;
    }

    public void updateTableDate(Date startDate) {
        this.dayColumnMap.clear();
        this.tableDate = startDate;
        this.tableDate.setHours(0);
        this.tableDate.setMinutes(0);
        this.tableDate.setSeconds(0);
        this.finalDate = new Date(this.tableDate.getTime() + (1000l * 60l * 60l * 24l * 7l));

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
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2.setRenderingHints(rh);
//        g2.setRenderingHints(rh2);
//        g2.setColor(Color.BLUE);
//        System.out.println("c name = "+c.getName());
//        System.out.println("c name2 "+c.getClass().getCanonicalName());
//        g2.fillRect(0, 0, c.getWidth(), c.getHeight());
        if (squareActive) {
            g2.setColor(Color.BLUE);
            g2.fillRect(0, 0, 200, 300);
        }
//        for (ActivityTurnOverlapPanel turnPanel : turnPanels) {
//
//        }
        if (this.activity != null && this.underTable != null && this.tableDate != null) {
            List<ActivityTurn> activityTurns = this.activity.getActivityTurns();
            int x = this.underTable.getColumnModel().getColumn(0).getWidth();
//            System.out.println("  --------------------- x- > " + x);
            int ccc = 0;
            for (ActivityTurn turn : activityTurns) {
//                System.out.println("turn start -> " + turn.getStartTime());
//                System.out.println("turn end -> " + turn.getEndTime());
//                System.out.println("tabletime -> " + tableDate);
//                System.out.println("final -> " + finalDate);
                if (turn.getStartTime() < tableDate.getTime()) {
//                    System.out.println("CONTINUE");
                    continue;
                } else {
                    if (turn.getStartTime() > (tableDate.getTime()) && turn.getStartTime() < (finalDate.getTime())) {
                        //CALCOLO
//                        System.out.println("DRAW");
                        int nDay = this.dayColumnMap.get(new Date(turn.getStartTime()).getDate());
                        int tableRowHeight = this.underTable.getRowHeight();
                        int hours = (new Date(turn.getEndTime()).getHours() - new Date(turn.getStartTime()).getHours()) * 2;
                        int h_scart = 0;
                        if ((new Date(turn.getStartTime()).getMinutes()) == 30 && (new Date(turn.getEndTime()).getMinutes() == 0)) {
                            hours--;
                        }
                        if (new Date(turn.getStartTime()).getMinutes() == 0 && new Date(turn.getEndTime()).getMinutes() == 30) {
                            hours++;
                        }
                        if (new Date(turn.getStartTime()).getMinutes() == 30) {
                            h_scart = 1;
                        }
                        int scart = this.underTable.getTableHeader().getHeight();
                        int sumColumnWidths = 0;
                        for (int i = 1; i < nDay; i++) {
                            sumColumnWidths += this.underTable.getColumnModel().getColumn(i).getWidth();
                        }
                        g2.setColor(ccc % 2 == 0 ? new Color(200, 50, 21, 220) : new Color(50, 21, 200, 220));
                        List<JobTurn> requiredProfiles = turn.getRequiredProfiles();

                        g2.fillRoundRect(
                                x + sumColumnWidths + 5,
                                tableRowHeight * (((new Date(turn.getStartTime()).getHours() - SettingsManager.getInstance().getOpenTime()) * 2) + h_scart) + scart,
                                this.underTable.getColumnModel().getColumn(nDay).getWidth() - 10,
                                tableRowHeight * hours,
                                15,
                                15
                        );
                        int puppi = 7;
                        for (JobTurn requiredProfile : requiredProfiles) {
                            g2.setColor(TrainerManager.getInstance().getColorByJob(requiredProfile.getJob()));
//                            System.out.println("COLOR: "+TrainerManager.getInstance().getColorByJob(requiredProfile));
                            g2.fillOval(
                                    x + sumColumnWidths + puppi,
                                    tableRowHeight * (((new Date(turn.getStartTime()).getHours() - SettingsManager.getInstance().getOpenTime()) * 2) + h_scart) + scart + 5,
                                    10,
                                    10);
                            puppi += 12;
                        }

                        List<ActivityTurnKeyword> atk = turn.getWantedKeywords();
                        List<Keyword> wantedKeywords = new ArrayList<>();
                        for (ActivityTurnKeyword activityTurnKeyword : atk) {
                            wantedKeywords.add(activityTurnKeyword.getKeyword());
                        }
                        if (!wantedKeywords.isEmpty()) {
                            g2.setColor(new Color(40, 40, 40, 200));

                            g2.setFont(new Font("Verdana", Font.BOLD, 11));
                            String wKeys = wantedKeywords.stream().map(i -> i.getKeyword()).collect(Collectors.joining(", "));
                            int stringWidth = g2.getFontMetrics().stringWidth("" + wKeys);
                            int stringHeight = g2.getFontMetrics().getHeight();

                            g2.fillRoundRect(
                                    (int) x + sumColumnWidths + (this.underTable.getColumnModel().getColumn(nDay).getWidth() / 2) - stringWidth / 2 - 4 - 6,
                                    tableRowHeight * (((new Date(turn.getStartTime()).getHours() - SettingsManager.getInstance().getOpenTime()) * 2) + h_scart) + scart + tableRowHeight * hours - stringHeight - 4,
                                    stringWidth + 8 + 16,
                                    stringHeight + 2,
                                    8,
                                    8);

                            g2.drawImage(
                                    keyIcon.getImage(),
                                    (int) x + sumColumnWidths + (this.underTable.getColumnModel().getColumn(nDay).getWidth() / 2) - stringWidth / 2 - 6,
                                    tableRowHeight * (((new Date(turn.getStartTime()).getHours() - SettingsManager.getInstance().getOpenTime()) * 2) + h_scart) + scart + tableRowHeight * hours - 8 - stringHeight / 2,
                                    null
                            );
                            g2.setPaint(Color.WHITE);
                            g2.drawString(
                                    wKeys,
                                    16 + (int) x + sumColumnWidths + (this.underTable.getColumnModel().getColumn(nDay).getWidth() / 2) - stringWidth / 2 - 6,
                                    tableRowHeight * (((new Date(turn.getStartTime()).getHours() - SettingsManager.getInstance().getOpenTime()) * 2) + h_scart) + scart + tableRowHeight * hours - 6
                            );
                        }

                        ccc++;
                    } else {
                        System.out.println("BREAK");
                        System.out.println("this.activity: " + this.activity);
                        System.out.println("this.tableDate: " + this.tableDate);
                        break;
                    }
                }
            }
        } else {
            System.out.println("PAINT ERROR: ACTIVITY-> " + (this.activity == null) + " UNDERTABLE -> " + (this.underTable == null) + " TABLE DATE -> " + (this.tableDate == null));
        }

    }

    @Override
    protected void processMouseEvent(MouseEvent e, JLayer<? extends JPanel> l) {
        super.processMouseEvent(e, l); //To change body of generated methods, choose Tools | Templates.

        Point p = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), l);
        int mX = p.x;
        int mY = p.y;

//        System.out.println("X: " + mX);
//        System.out.println("Y: " + mY);
    }

}
