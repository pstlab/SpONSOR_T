/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt;

import it.cnr.istc.i18n.translator.Language;
import it.cnr.istc.i18n.translator.TranslatorManager;
import it.cnr.istc.sponsor.tt.gui.login.LoginPanelFrame;
import it.cnr.istc.sponsor.tt.logic.MQTTClient;
import it.cnr.istc.sponsor.tt.logic.SettingsManager;

import java.io.IOException;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class Main {

    public static void main(String[] args) throws IOException {
        
        TranslatorManager.getInstance().loadLanguage(Language.IT);
        SettingsManager.getInstance().loadSettings();
        

//          /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(ActivityTableTesterFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(ActivityTableTesterFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(ActivityTableTesterFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(ActivityTableTesterFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new ActivityTableTesterFrame().setVisible(true);
//            }
//        });


        TranslatorManager.getInstance().translate();

        System.out.println("[Sponsor Televita Trainer]STARTED OK");
        System.out.println("[Sponsor Televita Trainer]Connecting to MQTT");
        MQTTClient.getInstance().connect();
        System.out.println("[Sponsor Televita Trainer]MQTT is connected!");

//                     System.out.println("Ciao");
//        HashMap<String, String> cfg = new HashMap<String, String>();
//        cfg.put("model", "true");
//        Context ctx = new Context(cfg);
//
//        Optimize ooo = ctx.mkOptimize();
//
//        IntExpr x1 = ctx.mkIntConst("X1");
//        BoolExpr vincoloX1 = ctx.mkLe(x1, ctx.mkInt(2)); // significa che sto creando il vincolo: x1 <= 2
//        //BoolExpr notVincoloX1 = ctx.mkNot(vincoloX1); // questo fa il negato di vincoloX1
//        ooo.Add(vincoloX1); // aggiunge questo vincolo al solver ( ovvero fa in modo che l'espressione boolena rappresentata dal vincolo sia true
//        Optimize.Handle handle = ooo.MkMaximize(x1); // significa voler massimizzare il valore da attribuire alla variabile x1 rispetto ai vincoli presenti
//        System.out.println("sto per trovare una soluzione");
//
//        Status status = ooo.Check(); //trova una soluzione
//
//        System.out.println("ho trovato una soluzione");
//
//        System.out.println("STATUS = " + status);
//
//        switch (status) {
//            case SATISFIABLE: {
//                System.out.println("OKKKK");
//                Model model = ooo.getModel();
//                System.out.println("SOLUZIONE: " + model);
//                Expr eval = model.eval(x1, true);
//                System.out.println("VALORE: " + eval);
//                break;
//            }
//            case UNKNOWN:
//                System.out.println("boooh");
//                break;
//            case UNSATISFIABLE:
//                System.out.println("NOT BUONO");
//                break;
//        }
//
//        System.out.println("Optimus Prime");
//    }
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
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                LoginPanelFrame loginPanelFrame = new LoginPanelFrame();
                loginPanelFrame.setLocationRelativeTo(null);
                loginPanelFrame.setVisible(true);
//                MainFrame mainFrame = new MainFrame();
//                mainFrame.setVisible(true);
//                mainFrame.init();
            }
        });

    }
}
