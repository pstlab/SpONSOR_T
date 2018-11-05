/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tc.gui;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class ConfigurationManager {

    private static ConfigurationManager _instance = null;
    public static final String config_file_name = "config.properties";
    public static final String ALTERNANZA = "alternanza";
    public static final String SERVERIP = "serverip";
    
    public boolean alternanza = true;
    public String ip = "";

    public static ConfigurationManager getInstance() {
        if (_instance == null) {
            _instance = new ConfigurationManager();
            return _instance;
        } else {
            return _instance;
        }
    }

    public String getIp() {
        return ip;
    }
    
    
    

    private ConfigurationManager() {
        super();
        loadSettings();
    }

    public boolean isAlternanza() {
        return this.alternanza;
    }

    public final void loadSettings() {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream(config_file_name);
            // load a properties file
            prop.load(input);

//            closeTime = Integer.valueOf(prop.getProperty(KEY_CLOSE_TIME));
            alternanza = Boolean.valueOf(prop.getProperty(ALTERNANZA));
            ip = prop.getProperty(SERVERIP);

            System.out.println("Loading: [ALTERNANZA]  = " + alternanza);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
