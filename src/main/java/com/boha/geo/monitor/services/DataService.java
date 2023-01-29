package com.boha.geo.monitor.services;


import com.boha.geo.monitor.data.*;
import com.boha.geo.repos.*;
import com.boha.geo.util.E;
import com.google.api.core.ApiFuture;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.result.UpdateResult;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.UUID;

@Service
public class DataService {
    public static final Logger LOGGER = LoggerFactory.getLogger(DataService.class.getSimpleName());
    private static final Gson G = new GsonBuilder().setPrettyPrinting().create();
    //    @Value("${databaseUrl}")
    private static final String databaseUrl = "https://monitor-2021.firebaseio.com";
    final Environment env;
    final GeofenceEventRepository geofenceEventRepository;

    final SettingsModelRepository settingsModelRepository;

    final RatingRepository ratingRepository;

    final MongoTemplate mongoTemplate;

    final AudioRepository audioRepository;
    final ProjectRepository projectRepository;
    final LocationResponseRepository locationResponseRepository;

    final ProjectPolygonRepository projectPolygonRepository;

    final CityRepository cityRepository;
    final PhotoRepository photoRepository;
    final VideoRepository videoRepository;
    final UserRepository userRepository;
    final CommunityRepository communityRepository;
    final ConditionRepository conditionRepository;
    final CountryRepository countryRepository;
    final OrganizationRepository organizationRepository;
    final ProjectPositionRepository projectPositionRepository;
    final OrgMessageRepository orgMessageRepository;
    final MessageService messageService;
    final FieldMonitorScheduleRepository fieldMonitorScheduleRepository;
    private boolean isInitialized = false;


    public DataService(Environment env, GeofenceEventRepository geofenceEventRepository,
                       SettingsModelRepository settingsModelRepository, RatingRepository ratingRepository, MongoTemplate mongoTemplate, AudioRepository audioRepository, ProjectRepository projectRepository,
                       LocationResponseRepository locationResponseRepository, ProjectPolygonRepository projectPolygonRepository, CityRepository cityRepository,
                       PhotoRepository photoRepository,
                       VideoRepository videoRepository,
                       UserRepository userRepository,
                       CommunityRepository communityRepository,
                       ConditionRepository conditionRepository,
                       CountryRepository countryRepository,
                       OrganizationRepository organizationRepository,
                       ProjectPositionRepository projectPositionRepository,
                       OrgMessageRepository orgMessageRepository,
                       MessageService messageService,
                       FieldMonitorScheduleRepository fieldMonitorScheduleRepository) {
        this.env = env;
        this.geofenceEventRepository = geofenceEventRepository;
        this.settingsModelRepository = settingsModelRepository;
        this.ratingRepository = ratingRepository;
        this.mongoTemplate = mongoTemplate;
        this.audioRepository = audioRepository;
        this.projectRepository = projectRepository;
        this.locationResponseRepository = locationResponseRepository;
        this.projectPolygonRepository = projectPolygonRepository;
        this.cityRepository = cityRepository;
        this.photoRepository = photoRepository;
        this.videoRepository = videoRepository;
        this.userRepository = userRepository;
        this.communityRepository = communityRepository;
        this.conditionRepository = conditionRepository;
        this.countryRepository = countryRepository;
        this.organizationRepository = organizationRepository;
        this.projectPositionRepository = projectPositionRepository;
        this.orgMessageRepository = orgMessageRepository;
        this.messageService = messageService;
        this.fieldMonitorScheduleRepository = fieldMonitorScheduleRepository;
        LOGGER.info(xx+" DataService constructed and repos injected \uD83C\uDF4F");
    }
    private static final String xx = E.COFFEE+E.COFFEE+E.COFFEE;

    public void initializeFirebase() throws Exception {
        String fbConfig = env.getProperty("FIREBASE_CONFIG");
        CredentialsProvider credentialsProvider = null;

        FirebaseApp app;
        try {
            if (!isInitialized) {
                LOGGER.info("\uD83C\uDFBD \uD83C\uDFBD \uD83C\uDFBD \uD83C\uDFBD  " +
                        "DataService: initializeFirebase: ... \uD83C\uDF4F" +
                        ".... \uD83D\uDC99 \uD83D\uDC99 \uD83D\uDC99 \uD83D\uDC99 monitor FIREBASE URL: "
                        + E.BLUE_DOT + " " + databaseUrl + " " + E.BLUE_DOT + E.BLUE_DOT);
                if (fbConfig != null) {
                    credentialsProvider
                            = FixedCredentialsProvider.create(
                            ServiceAccountCredentials.fromStream(new ByteArrayInputStream(fbConfig.getBytes())));
                    LOGGER.info("\uD83C\uDFBD \uD83C\uDFBD \uD83C\uDFBD \uD83C\uDFBD " +
                            "credentialsProvider gives us:  \uD83C\uDF4E  "
                            + credentialsProvider.getCredentials().toString() + " \uD83C\uDFBD \uD83C\uDFBD");
                }
                FirebaseOptions prodOptions;
                if (credentialsProvider != null) {
                    prodOptions = new FirebaseOptions.Builder()
                            .setCredentials((GoogleCredentials) credentialsProvider.getCredentials())
                            .setDatabaseUrl(databaseUrl)
                            .build();

                    app = FirebaseApp.initializeApp(prodOptions);
                    isInitialized = true;

                    LOGGER.info(E.BLUE_DOT + E.BLUE_DOT + "Firebase has been set up and initialized. " +
                            "\uD83D\uDC99 URL: " + app.getOptions().getDatabaseUrl() + " " + E.PINK + E.PINK + E.PINK + E.PINK);
                    LOGGER.info(E.BLUE_DOT + E.BLUE_DOT + "Firebase has been set up and initialized. " +
                            "\uD83E\uDD66 Name: " + app.getName() + " " + E.PINK + E.PINK);

                }


            } else {
                LOGGER.info("\uD83C\uDFBD \uD83C\uDFBD \uD83C\uDFBD \uD83C\uDFBD  DataService: Firebase is already initialized: ... \uD83C\uDF4F" +
                        ".... \uD83D\uDC99 \uD83D\uDC99 isInitialized: " + true + " \uD83D\uDC99 \uD83D\uDC99 "
                        + E.BLUE_DOT + E.BLUE_DOT + E.BLUE_DOT);
            }
        } catch (Exception e) {
            String msg = "Unable to initialize Firebase";
            LOGGER.info(E.RED_DOT.concat(E.RED_DOT).concat(msg));
            throw new Exception(msg, e);
        }


    }

//    public static String getGeoHash(double latitude, double longitude) {
//        return GeoHash.geoHashStringWithCharacterPrecision(latitude, longitude, 12);
//    }

    public User updateUser(User user) throws Exception {

        LOGGER.info(E.LEAF.concat(E.LEAF).concat("User updated on database: "
                + user.getName() + " id: "
                + user.getUserId() + " " + user.getFcmRegistration()));

        LOGGER.info(E.RED_APPLE+E.RED_APPLE + " update user and set the active flag to 9 ");
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(user.getUserId()));
        query.fields().include("userId");

        Update update = new Update();
        update.set("name", user.getName());
        update.set("cellphone", user.getCellphone());
        update.set("email", user.getEmail());
        update.set("fcmRegistration", user.getFcmRegistration());
        update.set("updated", DateTime.now().toDateTimeISO().toString());

        UpdateResult result = mongoTemplate.updateFirst(query, update, User.class);
        user.setPassword(UUID.randomUUID().toString());
        updateAuthedUser(user);

        LOGGER.info(E.RED_APPLE+E.RED_APPLE + " user has been modified: " + result.getModifiedCount());

        LOGGER.info(E.LEAF.concat(E.LEAF).concat("User updated on database: "
                + user.getName() + " id: "
                + user.getUserId() + " " + user.getFcmRegistration()));

        return user;
    }

    public User addUser(User user) throws FirebaseMessagingException {

        User mUser = userRepository.save(user);
        String result = messageService.sendMessage(user);
        LOGGER.info(E.LEAF.concat(E.LEAF).concat("User added to database: "
                + user.getName() + " result: "
                + result));

        return mUser;
    }

    public Rating addRating(Rating rating) throws Exception {
        if (rating.getRatingId() == null) {
            rating.setRatingId(UUID.randomUUID().toString());
        }
        Rating res = ratingRepository.insert(rating);
        LOGGER.info(E.LEAF.concat(E.LEAF).concat("Rating added to Mongo, ratingCode: " + rating.getRatingCode()));
        return res;
    }

    public String addPhoto(Photo photo) throws Exception {
        if (photo.getPhotoId() == null) {
            photo.setPhotoId(UUID.randomUUID().toString());
        }
        photoRepository.save(photo);
        LOGGER.info(E.LEAF.concat(E.LEAF).concat("Photo added to Mongo, : " + photo.getUrl()));
        return messageService.sendMessage(photo);
    }


    public String addVideo(Video video) throws Exception {
        if (video.getVideoId() == null) {
            video.setVideoId(UUID.randomUUID().toString());
        }
        videoRepository.save(video);
        LOGGER.info(E.LEAF.concat(E.LEAF).concat("Video added: " + video.getVideoId()));
        return messageService.sendMessage(video);
    }
    public String addAudio(Audio audio) throws Exception {

        audioRepository.save(audio);
        LOGGER.info(E.LEAF.concat(E.LEAF).concat("Video added: " + audio.getAudioId()));
        return messageService.sendMessage(audio);
    }

    public String addCondition(Condition condition) throws Exception {
        conditionRepository.save(condition);
        LOGGER.info(E.LEAF.concat(E.LEAF).concat("Condition added: " + condition.getConditionId()));
        return messageService.sendMessage(condition);
    }

    public OrgMessage addOrgMessage(OrgMessage orgMessage) throws Exception {
        orgMessageRepository.save(orgMessage);
        LOGGER.info(E.LEAF.concat(E.LEAF).concat("OrgMessage added: " + orgMessage.getMessage()));
        String result = messageService.sendMessage(orgMessage);
        orgMessage.setResult(result);
        return orgMessage;
    }

    public FieldMonitorSchedule addFieldMonitorSchedule(FieldMonitorSchedule fieldMonitorSchedule) throws Exception {
        fieldMonitorScheduleRepository.save(fieldMonitorSchedule);
        LOGGER.info(E.LEAF.concat(E.LEAF).concat("FieldMonitorSchedule added: " + fieldMonitorSchedule.getFieldMonitorId()));
        String resp = messageService.sendMessage(fieldMonitorSchedule);

        return fieldMonitorSchedule;
    }

    public ProjectPosition addProjectPosition(ProjectPosition projectPosition) throws Exception {
        LOGGER.info(E.RAIN.concat(E.RAIN).concat("addProjectPosition: "
                .concat(E.YELLOW)));

        ProjectPosition m = projectPositionRepository.save(projectPosition);
        LOGGER.info(E.YELLOW_BIRD + E.YELLOW_BIRD +
                "ProjectPosition added to: " + m.getProjectName()
                + " " + E.RAIN);

        return m;
    }
    public ProjectPolygon addProjectPolygon(ProjectPolygon projectPolygon) throws Exception {
        LOGGER.info(E.RAIN.concat(E.RAIN).concat("addProjectPolygon: "
                .concat(E.YELLOW)));

        ProjectPolygon m = projectPolygonRepository.save(projectPolygon);
        LOGGER.info(E.YELLOW_BIRD + E.YELLOW_BIRD +
                "ProjectPolygon added to: " + m.getProjectName()
                + " " + E.RAIN);

        return m;
    }

    public GeofenceEvent addGeofenceEvent(GeofenceEvent geofenceEvent) throws Exception {
        LOGGER.info(E.RAIN.concat(E.RAIN).concat("addGeofenceEvent: "
                .concat(E.YELLOW)));

        GeofenceEvent m = geofenceEventRepository.insert(geofenceEvent);
        LOGGER.info(E.YELLOW_BIRD + E.YELLOW_BIRD +
                "GeofenceEvent added to: " + m.getProjectName()
                + " " + E.RAIN);
         messageService.sendMessage(m);
        return m;
    }

    public String addProject(Project project) throws Exception {
        LOGGER.info(E.RAIN.concat(E.RAIN).concat("addProject: "
                .concat(project.getName()).concat(" ")
                .concat(E.YELLOW)));

        Project m = projectRepository.insert(project);

        LOGGER.info(E.LEAF.concat(E.LEAF)
                .concat("Project added: " + m.getProjectId()));
        return messageService.sendMessage(m);
    }

    public LocationResponse addLocationResponse(LocationResponse locationResponse) throws Exception {

        LocationResponse m = locationResponseRepository.insert(locationResponse);

        LOGGER.info(E.LEAF.concat(E.LEAF)
                .concat("locationResponse added: " + locationResponse.getOrganizationName()));
        return m;
    }


    public Project updateProject(Project project) throws Exception {
        LOGGER.info(E.RAIN.concat(E.RAIN).concat("updateProject: "
                .concat(project.getName()).concat(" ")
                .concat(E.YELLOW)));

        Project m = projectRepository.save(project);

        LOGGER.info(E.LEAF.concat(E.LEAF)
                .concat("Project added: " + project.getProjectId()));
        return m;
    }

    public City addCity(City city) throws Exception {
        city.setCityId(UUID.randomUUID().toString());
        City c = cityRepository.save(city);
        LOGGER.info(E.LEAF.concat(E.LEAF)
                .concat("City added to database : " + city.getCityId()));
        return c;
    }

    public Community addCommunity(Community community) throws Exception {
        community.setCommunityId(UUID.randomUUID().toString());
        Community cm = communityRepository.save(community);
        LOGGER.info(E.LEAF.concat(E.LEAF)
                .concat("Community: \uD83C\uDF3C "
                        + community.getName()
                        + " added to database: \uD83D\uDC24 "
                        + community.getCommunityId()));
        return cm;
    }

    public Country addCountry(Country country) throws Exception {
        country.setCountryId(UUID.randomUUID().toString());

        Country m = countryRepository.save(country);
        LOGGER.info(E.LEAF.concat(E.LEAF)
                .concat("Country added: " + country.getCountryId()));
        return m;
    }

    public SettingsModel addSettings(SettingsModel model) throws Exception {

        LOGGER.info(E.RED_DOT + " adding Settings model to db: " + G.toJson(model));
        SettingsModel m = settingsModelRepository.insert(model);
        LOGGER.info(E.LEAF.concat(E.LEAF)
                .concat("SettingsModel inserted: " + G.toJson(m)));
        return m;
    }


    public Organization addOrganization(Organization organization) throws Exception {
        organization.setOrganizationId(UUID.randomUUID().toString());
        organization.setCreated(new DateTime().toDateTimeISO().toString());

        Organization org = organizationRepository.save(organization);
        LOGGER.info(E.LEAF.concat(E.LEAF)
                .concat("Organization added: " + organization.getOrganizationId()));
        return org;
    }

    public User createUser(User user) throws Exception {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        UserRecord.CreateRequest createRequest = new UserRecord.CreateRequest();
        createRequest.setPhoneNumber(user.getCellphone());
        createRequest.setDisplayName(user.getName());
        createRequest.setPassword(user.getPassword());
        createRequest.setEmail(user.getEmail());

        ApiFuture<UserRecord> userRecord = firebaseAuth.createUserAsync(createRequest);
        String uid = userRecord.get().getUid();
        user.setUserId(uid);
        LOGGER.info(E.PINK + E.PINK
                + "Firebase user auth record created: "
                .concat(" \uD83E\uDDE1 ").concat(user.getName()
                        .concat(" \uD83E\uDDE1 ").concat(user.getEmail())
                        .concat(" \uD83E\uDDE1 ").concat(uid)));

        user.setPassword(null);
        return addUser(user);
    }

    public int updateAuthedUser(User user) throws Exception {
        try {
            UserRecord.UpdateRequest request = new UserRecord.UpdateRequest(user.getUserId())
                    .setEmail(user.getEmail())
                    .setPhoneNumber(user.getCellphone())
                    .setEmailVerified(false)
                    .setPassword(user.getPassword())
                    .setDisplayName(user.getName())
                    .setDisabled(false);

            UserRecord userRecord = FirebaseAuth.getInstance().updateUser(request);
            LOGGER.info(E.RED_DOT + E.RED_DOT + " Successfully updated user: " + userRecord.getUid());
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
           throw new Exception("User auth update failed: " + e.getMessage());
        }

    }
}
