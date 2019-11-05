package it.cnr.istc.pst.sponsor.server;

import static io.javalin.apibuilder.ApiBuilder.delete;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.patch;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.javalin.Javalin;
import io.moquette.broker.Server;
import io.moquette.broker.config.ClasspathResourceLoader;
import io.moquette.broker.config.ResourceLoaderConfig;
import io.moquette.interception.AbstractInterceptHandler;
import io.moquette.interception.messages.InterceptConnectMessage;
import io.moquette.interception.messages.InterceptConnectionLostMessage;
import io.moquette.interception.messages.InterceptDisconnectMessage;
import io.moquette.interception.messages.InterceptPublishMessage;
import io.netty.buffer.ByteBufUtil;
import it.cnr.istc.pst.sponsor.server.db.OrganizationEntity;
import it.cnr.istc.pst.sponsor.server.db.UserEntity;

/**
 * SpONSOR Server
 */
public class App {

    static final Logger LOG = LoggerFactory.getLogger(App.class);
    static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("SpONSOR_PU");

    public static void main(String[] args) throws IOException {
        // we create the app..
        final Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/public");
        });

        app.events(event -> {
            event.serverStarting(() -> LOG.info("Starting SpONSOR server.."));
            event.serverStarted(() -> {
                LOG.info("SpONSOR server is running..");
                EntityManager em = EMF.createEntityManager();

                List<UserEntity> users = em.createQuery("SELECT ue FROM UserEntity ue", UserEntity.class)
                        .getResultList();

                LOG.info("Loading {} users..", users.size());
                for (UserEntity ue : users)
                    UserController.ON_LINE.put(ue.getId(), false);

                List<OrganizationEntity> organizations = em
                        .createQuery("SELECT oe FROM OrganizationEntity oe", OrganizationEntity.class).getResultList();

                LOG.info("Loading {} organizations..", organizations.size());
                for (OrganizationEntity oe : organizations)
                    UserController.ON_LINE.put(oe.getId(), false);
            });
        });

        app.routes(() -> {
            post("login", UserController::login);
            path("users", () -> {
                get(UserController::getAllUsers);
                post(UserController::createUser);
                path(":id", () -> {
                    get(UserController::getUser);
                    patch(UserController::updateUser);
                    delete(UserController::deleteUser);
                });
            });
        });

        // we start the app..
        app.start(7000);

        // we create the MQTT broker..
        final Server mqtt_broker = new Server();

        // we start the MQTT broker..
        mqtt_broker.startServer(new ResourceLoaderConfig(new ClasspathResourceLoader()),
                Collections.singletonList(new AbstractInterceptHandler() {

                    @Override
                    public String getID() {
                        return "EmbeddedLauncherPublishListener";
                    }

                    @Override
                    public void onDisconnect(InterceptDisconnectMessage idm) {
                        UserController.ON_LINE.put(Long.parseLong(idm.getClientID()), false);
                    }

                    @Override
                    public void onConnectionLost(InterceptConnectionLostMessage iclm) {
                        UserController.ON_LINE.put(Long.parseLong(iclm.getClientID()), false);
                    }

                    @Override
                    public void onConnect(InterceptConnectMessage icm) {
                        UserController.ON_LINE.put(Long.parseLong(icm.getClientID()), true);
                    }

                    @Override
                    public void onPublish(InterceptPublishMessage msg) {
                        final String decodedPayload = new String(ByteBufUtil.getBytes(msg.getPayload()), UTF_8);
                        System.out.println("Received on topic: " + msg.getTopicName() + " content: " + decodedPayload);
                    }
                }));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            app.stop();
            mqtt_broker.stopServer();
            EMF.close();
        }));
    }
}