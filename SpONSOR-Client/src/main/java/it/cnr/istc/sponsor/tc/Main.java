/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tc;

import it.cnr.istc.i18n.translator.Language;
import it.cnr.istc.i18n.translator.TranslatorManager;
import it.cnr.istc.sponsor.tc.gui.SubscritionForm;
import it.cnr.istc.sponsor.tc.mqtt.MQTTClient;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {

//        try {
//            System.out.println("Ciao gino");
//            JAXBContext context = JAXBContext.newInstance(XMLQuestionnary.class);
//            Unmarshaller um = context.createUnmarshaller();
//            FileReader fileReader = new FileReader("perception-q/questionnary.xml");
//
//            XMLQuestionnary questionnary = (XMLQuestionnary) um.unmarshal(fileReader);
//            System.out.println("Q: "+questionnary.getName());
//            System.out.println("MAXXXXXXXXXXXXXX -> "+questionnary.getMax());
//            for (XMLQuestion question : questionnary.getQuestions()) {
//                System.out.println("Question-> "+question.getTitle().trim()+": "+question.getSubtitle().trim());
//                for (XMLAnswer answer : question.getAnswers()) {
//                    System.out.println("\t"+answer.getCode().trim()+") "+answer.getText().trim());
//                }
//                System.out.println("------------------");
//            }
//        } catch (JAXBException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        }
            System.out.println("Welcome to Sponsor Televita");
            MQTTClient.getInstance().connect();
            TranslatorManager.getInstance().loadLanguage(Language.IT);

            TranslatorManager.getInstance().translate();
            System.out.println("MQTT SERVER IS CONNECTED");


            /* Set the Nimbus look and feel */
//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
* For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
             */
            try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (ClassNotFoundException ex) {
                java.util.logging.Logger.getLogger(SubscritionForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                java.util.logging.Logger.getLogger(SubscritionForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(SubscritionForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(SubscritionForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
//</editor-fold>

            /* Create and display the form */
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new SubscritionForm().setVisible(true);
                }
            });

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        //</editor-fold>

    }

}
