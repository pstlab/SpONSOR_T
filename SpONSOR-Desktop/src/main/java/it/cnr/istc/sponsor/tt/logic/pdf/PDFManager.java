/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.logic.pdf;

import it.cnr.istc.sponsor.tt.logic.model.Person;
import it.cnr.istc.sponsor.tt.logic.pdf.generator.AnagraficaPrinter;
import it.cnr.istc.sponsor.tt.logic.pdf.generator.PDFTable;
import it.cnr.istc.sponsor.tt.logic.pdf.generator.PDFUtility;
import it.cnr.istc.sponsor.tt.logic.pdf.generator.panel.TableImageGeneratorPanel;
import it.cnr.istc.sponsor.tt.logic.pdf.generator.test.MyTableRenderer;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it>
 */
public class PDFManager {

    private static PDFManager _instance = null;

    public static PDFManager getInstance() {
        if (_instance == null) {
            _instance = new PDFManager();
            return _instance;
        } else {
            return _instance;
        }
    }

    private PDFManager() {
        super();
    }

    public void printPDF(TableImageGeneratorPanel panel, Person person) {

        String imgName = panel.printImg();

        try {
            String outputFileName = person.getName()+"_"+person.getSurname()+"_"+panel.getMonthName()+"_" + (new Date().getTime() + ".pdf");
            PDDocument document = new PDDocument();
            PDPage page1 = new PDPage(PDPage.PAGE_SIZE_A4);
            PDRectangle rect = page1.getMediaBox();
            document.addPage(page1);

            PDPageContentStream cos = new PDPageContentStream(document, page1);
//          PDFUtility.getInstance().addLargeText(cos, 30, 200,  rect.getWidth() - 70, 20, 10,
//            PDFUtility.getInstance().addLargeText(cos, 30, 200, rect.getWidth()-60, 20, 14, "Elenco attività per il secondo trimestre dal 2 febbraio al 2 aprile", Color.RED);

            PDFUtility.getInstance().setBackgroundImage(document, page1, cos, new File("blumehA4.png"));
//                PDFUtility.getInstance().drawRoundedRect(page1, cos, 30, rect.getHeight() - 420, rect.getWidth() - 60, 300, 15, Color.white, new Color(255, 255, 255, 190));
            System.out.println("H : " + rect.getHeight());
//                PDFUtility.getInstance().drawRoundedRect(page1, cos, 30, rect.getHeight() - 800, rect.getWidth() - 60, 100, 15, Color.white, new Color(190, 190, 190, 190));
            PDFUtility.getInstance().insertLogo(document, page1, cos, new File("sponsor.png"));

            PDFUtility.getInstance().addTextBoxRG(cos, 200, rect.getHeight() - 30, 30, new String[]{"Televita", "P.IVA: 1836394638463746", "Via Gerardo maestrini 13", "Tel. 32854048578"}, false, 8, Color.black, 3);

            float baseline = PDFUtility.getInstance().addFieldsBoxRG(cos, 15, rect.getHeight() - 130, 20, new String[]{"Nome", "Cognome"}, "dati personali", true, 10, Color.BLACK);

            PDFUtility.getInstance().addTextBoxValue(cos, "dati personali", "Nome", person.getName(), false, Color.black, 10, 10);
            PDFUtility.getInstance().addTextBoxValue(cos, "dati personali", "Cognome", person.getSurname(), false, Color.black, 10, 10);

            PDFUtility.getInstance().addCenteredLine(page1, cos, (int) baseline - 20, 40, Color.GRAY, 0.5f);

            PDFUtility.getInstance().addImage(document, page1, cos, new File(imgName), "Immagine", "non si vede bene ma è figa", new Color(255, 255, 255, 190), 40, 200, rect.getWidth() - 60, 350);

//            PDFUtility.getInstance().addLargeText(cos, 30, 200, rect.getWidth() - 70, 20, 10,
//                    "I came across this requirement recently, to find whether a specific word is present or not in a PDF file. Initially I thought this is a very simple requirement and created a simple application in Java, that would first extract text from PDF files and then do a linear character matching like mystring.contains(mysearchterm) == true. It did give me the expected output, but linear character matching operations are suitable only when the content you are searching is very small. Otherwise it is very expensive, in complexity terms O(np) where n= number of words to search and p= number of search terms.\n"
//                    + "\n"
//                    + "\n"
//                    + "The best solution is to go for a simple search engine which will first pre-parse all your data in to tokens to create an index and then allow us to query the index to retrieve matching results. This means the whole content will be first broken down into terms and then each of it will point to the content. For example, consider the raw data, ", Color.BLACK
//            );
            PDFUtility.getInstance().addLargeText(cos, 30, rect.getHeight() - 240, rect.getWidth() - 60, 20, 14, "Elenco attività per il mese di " + panel.getMonthName(), Color.BLACK);
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

    public void printPDF2(Person person) {
        try {
            String outputFileName = "Preventivo_" + (new Date().getTime() + ".pdf");
            PDDocument document = new PDDocument();
            PDPage page1 = new PDPage(PDPage.PAGE_SIZE_A4);
            PDRectangle rect = page1.getMediaBox();
            document.addPage(page1);

            PDPageContentStream cos = new PDPageContentStream(document, page1);
//          PDFUtility.getInstance().addLargeText(cos, 30, 200,  rect.getWidth() - 70, 20, 10,
//            PDFUtility.getInstance().addLargeText(cos, 30, 200, rect.getWidth()-60, 20, 14, "Elenco attività per il secondo trimestre dal 2 febbraio al 2 aprile", Color.RED);

            PDFUtility.getInstance().setBackgroundImage(document, page1, cos, new File("blumehA4.png"));
            PDFUtility.getInstance().drawRoundedRect(page1, cos, 30, rect.getHeight() - 420, rect.getWidth() - 60, 300, 15, Color.white, new Color(255, 255, 255, 190));
            System.out.println("H : " + rect.getHeight());
            PDFUtility.getInstance().drawRoundedRect(page1, cos, 30, rect.getHeight() - 800, rect.getWidth() - 60, 100, 15, Color.white, new Color(190, 190, 190, 190));
            PDFUtility.getInstance().insertLogo(document, page1, cos, new File("sponsor.png"));

            PDFUtility.getInstance().addTextBoxRG(cos, 200, rect.getHeight() - 30, 30, new String[]{"Televita", "P.IVA: 1836394638463746", "Via Gerardo maestrini 13", "Tel. 32854048578"}, false, 8, Color.black, 3);

            float baseline = PDFUtility.getInstance().addFieldsBoxRG(cos, 15, rect.getHeight() - 130, 20, new String[]{"Nome", "Cognome"}, "dati personali", true, 10, Color.BLACK);

            PDFUtility.getInstance().addTextBoxValue(cos, "dati personali", "Nome", person.getName(), false, Color.black, 10, 10);
            PDFUtility.getInstance().addTextBoxValue(cos, "dati personali", "Cognome", person.getSurname(), false, Color.black, 10, 10);

            PDFUtility.getInstance().addCenteredLine(page1, cos, (int) baseline - 20, 40, Color.GRAY, 0.5f);

            PDFTable table = new PDFTable();
            table.setHeader(new String[]{"Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato", "Domenica"});
            table.setGridLineColor(Color.BLACK);
            table.setOutsideLineSize(0.6f);
            table.setGridLineSize(0.5f);
            MyTableRenderer renderer = new MyTableRenderer();
            String data[][] = new String[][]{
                {"", "12:00 - 12:30;Froci", "", "", "12:00 - 12:30;Laboratorio", "", ""},
                {"", "12:00 - 14:30;Froci", "", "", "", "", ""},
                {"", "", "", "", "", "17:00 - 18:30;Telepresenza", "17:00 - 18:30;Telepresenza"},
                {"", "12:00 - 14:30;Mensa", "", "17:00 - 18:30;Telepresenza", "", "", ""}

            };
            table.setRenderer(renderer);
            table.setData(data);
            PDFUtility.getInstance().addTable(page1, cos, table, 50, baseline - 50, rect.getWidth() - 100);

            PDFUtility.getInstance().addImage(document, page1, cos, new File("Sample.jpg"), "Immagine", "non si vede bene ma è figa", new Color(255, 255, 255, 190), 40, 250, 150, 150);

//            PDFUtility.getInstance().addLargeText(cos, 30, 200, rect.getWidth() - 70, 20, 10,
//                    "I came across this requirement recently, to find whether a specific word is present or not in a PDF file. Initially I thought this is a very simple requirement and created a simple application in Java, that would first extract text from PDF files and then do a linear character matching like mystring.contains(mysearchterm) == true. It did give me the expected output, but linear character matching operations are suitable only when the content you are searching is very small. Otherwise it is very expensive, in complexity terms O(np) where n= number of words to search and p= number of search terms.\n"
//                    + "\n"
//                    + "\n"
//                    + "The best solution is to go for a simple search engine which will first pre-parse all your data in to tokens to create an index and then allow us to query the index to retrieve matching results. This means the whole content will be first broken down into terms and then each of it will point to the content. For example, consider the raw data, ", Color.BLACK
//            );
            PDFUtility.getInstance().addLargeText(cos, 30, rect.getHeight() - 100, rect.getWidth() - 60, 20, 14, "Elenco attività per il secondo trimestre dal 2 febbraio al 2 aprile", Color.RED);
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
