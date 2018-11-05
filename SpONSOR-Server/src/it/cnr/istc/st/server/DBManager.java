/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.st.server;

import it.cnr.istc.st.server.app.AppTurno;
import it.cnr.istc.st.server.controllers.ActivityTurnJpaController;
import it.cnr.istc.st.server.controllers.AppCollegueJpaController;
import it.cnr.istc.st.server.controllers.AppTurnIndexJpaController;
import it.cnr.istc.st.server.controllers.AppTurnoJpaController;
import it.cnr.istc.st.server.controllers.PersonJpaController;
import it.cnr.istc.st.server.entity.ActivityTurn;
import it.cnr.istc.st.server.entity.ComfirmedTurn;
import it.cnr.istc.st.server.entity.Person;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author user
 */
public class DBManager {
    
    private static DBManager _instance;
    private AppTurnIndexJpaController indexController = new AppTurnIndexJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
    private AppTurnoJpaController appTurnoController = new AppTurnoJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
    private AppCollegueJpaController appCollegueController = new AppCollegueJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
    private PersonJpaController personController = new PersonJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
    private ActivityTurnJpaController activityTurnController = new ActivityTurnJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
    
    
    private DBManager(){
        
    }
    
    public static DBManager getInstance(){
        if(_instance == null){
            _instance = new DBManager();
        }
        return _instance;
    }
    
    
    
    
}
