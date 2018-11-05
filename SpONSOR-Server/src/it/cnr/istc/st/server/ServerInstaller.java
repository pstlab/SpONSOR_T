/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.st.server;

import it.cnr.istc.st.server.controllers.JobJpaController;
import it.cnr.istc.st.server.controllers.KeywordJpaController;
import it.cnr.istc.st.server.controllers.SkillJpaController;
import it.cnr.istc.st.server.entity.Job;
import it.cnr.istc.st.server.entity.Keyword;
import it.cnr.istc.st.server.entity.Skill;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;
import javax.persistence.Persistence;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class ServerInstaller {

    private static ServerInstaller _instance = null;
    private boolean skillCreated = false;
    public static final String SKILL_STORED = "skill-stored";

    public static ServerInstaller getInstance() {
        if (_instance == null) {
            _instance = new ServerInstaller();
            return _instance;
        } else {
            return _instance;
        }
    }

    private ServerInstaller() {
        super();

    }

    public void install() {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream("server-conf.properties");
            // load a properties file
            prop.load(input);
            // get the property value and print it out
            skillCreated = Boolean.valueOf(prop.getProperty(SKILL_STORED));
            System.out.println(prop.getProperty(SKILL_STORED));
            if (!skillCreated) {
                OutputStream output = null;
                try {
                    SkillJpaController controller = new SkillJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
                    KeywordJpaController keycontroller = new KeywordJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
                    JobJpaController jobcontroller = new JobJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
                    Skill leader = new Skill("label.skill1");
                    Skill stutturatore = new Skill("label.skill2");
                    Skill geniale = new Skill("label.skill3");
                    Skill valutatore = new Skill("label.skill4");
                    Skill concreto = new Skill("label.skill5");
                    Skill esploratore = new Skill("label.skill6");
                    Skill lavoratore = new Skill("label.skill7");
                    Skill obiettivista = new Skill("label.skill8");
                    controller.create(leader);
                    controller.create(stutturatore);
                    controller.create(geniale);
                    controller.create(valutatore);
                    controller.create(concreto);
                    controller.create(esploratore);
                    controller.create(lavoratore);
                    controller.create(obiettivista);

                    Keyword chiavi = new Keyword();
                    chiavi.setKeyword("Chiavi");
                    Keyword spirituale = new Keyword();
                    spirituale.setKeyword("Spirituale");

                    keycontroller.create(chiavi);
                    keycontroller.create(spirituale);
                    System.out.println("KEYWORD created !");

                    output = new FileOutputStream("server-conf.properties");
                    skillCreated = true;
                    // set the properties value
                    prop.setProperty(SKILL_STORED, "" + skillCreated);
                    // save properties to project root folder
                    prop.store(output, null);
                    System.out.println("Skill Created -> TRUE");
                    System.out.println("Modifiche salvate su file.");
                    
                    
                    System.out.println("creating profiles");
                    
                    Job respFascia = new Job();
                    respFascia.setName("Resp. Fascia");
                    respFascia.getSkills().add(leader);
                    respFascia.getSkills().add(geniale);
                    respFascia.getSkills().add(concreto);
                    respFascia.getSkills().add(lavoratore);
                    jobcontroller.create(respFascia);
                    
                    Job telefonistaStandar = new Job();
                    telefonistaStandar.setName("Telefonista Standard");
                    telefonistaStandar.getSkills().add(stutturatore);
                    telefonistaStandar.getSkills().add(valutatore);
                    telefonistaStandar.getSkills().add(esploratore);
                    telefonistaStandar.getSkills().add(obiettivista);
                    jobcontroller.create(telefonistaStandar);
                    
                    Job telesoccorso = new Job();
                    telesoccorso.setName("Telesoccorso");
                    telesoccorso.getSkills().add(leader);
                    telesoccorso.getSkills().add(concreto);
                    telesoccorso.getSkills().add(lavoratore);
                    telesoccorso.getSkills().add(obiettivista);
                    jobcontroller.create(telesoccorso);
                    
                    Job teleassistenza = new Job();
                    teleassistenza.setName("Teleassistenza");
                    teleassistenza.getSkills().add(stutturatore);
                    teleassistenza.getSkills().add(valutatore);
                    teleassistenza.getSkills().add(esploratore);
                    teleassistenza.getSkills().add(lavoratore);
                    jobcontroller.create(teleassistenza);
                    
                    Job ascoltoEsupporto = new Job();
                    ascoltoEsupporto.setName("Ascolto e Supporto");
                    ascoltoEsupporto.getSkills().add(leader);
                    ascoltoEsupporto.getSkills().add(geniale);
                    ascoltoEsupporto.getSkills().add(valutatore);
                    ascoltoEsupporto.getSkills().add(lavoratore);
                    jobcontroller.create(ascoltoEsupporto);
                    
                    Job bancaDelCibo = new Job();
                    bancaDelCibo.setName("Banca del Cibo");
                    bancaDelCibo.getSkills().add(stutturatore);
                    bancaDelCibo.getSkills().add(concreto);
                    bancaDelCibo.getSkills().add(lavoratore);
                    bancaDelCibo.getSkills().add(obiettivista);
                    jobcontroller.create(bancaDelCibo);
                    
                    Job consulenza = new Job();
                    consulenza.setName("Consulenza");
                    consulenza.getSkills().add(leader);
                    consulenza.getSkills().add(stutturatore);
                    consulenza.getSkills().add(geniale);
                    consulenza.getSkills().add(esploratore);
                    consulenza.getSkills().add(obiettivista);
                    jobcontroller.create(consulenza);
                    
                    Job pranziEmerende = new Job();
                    pranziEmerende.setName("Pranzi e Merende");
                    pranziEmerende.getSkills().add(stutturatore);
                    pranziEmerende.getSkills().add(valutatore);
                    pranziEmerende.getSkills().add(concreto);
                    pranziEmerende.getSkills().add(lavoratore);
                    jobcontroller.create(pranziEmerende);
                    
                    Job animazione = new Job();
                    animazione.setName("Animazione");
                    animazione.getSkills().add(stutturatore);
                    animazione.getSkills().add(geniale);
                    animazione.getSkills().add(esploratore);
                    animazione.getSkills().add(obiettivista);
                    jobcontroller.create(animazione);
                    
                    Job capoGita = new Job();
                    capoGita.setName("Capo Gita");
                    capoGita.getSkills().add(leader);
                    capoGita.getSkills().add(stutturatore);
                    capoGita.getSkills().add(concreto);
                    capoGita.getSkills().add(lavoratore);
                    jobcontroller.create(capoGita);
                    
                    Job supportoSpirituale = new Job();
                    supportoSpirituale.setName("Supporto Spirituale");
                    supportoSpirituale.getSkills().add(leader);
                    supportoSpirituale.getSkills().add(geniale);
                    supportoSpirituale.getSkills().add(valutatore);
                    supportoSpirituale.getSkills().add(obiettivista);
                    jobcontroller.create(supportoSpirituale);
                    
                    
                    System.out.println("Profiles: OK");
                    
                } catch (IOException io) {
                    io.printStackTrace();
                } finally {
                    if (output != null) {
                        try {
                            output.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
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
