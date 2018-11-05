/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.abstracts;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import javax.swing.JPanel;

/**
 *
 * @author jure, Luca Coraci
 */
public class ImagePanel extends JPanel {

    private Image image;
    private ImageObserver observer;

    public void setImage(Image image) {
        this.image = image;
    }

    public void setObserver(ImageObserver observer) {
        this.observer = observer;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, getWidth(), getHeight(), observer);
        super.paint(g);
    }
}
