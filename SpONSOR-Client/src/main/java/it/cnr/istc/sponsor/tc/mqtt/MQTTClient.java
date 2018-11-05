/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tc.mqtt;

import com.google.gson.Gson;
import it.cnr.istc.sponsor.tc.gui.ConfigurationManager;
import it.cnr.istc.sponsor.tc.logic.Account;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
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
    public static String subscriptionTopic = "SUBSCRIPTION";
    public static String clientRegistrationDone = "clientRegistrationDone";
    private MqttClient sampleClient = null;
    private String broker = "";
//            "tcp://150.146.65.103:1883";
//    private String broker = "tcp://localhost:1883";
    private String clientId = "client-sponsor";

    public static MQTTClient getInstance() {
        if (_instance == null) {
            _instance = new MQTTClient();
            return _instance;
        } else {
            return _instance;
        }
    }

    public void connect() {
        try {
            MemoryPersistence persistence = new MemoryPersistence();
            sampleClient = new MqttClient(broker, clientId+(""+new Date().getTime()), persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: " + broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");

            sampleClient.setCallback(_instance);
            sampleClient.subscribe("#");
        } catch (MqttException ex) {
            Logger.getLogger(MQTTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private MQTTClient() {
        super();
        broker = "tcp://" + ConfigurationManager.getInstance().getIp() + ":1883";

    }

    @Override
    public void connectionLost(Throwable thrwbl) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage mm) throws Exception {
        if (topic.equals(subscriptionTopic)) {
            System.out.println("Qualcuno si è iscritto");
            String message = new String(mm.getPayload());
            System.out.println("Message -> " + message);

            Gson gson = new Gson();
            Account accountPewe = gson.fromJson(message, Account.class);
            System.out.println("Account name:       " + accountPewe.getName());
            System.out.println("Account surname:    " + accountPewe.getSurname());
            System.out.println("Account date   :    " + accountPewe.getBornDate());
            System.out.println("Account gender :    " + accountPewe.getGender());
            System.out.println("Account animazione: " + accountPewe.isAnimation());
            System.out.println("Account live close: " + accountPewe.isLiveClose());
            System.out.println("Account teleassist: " + accountPewe.isTeleAssistent());
            System.out.println("==============================================");

        } else if (topic.equals(clientRegistrationDone)) {
            System.out.println("Qualcuno si è iscritto bis");
            String message = new String(mm.getPayload());
            System.out.println("Message -> " + message);
            String[] split = message.split(":");
            String name = split[0];
            String code = split[1];
            if (ConfigurationManager.getInstance().isAlternanza()) {
                JOptionPane.showMessageDialog(null, "I tuoi dati sono stati inviati correttamente!", "Questionario completato!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, name + ", grazie di esserti iscritto. Di seguito il codice per attivare Sponsor-App: " + code, "Iscrizione riuscita!", JOptionPane.INFORMATION_MESSAGE);
            }
        }

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) {

    }

    public void publish(String topic, String message) throws MqttException {
        sampleClient.publish(topic, new MqttMessage(message.getBytes()));
    }

}
