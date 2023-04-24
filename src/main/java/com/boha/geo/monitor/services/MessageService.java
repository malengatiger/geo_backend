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
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor

@Service
public class MessageService {
    private static final Gson G = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class.getSimpleName());
    private static final String xx = E.COFFEE + E.COFFEE + E.COFFEE;

    private final UserRepository userRepository;
    private final KillResponseRepository killResponseRepository;
    private final MongoTemplate mongoTemplate;

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

    public int sendMessage(ProjectPosition projectPosition) {
        try {
            String topic = "projectPositions_" + projectPosition.getOrganizationId();
            Notification notification = Notification.builder()
                    .setBody(projectPosition.getTranslatedMessage())
                    .setTitle(projectPosition.getTranslatedTitle())
                    .build();

            ProjectPosition m = new ProjectPosition();
            m.setPosition(projectPosition.getPosition());
            m.setProjectName(projectPosition.getProjectName());
            m.setOrganizationId(projectPosition.getOrganizationId());
            m.setProjectId(projectPosition.getProjectId());
            m.setUserId(projectPosition.getUserId());
            m.setUserName(projectPosition.getUserName());
            m.setCreated(projectPosition.getCreated());
            m.setCaption(projectPosition.getCaption());
            m.setNearestCities(null);

            Message message = buildMessage("projectPosition", topic,
                    G.toJson(m), notification);
            FirebaseMessaging.getInstance().send(message);

        } catch (Exception e) {
            LOGGER.error("Failed to send projectPosition FCM message");
            e.printStackTrace();
        }
        return 0;
    }

    public int sendMessage(ProjectAssignment projectAssignment) throws FirebaseMessagingException {
        String topic = "projectAssignments_" + projectAssignment.getOrganizationId();
        Message message = buildMessage("projectAssignment", topic, G.toJson(projectAssignment));

        FirebaseMessaging.getInstance().send(message);
        return 0;
    }

    public int sendMessage(ProjectPolygon projectPolygon) throws FirebaseMessagingException {
        String topic = "projectPolygons_" + projectPolygon.getOrganizationId();
        Notification notification = Notification.builder()
                .setBody(projectPolygon.getTranslatedMessage())
                .setTitle(projectPolygon.getTranslatedTitle())
                .build();
        Message message = buildMessage("projectPolygon", topic, G.toJson(projectPolygon), notification);
        FirebaseMessaging.getInstance().send(message);
        return 0;
    }

    public int sendMessage(ActivityModel activityModel) throws FirebaseMessagingException {
        String topic = "activities_" + activityModel.getOrganizationId();

        Message message = buildMessage("activity",
                topic, G.toJson(activityModel));
        FirebaseMessaging.getInstance().send(message);
        return 0;
    }

    public int sendMessage(Photo photo) throws FirebaseMessagingException {
        String topic = "photos_" + photo.getOrganizationId();
        Notification notification = Notification.builder()
                .setBody(photo.getTranslatedMessage())
                .setTitle(photo.getTranslatedTitle())
                .build();
        Message message = buildMessage("photo", topic, G.toJson(photo), notification);

        FirebaseMessaging.getInstance().send(message);
        return 0;
    }

    public int sendMessage(SettingsModel settingsModel) throws FirebaseMessagingException {
        String topic = "settings_" + settingsModel.getOrganizationId();
        Notification notification = Notification.builder()
                .setBody(settingsModel.getTranslatedMessage())
                .setTitle(settingsModel.getTranslatedTitle())
                .build();
        Message message = buildMessage("settings", topic, G.toJson(settingsModel), notification);


        FirebaseMessaging.getInstance().send(message);
        return 0;
    }

    public int sendMessage(GeofenceEvent geofenceEvent) throws FirebaseMessagingException {
        String topic = "geofenceEvents_" + geofenceEvent.getOrganizationId();

        Notification notification = Notification.builder()
                .setBody(geofenceEvent.getTranslatedMessage())
                .setTitle(geofenceEvent.getTranslatedTitle())
                .build();

        Message message = buildMessage("geofenceEvent", topic, G.toJson(geofenceEvent), notification);

        FirebaseMessaging.getInstance().send(message);
        return 0;
    }

    public int sendMessage(Audio audio) throws FirebaseMessagingException {
        String topic = "audios_" + audio.getOrganizationId();

        Notification notification = Notification.builder()
                .setBody(audio.getTranslatedMessage())
                .setTitle(audio.getTranslatedTitle())
                .build();
        Message message = buildMessage("audio", topic, G.toJson(audio), notification);

        FirebaseMessaging.getInstance().send(message);
        return 0;
    }

    public int sendMessage(LocationRequest locationRequest) throws FirebaseMessagingException {
        String topic = "locationRequests_" + locationRequest.getOrganizationId();

        Notification notification = Notification.builder()
                .setBody(locationRequest.getTranslatedMessage())
                .setTitle(locationRequest.getTranslatedTitle())
                .build();

        Message message = buildMessage("locationRequest",
                topic, G.toJson(locationRequest), notification);

        FirebaseMessaging.getInstance().send(message);

        return 0;
    }

    public int sendMessage(LocationResponse locationResponse) throws FirebaseMessagingException {
        String topic = "locationResponses_" + locationResponse.getOrganizationId();

        Notification notification = Notification.builder()
                .setBody(locationResponse.getTranslatedMessage())
                .setTitle(locationResponse.getTranslatedTitle())
                .build();

        Message message = buildMessage("locationResponse", topic,
                G.toJson(locationResponse), notification);

        FirebaseMessaging.getInstance().send(message);
        return 0;
    }

    public int sendMessage(Video video) throws FirebaseMessagingException {
        String topic = "videos_" + video.getOrganizationId();

        Notification notification = Notification.builder()
                .setBody(video.getTranslatedMessage())
                .setTitle(video.getTranslatedTitle())
                .build();

        Message message = buildMessage("video", topic, G.toJson(video), notification);

        FirebaseMessaging.getInstance().send(message);
        return 0;
    }

    public int sendMessage(Condition condition) throws FirebaseMessagingException {
        String topic = "conditions_" + condition.getOrganizationId();
        Message message = buildMessage("condition", topic, G.toJson(condition));

        FirebaseMessaging.getInstance().send(message);
        return 0;
    }

    public int sendMessage(FieldMonitorSchedule fieldMonitorSchedule) throws FirebaseMessagingException {
        String topic = "fieldMonitorSchedules_" + fieldMonitorSchedule.getOrganizationId();
        Message message = buildMessage("fieldMonitorSchedule", topic, G.toJson(fieldMonitorSchedule));

        FirebaseMessaging.getInstance().send(message);
        return 0;
    }

    public int sendMessage(OrgMessage orgMessage) throws FirebaseMessagingException {
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
        FirebaseMessaging.getInstance().send(message);
        return 0;

    }

    public int sendMessage(Project project) throws FirebaseMessagingException {
        String topic = "projects_" + project.getOrganizationId();

        Notification notification = Notification.builder()
                .setBody(project.getTranslatedMessage())
                .setTitle(project.getTranslatedTitle())
                .build();
        Message message = buildMessage("project", topic, G.toJson(project), notification);
        FirebaseMessaging.getInstance().send(message);
        return 0;
    }

    public int sendMessage(User user) throws FirebaseMessagingException {
        String topic = "users_" + user.getOrganizationId();
        Notification notification = Notification.builder()
                .setBody(user.getTranslatedMessage())
                .setTitle(user.getTranslatedTitle())
                .build();
        Message message = buildMessage("user", topic, G.toJson(user), notification);

        FirebaseMessaging.getInstance().send(message);
        return 0;
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
