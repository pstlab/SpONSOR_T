/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.activities;

import it.cnr.istc.sponsor.tt.abstracts.MyJLayer;
import it.cnr.istc.sponsor.tt.abstracts.MyLayer;
import it.cnr.istc.sponsor.tt.gui.engine.GuiEventManager;
import java.awt.AWTEvent;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.TransferHandler;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class ActivityTurnGlassPane extends MyLayer<JComponent> {

    private float transparencyCoefficient = 0.75f;
    private Color background = Color.BLACK;
    private Color foreground = Color.WHITE;
    private JComponent installedComponent;
    private int clockWidth = 200;
    private int clochHeight = 60;
    private Timer timer = new Timer();
    private int x = -1;
    private int y = -1;
    private int dx = 0;
    private int dy = 0;
    private boolean saved = false;

    public ActivityTurnGlassPane() {
        super();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                ActivityTurnGlassPane.this.repaint();
            }
        }, 0, 1000);

    }

    public void setX(int x) {
        this.x = x;
        if (this.installedComponent != null) {
            this.installedComponent.repaint();
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    

    public void setY(int y) {
        this.y = y;
        if (this.installedComponent != null) {
            this.installedComponent.repaint();
        }
    }

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public Color getForeground() {
        return foreground;
    }

    public void setForeground(Color foreground) {
        this.foreground = foreground;
    }

    public void setTransparencyCoefficient(float transparencyCoefficient) {
        this.transparencyCoefficient = transparencyCoefficient;
    }

    public float getTransparencyCoefficient() {
        return transparencyCoefficient;
    }

    public int getClockWidth() {
        return clockWidth;
    }

    public void setClockWidth(int clockWidth) {
        this.clockWidth = clockWidth;
    }

    public int getClochHeight() {
        return clochHeight;
    }

    public void setClochHeight(int clochHeight) {
        this.clochHeight = clochHeight;
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        System.out.println("INSTALLED c: "+c.getClass().getCanonicalName());
        MyJLayer jlayer = (MyJLayer) c;
        jlayer.setLayerEventMask(
                AWTEvent.MOUSE_EVENT_MASK
                | AWTEvent.MOUSE_MOTION_EVENT_MASK);
    }

    @Override
    public void uninstallUI(JComponent c) {
        MyJLayer jlayer = (MyJLayer) c;
        jlayer.setLayerEventMask(0);
        super.uninstallUI(c);
    }

    public void repaint() {
        if (this.installedComponent != null) {
            this.installedComponent.repaint();
        }
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);
        this.installedComponent = c;
        if (x == -1) {
            x = c.getWidth() - clockWidth;
        }
        if (y == -1) {
            y = 10;
        }
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        RoundRectangle2D base = new RoundRectangle2D.Double(x, y, clockWidth - 10, clochHeight, 15, 15);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                transparencyCoefficient));
        g2.setPaint(background);
        g2.fill(base);
        Font font = new Font(Font.SERIF, Font.BOLD, 20);
        Date date = new Date();
        SimpleDateFormat h_format = new SimpleDateFormat("HH:mm:ss");
        String hh = h_format.format(date);
        font = scaleFont(hh, base.getBounds(), g, font);
        g2.setFont(font);
        FontMetrics fm = g.getFontMetrics(font);
        int height = fm.getHeight();
        g2.setPaint(foreground);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                0.9f));
        g2.drawString(hh, x + ((clockWidth - fm.stringWidth(hh) - 10) / 2), y + height - 5);
        
        
        g2.dispose();
    }

    public Font scaleFont(String text, Rectangle rect, Graphics g, Font pFont) {
        float fontSize = 20.0f;
        Font font = pFont;

        font = g.getFont().deriveFont(fontSize);
        int width = g.getFontMetrics(font).stringWidth(text);
        fontSize = (rect.width / width) * fontSize;
        return g.getFont().deriveFont(fontSize);
    }

    private boolean isMouseInsideClock(int _x, int _y) {
        boolean dentro = (_x >= x && _x <= x + clockWidth && _y >= y && _y <= y + clochHeight);
        if(dentro){
            System.out.println("DENTRO");
        }else{
            System.out.println("FUORi");
        }
        return dentro;
    }

    @Override
    protected void processMouseEvent(MouseEvent e, MyJLayer l) {
//        System.out.println("X: "+e.getX());
//        System.out.println("Y: "+e.getY());
        if (!isMouseInsideClock(e.getX(), e.getY())) {
//            System.out.println("FUORI");
            return;
        }
//        System.out.println("DENTRO");

        if (e.getID() == MouseEvent.MOUSE_PRESSED) {
//            System.out.println("mause pressed");
            if (!saved) {
                dx = e.getX() - x;
                dy = e.getY() - y;
                saved = true;
//                System.out.println("saved");
            }
        }
        if (e.getID() == MouseEvent.MOUSE_RELEASED) {
            saved = false;
        }
//        if (e.getID() == MouseEvent.MOUSE_DRAGGED) {
//            System.out.println("i must drag !");
//            x = e.getX() - dx;
//            y = e.getY() - dy;
//
////            setBounds(lab.getX() +e.getX() -dx,lab.getY() +e.getY() -dy, lab.getWidth(),lab.getHeight());
//        }
//        l.repaint();
    }

    @Override
    protected void processMouseMotionEvent(MouseEvent e, MyJLayer l) {
        if (e.getID() == MouseEvent.MOUSE_DRAGGED && saved) {
//            System.out.println("i must drag !");
            x = e.getX() - dx;
            y = e.getY() - dy;
//            dx=x;
//            dy=y;
            l.repaint();
        }
    }
    
    
    
    

}
