/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.logic.pdf.generator.test;

import it.cnr.istc.sponsor.tt.logic.pdf.generator.PDFTableRenderer;
import java.awt.Color;
import java.awt.Image;
import org.apache.pdfbox.pdmodel.font.PDFont;


/**
 *
 * @author Luca
 */
public class MyTableRenderer extends PDFTableRenderer {

    @Override
    public Color getCellBackground(int row, int column, String value) {
        return Color.CYAN;
    }

    @Override
    public Color getCellForeground(int row, int column, String value) {
        if (row == 0) {
            return Color.BLUE;
        } else {
            return Color.BLACK;
        }
    }

    @Override
    public PDFont getCellFont(int row, int column, String value) {
        if (row == 0) {
            return PDFTableRenderer.fontBold;
        } else {
            return PDFTableRenderer.fontPlain;
        }
    }

    @Override
    public Image switchTextToIcon(int row, int column, String value) {
        return null;
    }

    @Override
    public int getCellTextSize(int row, int column, String value) {
        if (row == 0) {
            return 10;
        } else {
            return 8;
        }
    }

    @Override
    public int getColumnSize(int column) {

        if (column == 0) {
            return 70;
        } else if (column == 1) {
            return 70;
        }else if (column == 2) {
            return 70;
        }else if (column == 3) {
            return 70;
        }else if (column == 4) {
            return 70;
        }else if (column == 5) {
            return 70;
        }else if (column == 6) {
            return 70;
        } else {
            return -1;

        }
    }

}
