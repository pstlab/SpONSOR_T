/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.logic.pdf.generator;

import it.cnr.istc.sponsor.tt.logic.model.Person;
import it.cnr.istc.sponsor.tt.logic.pdf.PDFManager;

/**
 *
 * @author Luca
 */
public class PdfGenerator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        System.out.println("welcome to pdf writer..");
//        AnagraficaPrinter.getInstance().print(new Persona("Luca", "Coraci", "CRCLCU"));

//        AnagraficaPrinter.getInstance().preventivo();
        Person person = new Person();
        person.setName("Luca");
        person.setSurname("Coraci");
//        PDFManager.getInstance().printPDF(person);
        System.out.println("pdf printed");

    }

//        try {
//
//            String outputFileName = "Simple.pdf";
//            if (args.length > 0) {
//                outputFileName = args[0];
//            }
//
//            // Create a document and add a page to it
//            PDDocument document = new PDDocument();
//            PDPage page1 = new PDPage(PDPage.PAGE_SIZE_A4);
//            // PDPage.PAGE_SIZE_LETTER is also possible
//            PDRectangle rect = page1.getMediaBox();
//            // rect can be used to get the page width and height
//            document.addPage(page1);
//
//            // Create a new font object selecting one of the PDF base fonts
//            PDFont fontPlain = PDType1Font.HELVETICA;
//            PDFont fontBold = PDType1Font.HELVETICA_BOLD;
//            PDFont fontItalic = PDType1Font.HELVETICA_OBLIQUE;
//            PDFont fontMono = PDType1Font.COURIER;
//
//            // Start a new content stream which will "hold" the to be created content
//            PDPageContentStream cos = new PDPageContentStream(document, page1);
//
//            int line = 0;
//
//            // Define a text content stream using the selected font, move the cursor and draw some text
//            cos.beginText();
//            cos.setFont(fontPlain, 12);
//            cos.moveTextPositionByAmount(100, rect.getHeight() - 50 * (++line));
//            cos.drawString("Hello World");
//            cos.endText();
//
//            cos.beginText();
//            cos.setFont(fontItalic, 12);
//            cos.moveTextPositionByAmount(100, rect.getHeight() - 50 * (++line));
//            cos.drawString("Italic");
//            cos.endText();
//
//            cos.beginText();
//            cos.setFont(fontBold, 12);
//            cos.moveTextPositionByAmount(100, rect.getHeight() - 50 * (++line));
//            cos.drawString("Bold");
//            cos.endText();
//
//            cos.beginText();
//            cos.setFont(fontMono, 12);
//            cos.setNonStrokingColor(Color.BLUE);
//            cos.moveTextPositionByAmount(100, rect.getHeight() - 50 * (++line));
//            cos.drawString("Monospaced blue");
//            cos.endText();
//
//            // Make sure that the content stream is closed:
//            cos.close();
//
//            PDPage page2 = new PDPage(PDPage.PAGE_SIZE_A4);
//            document.addPage(page2);
//            cos = new PDPageContentStream(document, page2);
//
//            // draw a red box in the lower left hand corner
//            cos.setNonStrokingColor(Color.RED);
//            cos.fillRect(10, 10, 100, 100);
//
//            // add two lines of different widths
//            cos.setLineWidth(1);
//            cos.addLine(200, 250, 400, 250);
//            cos.closeAndStroke();
//            cos.setLineWidth(5);
//            cos.addLine(200, 300, 400, 300);
//            cos.closeAndStroke();
//
//            // add an image
//            try {
//                BufferedImage awtImage = ImageIO.read(new File("Sample.jpg"));
//                PDXObjectImage ximage = new PDPixelMap(document, awtImage);
//                float scale = 0.2f; // alter this value to set the image size
//                cos.drawXObject(ximage, 100, 400, ximage.getWidth() * scale, ximage.getHeight() * scale);
//            } catch (FileNotFoundException fnfex) {
//                System.out.println("No image for you");
//            }
//
//            // close the content stream for page 2
//            cos.close();
//
//            // Save the results and ensure that the document is properly closed:
//            document.save(outputFileName);
//            document.close();
//        } catch (IOException ex) {
//            Logger.getLogger(PdfGenerator.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (COSVisitorException ex) {
//            Logger.getLogger(PdfGenerator.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
}
