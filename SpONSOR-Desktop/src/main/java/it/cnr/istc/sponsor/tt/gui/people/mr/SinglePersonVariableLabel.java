/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.people.mr;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it>
 */
public class SinglePersonVariableLabel extends JLabel {

    private String value;
    private boolean circled = false;
    private boolean squared = false;

    public SinglePersonVariableLabel() {
        super();
    }

    public SinglePersonVariableLabel(String value) {
        super();
        this.value = value;
//        this.setText(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
//        this.setText(value);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g); //To change body of generated methods, choose Tools | Templates.
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        int width = this.getWidth();
        int height = this.getHeight();
        g2.setFont(new Font("Verdana", Font.BOLD, 12));
        int stringWidth = g2.getFontMetrics().stringWidth(value);
        int stringHeight = g2.getFontMetrics().getHeight();
        if (this.getHorizontalAlignment() == SwingConstants.CENTER) {
            g2.drawString(value, width / 2 - stringWidth / 2, height - 5);
        } else if (this.getHorizontalAlignment() == SwingConstants.LEFT) {
            g2.drawString(value, 10, height - 5);
        }

        if (squared) {
            g2.setPaint(new Color(0, 180, 0));
            g2.setStroke(new BasicStroke(2));
            g2.drawRect(width / 2 - 15, height / 2 - 10, 30, 20);
        }
        if (circled) {
            g2.setPaint(Color.RED);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(width / 2 - 13, height / 2 - 9, 26, 18);
        }
    }

    public boolean isCircled() {
        return circled;
    }

    public void setCircled(boolean circled) {
        this.circled = circled;
    }

    public boolean isSquared() {
        return squared;
    }

    public void setSquared(boolean squared) {
        this.squared = squared;
    }

}
