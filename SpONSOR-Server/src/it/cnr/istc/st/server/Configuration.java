/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.st.server;

import static it.cnr.istc.st.server.ServerInstaller.SKILL_STORED;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it>
 */
public class Configuration {

    private static Configuration _instance = null;
    private boolean releaseActualTurns = false;
    private boolean win = true;
    public static final String RELEASE_ACTUAL_TURNS = "release-actual-turns";
    public static final String WIN = "win";

    public static Configuration getInstance() {
        if (_instance == null) {
            _instance = new Configuration();
            return _instance;
        } else {
            return _instance;
        }
    }

    private Configuration() {
        super();
    }

    public boolean isReleaseActualTurns() {
        return releaseActualTurns;
    }

    public boolean isWin() {
        return win;
    }
    

    public void loadConfiguration() {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream("server-conf.properties");
            // load a properties file
            prop.load(input);
            // get the property value and print it out
            releaseActualTurns = Boolean.valueOf(prop.getProperty(RELEASE_ACTUAL_TURNS));
            win = Boolean.valueOf(prop.getProperty(WIN));
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
