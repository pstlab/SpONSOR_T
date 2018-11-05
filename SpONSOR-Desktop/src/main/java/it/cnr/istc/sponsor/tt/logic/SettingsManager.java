/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.logic;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class SettingsManager {

    public static final String config_file_name = "trainer-conf.properties";
    public static final String KEY_OPEN_TIME = "open-time";
    public static final String KEY_CLOSE_TIME = "close-time";
    public static final String SERVER_IP = "server-ip";
    public static final String TIMEOUT = "timeout";
    public static final String KEY_POPOLA = "popola";
    private static SettingsManager _instance = null;
    private int openTime = 9;
    private int closeTime = 22;
    private String serverIP = "";
    private String timeout = "";
    private boolean popola = false;

    public static SettingsManager getInstance() {
        if (_instance == null) {
            _instance = new SettingsManager();
            return _instance;
        } else {
            return _instance;
        }
    }

    private SettingsManager() {
        super();
    }

    public int getCloseTime() {
        return closeTime;
    }

    public int getOpenTime() {
        return openTime;
    }

    public boolean isPopola() {
        return popola;
    }
    
    

    public String getServerIP() {
        return serverIP;
    }

    public String getTimeout() {
        return timeout;
    }
    
    

    public void loadSettings() {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream(config_file_name);
            // load a properties file
            prop.load(input);

            openTime = Integer.valueOf(prop.getProperty(KEY_OPEN_TIME));
            closeTime = Integer.valueOf(prop.getProperty(KEY_CLOSE_TIME));
            popola = Boolean.valueOf(prop.getProperty(KEY_POPOLA));
            serverIP = prop.getProperty(SERVER_IP);
            timeout = prop.getProperty(TIMEOUT);
            
            System.out.println("Loading: [OPEN TIME]  = "+openTime);
            System.out.println("Loading: [CLOSE TIME] = "+closeTime);
            System.out.println("Loading: [Server IP]  = "+serverIP);
            System.out.println("Loading: [TIMEOUT]  = "+timeout);

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
