package com.boha.geo.monitor.services;

import com.boha.geo.models.KillResponse;
import com.boha.geo.monitor.data.*;
import com.boha.geo.repos.KillResponseRepository;
import com.boha.geo.repos.UserRepository;
import com.boha.geo.util.E;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.result.UpdateResult;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    private static final Gson G = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class.getSimpleName());
    private static final String xx = E.COFFEE+E.COFFEE+E.COFFEE;

    private final UserRepository userRepository;
    private final KillResponseRepository killResponseRepository;
    private final MongoTemplate mongoTemplate;

    public MessageService(UserRepository userRepository, KillResponseRepository killResponseRepository, MongoTemplate mongoTemplate) {
        this.userRepository = userRepository;
        this.killResponseRepository = killResponseRepository;
        this.mongoTemplate = mongoTemplate;
    }


    public String sendMessage(Photo photo) throws FirebaseMessagingException {
        String topic = "photos_" + photo.getOrganizationId();
        Message message = Message.builder()
                .putData("photo", G.toJson(photo))
                .setTopic(topic)
                .build();
        String response = FirebaseMessaging.getInstance().send(message);
        LOGGER.info(E.RED_APPLE + E.RED_APPLE + "Successfully sent photo message to FCM topic: "
                + topic+ E.RED_APPLE);
        return response;
    }
    public String sendMessage(Audio audio) throws FirebaseMessagingException {
        String topic = "audios_" + audio.getOrganizationId();
        Message message = Message.builder()
                .putData("audio", G.toJson(audio))
                .setTopic(topic)
                .build();
        String response = FirebaseMessaging.getInstance().send(message);
        LOGGER.info(E.RED_APPLE + E.RED_APPLE + "Successfully sent audio message to FCM topic: "
                + topic+ E.RED_APPLE);
        return response;
    }
    public String sendMessage(Video video) throws FirebaseMessagingException {
        String topic = "videos_" + video.getOrganizationId();
        Message message = Message.builder()
                .putData("video", G.toJson(video))
                .setTopic(topic)
                .build();
        String response = FirebaseMessaging.getInstance().send(message);
        LOGGER.info(E.RED_APPLE + E.RED_APPLE + "Successfully sent video message to FCM topic: "
                + topic + E.RED_APPLE);
        return response;
    }
    public String sendMessage(Condition condition) throws FirebaseMessagingException {
        String topic = "conditions_" + condition.getOrganizationId();
        Message message = Message.builder()
                .putData("condition", G.toJson(condition))
                .setTopic(topic)
                .build();
        String response = FirebaseMessaging.getInstance().send(message);
        LOGGER.info(E.RED_APPLE + E.RED_APPLE + "Successfully sent condition message to FCM topic: "
                + topic + " "  + E.RED_APPLE);
        return response;
    }
    public String sendMessage(FieldMonitorSchedule fieldMonitorSchedule) throws FirebaseMessagingException {
        String topic = "fieldMonitorSchedules_" + fieldMonitorSchedule.getOrganizationId();
        Message message = Message.builder()
                .putData("fieldMonitorSchedule", G.toJson(fieldMonitorSchedule))
                .setTopic(topic)
                .build();
        String response =FirebaseMessaging.getInstance().send(message);
        LOGGER.info(E.RED_APPLE + E.RED_APPLE + "Successfully sent fieldMonitorSchedule message to FCM topic: "
                + topic + " "  + E.RED_APPLE);
        return response;
    }
    public String sendMessage(OrgMessage orgMessage) throws FirebaseMessagingException {
        assert(orgMessage.getOrganizationId() != null);
        Notification notification = Notification.builder()
                .setBody(orgMessage.getMessage())
                .setTitle("Message from Digital Monitor")
                .build();
        if (orgMessage.getFcmRegistration() == null) {
            String topic = "messages_" + orgMessage.getOrganizationId();
            Message message = Message.builder()
                    .putData("message", G.toJson(orgMessage))
                    .setTopic(topic)
                    .setNotification(notification)
                    .build();
            String response = FirebaseMessaging.getInstance().send(message);
            LOGGER.info(E.RED_APPLE + E.RED_APPLE + "Successfully sent org message to FCM topic: "
                    + topic + E.RED_APPLE);
            return response;
        } else {
            assert(orgMessage.getFcmRegistration() != null);
            Message message = Message.builder()
                    .putData("message", G.toJson(orgMessage))
                    .setToken(orgMessage.getFcmRegistration())
                    .setNotification(notification)
                    .build();
            String response = FirebaseMessaging.getInstance().send(message);
            LOGGER.info(E.RED_APPLE + E.RED_APPLE + "Successfully sent user message to FCM device: "
                    + orgMessage.getFcmRegistration() + " " + E.RED_APPLE);
            return response;
        }
    }
    public String sendMessage(Project project) throws FirebaseMessagingException {
        String topic = "projects_" + project.getOrganizationId();
        Message message = Message.builder()
                .putData("project", G.toJson(project))
                .setTopic(topic)
                .build();
        String response = FirebaseMessaging.getInstance().send(message);
        LOGGER.info(E.RED_APPLE + E.RED_APPLE + "Successfully sent project message to FCM topic: "
                + topic + E.RED_APPLE);
        return response;
    }
    public String sendMessage(User user) throws FirebaseMessagingException {
        String topic = "users_" + user.getOrganizationId();
        Message message = Message.builder()
                .putData("user", G.toJson(user))
                .setTopic(topic)
                .build();
        String response = FirebaseMessaging.getInstance().send(message);
        LOGGER.info(E.RED_APPLE + E.RED_APPLE + "Successfully sent user message to FCM topic: "
                + topic + E.RED_APPLE);
        return response;
    }

    public KillResponse sendKillMessage(String userId, String killerId) throws Exception {

        User user = userRepository.findByUserId(userId);
        User killer = userRepository.findByUserId(killerId);
        String topic = "kill_" + user.getOrganizationId();

        Message message = Message.builder()
                .putData("kill", G.toJson(user))
                .setTopic(topic)
                .build();

        FirebaseMessaging.getInstance().send(message);

        LOGGER.info(E.RED_APPLE + E.RED_APPLE + "Successfully sent kill user message to FCM topic: "
                + topic + E.RED_APPLE);

        deleteAuthUser(userId);

        LOGGER.info(E.RED_APPLE + E.RED_APPLE + "Successfully deleted user from Firebase auth "
                + topic + E.RED_APPLE);

        //mark user as inactive
        LOGGER.info(E.RED_APPLE+E.RED_APPLE + " update user and set the active flag to 9 ");
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        query.fields().include("userId");

        Update update = new Update();
        update.set("active", 9);
        update.set("updated", DateTime.now().toDateTimeISO().toString());

        UpdateResult result = mongoTemplate.updateFirst(query, update, User.class);
        LOGGER.info(E.RED_APPLE+E.RED_APPLE + " user has been modified: " + result.getModifiedCount());
        //
        KillResponse resp = new KillResponse();
        resp.setMessage("User "+user.getName()+ " has been de-authenticated");
        resp.setDate(DateTime.now().toDateTimeISO().toString());
        resp.setUser(user);
        resp.setKiller(killer);
        resp.setOrganizationId(killer.getOrganizationId());

        KillResponse killResponse = killResponseRepository.insert(resp);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        LOGGER.info(E.RED_DOT +E.RED_DOT + " KillResponse added to database: " + E.YELLOW_STAR + gson.toJson(killResponse));

        return killResponse;
    }


    public int deleteAuthUser(String userId) throws Exception {
        LOGGER.info(E.WARNING.concat(E.WARNING.concat(E.WARNING)
                .concat(" DELETING AUTH USER from Firebase .... ").concat(E.RED_DOT)));
        try {
            FirebaseAuth.getInstance().deleteUser(userId);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 9;
        }


    }
}
