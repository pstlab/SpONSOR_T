/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tc.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import javax.swing.JButton;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class RedButton extends JButton {

    private String buttonText = "Avanti";

    private Color rollOverColor = Color.RED.darker();
    private Color backgroundColor = Color.RED;
    private Color currentColor = backgroundColor;

    public RedButton() {
        super();
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e); //To change body of generated methods, choose Tools | Templates.
                if(!isEnabled()){
                    return;
                }
                currentColor = rollOverColor;
                RedButton.this.setCursor(java.awt.Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e); //To change body of generated methods, choose Tools | Templates.
                if(!isEnabled()){
                    return;
                }
                currentColor = backgroundColor;
                RedButton.this.setCursor(java.awt.Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e); //To change body of generated methods, choose Tools | Templates.
                if(!isEnabled()){
                    return;
                }
                currentColor = rollOverColor.darker();
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e); 
                if(!isEnabled()){
                    return;
                }
                currentColor = rollOverColor;
                RedButton.this.setCursor(java.awt.Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                repaint();
            }
            
            

        });
    }

    @Override
    public void setText(String text) {
        super.setText(text); //To change body of generated methods, choose Tools | Templates.
        setButtonText(text);
    }
    
    

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    
    
//    @Override
//    protected void paintComponent(Graphics g) {
//        Graphics2D g2 = (Graphics2D) g.create();
//        g2.setRenderingHint( 
//                RenderingHints.KEY_TEXT_ANTIALIASING,
//                RenderingHints.VALUE_TEXT_ANTIALIAS_ON
//        );
////        g2.setPaint(new GradientPaint(new Point(0, 0), Color.WHITE, new Point(0,
////                getHeight()), Color.PINK.darker()));
//        g2.setPaint(Color.RED.darker());
//        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
////        g2.setPaint(Color.BLACK);
//        g2.drawString(getText(), 30, 12);
//        g2.dispose();
//
//         super.paintComponent(g2);
//    }
    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        g2.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        );
//        g2.setPaint(new GradientPaint(new Point(0, 0), Color.WHITE, new Point(0,
//                getHeight()), Color.PINK.darker()));
        g2.setPaint(currentColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        g2.setPaint(Color.WHITE);
        Font f = new Font("Comic Sans MS", Font.BOLD, 24);
        g2.setFont(f);
        FontMetrics fm = g2.getFontMetrics();
        FontRenderContext frc = g2.getFontRenderContext();
//        int text_height = (int)f.getLineMetrics(buttonText, frc).getHeight();
        int text_height = (int) (f.getLineMetrics(buttonText, frc).getAscent() - (int) f.getLineMetrics(buttonText, frc).getDescent());
        int text_width = fm.stringWidth(buttonText);
        g2.drawString(buttonText, (getWidth() - text_width) / 2, getHeight() / 2 + text_height / 2);
//        g2.drawLine(0, getHeight()/2 + text_height/2, 200,  getHeight()/2 + text_height/2);
//        g2.drawLine(100, getHeight()/2 + text_height/2, 100,  getHeight()/2 + text_height/2 - text_height);
        g2.dispose();

//         super.paintComponent(g2);
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b); //To change body of generated methods, choose Tools | Templates.
        if(!b){
            System.out.println("LIGHT GRAY");
            currentColor = Color.LIGHT_GRAY;
        }else{
            currentColor = backgroundColor;
        }
    }
    
    

}
