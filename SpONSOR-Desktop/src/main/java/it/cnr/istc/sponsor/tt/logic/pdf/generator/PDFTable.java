/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.logic.pdf.generator;

import java.awt.Color;
import java.awt.Image;
import org.apache.pdfbox.pdmodel.font.PDFont;

/**
 *
 * @author Luca
 */
public class PDFTable {

    private String[] header;
    private int columns;
    private int rows;
    private HORIZONTAL_ALIGN headerHorizontalAlign = HORIZONTAL_ALIGN.CENTER;
    private int hInset = 4;
    private int vInset = 2;
    private float outsideLineSize = 0.5f;
    private float gridLineSize = 0.3f;
    private Color outsideLineColor = Color.BLACK;
    private Color gridLineColor = Color.BLACK;
    private PDFTableRenderer renderer = new BasicRenderer();
    private String[][] data;

    public String[] getHeader() {
        return header;
    }

    public void setHeader(String[] header) {
        this.header = header;
        this.columns = header.length;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumn(int column) {
        this.columns = column;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public HORIZONTAL_ALIGN getHeaderHorizontalAlign() {
        return headerHorizontalAlign;
    }

    public void setHeaderHorizontalAlign(HORIZONTAL_ALIGN headerHorizontalAlign) {
        this.headerHorizontalAlign = headerHorizontalAlign;
    }

    public int gethInset() {
        return hInset;
    }

    public void sethInset(int hInset) {
        this.hInset = hInset;
    }

    public int getvInset() {
        return vInset;
    }

    public void setvInset(int vInset) {
        this.vInset = vInset;
    }

    public float getOutsideLineSize() {
        return outsideLineSize;
    }

    public void setOutsideLineSize(float outsideLineSize) {
        this.outsideLineSize = outsideLineSize;
    }

    public float getGridLineSize() {
        return gridLineSize;
    }

    public void setGridLineSize(float gridLineSize) {
        this.gridLineSize = gridLineSize;
    }

    public Color getOutsideLineColor() {
        return outsideLineColor;
    }

    public void setOutsideLineColor(Color outsideLineColor) {
        this.outsideLineColor = outsideLineColor;
    }

    public Color getGridLineColor() {
        return gridLineColor;
    }

    public void setGridLineColor(Color gridLineColor) {
        this.gridLineColor = gridLineColor;
    }

    public void setRenderer(PDFTableRenderer renderer) {
        this.renderer = renderer;
    }

    public PDFTableRenderer getRenderer() {
        return renderer;
    }

    public String[][] getData() {
        return data;
    }

    public void setData(String[][] data) throws Exception {
        if(header == null){
            throw new Exception("header must be already defined");
        }
        int m = -1;
        for (String[] row : data) {
            if (row.length > m) {
                m = row.length;
                if (m != header.length) {
                    throw new Exception("Wrong data format, m != header.lenght");
                }
            }
        }
        this.data = data;
        this.columns = m;
        this.rows = data.length;
    }

    public static enum HORIZONTAL_ALIGN {

        LEFT, CENTER, RIGHT
    };

    public class BasicRenderer extends PDFTableRenderer {

        @Override
        public Color getCellBackground(int row, int column, String value) {
            return Color.WHITE;
        }

        @Override
        public Color getCellForeground(int row, int column, String value) {
            return Color.MAGENTA;
        }

        @Override
        public Image switchTextToIcon(int row, int column, String value) {
            return null;
        }

        @Override
        public int getCellTextSize(int row, int column, String value) {
            return 6;
        }

        @Override
        public PDFont getCellFont(int row, int column, String value) {
            if (row == 0) {
                return fontBold;
            } else {
                return fontPlain;
            }
        }

        @Override
        public int getColumnSize(int column) {
            return -1; // all equals
        }

    }

}
