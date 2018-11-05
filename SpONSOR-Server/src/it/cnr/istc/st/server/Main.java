/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.st.server;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("[Sponsor MQTT] installing ..");
        Configuration.getInstance().loadConfiguration();
        ServerInstaller.getInstance().install();
        System.out.println("[Sponsor MQTT] Server installed! [OK]");
        
        System.out.println("[Sponsor MQTT] Starting ..");
        MQTT_Server_Manager.getInstance().connect();
        System.out.println("[Sponsor MQTT] MQTT Server is connected!");
    }

}
