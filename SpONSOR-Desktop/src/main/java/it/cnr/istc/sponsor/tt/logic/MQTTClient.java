/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.logic;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.cnr.istc.sponsor.tt.logic.model.Account;
import it.cnr.istc.sponsor.tt.logic.model.Activity;
import it.cnr.istc.sponsor.tt.logic.model.ActivityName;
import it.cnr.istc.sponsor.tt.logic.model.ActivityTurn;
import it.cnr.istc.sponsor.tt.logic.model.AppTurn;
import it.cnr.istc.sponsor.tt.logic.model.Job;
import it.cnr.istc.sponsor.tt.logic.model.Keyword;
import it.cnr.istc.sponsor.tt.logic.model.Person;
import it.cnr.istc.sponsor.tt.logic.model.Project;
import it.cnr.istc.sponsor.tt.logic.model.Skill;
import it.cnr.istc.sponsor.tt.logic.model.app.AppTurno;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class MQTTClient implements MqttCallback {

    private static MQTTClient _instance = null;
    private static final String subscriptionTopic = "SUBSCRIPTION";
    private MqttAsyncClient sampleClient = null;
    private String broker = "tcp://localhost:1883";
    private String clientId = "sponsor-trainer";
    private List<MQTTListener> listeners = new ArrayList<>();
    private String myCode = "123321";

    public static final String getAllPeople = "getAllPeople";
    public static final String getAllJobs = "getAllJobs";
    public static final String getAllSkills = "getAllSkills";
    public static final String getAllKeywords = "getAllKeywords";
    public static final String getAllDBActivities = "getAllDBActivities";
    public static final String createJob = "createJob";
    public static final String deleteJob = "deleteJob";
    public static final String jobDeleted = "jobDeleted";
    public static final String deleteUser = "deleteuser";
    public static final String createdUser = "createdUser";
    public static final String createdUser2 = "createdUser2";
    public static final String userDeleted = "userDeleted";
    public static final String updateUserSleep = "updateUserSleep";
    public static final String createKeyword = "createKeyword";
    public static final String deleteKeyword = "deleteKeyword";
    public static final String createdKeyword = "createdKeyword";
    public static final String updateKeywords = "updateKeywords";
    public static final String keyDeleted = "keyDeleted";
    public static final String queryBase = "query";
    public static final String RESULT = "result";
    public static final String saveTurns = "saveTurns";
    public static final String saveTurni = "saveTurni";
    public static final String saveProject = "saveProject";
    public static final String projectSaved = "projectSaved";
    public static final String getAllProjects = "getAllProjects";
    public static final String getProjectById = "getProjectById";
    public static final String jobCannotBeDeleted = "jobCannotBeDeleted";
    public static final String editPerson = "editPerson";
    public static final String createActivityname = "createActivityname";
    public static final String createFakes = "createFakes";
    public static final String projectDeleted = "projectDeleted";
    public static final String imalive = "imalive";
    public long lastServerCheck = 0;
    public long lastServerAnswer = 0;
    public boolean online = false;
    private boolean ignoreDuplicate=false;

    private List<Long> waitingSaveCode = new ArrayList<>();

    public static MQTTClient getInstance() {
        if (_instance == null) {
            _instance = new MQTTClient();
            return _instance;
        } else {
            return _instance;
        }

    }

    public boolean isServerActive() {

        boolean newStatus = (Math.abs(lastServerAnswer - lastServerCheck) < 5000);
        if (!online && newStatus) {
//            this.connect();
        } else if (online && !newStatus) {
//            this.disconnect();
        }

        online = newStatus;
        return online;
    }

    public synchronized void addMQTTListener(MQTTListener listener) {
        // JOptionPane.showMessageDialog(null, "TU VUOI ENTRARE: "+listener.getClass().getCanonicalName());
        this.listeners.add(listener);
    }

//    public void connect() {
//        try {
//            MemoryPersistence persistence = new MemoryPersistence();
//            sampleClient = new MqttAsyncClient(broker, clientId, persistence);
//            MqttConnectOptions connOpts = new MqttConnectOptions();
//            connOpts.setCleanSession(true);
//            System.out.println("Connecting to broker: " + broker);
//            sampleClient.connect(connOpts);
//            System.out.println("Connected");
//
//            sampleClient.setCallback(_instance);
//            sampleClient.subscribe("#",2);
//        } catch (MqttException ex) {
//            Logger.getLogger(MQTTClient.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    public synchronized void connect() {
        MemoryPersistence dataStore = new MemoryPersistence();

        try {

            clientId = MqttAsyncClient.generateClientId();
//            System.out.println("old ID      paho1383215117963899");
            System.out.println("CLIENT ID : " + clientId);

            broker = "tcp://" + SettingsManager.getInstance().getServerIP() + ":1883";
            sampleClient = new MqttAsyncClient(this.broker, clientId, dataStore);

            System.out.println("Connecting: " + broker); //$NON-NLS-1$

            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(false);
            connOpts.setKeepAliveInterval(Integer.MAX_VALUE);
            IMqttToken mqttToken = sampleClient.connect(connOpts);
            mqttToken.waitForCompletion(10000);
            if (mqttToken.isComplete()) {
                if (mqttToken.getException() != null) {
                    // TODO: retry
                    System.out.println("retry.. not done");
                }
            }
            sampleClient.setCallback(_instance);
            sampleClient.subscribe("#", 2);

            System.out.println("CONNECTION DONE");
            TrainerManager.getInstance();
        } catch (MqttException e) {
            e.printStackTrace();

            // TODO:  Log
            System.out.println("fatal error");
            JOptionPane.showMessageDialog(null, "Il server ha riscontrato un problema ed è stato disconnesso, attentede o contattare gli amministratori", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    public synchronized void disconnect() {
        try {
//            sampleClient.disconnect();
//            sampleClient.disconnectForcibly();
//            System.out.println("DISCONNESSO");
// Disconnect the client
            // Issue the disconnect and then use a token to wait until
            // the disconnect completes.
            System.out.println("DISCONNECTING");
            IMqttToken discToken = sampleClient.disconnect(null, null);
            discToken.waitForCompletion();
            System.out.println("DISCONNECTED");

        } catch (MqttException ex) {
            Logger.getLogger(MQTTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private MQTTClient() {
        super();
//        JOptionPane.showMessageDialog(null, "Startiamo il server check");
        Thread aliveT = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    lastServerCheck = new Date().getTime();
                    isServerAlive();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MQTTClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        aliveT.start();

    }

    @Override
    public void connectionLost(Throwable thrwbl) {

    }

    @Override
    public synchronized void messageArrived(String topic, MqttMessage mm) throws Exception {
        try {
            if (topic.equals(createdUser2)) {
                String message = new String(mm.getPayload());
//                System.out.println("Message -> " + message);

                Gson gson = new Gson();
                Person person = gson.fromJson(message, Person.class);
                System.out.println("Qualcuno si è iscritto con code: " + person.getCode());
                for (MQTTListener listener : listeners) {
                    listener.userCreated(person);
                }
//                TrainerManager.getInstance().addNewPerson(person);
            }
            if (topic.equals(createdUser)) {
                System.out.println("Qualcuno si è iscritto");
                String message = new String(mm.getPayload());
                System.out.println("Message -> " + message);

                Gson gson = new Gson();
                Account accountPewe = gson.fromJson(message, Account.class);
                System.out.println("Account ID:        " + accountPewe.getId());
                System.out.println("Account name:        " + accountPewe.getName());
                System.out.println("Account surname:     " + accountPewe.getSurname());
                System.out.println("Account date   :     " + accountPewe.getBornDate());
                System.out.println("Account gender :     " + accountPewe.getGender());
                System.out.println("Account animazione:  " + accountPewe.isAnimation());
                System.out.println("Account live close:  " + accountPewe.isLiveClose());
                System.out.println("Account teleassist:  " + accountPewe.isTeleAssistent());
                System.out.println("Account test result: " + accountPewe.getPerceptionQuestionnary());
                System.out.println("==============================================");
//            ResultTableModel model = new ResultTableModel();
//            model.parseData(accountPewe);
                TrainerManager.getInstance().newAccountDetected(accountPewe);

                System.out.println("NEW ACCOUNT TETECHTES >" + accountPewe.getSurname() + "<");
                if (accountPewe.getSurname().equals("") || accountPewe.getSurname().equals("-") || accountPewe.getSurname().equals("1") || accountPewe.getSurname().equals("P.") || accountPewe.getSurname().equals(" ")) {

                } else {
                    JOptionPane.showMessageDialog(null, accountPewe.getName() + " " + accountPewe.getSurname() + " si è registrato!");
                }

            } else if (topic.equals("result:" + getAllPeople)) {
                System.out.println("GET ALL PEOPLE !");
                Gson gson = new Gson();
                String message = new String(mm.getPayload());
                System.out.println("MESSAGE ->> " + message);
                Type type = new TypeToken<ArrayList<Person>>() {
                }.getType();
                List<Person> people = (List<Person>) gson.fromJson(message, type);
                Collections.sort(people);
                System.out.println("PEOPLE ARRIVED: " + people.size());
                for (Person person : people) {
                    person.setNote(person.getAccount().getNote());
                    if(person.getNote()!=null){
                        JOptionPane.showMessageDialog(null, "NOTA !! "+person.getNote());
                    }
                    System.out.println("NOTE = "+person.getNote());
                    System.out.println("NOTE ACC = "+person.getAccount().getNote());
                    System.out.println("---------------------------------");
                }

                for (MQTTListener listener : listeners) {
                    listener.peopleDataArrived(people);
                }

            } else if (topic.equals("result:" + getAllJobs)) {
                System.out.println("GET ALL JOBS !");
                Gson gson = new Gson();
                String message = new String(mm.getPayload());
                System.out.println("MESSAGE ->> " + message);
                Type type = new TypeToken<ArrayList<Job>>() {
                }.getType();
                List<Job> jobs = (List<Job>) gson.fromJson(message, type);
                for (MQTTListener listener : listeners) {
                    listener.jobsDataArrived(jobs);
                }

            } else if (topic.equals("result:" + getAllSkills)) {
                System.out.println("GET ALL SKILLS !");
                Gson gson = new Gson();
                String message = new String(mm.getPayload());
                System.out.println("MESSAGE ->> " + message);
                Type type = new TypeToken<ArrayList<Skill>>() {
                }.getType();
                List<Skill> skill = (List<Skill>) gson.fromJson(message, type);
                for (Skill SKSK : skill) {
                    System.out.println("SKILL -> " + SKSK.getName() + ", ID -> " + SKSK.getId());
                }
                for (MQTTListener listener : listeners) {
                    listener.skillsDataArrived(skill);
                }

            } else if (topic.equals("result:" + getAllKeywords)) {
                System.out.println("GET ALL KEYWORDS !");
                Gson gson = new Gson();
                String message = new String(mm.getPayload());
                System.out.println("MESSAGE ->> " + message);
                Type type = new TypeToken<ArrayList<Keyword>>() {
                }.getType();
                List<Keyword> keywords = (List<Keyword>) gson.fromJson(message, type);
                for (MQTTListener listener : listeners) {
                    listener.keywordsDataArrived(keywords);
                }

            } else if (topic.equals("result:" + userDeleted)) {
                System.out.println("USER DELETED !");
                Gson gson = new Gson();
                String message = new String(mm.getPayload());
                System.out.println("MESSAGE ->> " + message);
                Long id = gson.fromJson(message, Long.class);
                for (MQTTListener listener : listeners) {
                    listener.userDeleted(id);
                }

            } else if (topic.equals("result:" + jobDeleted)) {
                System.out.println("USER DELETED !");
                Gson gson = new Gson();
                String message = new String(mm.getPayload());
                System.out.println("MESSAGE ->> " + message);
                Long id = gson.fromJson(message, Long.class);
                for (MQTTListener listener : listeners) {
                    listener.jobDeleted(id);
                }

            } else if (topic.equals("result:" + keyDeleted)) {
                System.out.println("KEYWORD DELETED !");
                Gson gson = new Gson();
                String message = new String(mm.getPayload());
                System.out.println("MESSAGE ->> " + message);
                Long id = gson.fromJson(message, Long.class);
                for (MQTTListener listener : listeners) {
                    listener.keywordDeleted(id);
                }

            } else if (topic.equals("result:" + createdKeyword)) {
                System.out.println("KEYWORD CREATED !");
                Gson gson = new Gson();
                String message = new String(mm.getPayload());
                System.out.println("MESSAGE ->> " + message);
                Long id = gson.fromJson(message, Long.class);
                for (MQTTListener listener : listeners) {
                    listener.keywordCreated(id);
                }

            } else if (topic.equals("result:" + createActivityname)) {
                System.out.println("AN CREATED !");
                Gson gson = new Gson();
                String message = new String(mm.getPayload());
                System.out.println("MESSAGE ->> " + message);
                ActivityName an = gson.fromJson(message, ActivityName.class);
                for (MQTTListener listener : listeners) {
                    listener.activityNameCreated(an);
                }

            } else if (topic.equals("result:" + saveTurns)) {
                System.out.println("KEYWORD CREATED !");
                Gson gson = new Gson();
                String message = new String(mm.getPayload());
                System.out.println("MESSAGE ->> " + message);
                Long time = gson.fromJson(message, Long.class);
                if (this.waitingSaveCode.contains(time)) {
                    this.waitingSaveCode.remove(time);
                    JOptionPane.showMessageDialog(null, "Turnazione Savata nel server!");
                }
            } else if (topic.equals("result:" + createJob)) {
                System.out.println("JOB CREATED !");
                Gson gson = new Gson();
                String message = new String(mm.getPayload());
                String[] split = message.split(":");
                System.out.println("MESSAGE ->> " + message);
                String nameJob = split[0];
                Long idJob = gson.fromJson(split[1], Long.class);
                for (MQTTListener listener : listeners) {
                    listener.jobCreated(nameJob, idJob);
                }
            } else if (topic.equals("result:" + projectSaved)) {
                System.out.println("PROJECT CREATED !");
                Gson gson = new Gson();
                String message = new String(mm.getPayload());
                Project project = gson.fromJson(message, Project.class);
                JOptionPane.showMessageDialog(null, "Il progetto: " + project.getName() + " è stato salvato con successo", "Salvataggio riuscito", JOptionPane.INFORMATION_MESSAGE);
                for (MQTTListener listener : listeners) {
                    listener.projectCreated(project);
                }
            } else if (topic.equals("result:" + createActivityname)) {
                System.out.println("ACTIVITY NAME CREATED !");
                Gson gson = new Gson();
                String message = new String(mm.getPayload());
                ActivityName an = gson.fromJson(message, ActivityName.class);
                for (MQTTListener listener : listeners) {
                    listener.activityNameCreated(an);
                }
            } else if (topic.equals("result:" + getProjectById)) {
                if (!ignoreDuplicate) {
                    System.out.println("PROJECT CREATED !");
                    Gson gson = new Gson();
                    String message = new String(mm.getPayload());
                    System.out.println("Project LOADED: " + message);
                    Project project = gson.fromJson(message, Project.class);
//                JOptionPane.sho  wMessageDialog(null, "Il progetto: " + project.getName() + " è stato caricato con successo", "Salvataggio riuscito", JOptionPane.INFORMATION_MESSAGE);
                    List<Activity> activities = project.getActivities();
                    for (Activity activity : activities) {
                        for (ActivityTurn activityTurn : activity.getActivityTurns()) {
                            System.out.println("TURNS: " + activityTurn);
                        }
                    }
                    for (MQTTListener listener : listeners) {
                        listener.projectLoaded(project);
                    }
                    ignoreDuplicate = true;
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(7000);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(MQTTClient.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            ignoreDuplicate = false;
                        }
                    });
                    t.start();
                }

            } else if (topic.equals("result:" + getAllProjects)) {
                System.out.println("ALL PROJECTS FOUND");
                Gson gson = new Gson();
                String message = new String(mm.getPayload());

                System.out.println("MESSAGE ->> " + message);
                Type type = new TypeToken<ArrayList<Project>>() {
                }.getType();
                List<Project> projects = (List<Project>) gson.fromJson(message, type);
                for (MQTTListener listener : listeners) {
                    listener.projectsDataArrived(projects);
                }
            } else if (topic.equals("result:" + getAllDBActivities)) {
                System.out.println("ALL ACTIVITIES FOUND");
                Gson gson = new Gson();
                String message = new String(mm.getPayload());

                System.out.println("MESSAGE ->> " + message);
                Type type = new TypeToken<ArrayList<ActivityName>>() {
                }.getType();
                List<ActivityName> activities = (List<ActivityName>) gson.fromJson(message, type);
                for (MQTTListener listener : listeners) {
                    listener.activityNamesDataArrived(activities);
                }
            } else if (topic.equals("result:" + jobCannotBeDeleted)) {
                String message = new String(mm.getPayload());
                Long id = Long.parseLong(message);

                List<Job> jobs = TrainerManager.getInstance().getJobs();
                for (Job job : jobs) {
                    if (job.getId().equals(id)) {
                        JOptionPane.showMessageDialog(null, "Il profilo: " + job.getName() + " non può essere cancellato", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }
                
                //public static final String projectDeleted = "projectDeleted";
            } else if (topic.equals("result:" + imalive)) {
                String message = new String(mm.getPayload());
                if (message.equals("YES")) {
                    Date now = new Date();
                    this.lastServerAnswer = now.getTime();
                    System.out.println("server answer got. YES");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) {

    }

    public synchronized void publish(String topic, String message) throws MqttException {
        int t = 1;
        while (!sampleClient.isConnected()) {
            System.out.println("CLIENT NOT CONNECTED.. WAITING.. ATTEMPT. n. " + t);
            t++;
            try {
                Thread.sleep(1000);
                connect();
            } catch (InterruptedException ex) {
                Logger.getLogger(MQTTClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        sampleClient.publish(topic, new MqttMessage(message.getBytes()));
    }

    public synchronized void setSleeping(Account account, boolean sleeping) {
        String message = "" + account.getId() + ":" + sleeping;
        System.out.println("MESSAGE UPDATE ACCOUNT: " + message);
        try {
            publish(MQTTClient.updateUserSleep, message);
        } catch (MqttException ex) {
            Logger.getLogger(MQTTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void createKeyword(String key) {
        String message = key;
        System.out.println("MESSAGE ACCOUNT: " + message);
        try {
            publish(MQTTClient.queryBase + ":" + createKeyword, message);
        } catch (MqttException ex) {
            Logger.getLogger(MQTTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void updatePersonKeyword(Long idPerson, List<Keyword> keywords) {
        String message = "" + idPerson;
        for (Keyword keyword : keywords) {
            message += ":" + keyword.getId();

        }
        System.out.println("UPDATING KEYWORDS, MESSAGE: " + message);
        try {
            publish(MQTTClient.queryBase + ":" + updateKeywords, message);
        } catch (MqttException ex) {
            Logger.getLogger(MQTTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void createFakeAccount(List<Account> accounts) {
        try {
            Type type = new TypeToken<ArrayList<Account>>() {
            }.getType();
            Gson gson = new Gson();
            String message = gson.toJson(accounts, type);
            publish(MQTTClient.queryBase + ":" + createFakes, message);
        } catch (MqttException ex) {
            Logger.getLogger(MQTTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void createAccount(Account account) {
        Gson gson = new Gson();
        String message = gson.toJson(account, Account.class);
        System.out.println("MESSAGE ACCOUNT: " + message);
        try {
            publish(MQTTClient.subscriptionTopic, message);
        } catch (MqttException ex) {
            Logger.getLogger(MQTTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void createJob(Job job) {
        try {
            String topic = queryBase + ":" + createJob;

            Gson jsonJob = new Gson();
            String message = jsonJob.toJson(job, Job.class);
            System.out.println("SENDING QUERY: " + topic + ", message: " + message);
            publish(topic, message);
        } catch (MqttException ex) {
            Logger.getLogger(MQTTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void deleteUser(Long id) {
        try {
            String topic = queryBase + ":" + deleteUser + ":" + id;
            System.out.println("SENDING QUERY: " + topic);
            publish(topic, clientId);
        } catch (MqttException ex) {
            Logger.getLogger(MQTTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void deleteJob(Long id) {
        try {
            String topic = queryBase + ":" + deleteJob + ":" + id;
            System.out.println("SENDING QUERY: " + topic);
            publish(topic, clientId);
        } catch (MqttException ex) {
            Logger.getLogger(MQTTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void sendQueryToGetPeople() {
        try {
            String topic = queryBase + ":" + getAllPeople;
            System.out.println("SENDING QUERY: " + topic);
            publish(topic, clientId);
        } catch (MqttException ex) {
            Logger.getLogger(MQTTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void sendQueryToGetActivities() {
        try {
            String topic = queryBase + ":" + getAllDBActivities;
            System.out.println("SENDING QUERY: " + topic);
            publish(topic, clientId);
        } catch (MqttException ex) {
            Logger.getLogger(MQTTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void sendQueryToGetProjects() {
        try {
            String topic = queryBase + ":" + getAllProjects;
            System.out.println("SENDING QUERY: " + topic);
            publish(topic, clientId);
        } catch (MqttException ex) {
            Logger.getLogger(MQTTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void saveProject(String nameProject) {
        Project project = new Project();
        project.setName(nameProject);
        List<Activity> activities = TrainerManager.getInstance().getActivities();
        project.setActivities(activities);
        try {
            String topic = queryBase + ":" + saveProject;
            Gson jsonJob = new Gson();
            String message = jsonJob.toJson(project, Project.class);
            System.out.println("SENDING QUERY: " + topic + ", message: " + message);
            publish(topic, message);
        } catch (MqttException ex) {
            Logger.getLogger(MQTTClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public synchronized void sendQueryToGetProject(Long id) {
        try {
            String topic = queryBase + ":" + getProjectById;
            System.out.println("SENDING QUERY: " + topic);
            publish(topic, "" + id);
        } catch (MqttException ex) {
            Logger.getLogger(MQTTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void sendQueryToGetJobs() {
        try {
            String topic = queryBase + ":" + getAllJobs;
            System.out.println("SENDING QUERY: " + topic);
            publish(topic, clientId);
        } catch (MqttException ex) {
            Logger.getLogger(MQTTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void sendQueryToGetSkills() {
        try {
            String topic = queryBase + ":" + getAllSkills;
            System.out.println("SENDING QUERY: " + topic);
            publish(topic, clientId);
        } catch (MqttException ex) {
            Logger.getLogger(MQTTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void editPerson(Person person) {
        try {
            String topic = queryBase + ":" + editPerson;
            System.out.println("SENDING QUERY: " + topic);
            Gson jsonJob = new Gson();

            String message = jsonJob.toJson(person.getAccount(), Account.class);
            person.setOtherData(message);
            JOptionPane.showMessageDialog(null,"NOTE SAVING = "+person.getNote() +" accpunt note = "+person.getAccount().getNote());
            publish(topic, message);
            System.out.println("Edit person query sent");
//            person.setOtherData(message);
            person.setFixed(false);
            person.fix();
            TrainerManager.getInstance().updateGhosts();
        } catch (MqttException ex) {
            Logger.getLogger(MQTTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void sendQueryToGetKeywords() {
        try {
            String topic = queryBase + ":" + getAllKeywords;
            System.out.println("SENDING QUERY: " + topic);
            publish(topic, clientId);
        } catch (MqttException ex) {
            Logger.getLogger(MQTTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void isServerAlive() {
        try {
            String topic = queryBase + ":" + imalive;
//            System.out.println("\t\t\t\t\t\tis the server alive?? ");
//            System.out.println("SENDING QUERY: " + topic);
            publish(topic, myCode);
        } catch (MqttException ex) {
            Logger.getLogger(MQTTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void createActivityName(String name) {
        try {
            String topic = queryBase + ":" + createActivityname;
            System.out.println("SENDING QUERY: " + topic);
            publish(topic, name);
        } catch (MqttException ex) {
            Logger.getLogger(MQTTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void saveAppTurns(List<AppTurn> appTurns) {
        try {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<AppTurn>>() {
            }.getType();
            String message = gson.toJson(appTurns, type);
            Date d = new Date();
            waitingSaveCode.add(d.getTime());
            publish("query:" + saveTurns, message + "!%!" + (d.getTime()));
        } catch (MqttException ex) {
            Logger.getLogger(MQTTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void saveAppTurni(List<AppTurno> appTurns) {
        try {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<AppTurn>>() {
            }.getType();
            String message = gson.toJson(appTurns, type);
            Date d = new Date();
            waitingSaveCode.add(d.getTime());
            publish("query:" + saveTurni, message + "!%!" + (d.getTime()));
        } catch (MqttException ex) {
            Logger.getLogger(MQTTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
