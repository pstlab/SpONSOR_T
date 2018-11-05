/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.logic.pdf.generator;

import java.awt.Color;
import java.awt.Image;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 *
 * @author Luca
 */
public abstract class PDFTableRenderer {

    public static PDFont fontPlain = PDType1Font.HELVETICA;
    public static PDFont fontBold = PDType1Font.HELVETICA_BOLD;
    public static PDFont fontItalic = PDType1Font.HELVETICA_OBLIQUE;
    public static PDFont fontMono = PDType1Font.COURIER;

    public abstract Color getCellBackground(int row, int column, String value);

    public abstract Color getCellForeground(int row, int column, String value);

    public abstract PDFont getCellFont(int row, int column, String value);

    public abstract Image switchTextToIcon(int row, int column, String value);

    public abstract int getCellTextSize(int row, int column, String value);

    public abstract int getColumnSize(int column);

}
