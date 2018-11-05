/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.st.server;

import it.cnr.istc.st.server.app.AppPerson;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.cnr.istc.st.server.app.AppCollegue;
import it.cnr.istc.st.server.app.AppTurnIndex;
import it.cnr.istc.st.server.app.AppTurno;
import it.cnr.istc.st.server.controllers.ActivityJpaController;
import it.cnr.istc.st.server.controllers.ActivityNameJpaController;
import it.cnr.istc.st.server.controllers.ActivityTurnJpaController;
import it.cnr.istc.st.server.controllers.ActivityTurnKeywordJpaController;
import it.cnr.istc.st.server.controllers.AppCollegueJpaController;
import it.cnr.istc.st.server.controllers.AppTurnIndexJpaController;
import it.cnr.istc.st.server.controllers.AppTurnJpaController;
import it.cnr.istc.st.server.controllers.AppTurnoJpaController;
import it.cnr.istc.st.server.controllers.ComfirmedTurnJpaController;
import it.cnr.istc.st.server.controllers.GroupActivityJpaController;
import it.cnr.istc.st.server.controllers.JobJpaController;
import it.cnr.istc.st.server.controllers.JobTurnJpaController;
import it.cnr.istc.st.server.controllers.KeywordJpaController;
import it.cnr.istc.st.server.controllers.PersonJpaController;
import it.cnr.istc.st.server.controllers.ProjectJpaController;
import it.cnr.istc.st.server.controllers.SkillJpaController;
import it.cnr.istc.st.server.controllers.SolutionJpaController;
import it.cnr.istc.st.server.controllers.SolutionTokenJpaController;
import it.cnr.istc.st.server.controllers.TurnCancellationJpaController;
import it.cnr.istc.st.server.controllers.exceptions.NonexistentEntityException;
import it.cnr.istc.st.server.entity.Activity;
import it.cnr.istc.st.server.entity.ActivityName;
import it.cnr.istc.st.server.entity.ActivityTurn;
import it.cnr.istc.st.server.entity.ActivityTurnKeyword;
import it.cnr.istc.st.server.entity.AppTurn;
import it.cnr.istc.st.server.entity.ComfirmedTurn;
import it.cnr.istc.st.server.entity.GroupActivity;
import it.cnr.istc.st.server.entity.Job;
import it.cnr.istc.st.server.entity.JobTurn;
import it.cnr.istc.st.server.entity.Keyword;
import it.cnr.istc.st.server.entity.Person;
import it.cnr.istc.st.server.entity.Project;
import it.cnr.istc.st.server.entity.Skill;
import it.cnr.istc.st.server.entity.TurnCancellation;
import it.cnr.istc.st.server.model.FreeTimeToken;
import it.cnr.istc.st.server.xml.Account;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
//import javax.swing.JOptionPane;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it>
 */
public class MQTT_Server_Manager implements MqttCallback {

    private static MQTT_Server_Manager _instance = null;
    private Process mqtt = null;
    private static final String TASKLIST = "tasklist";
    private static final String KILL = "taskkill /F /IM ";
//    private List<MessageListener> listeners = new ArrayList<>();
    private String broker = "tcp://localhost:1883";
    private String clientId = "server";
    private boolean ignore = false;
    private MqttClient sampleClient = null;
    private String subscriptionTopic = "SUBSCRIPTION";
    private String getAllPeople = "getAllPeople";
    private String getAllJobs = "getAllJobs";
    public static final String getAllKeywords = "getAllKeywords";
    private String createJob = "createJob";
    private String getAllSkills = "getAllSkills";
    public static final String deleteUser = "deleteuser";
    public static final String userDeleted = "userDeleted";
    public static final String updateUserSleep = "updateUserSleep";
    public static final String QUERY = "query";
    public static final String RESULT = "result";
    public static final String createdUser = "createdUser";
    public static final String createdUser2 = "createdUser2";
    public static final String createKeyword = "createKeyword";
    public static final String deleteKeyword = "deleteKeyword";
    public static final String createdKeyword = "createdKeyword";
    public static final String updateKeywords = "updateKeywords";
    public static final String deleteJob = "deleteJob";
    public static final String jobDeleted = "jobDeleted";
    public static final String jobCannotBeDeleted = "jobCannotBeDeleted";
    public static final String keyDeleted = "keyDeleted";
    public static final String getTurnsByPersonCode = "getTurnsByPersonCode";
    public static final String getTurnosByPersonCode = "getTurnosByPersonCode";
    public static final String getAppPersonByCode = "getAppPersonByCode";
    public static final String deliverDelay = "deliverDelay";
    public static final String delayInfoData = "delayInfoData";
    public static final String turnCancellation_START = "turnCancellation_START";
    public static final String turnCancellation_PHASE_1 = "turnCancellation_PHASE_1";
    public static final String APP = "APP";
    public static final String APP2 = "APP2";
    public static final String saveTurns = "saveTurns";
    public static final String saveTurni = "saveTurni";
    public static final String getAllAppPerson = "getAllAppPerson";
    public static final String getAllCancellation = "getAllCancellation";
    public static final String personalChannel = "personalChannel";
    public static final String projectSaved = "projectSaved";
    public static final String saveProject = "saveProject";
    public static final String getAllProjects = "getAllProjects";
    public static final String getProjectById = "getProjectById";
    public static final String editPerson = "editPerson";
    public static final String getAllDBActivities = "getAllDBActivities";
    public static final String createActivityname = "createActivityname";
    public static final String createGroup = "createGroup";
    public static final String editGroup = "editGroup";
    public static final String deleteGroup = "deleteGroup";
    public static final String createFakes = "createFakes";
    public static String clientRegistrationDone = "clientRegistrationDone";
    public static final String imalive = "imalive";

    private Map<String, Person> cachePersonMap = new HashMap<>();
    private List<String> admins = new ArrayList<>();

    AppTurnoJpaController controllerAppTurno = new AppTurnoJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
    AppCollegueJpaController controllerAppCollegue = new AppCollegueJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
    AppTurnIndexJpaController controllerIndex = new AppTurnIndexJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
    TurnCancellationJpaController controllerCancellation = new TurnCancellationJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
    PersonJpaController controllerPerson = new PersonJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
    ProjectJpaController controllerProject = new ProjectJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
    SolutionJpaController controllerSolution = new SolutionJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
    SolutionTokenJpaController controllerSolutionToken = new SolutionTokenJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
    ActivityJpaController controllerActivity = new ActivityJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
    ActivityTurnJpaController controllerActivityTurn = new ActivityTurnJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
    JobTurnJpaController controllerJobTurn = new JobTurnJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
    ActivityNameJpaController controllerActivityName = new ActivityNameJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
    ComfirmedTurnJpaController controllerCoomfirmedTurn = new ComfirmedTurnJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
    GroupActivityJpaController controllerGroup = new GroupActivityJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
    KeywordJpaController controllerKeyword = new KeywordJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
    ActivityTurnKeywordJpaController controllerTurnKeyword = new ActivityTurnKeywordJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));

    public static MQTT_Server_Manager getInstance() {
        if (_instance == null) {
            _instance = new MQTT_Server_Manager();
            return _instance;
        } else {
            return _instance;
        }
    }

    private MQTT_Server_Manager() {
        super();
    }

    public void connect() {

        try {
            InetAddress IP = InetAddress.getLocalHost();
            System.out.println("IP of my system is := " + IP.getHostAddress());
            if (Configuration.getInstance().isWin()) {
                if (mqtt == null && !isProcessRunning("mosquitto.exe *32")) {
                    mqtt = Runtime.getRuntime().exec("./mosquitto/mosquitto.exe");
                    System.out.println("MQTT connected");
                    System.out.println("subscribing all");
                    MemoryPersistence persistence = new MemoryPersistence();
                    try {
                        sampleClient = new MqttClient(broker, clientId, persistence);
                        MqttConnectOptions connOpts = new MqttConnectOptions();
                        connOpts.setCleanSession(false);
                        connOpts.setKeepAliveInterval(Integer.MAX_VALUE);
                        System.out.println("Connecting to broker: " + broker);
                        sampleClient.connect(connOpts);
                        System.out.println("Connected");
                        sampleClient.setCallback(_instance);

                        sampleClient.subscribe("youtube");
                        sampleClient.subscribe("gesture");
                        sampleClient.subscribe("master");
                        sampleClient.subscribe("speak");
                        sampleClient.subscribe("#");
                        System.out.println("Disconnected");
                    } catch (MqttException me) {
                        System.out.println("reason " + me.getReasonCode());
                        System.out.println("msg " + me.getMessage());
                        System.out.println("loc " + me.getLocalizedMessage());
                        System.out.println("cause " + me.getCause());
                        System.out.println("excep " + me);
                        me.printStackTrace();
                    }
                } else {

                }

            }
        } catch (IOException ex) {
            Logger.getLogger(MQTT_Server_Manager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(MQTT_Server_Manager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void disconnect() {
        try {
            if (mqtt != null && isProcessRunning("mosquitto.exe")) {
                killProcess("mosquitto.exe");
                System.out.println("MQTT disconnected");
            }
        } catch (Exception ex) {
            Logger.getLogger(MQTT_Server_Manager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean isProcessRunning(String serviceName) throws Exception {

        Process p = Runtime.getRuntime().exec(TASKLIST);
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                p.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {

            System.out.println(line);
            if (line.contains(serviceName)) {
                System.out.println("TROVATO !");
                return true;
            }
        }

        return false;

    }

    public static void killProcess(String serviceName) throws Exception {
        System.out.println("service name : " + serviceName);

        Runtime.getRuntime().exec(KILL + serviceName);

    }

    @Override
    public void connectionLost(Throwable thrwbl) {
        System.out.println("we have a problem!");
        thrwbl.printStackTrace();
    }

    @Override
    public void messageArrived(String topic, MqttMessage mm) throws Exception {
        try {
            System.out.println("message received -> " + mm);
            System.out.println("topic: " + topic);
            if (topic.equals("youtube") && !ignore) {
                String message = new String(mm.getPayload());
                System.out.println("[speech message] " + message);
                switch (message) {
                    case "go":
                        Utils.openWebpage(new URI("https://www.youtube.com/watch?v=99n0yQG2pcI"));
                        System.out.println("go youtube");
                        break;
                }
            } else if (topic.equals("gesture") && !ignore) {
                String message = new String(mm.getPayload());
                System.out.println("[speech message] " + message);
                switch (message) {
                    case "pause":
                        System.out.println("PAUSE");
                        Robot robot = new Robot();
                        robot.mousePress(InputEvent.BUTTON1_MASK);
                        Thread.sleep(50);
                        robot.mouseRelease(InputEvent.BUTTON1_MASK);
                        break;
                }
            } else if (topic.startsWith(APP + ":" + getAllCancellation)) {
                System.out.println("ALL CANCELLATION !!");
                String personCode = new String(mm.getPayload());
                Person person = getPersonByCode(personCode);
                System.out.println("MESSAGE -> " + personCode); //message must be directly the person code
                List<TurnCancellation> cancellations = this.getCancellationByPersonCode(personCode);
                for (TurnCancellation cancellation : cancellations) {
                    AppCollegue collegue = cancellation.getCollegue();
                    Person cancellataPersona = getPersonByCode(collegue.getPersonCode());
                    Date startTime = new Date(cancellation.getAppTurno().getStartTime());
                    SimpleDateFormat sm = new SimpleDateFormat("HH:mm");
                    String orario = sm.format(startTime);
                    SimpleDateFormat sm2 = new SimpleDateFormat("dd/MM/yyyy");
                    String data = sm2.format(startTime);
                    publish(APP2 + ":" + turnCancellation_PHASE_1 + ":" + personCode, cancellation.getId() + ":" + cancellation.getAppTurno().getId() + ":" + person.getCode() + ":" + "Vuoi sostituire " + cancellataPersona + " al turno delle " + orario + " del " + data + " di " + cancellation.getAppTurno().getActivity());
                }
//
//            Type type = new TypeToken<ArrayList<TurnCancellation>>() {
//            }.getType();
//            Gson gson = new Gson();
//            String peopleResult = gson.toJson(cancellations, type);
//            publish(APP2+":"+getAllCancellation+":"+personCode, peopleResult);

            } else if (topic.equals(APP + ":" + turnCancellation_START) && !ignore) {
                String message = new String(mm.getPayload());
                System.out.println("MESSAGE -> " + message);
                String[] split = message.split(":");
                String idTurn = split[0];
                String personCode = split[1];
                this.startCancellationTurn(idTurn, personCode);

            } else if (topic.startsWith(APP + ":" + turnCancellation_PHASE_1)) {
                String message = new String(mm.getPayload());
                System.out.println("MESSAGE -> " + message);
                String[] split = message.split(":");
                String idCancellation = split[0];
                String personCode = split[1];
                String ans = split[2];
                TurnCancellation cancellation = controllerCancellation.findTurnCancellation(Long.parseLong(idCancellation));
                AppTurno problematicTurno = cancellation.getAppTurno();
                Person personaCancellata = getPersonByCode(cancellation.getCollegue().getPersonCode());
                Date start = new Date(problematicTurno.getStartTime());
                SimpleDateFormat sm = new SimpleDateFormat("HH:mm");
                String orario = sm.format(start);
                SimpleDateFormat sm2 = new SimpleDateFormat("dd/MM/yyyy");
                String data = sm2.format(start);
                Person personByCode = this.getPersonByCode(personCode);

                if (ans.equals("YES")) {
                    //aggiungo la persona alla lista ACK perché ha confermato che vuole sostituire
                    cancellation.getYesPeople().add(personByCode);
                }
                ListIterator<Person> listIterator = cancellation.getCandidatePeople().listIterator();
                //tolgo la persona dalla lista dei candidati
                while (listIterator.hasNext()) {
                    if (listIterator.next().getId().equals(personByCode.getId())) {
                        listIterator.remove();
                    }
                }

                if (cancellation.getCandidatePeople().isEmpty()) {
                    if (cancellation.getYesPeople().isEmpty()) {
                        dispatchAdmins("Ok", "ATTENZIONE! Non è stato possibile trovare alcun sostituto per il turno di " + problematicTurno.getActivity() + " in data " + data + " delle ore " + orario + " a causa dell'assenza di " + personaCancellata);
//                    JOptionPane.showMessageDialog(null, "TRAGEDIA");
                    } else {
                        Person declareWinner = declareWinner(cancellation);
                        // dispatchAdmins("Ok","ATTENZIONE! Non è stato possibile trovare alcun sostituto per il turno di "+problematicTurno.getActivity()+" in data "+data+" delle ore "+orario+" a causa dell'assenza di "+personByCode.toString());
//                    JOptionPane.showMessageDialog(null, "SI PUO PROCEDER !!");

                    }

                }
                controllerCancellation.edit(cancellation);

            } else if (topic.equals(subscriptionTopic) && !ignore) {
                System.out.println("Qualcuno si è iscritto");
                String message = new String(mm.getPayload());
                System.out.println("Message -> " + message);

                Gson gson = new Gson();
                Account accountPewe = gson.fromJson(message, Account.class);
                System.out.println("Account name:        " + accountPewe.getName());
                System.out.println("Account surname:     " + accountPewe.getSurname());
                System.out.println("Account date   :     " + accountPewe.getBornDate());
                System.out.println("Account gender :     " + accountPewe.getGender());
                System.out.println("Account animazione:  " + accountPewe.isAnimation());
                System.out.println("Account live close:  " + accountPewe.isLiveClose());
                System.out.println("Account teleassist:  " + accountPewe.isTeleAssistent());
                System.out.println("Account test result: " + accountPewe.getPerceptionQuestionnary());
                System.out.println("==============================================");

                Person person = new Person();
                person.setName(accountPewe.getName());
                person.setSurname(accountPewe.getSurname());
                person.setPhone(accountPewe.getPhone());
                person.setEmail(accountPewe.getEmail());
                person.setAdmin(accountPewe.isAdmin());
                person.setOneTurnPerDay(accountPewe.isOneTurnPerDay());
                person.setMaxTurnPerWeek(accountPewe.getMaxTurnPerWeek());
                person.setOtherData(message);
                person.setOnlyTheseActivities(accountPewe.getOnlyTheseActivities());
                PersonJpaController controller = new PersonJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
                controller.create(person);
                System.out.println("PERSON HAVE BEEN CREATED");
                accountPewe.setId(person.getId());
                if (person.isAdmin() && !admins.contains(person.getCode())) {
                    admins.add(person.getCode());
                }
                Gson gson2 = new Gson();
                publish(createdUser, gson2.toJson(accountPewe, Account.class));
                publish(createdUser2, gson2.toJson(person, Person.class));
                
                
                publish("clientRegistrationDone", person.toString() + ":"+person.getCode());

            } else if (topic.startsWith("query") && !ignore) {
                String[] split = topic.split(":");
                if (split[1].equals(getAllPeople)) {
                    PersonJpaController controller = new PersonJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
                    List<Person> findPersonEntities = controller.findPersonEntities();
                    checkAdmin(findPersonEntities);
                    Type type = new TypeToken<ArrayList<Person>>() {
                    }.getType();
                    Gson gson = new Gson();
                    String peopleResult = gson.toJson(findPersonEntities, type);
                    System.out.println("Message to send with result: " + peopleResult);
                    publish("result:" + getAllPeople, peopleResult);

                } else if (split[1].equals(editPerson)) {

                    String message = new String(mm.getPayload());
                    System.out.println("Message -> " + message);

                    Gson gson = new Gson();
//                Person person = gson.fromJson(message, Person.class);

                    Account accountPewe = gson.fromJson(message, Account.class);
                    System.out.println("Account name:        " + accountPewe.getName());
                    System.out.println("Account surname:     " + accountPewe.getSurname());
                    System.out.println("Account date   :     " + accountPewe.getBornDate());
                    System.out.println("Account gender :     " + accountPewe.getGender());
                    System.out.println("Account animazione:  " + accountPewe.isAnimation());
                    System.out.println("Account live close:  " + accountPewe.isLiveClose());
                    System.out.println("Account teleassist:  " + accountPewe.isTeleAssistent());
                    System.out.println("Account test result: " + accountPewe.getPerceptionQuestionnary());
                    System.out.println("==============================================");

                    Person person = controllerPerson.findPerson(accountPewe.getId());
                    person.setName(accountPewe.getName());
                    person.setSurname(accountPewe.getSurname());
                    person.setPhone(accountPewe.getPhone());
                    person.setEmail(accountPewe.getEmail());
                    person.setAdmin(accountPewe.isAdmin());
                    person.setOneTurnPerDay(accountPewe.isOneTurnPerDay());
                    person.setMaxTurnPerWeek(accountPewe.getMaxTurnPerWeek());
                    person.setOtherData(message);
                    person.setOnlyTheseActivities(accountPewe.getOnlyTheseActivities());
                    System.out.println("PERSON HAVE BEEN CREATED");
                    accountPewe.setId(person.getId());
                    if (person.isAdmin() && !admins.contains(person.getCode())) {
                        admins.add(person.getCode());
                    }

                    controllerPerson.edit(person);
                    System.out.println("Person edit [ok]");
                    Gson gson2 = new Gson();
                    String editedPerson = gson2.toJson(person, Person.class);
                    publish("result:" + editPerson, editedPerson);
                } else if (split[1].equals(getAllAppPerson)) {
                    PersonJpaController controller = new PersonJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
                    List<Person> findPersonEntities = controller.findPersonEntities();
                    checkAdmin(findPersonEntities);
                    List<AppPerson> apppersons = new ArrayList<>();
                    String code = split[2];
                    for (Person person : findPersonEntities) {
                        System.out.println("PERSON: " + person.getName() + " " + person.getSurname());
                        apppersons.add(person.toAppPerson());
                    }
                    Type type = new TypeToken<ArrayList<AppPerson>>() {
                    }.getType();
                    Gson gson = new Gson();
                    String peopleResult = gson.toJson(apppersons, type);
                    System.out.println("Message to send with result: " + peopleResult);
                    publish(APP2 + ":" + getAllPeople + ":" + code, peopleResult);

                } else if (split[1].equals(saveProject)) {
                    Gson gson = new Gson();
                    String message = new String(mm.getPayload());
                    Project project = gson.fromJson(message, Project.class);
                    saveProject(project);
                } else if (split[1].equals(getAllProjects)) {

                    List<Project> projects = controllerProject.findProjectEntities();

                    List<Project> pp = new ArrayList<>();
                    for (Project project : projects) {
                        Project p = new Project();
                        p.setId(project.getId());
                        p.setName(project.getName());
                        pp.add(p);
                    }

                    Type type = new TypeToken<ArrayList<Project>>() {
                    }.getType();
                    Gson gson = new Gson();
                    String projectsResult = gson.toJson(pp, type);

                    publish(RESULT + ":" + getAllProjects, projectsResult);

                } else if (split[1].equals(getAppPersonByCode) && !ignore) {
                    String code = split[2];
                    Person personByCode = getPersonByCode(code);
                    Gson gson = new Gson();
                    String appPerson = gson.toJson(personByCode.toApp(), AppPerson.class);
                    publish(APP2 + ":" + getAppPersonByCode + ":" + code, appPerson);
                } else if (split[1].equals(getProjectById) && !ignore) {
                    String message = new String(mm.getPayload());
                    Long id = Long.parseLong(message);
                    Project project = getProject(id);
                    Gson gson2 = new Gson();
                    String resultProject = gson2.toJson(project, Project.class);
                    System.out.println("resultProject : " + resultProject);
                    publish(RESULT + ":" + getProjectById, resultProject);
                } else if (split[1].equals(createFakes) && !ignore) {
                    String message = new String(mm.getPayload());
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<Account>>() {
                    }.getType();
                    List<Account> accounts = (List<Account>) gson.fromJson(message, type);
                    System.out.println("                                    SIZE ACCOUNTS: " + accounts.size());

                    for (Account accountPewe : accounts) {
                        System.out.println("CREATING ACCOUNT: " + accountPewe);
                        Person person = new Person();
                        person.setName(accountPewe.getName());
                        person.setSurname(accountPewe.getSurname());
                        person.setPhone(accountPewe.getPhone());
                        person.setEmail(accountPewe.getEmail());
                        person.setAdmin(accountPewe.isAdmin());
                        person.setOneTurnPerDay(accountPewe.isOneTurnPerDay());
                        person.setMaxTurnPerWeek(accountPewe.getMaxTurnPerWeek());
                        person.setOtherData(message);
                        person.setOnlyTheseActivities(accountPewe.getOnlyTheseActivities());
                        PersonJpaController controller = new PersonJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
                        controller.create(person);
                        System.out.println("PERSON HAVE BEEN CREATED");
                        accountPewe.setId(person.getId());
                        if (person.isAdmin() && !admins.contains(person.getCode())) {
                            admins.add(person.getCode());
                        }
                    }

                    publish(RESULT + ":" + createFakes, "OK");
                } else if (split[1].equals(getTurnosByPersonCode) && !ignore) {
                    //NEW TURNOS
                    String code = split[2];
                    System.out.println("<<<<<<<<<<<<<<<<<<<<<<< GET TURNOS by: " + code);
                    Date today = new Date();
                    List<AppTurno> turnos = getAppTurnoByCode(code);
                    if (Configuration.getInstance().isReleaseActualTurns()) {
                        ListIterator<AppTurno> listIterator = turnos.listIterator();
                        while (listIterator.hasNext()) {
                            if (listIterator.next().getStartTime() < today.getTime()) {
                                System.out.println("REMOVE");
                                listIterator.remove();
                            }
                        }
                    } else {
                        System.out.println("CONFIG WRONG");
                    }
                    Type type = new TypeToken<ArrayList<AppTurno>>() {
                    }.getType();
                    Gson gson = new Gson();
                    String turnResults = gson.toJson(turnos, type);
                    publish(APP2 + ":" + getTurnosByPersonCode + ":" + code, turnResults);

                } else if (split[1].equals(getTurnsByPersonCode) && !ignore) {
                    System.out.println("CODE: " + split[2]);
                    List<AppTurn> turns = this.getAppTurnByCode(split[2]);
                    Type type = new TypeToken<ArrayList<AppTurn>>() {
                    }.getType();
                    Gson gson = new Gson();
                    String turnResults = gson.toJson(turns, type);
                    System.out.println("Message to send with result: " + turnResults);

                    //scorre tutti i turni della persona in esame
                    for (AppTurn turn : turns) {
                        String delayMessage = "";
                        List<AppTurn> turniDelloStessoTurno = null;
                        EntityManagerFactory cem = Persistence.createEntityManagerFactory("sponsor-server-mqttPU");
                        TypedQuery<AppTurn> query
                                = cem.createEntityManager().createNamedQuery("AppTurn.findAllByCodeTurn", AppTurn.class);
                        query.setParameter("x", turn.getCodeTurn());
                        turniDelloStessoTurno = query.getResultList();
                        for (AppTurn appTurn : turniDelloStessoTurno) {
                            if (appTurn.getTime() != 0) {
                                delayMessage += ("" + appTurn.getCodePerson() + ":" + getPersonByCode(appTurn.getCodePerson()).toString() + ":" + appTurn.getTime() + ";");
                            }
                        }
                        delayMessage = delayMessage.substring(0, delayMessage.length() - 1);
                        publish(APP2 + ":" + delayInfoData + ":" + split[2], delayMessage);
                    }

                    publish(APP2 + ":" + getTurnsByPersonCode + ":" + split[2], turnResults);
                } else if (split[1].equals(saveTurns) && !ignore) {
                    //DEPRECATE
                    Gson gson = new Gson();
                    String message = new String(mm.getPayload());
                    String[] parts = message.split("!%!");
                    String real_message = parts[0];
                    String timestamp = parts[1];
                    System.out.println("TIMESTAMP: " + timestamp);
                    System.out.println("MESSAGE ->> " + message);
                    Type type = new TypeToken<ArrayList<AppTurn>>() {
                    }.getType();
                    List<AppTurn> appturns = (List<AppTurn>) gson.fromJson(real_message, type);
                    AppTurnJpaController controller = new AppTurnJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
                    List<AppTurn> allapps = controller.findAppTurnEntities();
                    for (AppTurn allapp : allapps) {
                        System.out.println("DESTROY APPTURN: " + allapp.getId());
                        controller.destroy(allapp.getId());
                    }
                    for (AppTurn appturn : appturns) {
                        controller.create(appturn);
                        System.out.println("SAVED APPTURN: " + appturn.getCodeTurn());
                    }
                    publish("result:" + saveTurns, timestamp);
                } else if (split[1].equals(saveTurni) && !ignore) {
                    Gson gson = new Gson();
                    String message = new String(mm.getPayload());
                    String[] parts = message.split("!%!");
                    String real_message = parts[0];
                    String timestamp = parts[1];
                    System.out.println("TIMESTAMP: " + timestamp);
                    System.out.println("MESSAGE ->> " + message);

                    Type type = new TypeToken<ArrayList<AppTurno>>() {
                    }.getType();
                    List<AppTurno> appturns = (List<AppTurno>) gson.fromJson(real_message, type);

                    System.out.println("clearing app db..");
                    boolean bb = clearAppDatabase();
                    if (!bb) {
//                    JOptionPane.showMessageDialog(null, "Error Database");
                        return;
                    }

                    System.out.println("database cleared. [OK]");

                    Map<String, AppTurnIndex> indexMap = new HashMap<>();

                    for (AppTurno appturn : appturns) {
                        List<AppCollegue> collegues = appturn.getCollegues();
                        for (AppCollegue collegue : collegues) {
                            if (!indexMap.containsKey(collegue.getPersonCode())) {
                                indexMap.put(collegue.getPersonCode(), new AppTurnIndex(collegue.getPersonCode()));
                            }
                            controllerAppCollegue.create(collegue);
                            indexMap.get(collegue.getPersonCode()).getTurns().add(appturn);

                        }
                        controllerAppTurno.create(appturn);
                        System.out.println("SAVED APPTURN: " + appturn.getId());
                    }
                    Collection<AppTurnIndex> values = indexMap.values();
                    for (AppTurnIndex index : values) {
                        System.out.println("INDEX: " + index.getPersonCode());
                        controllerIndex.create(index);
                    }
                    publish("result:" + saveTurns, timestamp);
                } else if (split[1].equals(getAllJobs) && !ignore) {
                    JobJpaController controller = new JobJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
                    List<Job> findJobEntities = controller.findJobEntities();
                    for (Job job : findJobEntities) {
                        System.out.println("JOB: " + job.getName());
                    }
                    Type type = new TypeToken<ArrayList<Job>>() {
                    }.getType();
                    Gson gson = new Gson();
                    String jobsResults = gson.toJson(findJobEntities, type);
                    System.out.println("Message to send with result: " + jobsResults);
                    publish("result:" + getAllJobs, jobsResults);
                } else if (split[1].equals(getAllDBActivities) && !ignore) {
                    List<ActivityName> acts = controllerActivityName.findActivityNameEntities();
                    for (ActivityName a : acts) {
                        System.out.println("ActivityName: " + a.getName());
                    }
                    Type type = new TypeToken<ArrayList<ActivityName>>() {
                    }.getType();
                    Gson gson = new Gson();
                    String actsResults = gson.toJson(acts, type);
                    System.out.println("Message to send with result: " + actsResults);
                    publish("result:" + getAllDBActivities, actsResults);
                } else if (split[1].equals(getAllSkills) && !ignore) {
                    SkillJpaController controller = new SkillJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
                    List<Skill> skills = controller.findSkillEntities();
                    for (Skill skill : skills) {
                        System.out.println("SKILL: " + skill.getName());
                    }
                    Type type = new TypeToken<ArrayList<Skill>>() {
                    }.getType();
                    Gson gson = new Gson();
                    String jobsResults = gson.toJson(skills, type);
                    System.out.println("Message to send with result: " + jobsResults);
                    publish("result:" + getAllSkills, jobsResults);
                } else if (split[1].equals(getAllKeywords) && !ignore) {
                    KeywordJpaController controller = new KeywordJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
                    List<Keyword> keys = controller.findKeywordEntities();
                    for (Keyword key : keys) {
                        System.out.println("KEY: " + key.getKeyword());
                    }
                    Type type = new TypeToken<ArrayList<Keyword>>() {
                    }.getType();
                    Gson gson = new Gson();
                    String keysResults = gson.toJson(keys, type);
                    System.out.println("Message to send with result: " + keysResults);
                    publish("result:" + getAllKeywords, keysResults);
                } else if (split[1].equals(createJob) && !ignore) {
                    String message = new String(mm.getPayload());
                    System.out.println("CREATING JOB: " + message);
                    JobJpaController controller = new JobJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
                    Gson gson = new Gson();
                    Job job = gson.fromJson(message, Job.class);
                    controller.create(job);
                    System.out.println("JOB [" + job.getName() + "] has been created [OK]");
                    publish(RESULT + ":" + createJob, job.getName() + ":" + job.getId());
                } else if (split[1].equals(imalive) && !ignore) {

                    publish(RESULT + ":" + imalive, "YES");
                } else if (split[1].equals(deleteUser) && !ignore) {
                    if (split.length == 3) {
                        System.out.println("chissà ?  - > " + split[2]);
                        Long id = Long.parseLong(split[2]);
                        System.out.println("DELETING USER -> " + id);
                        PersonJpaController controller = new PersonJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
                        controller.destroy(id);
                        System.out.println("USER DELETED!");

                        publish(RESULT + ":" + userDeleted, "" + id);

                    }

                } else if (split[1].equals(deleteJob) && !ignore) {

                    if (split.length == 3) {
                        Long id = -1l;
                        try {
                            System.out.println("chissà ?  - > " + split[2]);
                            id = Long.parseLong(split[2]);
                            //ERRORE SE C'è ECCEZIONE
                            System.out.println("DELETING USER -> " + id);
                            JobJpaController controller = new JobJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
                            controller.destroy(id);
                            System.out.println("USER DELETED!");

                            publish(RESULT + ":" + jobDeleted, "" + id);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            publish(RESULT + ":" + jobCannotBeDeleted, "" + id);
                        }

                    }

                } else if (split[1].equals(createKeyword) && !ignore) {
                    String message = new String(mm.getPayload());
                    System.out.println("CREATING KEYWORD: " + message);
                    KeywordJpaController controller = new KeywordJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
                    Keyword key = new Keyword();
                    key.setKeyword(message);
                    controller.create(key);
                    System.out.println("KEYWORD [" + key.getKeyword() + "] has been created [OK]");
                    publish(RESULT + ":" + createdKeyword, "" + key.getId());
                } else if (split[1].equals(createActivityname) && !ignore) {
                    String message = new String(mm.getPayload());
                    System.out.println("CREATING KEYWORD: " + message);
                    ActivityName activityName = new ActivityName();
                    activityName.setName(message);
                    controllerActivityName.create(activityName);
                    System.out.println("AN [" + activityName.getName() + "] has been created [OK]");
                    Gson gson = new Gson();
                    String mu = gson.toJson(activityName);
                    publish(RESULT + ":" + createActivityname, mu);
                } else if (split[1].equals(createGroup) && !ignore) {
                    String message = new String(mm.getPayload());
                    System.out.println("CREATING GROUP: " + message);
                    Gson gson = new Gson();
                    GroupActivity ga = gson.fromJson(message, GroupActivity.class);
                    controllerGroup.create(ga);
                    System.out.println("GROUP [" + ga.getId() + "] has been created [OK]");
                    Gson gson2 = new Gson();
                    publish(RESULT + ":" + createGroup, gson2.toJson(ga));
                } else if (split[1].equals(editGroup) && !ignore) {
                    String message = new String(mm.getPayload());
                    System.out.println("EDITING GROUP: " + message);
                    Gson gson = new Gson();
                    GroupActivity ga = gson.fromJson(message, GroupActivity.class);
                    controllerGroup.edit(ga);
                    System.out.println("GROUP [" + ga.getId() + "] has been edited [OK]");
//                Gson gson2 = new Gson();
//                publish(RESULT + ":" + editGroup, gson2.toJson(ga));
                } else if (split[1].equals(deleteGroup) && !ignore) {
                    String message = new String(mm.getPayload());
                    System.out.println("EDITING GROUP: " + message);
                    Gson gson = new Gson();
                    Long gaId = gson.fromJson(message, Long.class);
                    controllerGroup.destroy(gaId);
                    System.out.println("GROUP [" + gaId + "] has been deleted [OK]");
                    publish(RESULT + ":" + deleteGroup, "" + gaId);
                } else if (split[1].equals(deleteKeyword) && !ignore) {
                    String message = new String(mm.getPayload());
                    Long keyId = Long.parseLong(message);
                    KeywordJpaController controller = new KeywordJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
                    controller.destroy(keyId);
                    System.out.println("KEYWORD [" + keyId + "] has been deleted [OK]");
                    publish(RESULT + ":" + keyDeleted, "" + keyId);
                } else if (split[1].equals(updateKeywords) && !ignore) {
                    String message = new String(mm.getPayload()); //primo id è l'id della persona  e poi tutte le keys
                    String[] ids = message.split(":");
                    if (ids.length > 0) {
                        Long personId = Long.parseLong(ids[0]);
                        System.out.println("CREATING KEYWORD: " + message);
                        PersonJpaController personController = new PersonJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
                        Person findPerson = personController.findPerson(personId);
                        findPerson.getKeywords().clear();
                        KeywordJpaController keyController = new KeywordJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
                        if (findPerson != null) {
                            for (int i = 1; i < ids.length; i++) {
                                Long keyID = Long.parseLong(ids[i]);
                                Keyword kkey = keyController.findKeyword(keyID);
                                if (kkey != null) {
                                    findPerson.getKeywords().add(kkey);
                                }
                            }
                        }
                        personController.edit(findPerson);
                    }

                }

            } else if (topic.equals(updateUserSleep) && !ignore) {

                String message = new String(mm.getPayload());

                String[] split = message.split(":");
                Long id = Long.parseLong(split[0]);
                Boolean sleeping = Boolean.parseBoolean(split[1]);
                PersonJpaController controller = new PersonJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
                Person person = controller.findPerson(id);
                if (person != null) {
                    person.setSleeping(sleeping);
                }
                controller.edit(person);
                System.out.println("[DB] Edit person with id=" + id + ", sleep=" + sleeping);

            } else if (topic.startsWith(APP)) {
                System.out.println("QUALCOSA VIENE DALL'APP");
                String[] split = topic.split(":");
                if (split[0].equals(APP) && split.length == 3 && !ignore) {
                    if (split[2].equals(deliverDelay)) {
                        System.out.println("E' DELIVER DELAY");
                        String message = new String(mm.getPayload()); //primo id è l'id della persona  e poi tutte le keys
                        String[] parts = message.split(":");
                        String appTurn = parts[0];
                        String codePersonWithDelay = split[1];
                        int delay = Integer.parseInt(parts[1]);
                        System.out.println("delay with : " + delay);
                        System.out.println("APP TURN = " + appTurn);
                        deliverDelay(appTurn, codePersonWithDelay, delay);
                    }
                }

            }
        } catch (Exception ex) {
            System.out.println("ERROR DATABASE OR UNKWNOWN");
            ex.printStackTrace();

        }
    }

    public boolean clearAppDatabase() {
        try {
            List<AppTurno> allapps = controllerAppTurno.findAppTurnoEntities();
            List<AppTurnIndex> index = controllerIndex.findAppTurnIndexEntities();
            List<AppCollegue> colls = controllerAppCollegue.findAppCollegueEntities();
            List<TurnCancellation> cancellations = controllerCancellation.findTurnCancellationEntities();

            for (TurnCancellation cancellation : cancellations) {
                controllerCancellation.destroy(cancellation.getId());
            }

            for (AppTurnIndex ind : index) {
                System.out.println("DESTROY INDEX: " + ind.getId());
                controllerIndex.destroy(ind.getId());
            }

            for (AppTurno allapp : allapps) {
                System.out.println("DESTROYING APPTURN: " + allapp.getId());
                allapp.getCollegues().clear();
                controllerAppTurno.edit(allapp);
                System.out.println("Colleghi azzerati");
                controllerAppTurno.destroy(allapp.getId());
                System.out.println("DESTROYED");
            }

            for (AppCollegue coll : colls) {
                System.out.println("COLLEGUE: " + coll.getId());
                controllerAppCollegue.destroy(coll.getId());
            }
            return true;

        } catch (NonexistentEntityException ex) {
            Logger.getLogger(MQTT_Server_Manager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (Exception ex) {
            Logger.getLogger(MQTT_Server_Manager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) {
        System.out.println("delivery complete");
    }

    public void publish(String topic, String message) throws MqttException {
        System.out.println("sending message:");
        System.out.println("\tTopic: " + topic);
        System.out.println("\tMessage: " + message);
        System.out.println("----------------------------------------------");
        sampleClient.publish(topic, new MqttMessage(message.getBytes()));
    }

    public List<AppTurno> getAppTurnoByCode(String codePerson) {
        System.out.println("invoking get app turno bby code");
        List<AppTurno> result = null;
        try {
            EntityManagerFactory cem = Persistence.createEntityManagerFactory("sponsor-server-mqttPU");

            TypedQuery<AppTurnIndex> query
                    = cem.createEntityManager().createNamedQuery("AppTurnIndex.findAllByCodePerson", AppTurnIndex.class);
            query.setParameter("x", codePerson);
            AppTurnIndex singleResult = query.getSingleResult();
            if (singleResult == null) {
                result = new ArrayList<>();
            } else {
                result = singleResult.getTurns();
            }
        } catch (Exception ex) {
            System.out.println("NO RESULT");
            return new ArrayList<>();
        }
        System.out.println("QUERY COMPLETED");
        System.out.println("SIZE->" + result.size());
        return result;
    }

    public List<AppTurn> getAppTurnByCode(String codePerson) {
        System.out.println("invoking get app turn bby code");
        List<AppTurn> result = null;
        EntityManagerFactory cem = Persistence.createEntityManagerFactory("sponsor-server-mqttPU");

        TypedQuery<AppTurn> query
                = cem.createEntityManager().createNamedQuery("AppTurn.findAllByCodePerson", AppTurn.class);
        query.setParameter("x", codePerson);
        result = query.getResultList();
        System.out.println("QUERY COMPLETED");
        System.out.println("SIZE->" + result.size());
        for (AppTurn appTurn : result) {
            System.out.println("APPTURN ACTIVITY: " + appTurn.getActivity());
            System.out.println("APPTURN CODE:     " + appTurn.getCodeTurn());
            System.out.println("APPTURN START:    " + appTurn.getStartTime());
            System.out.println("APPTURN END:      " + appTurn.getEndTime());
        }
        System.out.println("**************************************************");

        return result;
    }

    public Person getPersonByCode(String code) {
        try {
            if (cachePersonMap.containsKey(code)) {
                return this.cachePersonMap.get(code);
            }
            EntityManagerFactory cem = Persistence.createEntityManagerFactory("sponsor-server-mqttPU");
            TypedQuery<Person> qq_ritardone
                    = cem.createEntityManager().createNamedQuery("Person.findPersonByCode", Person.class);
            qq_ritardone.setParameter("x", code);
            Person person = qq_ritardone.getSingleResult();
            this.cachePersonMap.put(code, person);
            return person;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

//    public void deliverDelay(String appTurn, String codePersonWithDelay, int delay) {
//        System.out.println("APP TURN -> " + appTurn);
//        System.out.println("DELAY    -> " + delay);
//
//        if (appTurn.equals("TEST")) {
//            try {
//                publish(APP + "2:" + "gcmuevnjx:" + deliverDelay, "Luca purtroppo arriverà al turno delle 9:00 del 23/03/2017 di teleassistenza con 15 minuti di ritardo.");
//            } catch (MqttException ex) {
//                Logger.getLogger(MQTT_Server_Manager.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        List<AppTurn> result = null;
//        EntityManagerFactory cem = Persistence.createEntityManagerFactory("sponsor-server-mqttPU");
//
//        TypedQuery<AppTurn> query
//                = cem.createEntityManager().createNamedQuery("AppTurn.findAllByCodeTurn", AppTurn.class);
//        query.setParameter("x", appTurn);
//        result = query.getResultList();
//
//        TypedQuery<Person> qq_ritardone
//                = cem.createEntityManager().createNamedQuery("Person.findPersonByCode", Person.class);
//        qq_ritardone.setParameter("x", codePersonWithDelay);
//        Person ritardatario = qq_ritardone.getSingleResult();
//
//        for (AppTurn at : result) {
//            String codePerson = at.getCodePerson();
//            if (codePerson.equals(ritardatario.getCode())) {
//                try {
//                    publish(APP2 + ":" + codePerson + ":" + deliverDelay, "Il tuo ritardo è stato comunicato agli altri");
//                } catch (MqttException ex) {
//                    Logger.getLogger(MQTT_Server_Manager.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                at.setTime(delay);
//                AppTurnJpaController c = new AppTurnJpaController(Persistence.createEntityManagerFactory("sponsor-server-mqttPU"));
//                try {
//                    c.edit(at);
//                } catch (Exception ex) {
//                    Logger.getLogger(MQTT_Server_Manager.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                continue;
//            }
//            TypedQuery<Person> qq2
//                    = cem.createEntityManager().createNamedQuery("Person.findPersonByCode", Person.class);
//            qq2.setParameter("x", codePerson);
//            Person person = qq2.getSingleResult();
//            try {
//                System.out.println("SENDING MESSAGE TO CODE: " + codePerson);
//                Date startTime = at.getStartTime();
//                SimpleDateFormat sm = new SimpleDateFormat("HH:mm");
//                String orario = sm.format(startTime);
//                SimpleDateFormat sm2 = new SimpleDateFormat("dd/MM/yyyy");
//                String data = sm2.format(startTime);
//
//                publish(APP2 + ":" + codePerson + ":" + deliverDelay, person.toString() + " purtroppo arriverà al turno delle " + orario + " del " + data + " di " + at.getActivity() + " con " + delay + " minuti di ritardo.");
//            } catch (MqttException ex) {
//                Logger.getLogger(MQTT_Server_Manager.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//
//    }
    public void deliverDelay(String idTurn, String codePersonWithDelay, int delay) {

        Person personByCode = this.getPersonByCode(codePersonWithDelay);
        AppTurno turno = controllerAppTurno.findAppTurno(Long.parseLong(idTurn));
        List<AppCollegue> collegues = turno.getCollegues();
        Date startTime = new Date(turno.getStartTime());
        SimpleDateFormat sm = new SimpleDateFormat("HH:mm");
        String orario = sm.format(startTime);
        SimpleDateFormat sm2 = new SimpleDateFormat("dd/MM/yyyy");
        String data = sm2.format(startTime);
        List<String> collCode = new ArrayList<>();
        for (AppCollegue collegue : collegues) {
            collCode.add(collegue.getPersonCode());
            if (collegue.getPersonCode().equals(codePersonWithDelay)) {
                try {
                    collegue.setDelay(delay);
                    controllerAppCollegue.edit(collegue);

                    publish(APP2 + ":" + delayInfoData, codePersonWithDelay + ":" + idTurn + ":" + delay + ":" + personByCode.toString() + " purtroppo arriverà al turno delle " + orario + " del " + data + " di " + turno.getActivity() + " con " + delay + " minuti di ritardo.");

                } catch (Exception ex) {
                    Logger.getLogger(MQTT_Server_Manager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        for (String admin : admins) {
            if (collCode.contains(admin)) {
                continue;
            }
            try {
                publish(APP2 + ":" + personalChannel + ":" + admin, "Ok:" + personByCode.toString() + " purtroppo arriverà al turno delle " + orario + " del " + data + " di " + turno.getActivity() + " con " + delay + " minuti di ritardo.");
            } catch (MqttException ex) {
                Logger.getLogger(MQTT_Server_Manager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void startCancellationTurn(String idTurn, String personCode) {
        Date startProcedure = new Date();
        Person personByCode = this.getPersonByCode(personCode);
        AppTurno turno = controllerAppTurno.findAppTurno(Long.parseLong(idTurn));
        Date startTime = new Date(turno.getStartTime());
        SimpleDateFormat sm = new SimpleDateFormat("HH:mm");
        String orario = sm.format(startTime);
        SimpleDateFormat sm2 = new SimpleDateFormat("dd/MM/yyyy");
        String data = sm2.format(startTime);

        List<AppCollegue> collegues = turno.getCollegues();
        AppCollegue escapedOne = null;
        for (AppCollegue collegue : collegues) {
            if (collegue.getPersonCode().equals(personCode)) {
                try {
                    System.out.println("COLLEGUE: " + collegue.getPersonCode() + "IS ESCAPED");
                    collegue.setAborted(true);
                    controllerAppCollegue.edit(collegue);
                    escapedOne = collegue;
                    System.out.println("DATABASE UPDATED OK");
                } catch (Exception ex) {
                    Logger.getLogger(MQTT_Server_Manager.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                    publish(APP2 + ":" + turnCancellation_START + ":" + collegue.getPersonCode(), personCode + ":" + idTurn + ":" + personByCode.toString() + " purtroppo non potrà venire al turno delle " + orario + " del " + data + " di " + turno.getActivity());
                    // publish(APP2 + ":" + personalChannel + ":" + collegue.getPersonCode(), "Ok" + ":" + "Il tuo collega " + personByCode + " non verrà al turno di "+turno.getActivity()+" del "+data+" delle ore "+orario+". Stiamo provando a cercare una soluzione.");
                } catch (MqttException ex) {
                    Logger.getLogger(MQTT_Server_Manager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if (escapedOne != null) {
            TurnCancellation cancellation = new TurnCancellation();
            cancellation.setAppTurno(turno);
            cancellation.setCollegue(escapedOne);
            cancellation.setTimeActivation(startProcedure.getTime());
            controllerCancellation.create(cancellation);
            System.out.println("CANCELLAZIONE CREATA!");

//            SimpleDateFormat sm = new SimpleDateFormat("HH:mm");
//            String orario = sm.format(startTime);
//            SimpleDateFormat sm2 = new SimpleDateFormat("dd/MM/yyyy");
//            String data = sm2.format(startTime);
            List<String> alreadySent = new ArrayList<>();
            alreadySent.add(personCode);
            try {
                publish(APP2 + ":" + turnCancellation_START + ":" + personCode, personCode + ":" + idTurn + ":" + personByCode.toString() + " purtroppo non potrà venire al turno delle " + orario + " del " + data + " di " + turno.getActivity());
            } catch (MqttException ex) {
                Logger.getLogger(MQTT_Server_Manager.class.getName()).log(Level.SEVERE, null, ex);
            }
            List<Person> candidatePeople = getAvailablePersonForTurn(turno, personCode);

            cancellation.setCandidatePeople(candidatePeople);
            try {
                controllerCancellation.edit(cancellation);
                for (Person person : candidatePeople) {
                    alreadySent.add(person.getCode());
                    publish(APP2 + ":" + turnCancellation_PHASE_1 + ":" + person.getCode(), cancellation.getId() + ":" + idTurn + ":" + personCode + ":" + "Vuoi sostituire il tuo collega al turno delle " + orario + " del " + data + " di " + turno.getActivity());
                }
            } catch (Exception ex) {
                Logger.getLogger(MQTT_Server_Manager.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("A D M I N " + admins);
            for (String admin : admins) {
                if (!alreadySent.contains(admin)) {
                    try {
                        publish(APP2 + ":" + turnCancellation_START + ":" + admin, cancellation.getId() + ":" + idTurn + ":" + "Ti informiamo che " + personByCode + " ha dichiarato di non poter venire al turno delle " + orario + " del " + data + " di " + turno.getActivity());
                    } catch (MqttException ex) {
                        Logger.getLogger(MQTT_Server_Manager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        }

    }

    public boolean isTurnoContainsPerson(AppTurno turno, String personCode) {
        for (AppCollegue collegue : turno.getCollegues()) {
            if (collegue.getPersonCode().equals(personCode)) {
                return true;
            }
        }
        return false;
    }

    public List<Person> getAvailablePersonForTurn(AppTurno turno, String personCodeNotMe) {
//        long startTime = turno.getStartTime();
//        long endTime = turno.getEndTime();
        List<Person> result = new ArrayList<>();
        List<Person> people = controllerPerson.findPersonEntities();
        for (Person person : people) {
            if (person.getCode().equals(personCodeNotMe)) {
                continue;
            }
            person.fix();

            for (FreeTimeToken availableTime : person.getExpandedFreeTimes()) {
                if (turno.getStartTime() + 1000 >= availableTime.getStarTime() && turno.getEndTime() - 1000 <= availableTime.getEndTime()) {
                    if (!isTurnoContainsPerson(turno, person.getCode())) {
                        result.add(person);
                        System.out.println(">>> CANDIDATE FOUND:" + person);
                    } else {
                        System.out.println("not this, is a collegone: " + person);
                    }
                }
            }
        }
        System.out.println("AVAIL for turn = " + result.size());
        return result;
    }

    public List<TurnCancellation> getCancellationByPersonCode(String code) {
        List<TurnCancellation> allCancellation = controllerCancellation.findTurnCancellationEntities();
        List<TurnCancellation> result = new ArrayList<>();

        for (TurnCancellation turnCancellation : allCancellation) {
            List<Person> candidates = turnCancellation.getCandidatePeople();
            for (Person candidate : candidates) {
                if (candidate.getCode().equals(code)) {
                    result.add(turnCancellation);
                }
            }
        }
        System.out.println("SIZE OF CANCELLATION IS: " + result.size());
        return result;
    }

    public Person declareWinner(TurnCancellation cancellation) throws MqttException {
        List<Person> ackPeople = cancellation.getYesPeople();
        Person winner = null;
        AppTurno problematicTurno = cancellation.getAppTurno();
        Person personaCancellata = getPersonByCode(cancellation.getCollegue().getPersonCode());
        Date start = new Date(problematicTurno.getStartTime());
        SimpleDateFormat sm = new SimpleDateFormat("HH:mm");
        String orario = sm.format(start);
        SimpleDateFormat sm2 = new SimpleDateFormat("dd/MM/yyyy");
        String data = sm2.format(start);

        for (Person person : ackPeople) {
            winner = person;
            break;
        }
//        JOptionPane.showMessageDialog(null, "SI PUO PROCEDER !!" + winner);

        for (Person person : cancellation.getYesPeople()) {
            if (person.getCode().equals(winner.getCode())) {
                try {
                    //WINNER
                    publish(APP2 + ":" + personalChannel + ":" + person.getCode(), "Ok" + ":" + "Sei stato selezionato per la sostituzione del turno di " + cancellation.getAppTurno().getActivity() + " del giorno " + data + " alle ore " + orario + " al posto di " + personaCancellata);
                } catch (MqttException ex) {
                    Logger.getLogger(MQTT_Server_Manager.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                //DISCARDED
                publish(APP2 + ":" + personalChannel + ":" + person.getCode(), "Ok" + ":" + "Grazie per la disponibiltà espressa al di " + cancellation.getAppTurno().getActivity() + " del giorno " + data + " alle ore " + orario + " al posto di " + personaCancellata + " ma il problema è stato risolto in altro modo. Non c'è più bisogno del tuo aiuto. Grazie lo stesso!");
            }
        }

//        for (String admin : admins) {
        dispatchAdmins("Ok", "ATTENZIONE!  L'assenza al turno di " + problematicTurno.getActivity() + " in data " + data + " delle ore " + orario + " a causa dell'assenza di " + personaCancellata + " è stata risolta. Al suo posto verrà il volontario: " + winner);
//        }
//        for (String admin : admins) {
//
//            try {
//                publish(APP2 + ":" + personalChannel + ":" + admin, "Ok" + ":" + "ATTENZIONE!  L'assenza al turno di " + problematicTurno.getActivity() + " in data " + data + " delle ore " + orario + " a causa dell'assenza di " + personaCancellata + " è stata risolta. Al suo posto verrà il volontario: " + winner);
//            } catch (MqttException ex) {
//                Logger.getLogger(MQTT_Server_Manager.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }

//        List<AppCollegue> collegues = problematicTurno.getCollegues();
        AppCollegue winnerCollegue = new AppCollegue();
        winnerCollegue.setPersonCode(winner.getCode());
        winnerCollegue.setSubsitute(true);
        problematicTurno.getCollegues().add(winnerCollegue);
        try {
            controllerAppTurno.edit(problematicTurno);
        } catch (Exception ex) {
            Logger.getLogger(MQTT_Server_Manager.class.getName()).log(Level.SEVERE, null, ex);
        }

//        for (AppCollegue collegue : collegues) {
//            if (collegue.isAborted()) {
//                collegue.setPersonCode(winner.getCode());
//                collegue.setSubsitute(true);
//                collegue.setAborted(false);
//                try {
//                    controllerAppCollegue.edit(collegue);
//                    System.out.println("TURN ABORTION OK NOW");
//                } catch (Exception ex) {
//                    Logger.getLogger(MQTT_Server_Manager.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                break;
//            }
//        }
        try {
            controllerCancellation.destroy(cancellation.getId());

            EntityManagerFactory cem = Persistence.createEntityManagerFactory("sponsor-server-mqttPU");

            TypedQuery<AppTurnIndex> query
                    = cem.createEntityManager().createNamedQuery("AppTurnIndex.findAllByCodePerson", AppTurnIndex.class);
            query.setParameter("x", winner.getCode());
            AppTurnIndex indexWinner = query.getSingleResult();

            indexWinner.getTurns().add(problematicTurno);
            try {
                controllerIndex.edit(indexWinner);
            } catch (Exception ex) {
                Logger.getLogger(MQTT_Server_Manager.class.getName()).log(Level.SEVERE, null, ex);
            }

//            TypedQuery<AppTurnIndex> queryLoser
//                    = cem.createEntityManager().createNamedQuery("AppTurnIndex.findAllByCodePerson", AppTurnIndex.class);
//            queryLoser.setParameter("x", cancellation.getCollegue().getPersonCode());
//            AppTurnIndex indexLoser = queryLoser.getSingleResult();
//            indexLoser.getTurns().remove(cancellation.getAppTurno());
//            try {
//                controllerIndex.edit(indexLoser);
//            } catch (Exception ex) {
//                Logger.getLogger(MQTT_Server_Manager.class.getName()).log(Level.SEVERE, null, ex);
//            }
            for (AppCollegue collegue : problematicTurno.getCollegues()) {
                Date today = new Date();
                List<AppTurno> turnos = getAppTurnoByCode(collegue.getPersonCode());
                if (Configuration.getInstance().isReleaseActualTurns()) {
                    ListIterator<AppTurno> listIterator = turnos.listIterator();
                    while (listIterator.hasNext()) {
                        if (listIterator.next().getStartTime() < today.getTime()) {
                            System.out.println("REMOVE");
                            listIterator.remove();
                        }
                    }
                } else {
                    System.out.println("CONFIG WRONG");
                }
                Type type = new TypeToken<ArrayList<AppTurno>>() {
                }.getType();
                Gson gson = new Gson();
                String turnResults = gson.toJson(turnos, type);
                publish(APP2 + ":" + getTurnosByPersonCode + ":" + collegue.getPersonCode(), turnResults);
            }

        } catch (NonexistentEntityException ex) {
            Logger.getLogger(MQTT_Server_Manager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return winner;
    }

    @Deprecated
    public void abortTurn(String idTurn, String personCodeWhoEscaped) {
        Person personByCode = this.getPersonByCode(personCodeWhoEscaped);
        AppTurno turno = controllerAppTurno.findAppTurno(Long.parseLong(idTurn));
        List<AppCollegue> collegues = turno.getCollegues();
        Date startTime = new Date(turno.getStartTime());
        for (AppCollegue collegue : collegues) {
            if (collegue.getPersonCode().equals(personCodeWhoEscaped)) {
                try {
                    collegue.setAborted(true);
                    controllerAppCollegue.edit(collegue);
                    SimpleDateFormat sm = new SimpleDateFormat("HH:mm");
                    String orario = sm.format(startTime);
                    SimpleDateFormat sm2 = new SimpleDateFormat("dd/MM/yyyy");
                    String data = sm2.format(startTime);

                    publish(APP2 + ":" + turnCancellation_START, personCodeWhoEscaped + ":" + idTurn + ":" + personByCode.toString() + " purtroppo non potrà venire al turno delle " + orario + " del " + data + " di " + turno.getActivity());

                } catch (Exception ex) {
                    Logger.getLogger(MQTT_Server_Manager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        //TO BE CONTINUED
    }

    private void saveProject(Project project) throws Exception {
        List<Activity> activities = project.getActivities();
        Map<Long, List<Long>> turnKeyMap = new HashMap<>();
        for (Activity activity : activities) {
            activity.setId(null);
            List<ActivityTurn> activityTurns = activity.getActivityTurns();
            for (ActivityTurn activityTurn : activityTurns) {
                activityTurn.setId(null);
                List<ComfirmedTurn> comfirmedTurns = activityTurn.getComfirmedTurns();
                for (ComfirmedTurn comfirmedTurn : comfirmedTurns) {
                    if (comfirmedTurn.getId() == null) {
                        controllerCoomfirmedTurn.create(comfirmedTurn);
                    }
                }
                for (JobTurn hiv : activityTurn.getRequiredProfiles()) {
                    hiv.setId(null);
                    controllerJobTurn.create(hiv);
//                    hiv.setWantedKeywords(wantedKeywords);
//                    for (Keyword wantedKeyword : wantedKeywords) {
//                        System.out.println("------------- WW "+wantedKeyword.getKeyword());
//                    }

                    System.out.println("[DB] job turn: " + hiv.toString() + " created [OK]");
                }

                for (ActivityTurnKeyword wantedKeyword : activityTurn.getWantedKeywords()) {
                    controllerTurnKeyword.create(wantedKeyword);
                }
//                List<Long> keyids = new ArrayList<>();
//                List<Keyword> wantedKeywords = activityTurn.getWantedKeywords();
//                System.out.println("THIS TURN WANT: " + wantedKeywords.size() + " keywords");
//                for (Keyword wantedKeyword : wantedKeywords) {
//                    System.out.println("KEY: " + wantedKeyword.getKeyword());
//                    keyids.add(wantedKeyword.getId());
//                }
                activityTurn.setId(null);
//                activityTurn.getWantedKeywords().clear();
                controllerActivityTurn.create(activityTurn);
//                turnKeyMap.put(activityTurn.getId(), keyids);
                System.out.println("[DB] activity turn: " + activityTurn.toString() + " created [OK]");
            }
            controllerActivity.create(activity);
            System.out.println("[DB] activity: " + activity.toString() + " created [OK]");

//            for (Long turnId : turnKeyMap.keySet()) {
//                ActivityTurn turn = controllerActivityTurn.findActivityTurn(turnId);
//                List<Long> keys = turnKeyMap.get(turnId);
//                for (Long key : keys) {
//                    Keyword findKeyword = controllerKeyword.findKeyword(key);
//                    turn.getWantedKeywords().add(findKeyword);
//                }
//                controllerActivityTurn.edit(turn);
//            }
        }
        controllerProject.create(project);

        System.out.println("<<<<<<<<<<<<<<<<   TEST  >>>>>>>>>>>>>>>>>>");
        System.out.println("***************************************************");
        System.out.println("               P R O J E C T");
        System.out.println("               " + project.getName());
        System.out.println("***************************************************");
        for (Activity activity : project.getActivities()) {
            System.out.println("\tActivity: " + activity.getActivityName().getName());
            System.out.println("\tTRIGGER EAGER. " + activity.getActivityTurns().isEmpty() + " size: " + activity.getActivityTurns().size());
            for (ActivityTurn activityTurn : activity.getActivityTurns()) {
                System.out.println("\t\tTRIGGER EAGER KEY. " + activityTurn.getWantedKeywords().isEmpty() + " size: " + activityTurn.getWantedKeywords().size());
                System.out.println("\t\tTURN: " + activityTurn);
                System.out.println("KEY TURN: " + activityTurn.getWantedKeywords());
            }
        }
        System.out.println("<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>");

        Project savedProject = controllerProject.findProject(project.getId());

        Gson gson2 = new Gson();
        String message = gson2.toJson(project, Project.class);
        try {
            publish(RESULT + ":" + projectSaved, message);
        } catch (MqttException ex) {
            Logger.getLogger(MQTT_Server_Manager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Project getProject(Long id) {
        Project project = controllerProject.findProject(id);
        System.out.println("***************************************************");
        System.out.println("               P R O J E C T");
        System.out.println("               " + project.getName());
        System.out.println("***************************************************");
        for (Activity activity : project.getActivities()) {
            System.out.println("\tActivity: " + activity.getActivityName().getName());
            System.out.println("\tTRIGGER EAGER. " + activity.getActivityTurns().isEmpty() + " size: " + activity.getActivityTurns().size());
            for (ActivityTurn activityTurn : activity.getActivityTurns()) {
                System.out.println("\t\tTRIGGER EAGER KEY. " + activityTurn.getWantedKeywords().isEmpty() + " size: " + activityTurn.getWantedKeywords().size());
                System.out.println("\t\tTURN: " + activityTurn);
                System.out.println("KEY TURN: " + activityTurn.getWantedKeywords());
            }
        }
        System.out.println("---------------------------------------------------");
        return project;
    }

    private void dispatchAdmins(String yesButton, String message) {
        for (String admin : admins) {
            try {
                publish(APP2 + ":" + personalChannel + ":" + admin, yesButton + ":" + message);
            } catch (MqttException ex) {
                Logger.getLogger(MQTT_Server_Manager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void checkAdmin(List<Person> pe) {
        System.out.println("CHECK ADMINNO");
        if (admins.isEmpty()) {
            for (Person person : pe) {
                System.out.println("searching for admin - > PERSON: " + person.getName() + " " + person.getSurname() + "is admin: " + person.isAdmin());
                if (person.isAdmin() && !admins.contains(person.getCode())) {
                    admins.add(person.getCode());
                }
            }
        }
    }
}
