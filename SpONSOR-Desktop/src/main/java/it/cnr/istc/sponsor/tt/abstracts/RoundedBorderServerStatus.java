/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.abstracts;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.border.AbstractBorder;
import it.cnr.istc.sponsor.tt.logic.MQTTClient;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class RoundedBorderServerStatus extends AbstractBorder {

    private final Color color;
    private final int gap;
    private String text = "CONNECTING..";
    private JLabel labelToRepaint = null;
//    private int personAvailable = 0;
//    private ImageIcon icon = new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/check32.png")); 

    public RoundedBorderServerStatus(Color c, int g, JLabel labelToRepaint) {
        color = c;
        this.labelToRepaint = labelToRepaint;
        gap = g;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(RoundedBorderServerStatus.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    text = MQTTClient.getInstance().isServerActive() ? "ONLINE" : "OFFLINE";
                    labelToRepaint.repaint();

                }
            }
        });
        t.start();
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setColor(color);
        RoundRectangle2D.Double rect = new RoundRectangle2D.Double(x + 1, y + 1, width - 3, height - 3, gap, gap);
//        g2d.draw(rect);
        g2d.fill(rect);
//        g2d.setColor(personAvailable >= 0 ? Color.GREEN : Color.RED);
        if (text.equals("CONNECTING..")) {
            g2d.setColor(Color.YELLOW);
        } else {
            g2d.setColor(text.equals("OFFLINE") ? Color.RED : Color.GREEN);
        }
//        String m = "" + personAvailable+" ("+TrainerManager.getInstance().getPeople().size()+")";
        int stringWidth = g2d.getFontMetrics().stringWidth(text);
        int stringHeight = g2d.getFontMetrics().getHeight();
        g2d.drawString(text, (int) ((width - stringWidth) / 2), height - 7); //(int)((height-stringHeight)/2)

        g2d.dispose();
    }
//
//    public void setPersonAvailable(int personAvailable) {
//        this.personAvailable = personAvailable;
//    }

    @Override
    public Insets getBorderInsets(Component c) {
        return (getBorderInsets(c, new Insets(gap, gap, gap, gap)));
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = insets.top = insets.right = insets.bottom = gap / 2;
        return insets;
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }
}
