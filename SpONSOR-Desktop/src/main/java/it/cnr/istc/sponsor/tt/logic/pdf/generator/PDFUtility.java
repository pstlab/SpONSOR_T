/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.logic.pdf.generator;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.PDExtendedGraphicsState;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDPixelMap;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

/**
 *
 * @author Luca
 */
public class PDFUtility {

    class Coord {

        public float x;
        public float y;

        public Coord(float x, float y) {
            this.x = x;
            this.y = y;
        }

    }

    private static PDFUtility _instance = null;
    private Map<String, Map<String, Coord>> textBoxMap = new HashMap<>();
    PDFont fontPlain = PDType1Font.HELVETICA;
    PDFont fontBold = PDType1Font.HELVETICA_BOLD;
    PDFont fontItalic = PDType1Font.HELVETICA_OBLIQUE;
    PDFont fontMono = PDType1Font.COURIER;
    private List<Integer> strongTableLines = new ArrayList<>();
    private List<Integer> headersStarts = new ArrayList<>();
    public static final String HEADER_TAG = "HEADER_TAG";
    private int startTableY;
    private int endTableY;

    public static PDFUtility getInstance() {
        if (_instance == null) {
            _instance = new PDFUtility();
            return _instance;
        } else {
            return _instance;
        }
    }

    private PDFUtility() {
        super();
    }

    /**
     * disegna un riquadro con i bordi arrotondati, partendo dall'angolo in
     * basso a sinistra
     *
     * @param page
     * @param cos
     * @param startX
     * @param startY
     * @param width
     * @param height
     * @param roundCorner
     * @param stroke
     * @param fill
     * @throws IOException
     */
    public void drawRoundedRect(PDPage page, PDPageContentStream cos, float startX, float startY, float width, float height, int roundCorner, Color stroke, Color fill) throws IOException {

        PDExtendedGraphicsState graphicsState = new PDExtendedGraphicsState();
// set the opacity of the graphics state
//        if (fill.getAlpha() < 255) {
        System.out.println("alpha = " + fill.getAlpha());
        float alpha = ((float) fill.getAlpha()) / 255f;
        System.out.println("new alpha -> " + alpha);
        graphicsState.setNonStrokingAlphaConstant(alpha);

        // find the resources dictionary for the page. In my use case, this is never null.
        PDResources resources = page.findResources();

// try get the graphics state dictionary. Javadocs say it could return null,
// but I'm doubtful...
        Map graphicsStateDictionary = resources.getGraphicsStates();
        if (graphicsStateDictionary == null) {
            // There is no graphics state dictionary in the resources dictionary, create one.
            graphicsStateDictionary = new TreeMap();
        }
        String commandName = "alpha" + (new Date().getTime());
        graphicsStateDictionary.put(commandName, graphicsState);
        resources.setGraphicsStates(graphicsStateDictionary);
        cos.appendRawCommands("/" + commandName + " gs\n");
//        }

        cos.setNonStrokingColor(fill);

        cos.moveTo(startX, startY + roundCorner);
        cos.lineTo(startX, startY + height - roundCorner);
        cos.addBezier312(startX, startY + height, startX, startY + height, startX + roundCorner, startY + height);
        cos.lineTo(startX + width - roundCorner, startY + height);
        cos.addBezier312(startX + width, startY + height, startX + width, startY + height, startX + width, startY + height - roundCorner);
        cos.lineTo(startX + width, startY + roundCorner);
        cos.addBezier312(startX + width, startY, startX + width, startY, startX + width - roundCorner, startY);
        cos.lineTo(startX + roundCorner, startY);
        cos.addBezier312(startX, startY, startX, startY, startX, startY + roundCorner);
        cos.closeSubPath();
        cos.fill(0);
        if (stroke != null) {
            cos.setStrokingColor(stroke);
            cos.stroke();
        }

    }

    public float addFieldsBoxRG(PDPageContentStream cos, float startX, float startY, float margin, String[] keywords, String boxname, boolean bold, int size, Color textColor) throws IOException {
        //alliena a destra e fissa le coordinate di scrittura
        cos.setFont(bold ? fontBold : fontPlain, size);
        cos.setNonStrokingColor(textColor);
        System.out.println("start x : " + startX);
        System.out.println("margin  : " + margin);
//        cos.addLine(startX, startY, 1000, startY);
//        cos.addLine(startX, startY, startX, 0);
//        cos.closeAndStroke();

        //trova la chiave più lunga
        String maxValue = Arrays.asList(keywords).stream().max(new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                return o1.length();
            }
        }).get();

        PDFont choosenFont = bold ? fontBold : fontPlain;
        float baseLine = startY + margin;
        float stringHeight = size + 6;
        float limit = choosenFont.getStringWidth(maxValue + ":") / 1000 * size;
        System.out.println("=====================================");
        baseLine -= margin;
        float globalX = startX + margin + margin + margin + startX + limit;
        this.textBoxMap.put(boxname, new HashMap<String, Coord>());
        for (String keyword : keywords) {
            System.out.println("-----------------------------");
            baseLine -= stringHeight;
            cos.beginText();
            float stringWidth = choosenFont.getStringWidth(keyword + ":") / 1000 * size;
//            float xxx = startX+limit-stringWidth < margin ? margin: startX+limit-stringWidth;
            System.out.println("key at : " + (margin + startX + limit - stringWidth));
            System.out.println("keyword: " + keyword);
            System.out.println("size   : " + stringWidth);
            cos.moveTextPositionByAmount(startX + margin + margin + margin + startX + limit - stringWidth, baseLine);
            cos.drawString(keyword + ":");
            cos.endText();
            textBoxMap.get(boxname).put(keyword, new Coord(globalX, baseLine));

        }
        return baseLine;

    }

    public void addTextBoxRG(PDPageContentStream cos, float startX, float startY, float margin, String[] keywords, boolean bold, int size, Color textColor, int interline) throws IOException {
        //alliena a destra e fissa le coordinate di scrittura
        cos.setFont(bold ? fontBold : fontPlain, size);
        cos.setNonStrokingColor(textColor);
//        cos.addLine(startX, startY, 1000, startY);
//        cos.addLine(startX, startY, startX, 0);
//        cos.closeAndStroke();

        //trova la chiave più lunga
        String maxValue = Arrays.asList(keywords).stream().max(new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                return o1.length();
            }
        }).get();

        PDFont choosenFont = bold ? fontBold : fontPlain;
        float baseLine = startY + margin;
        float stringHeight = size + interline;
        float limit = choosenFont.getStringWidth(maxValue) / 1000 * size;
        System.out.println("=====================================");
        baseLine -= margin;
        for (String keyword : keywords) {
            System.out.println("-----------------------------");
            baseLine -= stringHeight;
            cos.beginText();
            float stringWidth = choosenFont.getStringWidth(keyword) / 1000 * size;
            cos.moveTextPositionByAmount(startX + margin + margin + margin + startX + limit - stringWidth, baseLine);
            cos.drawString(keyword);
            cos.endText();

        }

    }

    public void addTextBoxValue(PDPageContentStream cos, String boxName, String field, String value, boolean bold, Color textColor, float distance, int size) throws IOException {
        cos.setFont(bold ? fontBold : fontPlain, size);
        cos.setNonStrokingColor(textColor);
        cos.beginText();
        cos.moveTextPositionByAmount(this.textBoxMap.get(boxName).get(field).x + distance, this.textBoxMap.get(boxName).get(field).y);
        cos.drawString(value);
        cos.endText();
    }

    /**
     * aggiunge una linea centrata orizzontale all'altezza h con margini destro
     * e sinistro uguale a "margin".
     *
     * @param h
     * @param margin
     * @param color
     * @param size
     */
    public void addCenteredLine(PDPage page, PDPageContentStream cos, int h, int margin, Color color, float size) throws IOException {

        PDRectangle rect = page.getMediaBox();

        cos.setStrokingColor(color);
        cos.setLineWidth(size);
        cos.drawLine(margin, h, rect.getWidth() - margin, h);
    }

    public void addLargeText(PDPageContentStream cos, float startX, float startY, float width, float margin, float fontSize, String text, Color textColor) throws IOException {
        PDFont pdfFont = fontPlain;
//        float fontSize = 25;
        float leading = 1.5f * fontSize;
        cos.setNonStrokingColor(textColor);
//        PDRectangle mediabox = page.findMediaBox();
//        float margin = 72;
//        float width = mediabox.getWidth() - 2 * margin;
//        float startX = mediabox.getLowerLeftX() + margin;
//        float startY = mediabox.getUpperRightY() - margin;

//        String text = "I am trying to create a PDF file with a lot of text contents in the document. I am using PDFBox";
        List<String> lines = new ArrayList<String>();
        int lastSpace = -1;
        while (text.length() > 0) {
            int spaceIndex = text.indexOf(' ', lastSpace + 1);
            if (spaceIndex < 0) {
                lines.add(text);
                text = "";
            } else {
                String subString = text.substring(0, spaceIndex);
                float size = fontSize * pdfFont.getStringWidth(subString) / 1000;
                if (size > width) {
                    if (lastSpace < 0) // So we have a word longer than the line... draw it anyways
                    {
                        lastSpace = spaceIndex;
                    }
                    subString = text.substring(0, lastSpace);
                    lines.add(subString);
                    text = text.substring(lastSpace).trim();
                    lastSpace = -1;
                } else {
                    lastSpace = spaceIndex;
                }
            }
        }

        cos.beginText();
        cos.setFont(pdfFont, fontSize);
        cos.moveTextPositionByAmount(startX, startY);
        for (String line : lines) {
            cos.drawString(line);
            cos.moveTextPositionByAmount(0, -leading);
        }
        cos.endText();
    }

    public void setBackgroundImage(PDDocument document, PDPage page, PDPageContentStream cos, BufferedImage image) throws IOException {
        try {
//            BufferedImage awtImage = ImageIO.read(new File("Sample.jpg"));
            PDRectangle rect = page.getMediaBox();
            PDXObjectImage ximage = new PDPixelMap(document, image);
            cos.drawXObject(ximage, 0, 0, rect.getWidth(), rect.getHeight());
        } catch (FileNotFoundException fnfex) {
            System.out.println("No image for you");
        }
    }

    public void setBackgroundImage(PDDocument document, PDPage page, PDPageContentStream cos, File fileImage) throws IOException {
        try {

            BufferedImage awtImage = ImageIO.read(fileImage);
            PDRectangle rect = page.getMediaBox();
            PDXObjectImage ximage = new PDPixelMap(document, awtImage);
            cos.drawXObject(ximage, 0, 0, rect.getWidth(), rect.getHeight());
        } catch (FileNotFoundException fnfex) {
            System.out.println("No image for you");
        }
    }

    public void insertLogo(PDDocument document, PDPage page, PDPageContentStream cos, File fileImage) throws IOException {
        try {
            BufferedImage awtImage = ImageIO.read(fileImage);
            PDRectangle rect = page.getMediaBox();
            PDXObjectImage ximage = new PDPixelMap(document, awtImage);
            cos.drawXObject(ximage, 30, rect.getHeight() - 84, 128, 47);
        } catch (FileNotFoundException fnfex) {
            System.out.println("No image for you");
        }
    }

    /**
     *
     * @param document
     * @param page
     * @param cos
     * @param fileImage
     * @param Title
     * @param comment
     * @param backgroundm
     * @param x in basso a sinistra
     * @param y in basso a sinistra
     * @param imageW
     * @param imageH
     * @throws IOException
     */
    public void addImage(PDDocument document, PDPage page, PDPageContentStream cos, File fileImage, String title, String comment, Color backgroundm, float x, float y, float imageW, float imageH) throws IOException {
        try {
            this.drawRoundedRect(page, cos, x - 10, y - 60, imageW + 20, imageH + 20 + 50, 10, null, backgroundm);

            PDExtendedGraphicsState graphicsState = new PDExtendedGraphicsState();
            float alpha = 1;
            System.out.println("new alpha1 -> " + alpha);
            graphicsState.setNonStrokingAlphaConstant(alpha);
            PDResources resources = page.findResources();
            Map graphicsStateDictionary = resources.getGraphicsStates();
            if (graphicsStateDictionary == null) {
                graphicsStateDictionary = new TreeMap();
            }
            String commandName = "alpha2" + (new Date().getTime());
            graphicsStateDictionary.put(commandName, graphicsState);
            resources.setGraphicsStates(graphicsStateDictionary);
            cos.appendRawCommands("/" + commandName + " gs\n");

            BufferedImage awtImage = ImageIO.read(fileImage);
            PDRectangle rect = page.getMediaBox();
            PDXObjectImage ximage = new PDPixelMap(document, awtImage);
            cos.drawXObject(ximage, x, y, imageW, imageH);
            cos.beginText();
            cos.moveTextPositionByAmount(x - 10, y + imageH + 10);
            cos.setFont(fontBold, 10);
            cos.drawString(title);
            cos.endText();
        } catch (FileNotFoundException fnfex) {
            System.out.println("No image for you");
        }
    }

//    public void addCalendarAsTable(PDPage page, PDPageContentStream cos, PDFTable table, float x, float y, float width){
//        
//    } 
    public void addTable(PDPage page, PDPageContentStream cos, PDFTable table, float x, float y, float width) throws IOException {
        PDRectangle rect = page.getMediaBox();
        PDFTableRenderer renderer = table.getRenderer();
        //header section
        float minW = 0;
        for (int i = 0; i < table.getHeader().length; i++) {
            minW += (renderer.getColumnSize(i) == -1 ? 0 : renderer.getColumnSize(i));
        }

        if (minW > width) {
            minW = width;
        }
        System.out.println("minwW = " + minW);
        float standardColumnSize = (width - minW) / (table.getHeader().length - 1);
        System.out.println("standard size = " + standardColumnSize);
        System.out.println("WWW = " + width);
        System.out.println("outside line size = " + table.getOutsideLineSize());
        System.out.println("grid line size = " + table.getGridLineSize());

        cos.setStrokingColor(table.getGridLineColor());
        cos.setLineWidth(table.getGridLineSize());
        float rowLineX = 0;

        for (int i = 0; i < table.getHeader().length; i++) {
            //write the header name:
            cos.setFont(renderer.getCellFont(0, i, table.getHeader()[i]), renderer.getCellTextSize(0, i, table.getHeader()[i]));
            cos.beginText();
            cos.setNonStrokingColor(renderer.getCellForeground(0, i, table.getHeader()[i]));
            cos.moveTextPositionByAmount(x + table.gethInset() + rowLineX, y - table.gethInset() - renderer.getCellTextSize(0, i, table.getHeader()[i]));
            cos.drawString(table.getHeader()[i]);
            cos.endText();
            rowLineX = rowLineX + (renderer.getColumnSize(i) == -1 ? standardColumnSize : renderer.getColumnSize(i));
        }

        rowLineX = renderer.getColumnSize(0) == -1 ? standardColumnSize : renderer.getColumnSize(0);
        String[][] data = table.getData();

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < 10; j++) {
                if(data[i][j].equals(HEADER_TAG)){
                    
                }
            }

        }

    }

    public void addTableOLD(PDPage page, PDPageContentStream cos, PDFTable table, float x, float y, float width) throws IOException {

        PDRectangle rect = page.getMediaBox();
        PDFTableRenderer renderer = table.getRenderer();

        //header section
        float minW = 0;
        for (int i = 0; i < table.getHeader().length; i++) {
            minW += (renderer.getColumnSize(i) == -1 ? 0 : renderer.getColumnSize(i));
        }

        if (minW > width) {
            minW = width;
        }
        System.out.println("minwW = " + minW);
        float standardColumnSize = (width - minW) / (table.getHeader().length - 1);
        System.out.println("standard size = " + standardColumnSize);
        System.out.println("WWW = " + width);
        System.out.println("outside line size = " + table.getOutsideLineSize());
        System.out.println("grid line size = " + table.getGridLineSize());

        cos.setStrokingColor(table.getGridLineColor());
        cos.setLineWidth(table.getGridLineSize());
        float rowLineX = 0;

        for (int i = 0; i < table.getHeader().length; i++) {
            //write the header name:
            cos.setFont(renderer.getCellFont(0, i, table.getHeader()[i]), renderer.getCellTextSize(0, i, table.getHeader()[i]));
            cos.beginText();
            cos.setNonStrokingColor(renderer.getCellForeground(0, i, table.getHeader()[i]));
            cos.moveTextPositionByAmount(x + table.gethInset() + rowLineX, y - table.gethInset() - renderer.getCellTextSize(0, i, table.getHeader()[i]));
            cos.drawString(table.getHeader()[i]);
            cos.endText();
            rowLineX = rowLineX + (renderer.getColumnSize(i) == -1 ? standardColumnSize : renderer.getColumnSize(i));
        }

        rowLineX = renderer.getColumnSize(0) == -1 ? standardColumnSize : renderer.getColumnSize(0);
        int tableHeight = table.gethInset() * (2 * (table.getRows() + 2));
//        for (int i = 0; i < table.getRows(); i++) {
//            tableHeight += table.getRenderer().getCellTextSize(0, i, "");
//        }
//        for (int i = 1; i < table.getHeader().length; i++) {
//            System.out.println("rowLineX = " + rowLineX);
//            if (table.getGridLineSize() > 0f) {
//                cos.drawLine(x + rowLineX, y, x + rowLineX, y - tableHeight);
//            }
//            System.out.println("X LINE -> " + (x + rowLineX));
//            rowLineX = rowLineX + (renderer.getColumnSize(i) == -1 ? standardColumnSize : renderer.getColumnSize(i));
//        }
//
//        if (table.getOutsideLineSize() > 0f) {
//            cos.setLineWidth(table.getOutsideLineSize());
//            cos.setStrokingColor(table.getOutsideLineColor());
//            cos.drawLine(x, y, x + width, y); // TOP MAIN LINE
//            float uqqy = y - 2 * table.gethInset() - renderer.getCellTextSize(0, 0, table.getHeader()[0]);
//            cos.drawLine(x, uqqy, x + width, uqqy); // TOP MAIN LINE
//
//        }

        String[][] data = table.getData();
        float downY = 0;
        if (data.length != 0) {

            float yay = y - 3 * table.gethInset() - renderer.getCellTextSize(0, 0, table.getHeader()[0]) - renderer.getCellTextSize(1, 1, data[1][1]);
            for (int i = 0; i < data.length; i++) {
                rowLineX = 0;
                float xXx = rowLineX + x + table.gethInset();
                for (int j = 0; j < table.getColumns(); j++) {
//                    if (!data[i][j].isEmpty()) {

                    String value = data[i][j];
                    String time = "";
                    String activity = "";
                    if (!value.isEmpty()) {
                        String[] split = value.split(";");
                        time = split[0];
                        activity = split[1];
                        cos.setNonStrokingColor(Color.GREEN);
                        cos.fillPolygon(new float[]{xXx - table.gethInset(), xXx + 65.7f, xXx + 65.7f, xXx - table.gethInset()}, new float[]{yay + 3 * table.gethInset() - table.getGridLineSize(), yay + 3 * table.gethInset() - table.getGridLineSize(), yay - 3 * table.gethInset() - 2, yay - 3 * table.gethInset() - 2});
//                        if (i < data.length - 1) {
                        cos.setLineWidth(table.getGridLineSize());
                        cos.setStrokingColor(table.getGridLineColor());
                        cos.drawLine(x, yay - 3 * table.gethInset() - 2, x + width - 5, yay - 3 * table.gethInset() - 2);
//                        }
                    }

                    cos.setFont(renderer.getCellFont(i + 1, j, time), renderer.getCellTextSize(i + 1, j, time));
                    cos.beginText();
                    cos.setNonStrokingColor(renderer.getCellForeground(i + 1, j, time));
                    System.out.println("moving to: " + xXx);
                    cos.moveTextPositionByAmount(xXx, yay);
                    cos.drawString(time);
                    cos.endText();
//                    xXx += (renderer.getColumnSize(j) == -1 ? standardColumnSize : renderer.getColumnSize(j));

                    yay -= 10;

                    cos.setFont(PDFTableRenderer.fontBold, renderer.getCellTextSize(i + 1, j, activity));
                    cos.beginText();
                    cos.setNonStrokingColor(renderer.getCellForeground(i + 1, j, activity));
                    System.out.println("moving to: " + xXx);
                    cos.moveTextPositionByAmount(xXx, yay);
                    cos.drawString(activity);
                    cos.endText();
                    xXx += (renderer.getColumnSize(j) == -1 ? standardColumnSize : renderer.getColumnSize(j));
//                    } 
//                    else {
//                        cos.beginText();
//                        cos.moveTextPositionByAmount(xXx, yay);
//                        yay -= 10;
//                        cos.moveTextPositionByAmount(xXx, yay);
//                        cos.endText();
//                    }
                    yay += 10;
//                    xXx-=0.6f;
                }

                yay -= 10;

                yay -= (2 * table.gethInset() + renderer.getCellTextSize(i, 0, data[i][0])) - 2; // +12
//                if (i < data.length - 1) {
//                    cos.setLineWidth(table.getGridLineSize());
//                    cos.setStrokingColor(table.getGridLineColor());
//                    cos.drawLine(x, yay+12, x + width - 5, yay+12);
//                }
            }
            downY = yay;
            downY += (2 * table.gethInset()) + 2;
        }

        for (int i = 0; i < table.getRows(); i++) {
            tableHeight += table.getRenderer().getCellTextSize(0, i, "");
        }
        for (int i = 0; i <= table.getHeader().length; i++) {
            if (i == 0 || i == table.getHeader().length) {
                cos.setLineWidth(1f);
            } else {
                cos.setLineWidth(table.getGridLineSize());
            }
            System.out.println("rowLineX = " + rowLineX);
            if (table.getGridLineSize() > 0f) {
                cos.drawLine(x + rowLineX, y, x + rowLineX, downY);
            }
            System.out.println("X LINE -> " + (x + rowLineX));
            rowLineX = rowLineX + (renderer.getColumnSize(i) == -1 ? standardColumnSize : renderer.getColumnSize(i));
        }

        if (table.getOutsideLineSize() > 0f) {
            cos.setLineWidth(1f);
            cos.setStrokingColor(table.getOutsideLineColor());
            cos.drawLine(x, y, x + width - 5, y); // TOP MAIN LINE
            float uqqy = y - 2 * table.gethInset() - renderer.getCellTextSize(0, 0, table.getHeader()[0]);
            cos.drawLine(x, uqqy, x + width - 5, uqqy); // TOP MAIN LINE
            cos.drawLine(x, downY, x + width - 5, downY); // DOWN MAIN LINE

        }

    }

}
