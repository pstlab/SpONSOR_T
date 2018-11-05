/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.logic.pdf.generator;

import it.cnr.istc.sponsor.tt.logic.pdf.generator.test.MyTableRenderer;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 *
 * @author Luca
 */
public class AnagraficaPrinter {

    private static AnagraficaPrinter _instance = null;

    public static AnagraficaPrinter getInstance() {
        if (_instance == null) {
            _instance = new AnagraficaPrinter();
            return _instance;
        } else {
            return _instance;
        }
    }

    private AnagraficaPrinter() {
        super();
    }

    public void print(Persona p) {

        try {
            PDFont fontPlain = PDType1Font.HELVETICA;
            PDFont fontBold = PDType1Font.HELVETICA_BOLD;
            PDFont fontItalic = PDType1Font.HELVETICA_OBLIQUE;
            PDFont fontMono = PDType1Font.COURIER;

            String outputFileName = p.getNome() + "_" + p.getCognome() + "_" + (new Date().getTime() + ".pdf");
            PDDocument document = new PDDocument();
            PDPage page1 = new PDPage(PDPage.PAGE_SIZE_A4);
            PDRectangle rect = page1.getMediaBox();
            document.addPage(page1);

            PDPageContentStream cos = new PDPageContentStream(document, page1);

            int line = 0;

            cos.setNonStrokingColor(Color.GRAY);
//            cos.fillPolygon(new float[]{10, rect.getWidth() - 10, rect.getWidth() - 10, 10}, new float[]{rect.getHeight() - 400, rect.getHeight() - 400, rect.getHeight() - 20, rect.getHeight() - 20});
            // Define a text content stream using the selected font, move the cursor and draw some text

            cos.setStrokingColor(Color.RED);

            PDFUtility.getInstance().setBackgroundImage(document, page1, cos, new File("Sample.jpg"));

            PDFUtility.getInstance().drawRoundedRect(page1, cos, 20, 20, rect.getWidth() - 40, rect.getHeight() - 40, 15, Color.BLUE, new Color(0, 0, 100, 150));

            PDFUtility.getInstance().drawRoundedRect(page1, cos, 40, rect.getHeight() - 400, rect.getWidth() - 80, 300, 10, Color.RED, Color.GREEN);

            PDFUtility.getInstance().addFieldsBoxRG(cos, 40, rect.getHeight() - 100, 5, new String[]{"Nome", "Cognome", "Codice Fiscale", "Data di Nascita", "Indirizzo", "Telefono", "Cellulare"}, "dati personali", true, 10, Color.BLUE);

            PDFUtility.getInstance().addTextBoxValue(cos, "dati personali", "Nome", "Luca", false, Color.black, 10, 10);
            PDFUtility.getInstance().addTextBoxValue(cos, "dati personali", "Indirizzo", "Via San Francesco 15/D", false, Color.black, 10, 10);
            PDFUtility.getInstance().addTextBoxValue(cos, "dati personali", "Telefono", "3285770588", false, Color.red, 10, 10);

            PDFUtility.getInstance().addLargeText(cos, 30, 200, rect.getWidth() - 70, 20, 10,
                    "I came across this requirement recently, to find whether a specific word is present or not in a PDF file. Initially I thought this is a very simple requirement and created a simple application in Java, that would first extract text from PDF files and then do a linear character matching like mystring.contains(mysearchterm) == true. It did give me the expected output, but linear character matching operations are suitable only when the content you are searching is very small. Otherwise it is very expensive, in complexity terms O(np) where n= number of words to search and p= number of search terms.\n"
                    + "\n"
                    + "\n"
                    + "The best solution is to go for a simple search engine which will first pre-parse all your data in to tokens to create an index and then allow us to query the index to retrieve matching results. This means the whole content will be first broken down into terms and then each of it will point to the content. For example, consider the raw data, ", Color.BLACK
            );

//            cos.setLineJoinStyle(0);
//            cos.saveGraphicsState();
//            cos.moveTo(100, 100);
//            cos.lineTo( 300,300);
//            cos.lineTo(100,200);
//            cos.lineTo( 100,100);
//            cos.closeSubPath();
//            cos.fill(1);
////            
//            cos.moveTo(20, 20);
//            cos.lineTo(20, 300);
//            cos.addBezier312(20,350,20,350,70,350);
//            cos.lineTo(rect.getWidth()-70,350);
//            cos.lineTo(rect.getWidth()-70,20);
//            cos.closeSubPath();
//            cos.fill(1);
//            cos.restoreGraphicsState();
            cos.beginText();
            cos.setFont(fontBold, 10);
            cos.moveTextPositionByAmount(20, rect.getHeight() - 60);
            cos.setNonStrokingColor(Color.black);
            cos.drawString("Nome: ");
            cos.endText();

            cos.beginText();
            cos.setFont(fontPlain, 10);
            cos.moveTextPositionByAmount(80, rect.getHeight() - 60);
            cos.setNonStrokingColor(Color.blue);
            cos.drawString(p.getNome());
            cos.endText();

            cos.beginText();
            cos.setFont(fontBold, 10);
            cos.setNonStrokingColor(Color.black);
            cos.moveTextPositionByAmount(20, rect.getHeight() - 75);
            cos.drawString("Cognome: ");
            cos.endText();

            cos.beginText();
            cos.setFont(fontPlain, 10);
            cos.moveTextPositionByAmount(80, rect.getHeight() - 75);
            cos.setNonStrokingColor(Color.blue);
            cos.drawString(p.getCognome());
            cos.endText();

            cos.close();

            document.save(outputFileName);
            document.close();

        } catch (IOException ex) {
            Logger.getLogger(AnagraficaPrinter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (COSVisitorException ex) {
            Logger.getLogger(AnagraficaPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    void preventivo() {

        try {
            String outputFileName = "Preventivo_" + (new Date().getTime() + ".pdf");
            PDDocument document = new PDDocument();
            PDPage page1 = new PDPage(PDPage.PAGE_SIZE_A4);
            PDRectangle rect = page1.getMediaBox();
            document.addPage(page1);

            PDPageContentStream cos = new PDPageContentStream(document, page1);

            
            
            PDFUtility.getInstance().setBackgroundImage(document, page1, cos, new File("blumehA4.png"));
            PDFUtility.getInstance().drawRoundedRect(page1, cos, 30, rect.getHeight()-420, rect.getWidth()-60, 300, 15, Color.white, new Color(255,255,255,190));
            System.out.println("H : "+rect.getHeight());
            PDFUtility.getInstance().drawRoundedRect(page1, cos, 30, rect.getHeight()-800, rect.getWidth()-60, 100, 15, Color.white, new Color(190,190,190,190));
            PDFUtility.getInstance().insertLogo(document, page1, cos, new File("BMW_1936.png"));
            
            PDFUtility.getInstance().addTextBoxRG(cos, 200, rect.getHeight() - 30, 30, new String[]{"Rivenditore Carletti & co.","P.IVA: 1836394638463746","Via Gerardo maestrini 13","Tel. 32854048578"}, false, 8, Color.black, 3);

            
            float baseline = PDFUtility.getInstance().addFieldsBoxRG(cos, 15, rect.getHeight() - 130, 20, new String[]{"Nome", "Cognome", "Codice Fiscale", "Data di Nascita", "Indirizzo", "Telefono", "Cellulare"}, "dati personali", true, 10, Color.BLACK);

            PDFUtility.getInstance().addTextBoxValue(cos, "dati personali", "Nome", "Luca", false, Color.black, 10, 10);
            PDFUtility.getInstance().addTextBoxValue(cos, "dati personali", "Indirizzo", "Via San Francesco 15/D", false, Color.black, 10, 10);
            PDFUtility.getInstance().addTextBoxValue(cos, "dati personali", "Cellulare", "44552774", false, Color.red, 10, 10);
            
            PDFUtility.getInstance().addCenteredLine(page1, cos, (int)baseline-20, 40, Color.GRAY, 0.5f);
            
            PDFTable table = new PDFTable();
            table.setHeader(new String[]{"Nome","Cognome","Telefono","Lunghezza bracci","Genoma Umano"});
            table.setGridLineColor(Color.RED);
            table.setOutsideLineSize(1.3f);
            table.setGridLineSize(1f);
            MyTableRenderer renderer = new MyTableRenderer();
            String data[][] = new String[][]{
                {"Luca","Coraci","3285770588","15","xiauyakf97s"},
                {"Elisa","Fighetti","","30","aolkjob97l"},
                {"Gabriella","Rossi","123456789","12","12332msada"},
                {"Ciarpame","Conco","1092","121","aaaaaaa"},
                {"Menabreo","Ebreo","4455 6526","15.7","arcatuaoa"},
            
            };
            table.setRenderer(renderer);
            table.setData(data);
            PDFUtility.getInstance().addTable(page1, cos, table, 50, baseline - 50, rect.getWidth()-100);
            
            PDFUtility.getInstance().addImage(document, page1, cos, new File("Sample.jpg"), "Immagine", "non si vede bene ma è figa", new Color(255,255,255,190), 40, 250, 150, 150);
            
            cos.close();
            document.save(outputFileName);
            document.close();

        } catch (IOException ex) {
            Logger.getLogger(AnagraficaPrinter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (COSVisitorException ex) {
            Logger.getLogger(AnagraficaPrinter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(AnagraficaPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
