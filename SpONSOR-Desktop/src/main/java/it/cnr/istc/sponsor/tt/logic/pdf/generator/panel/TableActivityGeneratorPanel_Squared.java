/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.logic.pdf.generator.panel;

import it.cnr.istc.sponsor.tt.logic.SettingsManager;
import it.cnr.istc.sponsor.tt.logic.model.Activity;
import it.cnr.istc.sponsor.tt.logic.model.ActivityTurn;
import it.cnr.istc.sponsor.tt.logic.model.ComfirmedTurn;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.beans.Beans;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.JOptionPane;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class TableActivityGeneratorPanel_Squared extends javax.swing.JPanel {

    private String monthName = "";
    private String[][] data = null;
    private static final String TIME = "Orario";
    private static final String LUN = "LUN";
    private static final String MAR = "MAR";
    private static final String MER = "MER";
    private static final String GIO = "GIO";
    private static final String VEN = "VEN";
    private static final String SAB = "SAB";
    private static final String DOM = "DOM";
    private final String[] weekLabels = new String[]{TIME, LUN, MAR, MER, GIO, VEN, SAB, DOM};
    private List<String> orarioList = new ArrayList<>();
    private int miniRowHeight = 48;
    private int numberGap = -1;
    private boolean greenActive = true;
    private int lowestPoint = -1;
    private Activity activity;

    /**
     * Creates new form TableImageGeneratorPanel
     */
    public TableActivityGeneratorPanel_Squared() {
        initComponents();
        int openTime = 9;
        int closeTime = 22;
        if (!Beans.isDesignTime()) {
            openTime = SettingsManager.getInstance().getOpenTime();
            closeTime = SettingsManager.getInstance().getCloseTime();
        }

        System.out.println("OPEN TIME= " + openTime);
        System.out.println("CLOSE TIME= " + closeTime);

        for (int i = openTime; i < closeTime; i++) {
            String time = "";
            String time2 = "";
            if (i < 10) {
                time = "0" + i + ":00";
                time2 = "0" + i + ":30";
            } else {
                time = i + ":00";
                time2 = i + ":30";
            }
            System.out.println("time = " + time);
            System.out.println("time2 = " + time2);
            this.orarioList.add(time);
            this.orarioList.add(time2);
//            JOptionPane.showMessageDialog(null, "time: "+time+", time2: "+time2);
        }

//        this.setData(new String[][]{
////            {"09:00","NULL", "NULL", "NULL", "NULL", "NULL", "NULL", "1"},
////            {"10:00","2", "3", "4", "5", "6#10:00 - 12:00;Telepresenza", "7", "8#10:00 - 12:00;Telepresenza!12:00 - 14:00;Laboratorio"},
////            {"11:00","9", "10#10:00 - 12:00;Telepresenza", "11", "12", "13", "14", "15"},
////            {"12:00","16", "17", "18", "19", "20", "21", "22"},
////            {"13:00","23#14:00 - 18:00;Telepresenza!18:30 - 20:00;Servizio Mensa!18:30 - 20:00;Servizio Mensa!18:30 - 20:00;Servizio Mensa", "24#09:00 - 11:00;Lab Informatica", "25", "26", "27", "28", "29"},
////            {"14:00","30#14:00 - 18:00;Telepresenza!18:30 - 20:00;Servizio Mensa!20:00 - 21:00;Pulizie", "31#10:00 - 12:00;Telepresenza", "NULL", "NULL", "NULL", "NULL", "NULL"},});
////
////        
//             {"NULL", "NULL", "NULL", "NULL", "NULL", "NULL", "1"},
//            {"2", "3", "4", "5", "6#10:00 - 12:00;Telepresenza", "7", "8#10:00 - 12:00;Telepresenza!12:00 - 14:00;Laboratorio"},
//            {"9", "10#10:00 - 12:00;Telepresenza", "11", "12", "13", "14", "15"},
//            {"16", "17", "18", "19", "20", "21", "22"},
//            {"23#14:00 - 18:00;Telepresenza!18:30 - 20:00;Servizio Mensa!18:30 - 20:00;Servizio Mensa!18:30 - 20:00;Servizio Mensa", "24#09:00 - 11:00;Lab Informatica", "25", "26", "27", "28", "29"},
//            {"30#14:00 - 18:00;Telepresenza!18:30 - 20:00;Servizio Mensa!20:00 - 21:00;Pulizie", "31#10:00 - 12:00;Telepresenza", "NULL", "NULL", "NULL", "NULL", "NULL"},});
    }

//    public void setData(String[][] data) {
//        this.data = data;
//    }
    public void setActivity(Activity activity) {
        this.activity = activity;
        List<ActivityTurn> activityTurns = activity.getActivityTurns();
        Collections.sort(activityTurns);
        for (ActivityTurn activityTurn : activityTurns) {
            List<ComfirmedTurn> comfirmedTurns = activityTurn.getComfirmedTurns();
            for (ComfirmedTurn comfirmedTurn : comfirmedTurns) {
//                comfirmedTurn.getClass() 
            }
        }
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public boolean isGreenActive() {
        return greenActive;
    }

    public void setGreenActive(boolean greenActive) {
        this.greenActive = greenActive;
    }

    public String printImg() {
        BufferedImage bImg = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D cg = bImg.createGraphics();
        this.paintAll(cg);
        String fileName = "";
        try {
            fileName = "./" + (new Date().getTime()) + ".png";
            if (ImageIO.write(bImg, "png", new File(fileName))) {
                System.out.println("-- saved");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return fileName;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g); //To change body of generated methods, choose Tools | Templates.
//        if (data == null) {
//            return;
//        }
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        //SET HEADER
        int width = this.getWidth();
        int height = this.getHeight();
        int startX = 0;
        int time_widht = 60;
        int columnWidth = (width - time_widht) / 7;
        int headHeight = 40;
        g2.setPaint(Color.LIGHT_GRAY);
        g2.fillRect(time_widht, 0, width, headHeight);
        int rowHeight = 26;
        int hRow = headHeight;
        //TIME FONT
        g2.setFont(new Font("TimesRoman", Font.BOLD, 14));
//        int xLabel = headHeight;

        for (int i = 0; i < orarioList.size(); i++) {

            g2.setPaint(i % 2 == 0 ? Color.WHITE : new Color(240, 240, 240));
            g2.fillRect(0, hRow, width, rowHeight);

            String m = orarioList.get(i);
            g2.setPaint(Color.BLACK);
            int stringWidth = g2.getFontMetrics().stringWidth(m);
            int stringHeight = g2.getFontMetrics().getHeight();
            hRow += rowHeight;
            g2.drawString(m, 10, hRow - (stringHeight / 2)); //(int)((height-stringHeight)/2)

        }

        //Disegna le label in cima
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("TimesRoman", Font.BOLD, 16));

        //DISEGNA RIGHE
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(time_widht, 0, time_widht, height);
        g2.drawLine(0, headHeight, width, headHeight);
        int colH = time_widht + columnWidth;
        for (int i = 0; i < 7; i++) {
            if (i < 6) {
                g2.drawLine(colH, 0, colH, height);
            }
            int stringWidth = g2.getFontMetrics().stringWidth(weekLabels[i + 1]);
            g2.drawString(weekLabels[i + 1], colH - columnWidth / 2 - stringWidth/2, 20);
            colH += columnWidth;

        }
        g2.drawLine(width - 2, 0, width - 2, height);

//        if (data != null) {
//            int xLabel = 0;
//            g2.setPaint(Color.LIGHT_GRAY);
//            g2.fillRect(0, 10, width, 30);
//            g2.setColor(Color.BLACK);
//            for (int i = 0; i < data.length; i++) {
//                g2.setFont(new Font("TimesRoman", Font.BOLD, 18));
//                for (int j = 0; j < 8; j++) {
//                    if (j == 7) {
//                        g2.setColor(Color.RED);
//                    } else {
//                        g2.setColor(Color.BLACK);
//                    }
//                    String m = weekLabels[j];
//                    int stringWidth = g2.getFontMetrics().stringWidth(m);
//                    int stringHeight = g2.getFontMetrics().getHeight();
//                    g2.drawString(m, (int) (((xLabel + columnWidth) - stringWidth) / 2), 30); //(int)((height-stringHeight)/2)
//                    xLabel += columnWidth * 2;
//                }
//            }
//        }
//
//        //WORKING DATA
//        int workingRow = 40;
//        for (int i = 0; i < data.length; i++) {
//            int maxRowWeek = 0;
//            for (int j = 0; j < 8; j++) {
//                if (data[i][j].contains("!")) {
//                    int localMax = data[i][j].split("!").length;
//                    if (localMax > maxRowWeek) {
//                        maxRowWeek = localMax;
//                    }
//                } else if (!data[i][j].equals("NULL") && maxRowWeek == 0) {
//                    maxRowWeek = 1;
//                }
//            }
//            int xLine = 0;
//            fixNumberGap(g2);
//            System.out.println("NUMBER GAP -> " + numberGap + ", and max h - > " + maxRowWeek);
////            int localHeight = maxRowWeek > 1 ? (miniRowHeight * maxRowWeek) - numberGap * 2 : miniRowHeight * maxRowWeek;
//            int localHeight = (numberGap + 10) * maxRowWeek + 30;
//            System.out.println("local H : " + localHeight);
////            int localHeight =  (miniRowHeight * maxRowWeek) - numberGap;
//            //backgroundrow
//            g2.setPaint(i % 2 == 0 ? Color.WHITE : new Color(240, 240, 240));
//            g2.fillRect(0, workingRow, width, localHeight);
//            if (workingRow + localHeight > lowestPoint) {
//                lowestPoint = workingRow + localHeight;
//            }
//
//            for (int j = 0; j < 8; j++) {
////                System.out.println("MAX ROW H = " + maxRowWeek);
//                boolean sunday = j == 7 ? true : false;
//                if (data[i][j].equals("NULL")) {
//                    g2.setColor(Color.BLACK);
//                    g2.setStroke(new BasicStroke(1));
////                    g2.drawLine(xLine + 5, workingRow + 5, xLine + columnWidth - 5, workingRow + localHeight - 5);
//                } else if (data[i][j].contains("#")) {
//
//                    if (greenActive) {
//                        g2.setPaint(new Color(100, 200, 100));
//                        g2.fillRect(xLine, workingRow, columnWidth, localHeight);
//                    }
//
//                    String[] split = data[i][j].split("#");
//                    String number = split[0];
//                    drawNumber(g2, number, xLine, workingRow, sunday);
//                    if (split[1].contains("!")) {
//                        String[] apps = split[1].split("!");
//                        int yYy = -1;
//                        for (String app : apps) {
//                            if (yYy == -1) {
//                                yYy = drawAppointment(g2, app, xLine, workingRow + numberGap);
//                            } else {
//                                yYy = drawAppointment(g2, app, xLine, yYy);
//                            }
//                        }
//                        if (yYy > lowestPoint) {
//                            lowestPoint = yYy;
//                        }
//                    } else {
//                        drawAppointment(g2, split[1], xLine, workingRow + numberGap);
//                    }
//                } else {
//                    drawNumber(g2, data[i][j], xLine, workingRow, sunday);
//                }
//
//                xLine += columnWidth;
////                g2.setStroke(new BasicStroke(3));
////            g2.drawLine(0, workingRow, width, workingRow);
//            }
//            g2.setPaint(Color.BLACK);
//            g2.setStroke(new BasicStroke(1));
//            g2.drawLine(0, workingRow, width, workingRow);
//            workingRow += localHeight;
//        }
//        //END DATA
//
//        //DRAWING HEADER ROWS
//        g2.setColor(Color.BLACK);
//        g2.setStroke(new BasicStroke(2));
//        g2.drawLine(0, 10, width, 10);
//        g2.drawLine(0, 40, width, 40);
//
//        //END DRAWING HEADER ROWS
//        //DRAWING COLUMN
//        int xLine = 0;
//        g2.setColor(Color.BLACK);
//        g2.setStroke(new BasicStroke(3));
//        for (int i = 0; i < 8; i++) {
//            g2.drawLine(xLine, 10, xLine, lowestPoint);
//            xLine += columnWidth;
//            if (i != 7) {
//                g2.setStroke(new BasicStroke(3));
//                g2.drawLine(xLine, 10, xLine, 40);
//            }
//            g2.setStroke(new BasicStroke(3));
//
//        }
//        g2.setStroke(new BasicStroke(3));
//        g2.drawLine(width - 2, 10, width - 2, lowestPoint);
//        //END DRAWING COLUMN
//        g2.setColor(Color.BLACK);
//        g2.setStroke(new BasicStroke(3));
//        System.out.println("LOWEST POINT = " + lowestPoint);
//        g2.drawLine(0, lowestPoint, width, lowestPoint);
    }

    /**
     *
     * @param number x coordinate of up right corner
     * @param x y coordinate of up right corner
     * @param y
     */
    protected void drawNumber(Graphics2D g2, String number, int x, int y, boolean sunday) {
        g2.setFont(new Font("Verdana", Font.BOLD, 12));
        int stringWidth = g2.getFontMetrics().stringWidth(number);
        int stringHeight = g2.getFontMetrics().getHeight();
        g2.setPaint(sunday ? new Color(200, 0, 0) : Color.ORANGE);
        g2.fillRoundRect(x + 5, y + 5, stringWidth + 10, stringHeight + 6, 8, 8);
        g2.setColor(sunday ? Color.WHITE : Color.BLACK);
        g2.drawString(number, x + 5 + 5, y + 5 + stringHeight);
        if (numberGap == -1) {
            numberGap = stringHeight + 5;
        }
    }

    protected int drawAppointment(Graphics2D g2, String appoint, int x, int y) {
        g2.setFont(new Font("Verdana", Font.PLAIN, 9));
        String split[] = appoint.split(";");
        int stringWidth = g2.getFontMetrics().stringWidth(appoint);
        int stringHeight = g2.getFontMetrics().getHeight();
        g2.setColor(Color.BLACK);
        g2.drawString(split[0], x + 5 + 5, y + 10 + stringHeight);
        y = y + stringHeight + 5;
        g2.setFont(new Font("Verdana", Font.BOLD, 9));
        g2.drawString(split[1], x + 5 + 5, y + 5 + stringHeight);
        return y + stringHeight;
    }

    private void fixNumberGap(Graphics2D g2) {
        g2.setFont(new Font("Verdana", Font.BOLD, 12));
        int stringWidth = g2.getFontMetrics().stringWidth("99");
        int stringHeight = g2.getFontMetrics().getHeight();
        numberGap = stringHeight + 5;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new Color(255, 255, 255));

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 679, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 758, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
