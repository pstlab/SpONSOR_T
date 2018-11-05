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
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.border.AbstractBorder;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class RoundedBorderGhosts extends AbstractBorder {

    private final Color color;
    private final int gap;
    private String text = "102";
    private int ghosts = 0;
    private int jobs = 0;
    private ImageIcon icon = new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tt/gui/icons/check32.png"));

    public RoundedBorderGhosts(Color c, int g) {
        color = c;
        gap = g;
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
        g2d.setColor(ghosts >= jobs ? Color.CYAN : Color.RED);

        int stringWidth = g2d.getFontMetrics().stringWidth("" + jobs + "/" + ghosts);
        int stringHeight = g2d.getFontMetrics().getHeight();
        g2d.drawString("" + "" + jobs + "/" + ghosts, (int) ((width - stringWidth) / 2), height - 7); //(int)((height-stringHeight)/2)
        g2d.dispose();
    }

    public void setGhosts(int ghosts) {
        this.ghosts = ghosts;
    }

    public void setJobs(int jobs) {
        this.jobs = jobs;
    }

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
