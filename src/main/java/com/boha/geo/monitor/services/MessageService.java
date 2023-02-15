package com.boha.geo.monitor.services;

import com.boha.geo.models.KillResponse;
import com.boha.geo.monitor.data.*;
import com.boha.geo.repos.KillResponseRepository;
import com.boha.geo.repos.UserRepository;
import com.boha.geo.util.E;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.*;
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

import java.util.HashMap;
import java.util.Map;

@Service
public class MessageService {
    private static final Gson G = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class.getSimpleName());
    private static final String xx = E.COFFEE + E.COFFEE + E.COFFEE;

    private final UserRepository userRepository;
    private final KillResponseRepository killResponseRepository;
    private final MongoTemplate mongoTemplate;

    public MessageService(UserRepository userRepository, KillResponseRepository killResponseRepository, MongoTemplate mongoTemplate) {
        this.userRepository = userRepository;
        this.killResponseRepository = killResponseRepository;
        this.mongoTemplate = mongoTemplate;

        setAPNSHeaders();
    }
/*
const admin = require("firebase-admin");

admin.initializeApp({
  credential: admin.credential.cert(require("./service-account-file.json")),
  databaseURL: "https://....firebaseio.com",
});

admin.messaging().send({
  token: "device token",
  data: {
    hello: "world",
  },
  // Set Android priority to "high"
  android: {
    priority: "high",
  },
  // Add APNS (Apple) config
  apns: {
    payload: {
      aps: {
        contentAvailable: true,
      },
    },
    headers: {
      "apns-push-type": "background",
      "apns-priority": "5", // Must be `5` when `contentAvailable` is set to true.
      "apns-topic": "io.flutter.plugins.firebase.messaging", // bundle identifier
    },
  },
});
 */

    Map<String, String> apns = new HashMap<>();

    void setAPNSHeaders() {
        apns.put("apns-push-type", "background");
        apns.put("apns-priority", "5");
        apns.put("apns-topic", "io.flutter.plugins.firebase.messaging");
    }

    private Message buildMessage(String dataName, String topic, String payload) {
        return Message.builder()
                .putData(dataName, payload)
                .setFcmOptions(FcmOptions.builder()
                        .setAnalyticsLabel("GeoFCM").build())
                .setAndroidConfig(AndroidConfig.builder()
                        .setPriority(AndroidConfig.Priority.HIGH)
                        .build())
//                .setApnsConfig(ApnsConfig.builder()
//                        .putAllHeaders(apns)
//                        .setAps(Aps.builder()
//                                .setAlert("Geo Message")
//                                .build())
//                        .build())
                .setTopic(topic)
                .build();
    }
    private Message buildMessage(String dataName, String topic, String payload, Notification notification) {
        return Message.builder()
                .setNotification(notification)
                .putData(dataName, payload)
                .setFcmOptions(FcmOptions.builder()
                        .setAnalyticsLabel("GeoFCM").build())
                .setAndroidConfig(AndroidConfig.builder()
                        .setPriority(AndroidConfig.Priority.HIGH)
                        .build())
//                .setApnsConfig(ApnsConfig.builder()
//                        .putAllHeaders(apns)
//                        .setAps(Aps.builder()
//                                .setAlert("Geo Message")
//                                .build())
//                        .build())
                .setTopic(topic)
                .build();
    }

    public void sendMessage(ProjectPosition projectPosition) throws FirebaseMessagingException {
        String topic = "projectPositions_" + projectPosition.getOrganizationId();
        Notification notification = Notification.builder()
                .setBody("A project location added by " + projectPosition.getUserName())
                .setTitle("Message from Geo")
                .build();
        Message message = buildMessage("projectPosition", topic, G.toJson(projectPosition),notification);
        String response = FirebaseMessaging.getInstance().send(message);
        LOGGER.info(E.RED_APPLE + E.RED_APPLE + "Successfully sent projectPosition message to FCM topic: "
                + topic + E.RED_APPLE + response);
    }

    public String sendMessage(ProjectAssignment projectAssignment) throws FirebaseMessagingException {
        String topic = "projectAssignments_" + projectAssignment.getOrganizationId();
        Message message = buildMessage("projectAssignment", topic, G.toJson(projectAssignment));

        String response = FirebaseMessaging.getInstance().send(message);
        LOGGER.info(E.RED_APPLE + E.RED_APPLE + "Successfully sent projectAssignment message to FCM topic: "
                + topic + E.RED_APPLE + response);
        return response;
    }

    public void sendMessage(ProjectPolygon projectPolygon) throws FirebaseMessagingException {
        String topic = "projectPolygons_" + projectPolygon.getOrganizationId();
        Notification notification = Notification.builder()
                .setBody("A project area has been added by " + projectPolygon.getUserName())
                .setTitle("Message from Geo")
                .build();
        Message message = buildMessage("projectPolygon",topic,G.toJson(projectPolygon),notification);
        String response = FirebaseMessaging.getInstance().send(message);
        LOGGER.info(E.RED_APPLE + E.RED_APPLE + "Successfully sent projectPolygon message to FCM topic: "
                + topic + E.RED_APPLE + response);
    }

    public void sendMessage(ActivityModel activityModel) throws FirebaseMessagingException {
        String topic = "activities_" + activityModel.getOrganizationId();

        Message message = buildMessage("activity",
                topic, G.toJson(activityModel));
        String response = FirebaseMessaging.getInstance().send(message);

        LOGGER.info(E.RED_APPLE + E.RED_APPLE + "Successfully sent activityModel message to FCM topic: "
                + topic + E.RED_APPLE + response);
    }

    public void sendMessage(Photo photo) throws FirebaseMessagingException {
        String topic = "photos_" + photo.getOrganizationId();
        Notification notification = Notification.builder()
                .setBody("A photo from the field has arrived from " + photo.getUserName())
                .setTitle("Message from Geo")
                .build();
        Message message = buildMessage("photo", topic, G.toJson(photo), notification);

        String response = FirebaseMessaging.getInstance().send(message);
        LOGGER.info(E.RED_APPLE + E.RED_APPLE + "Successfully sent photo message to FCM topic: "
                + topic + E.RED_APPLE);
    }

    public void sendMessage(SettingsModel settingsModel) throws FirebaseMessagingException {
        String topic = "settings_" + settingsModel.getOrganizationId();
        Message message = buildMessage("settings", topic, G.toJson(settingsModel));


        String response = FirebaseMessaging.getInstance().send(message);
        LOGGER.info(E.RED_APPLE + E.RED_APPLE + "Successfully sent settingsModel message to FCM topic: "
                + topic + E.RED_APPLE + response);
    }

    public void sendMessage(GeofenceEvent geofenceEvent) throws FirebaseMessagingException {
        String topic = "geofenceEvents_" + geofenceEvent.getOrganizationId();

        Notification notification = Notification.builder()
                .setBody("A member has arrived in the field: " + geofenceEvent.getUser().getName())
                .setTitle("Message from Geo")
                .build();

        Message message = buildMessage("geofenceEvent", topic, G.toJson(geofenceEvent), notification);

        FirebaseMessaging.getInstance().send(message);
        LOGGER.info(E.RED_APPLE + E.RED_APPLE + "Successfully sent geofenceEvent message to FCM topic: "
                + topic + E.RED_APPLE);
    }

    public String sendMessage(Audio audio) throws FirebaseMessagingException {
        String topic = "audios_" + audio.getOrganizationId();

        Notification notification = Notification.builder()
                .setBody("An audio clip from the field has arrived")
                .setTitle("Message from Geo")
                .build();
        Message message = buildMessage("audio", topic, G.toJson(audio), notification);

        String response = FirebaseMessaging.getInstance().send(message);
        LOGGER.info(E.RED_APPLE + E.RED_APPLE + "Successfully sent audio message to FCM topic: "
                + topic + E.RED_APPLE);
        return response;
    }

    public LocationRequest sendMessage(LocationRequest locationRequest) throws FirebaseMessagingException {
        String topic = "locationRequests_" + locationRequest.getOrganizationId();

        Notification notification = Notification.builder()
                .setBody("A request for location arrived")
                .setTitle("Message from Geo")
                .build();

        Message message = buildMessage("locationRequest",
                topic, G.toJson(locationRequest), notification);

        String response = FirebaseMessaging.getInstance().send(message);
        LOGGER.info(E.RED_APPLE + E.RED_APPLE + "Successfully sent locationRequest message to FCM topic: "
                + topic + E.RED_APPLE + " resp: " + response);
        return locationRequest;
    }
    public void sendMessage(LocationResponse locationResponse) throws FirebaseMessagingException {
        String topic = "locationResponses_" + locationResponse.getOrganizationId();

        Notification notification = Notification.builder()
                .setBody("A response to location request")
                .setTitle("Message from Geo")
                .build();

        Message message = buildMessage("locationResponse", topic,
                G.toJson(locationResponse), notification);

        String response = FirebaseMessaging.getInstance().send(message);
        LOGGER.info(E.RED_APPLE + E.RED_APPLE + "Successfully sent locationResponse message to FCM topic: "
                + topic + E.RED_APPLE + " resp: " + response);
    }

    public String sendMessage(Video video) throws FirebaseMessagingException {
        String topic = "videos_" + video.getOrganizationId();
//        Message message = buildMessage("video", topic, G.toJson(video));

        Notification notification = Notification.builder()
                .setBody("A video from the field has arrived")
                .setTitle("Message from Geo")
                .build();

        Message message = buildMessage("video", topic, G.toJson(video),notification);

        String response = FirebaseMessaging.getInstance().send(message);
        LOGGER.info(E.RED_APPLE + E.RED_APPLE + "Successfully sent video message to FCM topic: "
                + topic + E.RED_APPLE);
        return response;
    }

    public String sendMessage(Condition condition) throws FirebaseMessagingException {
        String topic = "conditions_" + condition.getOrganizationId();
        Message message = buildMessage("condition", topic, G.toJson(condition));

        String response = FirebaseMessaging.getInstance().send(message);
        LOGGER.info(E.RED_APPLE + E.RED_APPLE + "Successfully sent condition message to FCM topic: "
                + topic + " " + E.RED_APPLE);
        return response;
    }

    public String sendMessage(FieldMonitorSchedule fieldMonitorSchedule) throws FirebaseMessagingException {
        String topic = "fieldMonitorSchedules_" + fieldMonitorSchedule.getOrganizationId();
        Message message = buildMessage("fieldMonitorSchedule", topic, G.toJson(fieldMonitorSchedule));

        String response = FirebaseMessaging.getInstance().send(message);
        LOGGER.info(E.RED_APPLE + E.RED_APPLE + "Successfully sent fieldMonitorSchedule message to FCM topic: "
                + topic + " " + E.RED_APPLE);
        return response;
    }

    public String sendMessage(OrgMessage orgMessage) throws FirebaseMessagingException {
        assert (orgMessage.getOrganizationId() != null);
        Notification notification = Notification.builder()
                .setBody(orgMessage.getMessage())
                .setTitle("Message from Geo")
                .build();
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

    }

    public String sendMessage(Project project) throws FirebaseMessagingException {
        String topic = "projects_" + project.getOrganizationId();

        Notification notification = Notification.builder()
                .setBody("A project has been added: " + project.getName())
                .setTitle("Message from Geo")
                .build();
        Message message = buildMessage("project", topic, G.toJson(project), notification);

        String response = FirebaseMessaging.getInstance().send(message);
        LOGGER.info(E.RED_APPLE + E.RED_APPLE + "Successfully sent project message to FCM topic: "
                + topic + E.RED_APPLE);
        return response;
    }

    public String sendMessage(User user) throws FirebaseMessagingException {
        String topic = "users_" + user.getOrganizationId();
        Notification notification = Notification.builder()
                .setBody("A member has been added or modified")
                .setTitle("Message from Geo")
                .build();
        Message message = buildMessage("user", topic, G.toJson(user), notification);

        String response = FirebaseMessaging.getInstance().send(message);
        LOGGER.info(E.RED_APPLE + E.RED_APPLE + "Successfully sent user message to FCM topic: "
                + topic + E.RED_APPLE);
        return response;
    }

    public KillResponse sendKillMessage(String userId, String killerId) throws Exception {

        User user = userRepository.findByUserId(userId);
        User killer = userRepository.findByUserId(killerId);
        String topic = "kill_" + user.getOrganizationId();

        Message message = buildMessage("kill", topic, G.toJson(user));
        FirebaseMessaging.getInstance().send(message);

        LOGGER.info(E.RED_APPLE + E.RED_APPLE + "Successfully sent kill user message to FCM topic: "
                + topic + E.RED_APPLE);

        deleteAuthUser(userId);
        LOGGER.info(E.RED_APPLE + E.RED_APPLE + "Successfully deleted user from Firebase auth "
                + topic + E.RED_APPLE);

        //mark user as inactive
        LOGGER.info(E.RED_APPLE + E.RED_APPLE + " update user and set the active flag to 9 ");
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        query.fields().include("userId");

        Update update = new Update();
        update.set("active", 9);
        update.set("updated", DateTime.now().toDateTimeISO().toString());

        UpdateResult result = mongoTemplate.updateFirst(query, update, User.class);
        LOGGER.info(E.RED_APPLE + E.RED_APPLE + " user has been modified: " + result.getModifiedCount());
        //
        KillResponse resp = new KillResponse();
        resp.setMessage("User " + user.getName() + " has been de-authenticated");
        resp.setDate(DateTime.now().toDateTimeISO().toString());
        resp.setUser(user);
        resp.setKiller(killer);
        resp.setOrganizationId(killer.getOrganizationId());

        KillResponse killResponse = killResponseRepository.insert(resp);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        LOGGER.info(E.RED_DOT + E.RED_DOT + " KillResponse added to database: " + E.YELLOW_STAR + gson.toJson(killResponse));

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
