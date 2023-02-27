package com.boha.geo.monitor.services;


import com.boha.geo.monitor.data.*;
import com.boha.geo.repos.*;
import com.boha.geo.services.MailService;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.result.UpdateResult;
import lombok.val;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
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
    final LocationRequestRepository locationRequestRepository;

    final ProjectPolygonRepository projectPolygonRepository;

    final CityRepository cityRepository;
    final PhotoRepository photoRepository;
    final ActivityModelRepository activityModelRepository;

    final ProjectAssignmentRepository projectAssignmentRepository;
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

    final ProjectSummaryRepository projectSummaryRepository;
    private boolean isInitialized = false;
    private final MailService mailService;


    public DataService(Environment env, GeofenceEventRepository geofenceEventRepository,
                       SettingsModelRepository settingsModelRepository, RatingRepository ratingRepository, MongoTemplate mongoTemplate, AudioRepository audioRepository, ProjectRepository projectRepository,
                       LocationResponseRepository locationResponseRepository, LocationRequestRepository locationRequestRepository, ProjectPolygonRepository projectPolygonRepository, CityRepository cityRepository,
                       PhotoRepository photoRepository,
                       ActivityModelRepository activityModelRepository, ProjectAssignmentRepository projectAssignmentRepository, VideoRepository videoRepository,
                       UserRepository userRepository,
                       CommunityRepository communityRepository,
                       ConditionRepository conditionRepository,
                       CountryRepository countryRepository,
                       OrganizationRepository organizationRepository,
                       ProjectPositionRepository projectPositionRepository,
                       OrgMessageRepository orgMessageRepository,
                       MessageService messageService,
                       FieldMonitorScheduleRepository fieldMonitorScheduleRepository, ProjectSummaryRepository projectSummaryRepository, MailService mailService) {
        this.env = env;
        this.geofenceEventRepository = geofenceEventRepository;
        this.settingsModelRepository = settingsModelRepository;
        this.ratingRepository = ratingRepository;
        this.mongoTemplate = mongoTemplate;
        this.audioRepository = audioRepository;
        this.projectRepository = projectRepository;
        this.locationResponseRepository = locationResponseRepository;
        this.locationRequestRepository = locationRequestRepository;
        this.projectPolygonRepository = projectPolygonRepository;
        this.cityRepository = cityRepository;
        this.photoRepository = photoRepository;
        this.activityModelRepository = activityModelRepository;
        this.projectAssignmentRepository = projectAssignmentRepository;
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
        this.projectSummaryRepository = projectSummaryRepository;
        this.mailService = mailService;
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

    public List<ProjectSummary> createDailyOrganizationSummaries(String organizationId, String fromDate, String toDate) throws Exception {
        //todo - check if this range has been created before - maybe think of a key strategy to prevent duplicates
        List<ProjectSummary> counts = new ArrayList<>();
        DateTime dt = DateTime.parse(fromDate).toDateTimeISO();
        DateTime dtStart = dt.withTimeAtStartOfDay();
        DateTime dtTo = DateTime.parse(toDate).toDateTimeISO();


        List<Project> projects = projectRepository.findByOrganizationId(organizationId);
        long daysDiff = Math.abs(Days.daysBetween(dtStart, dtTo).getDays());

        for (Project project : projects) {
            DateTime myStart = DateTime.parse(dtStart.toString());
            val positions = projectPositionRepository.countByProject(project.getProjectId());
            val polygons = projectPositionRepository.countByProject(project.getProjectId());
            val schedules = projectPositionRepository.countByProject(project.getProjectId());
            for (int i = 0; i < daysDiff; i++) {
                val pc = createProjectCount(project, myStart.toString(), (myStart.plusDays(1)).toString());
                pc.setCalculatedHourly(1);
                pc.setProjectPositions(positions);
                pc.setProjectPolygons(polygons);
                pc.setSchedules(schedules);
                counts.add(pc);
                myStart = myStart.plusDays(1);
            }

        }
        projectSummaryRepository.insert(counts);
        LOGGER.info(E.GLOBE+E.GLOBE+ " createDailyOrganizationCounts has added " + counts.size()
                + " projectCounts, days: " + daysDiff);

        return counts;
    }

    public List<ProjectSummary> createHourlyOrganizationSummaries(String organizationId, String fromDate, String toDate) throws Exception {
        //todo - check if this range has been created before - maybe think of a key strategy to prevent duplicates

        List<ProjectSummary> counts = new ArrayList<>();
        DateTime dtFrom = DateTime.parse(fromDate).toDateTimeISO();
        DateTime dtStart = dtFrom.withTimeAtStartOfDay();
        DateTime dtTo = DateTime.parse(toDate).toDateTimeISO();

        List<Project> projects = projectRepository.findByOrganizationId(organizationId);
        long hoursDiff = Math.abs(Hours.hoursBetween(dtStart, dtTo).getHours());

        for (Project project : projects) {
            DateTime myStart = DateTime.parse(dtStart.toString());
            val positions = projectPositionRepository.countByProject(project.getProjectId());
            val polygons = projectPositionRepository.countByProject(project.getProjectId());
            val schedules = projectPositionRepository.countByProject(project.getProjectId());
            for (int i = 0; i < hoursDiff; i++) {
                val pc = createProjectCount(project, myStart.toString(), (myStart.plusHours(1)).toString());
                pc.setCalculatedHourly(0);
                pc.setProjectPositions(positions);
                pc.setProjectPolygons(polygons);
                pc.setSchedules(schedules);
                counts.add(pc);
                myStart = myStart.plusHours(1);
            }
        }
        projectSummaryRepository.insert(counts);
        LOGGER.info(E.GLOBE+E.GLOBE+ " createHourlyOrganizationCounts has added " + counts.size()
                + " projectCounts; hours: " + hoursDiff);
        return counts;
    }

    public ProjectSummary createProjectCount(Project project, String startDate, String endDate) throws Exception {

        long start = System.currentTimeMillis();
        val photos = photoRepository.countByProjectPeriod(project.getProjectId(), startDate, endDate);
        val videos = videoRepository.countByProjectPeriod(project.getProjectId(), startDate, endDate);
        val audios = audioRepository.countByProjectPeriod(project.getProjectId(), startDate, endDate);

        val pc = new ProjectSummary();
        pc.setProjectId(project.getProjectId());
        pc.setProjectName(project.getName());
        pc.setOrganizationId(project.getOrganizationId());
        pc.setOrganizationName(project.getOrganizationName());
        pc.setDate(DateTime.now().toDateTimeISO().toString());
        pc.setAudios(audios);
        pc.setVideos(videos);
        pc.setPhotos(photos);


        long end = System.currentTimeMillis();
        long ms = (end - start);
        long delta = ms / 60;
        LOGGER.info(E.PANDA + E.PANDA + E.PANDA + " project count took " + delta + " seconds");

        return pc;
    }


    public User updateUser(User user) throws Exception {

        LOGGER.info(E.LEAF.concat(E.LEAF).concat("User to be updated on database: "
                + G.toJson(user)));

        LOGGER.info(E.RED_APPLE+E.RED_APPLE + " update user and set all properties ");
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(user.getUserId()));
        query.fields().include("userId");

        Update update = new Update();
        update.set("name", user.getName());
        update.set("cellphone", user.getCellphone());
        update.set("email", user.getEmail());
        update.set("fcmRegistration", user.getFcmRegistration());
        update.set("imageUrl", user.getImageUrl());
        update.set("countryId", user.getCountryId());
        update.set("thumbnailUrl", user.getThumbnailUrl());
        update.set("gender", user.getGender());
        update.set("updated", DateTime.now().toDateTimeISO().toString());

        UpdateResult result = mongoTemplate.updateFirst(query, update, User.class);

        LOGGER.info(E.RED_APPLE+E.RED_APPLE + " user has been modified: " + result.getModifiedCount());

        LOGGER.info(E.LEAF.concat(E.LEAF).concat("User updated on database: "
                + user.getName() + " id: "
                + user.getUserId() + " " + user.getFcmRegistration()));

        messageService.sendMessage(user);
        String message = "Dear " + user.getName() +
                "      ,\n\nYour account has been updated with new information. '\n" +
                "      \nIf you have not changed anything yourself please contact your Administrator or your Supervisor.\n" +
                "      \n\nThank you for working with GeoMonitor. \nBest Regards,\nThe GeoMonitor Team\ninfo@geomonitorapp.io\n\n";

        mailService.sendHtmlEmail( user.getEmail(), message,"GeoMonitor Account Updated" );


        ActivityModel am = new ActivityModel();
        am.setActivityType(ActivityType.userAddedOrModified);
        am.setActivityModelId(UUID.randomUUID().toString());
        am.setDate(DateTime.now().toDateTimeISO().toString());
        am.setProjectId(null);
        am.setUserId(user.getUserId());
        am.setOrganizationName(user.getOrganizationName());
        am.setOrganizationId(user.getOrganizationId());
        am.setUserName(user.getName());
        am.setProjectName(null);
        am.setUser(user);

        addActivityModel(am);
        return user;
    }

    public User addUser(User user) throws Exception {

        User mUser = userRepository.save(user);
        String result = messageService.sendMessage(user);
        LOGGER.info(E.LEAF.concat(E.LEAF).concat("User added to database: "
                + user.getName() + " result: "
                + result));

        ActivityModel am = new ActivityModel();
        am.setActivityType(ActivityType.userAddedOrModified);
        am.setActivityModelId(UUID.randomUUID().toString());
        am.setDate(DateTime.now().toDateTimeISO().toString());
        am.setProjectId(null);
        am.setUserId(user.getUserId());
        am.setOrganizationName(user.getOrganizationName());
        am.setOrganizationId(user.getOrganizationId());
        am.setUserName(user.getName());
        am.setProjectName(null);

        addActivityModel(am);

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

    public void addPhoto(Photo photo) throws Exception {
        if (photo.getPhotoId() == null) {
            photo.setPhotoId(UUID.randomUUID().toString());
        }
        photoRepository.save(photo);
        LOGGER.info(E.LEAF.concat(E.LEAF).concat("Photo added to Mongo, : " + photo.getUrl()));
        messageService.sendMessage(photo);

        ActivityModel am = new ActivityModel();
        am.setActivityType(ActivityType.photoAdded);
        am.setActivityModelId(UUID.randomUUID().toString());
        am.setDate(DateTime.now().toDateTimeISO().toString());
        am.setProjectId(photo.getProjectId());
        am.setUserId(photo.getUserId());
        am.setOrganizationName(null);
        am.setOrganizationId(photo.getOrganizationId());
        am.setUserName(photo.getUserName());
        am.setProjectName(photo.getProjectName());
        am.setPhoto(photo);

        addActivityModel(am);

    }
    public void addActivityModel(ActivityModel model)  throws Exception{

        activityModelRepository.insert(model);
        LOGGER.info(E.LEAF.concat(E.LEAF).concat("ActivityModel added to Mongo "));

        messageService.sendMessage(model);
    }

    public String addProjectAssignment(ProjectAssignment projectAssignment) throws Exception {
        if (projectAssignment.getProjectAssignmentId() == null) {
            projectAssignment.setProjectAssignmentId(UUID.randomUUID().toString());
        }
        projectAssignmentRepository.insert(projectAssignment);
        LOGGER.info(E.LEAF.concat(E.LEAF).concat("ProjectAssignment added to Mongo, user: "
                +  projectAssignment.getUserName() + " project: " + projectAssignment.getProjectName()));
        return messageService.sendMessage(projectAssignment);
    }


    public String addVideo(Video video) throws Exception {
        if (video.getVideoId() == null) {
            video.setVideoId(UUID.randomUUID().toString());
        }
        videoRepository.insert(video);

        ActivityModel am = new ActivityModel();
        am.setActivityType(ActivityType.videoAdded);
        am.setActivityModelId(UUID.randomUUID().toString());
        am.setDate(DateTime.now().toDateTimeISO().toString());
        am.setProjectId(video.getProjectId());
        am.setUserId(video.getUserId());
        am.setOrganizationName(null);
        am.setOrganizationId(video.getOrganizationId());
        am.setUserName(video.getUserName());
        am.setProjectName(video.getProjectName());
        am.setVideo(video);

        addActivityModel(am);
        LOGGER.info(E.LEAF.concat(E.LEAF).concat("Video added: " + video.getVideoId()));
        return messageService.sendMessage(video);
    }
    public String addAudio(Audio audio) throws Exception {

        audioRepository.save(audio);
        LOGGER.info(E.LEAF.concat(E.LEAF).concat("Video added: " + audio.getAudioId()));

        ActivityModel am = new ActivityModel();
        am.setActivityType(ActivityType.audioAdded);
        am.setActivityModelId(UUID.randomUUID().toString());
        am.setDate(DateTime.now().toDateTimeISO().toString());
        am.setProjectId(audio.getProjectId());
        am.setUserId(audio.getUserId());
        am.setOrganizationName(null);
        am.setOrganizationId(audio.getOrganizationId());
        am.setUserName(audio.getUserName());
        am.setProjectName(audio.getProjectName());
        am.setAudio(audio);

        addActivityModel(am);
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

        ActivityModel am = new ActivityModel();
        am.setActivityType(ActivityType.messageAdded);
        am.setActivityModelId(UUID.randomUUID().toString());
        am.setDate(DateTime.now().toDateTimeISO().toString());
        am.setProjectId(orgMessage.getProjectId());
        am.setUserId(orgMessage.getUserId());
        am.setOrganizationName(null);
        am.setOrganizationId(orgMessage.getOrganizationId());
        am.setUserName(orgMessage.getAdminName());
        am.setProjectName(orgMessage.getProjectName());
       am.setOrgMessage(orgMessage);

        addActivityModel(am);
        return orgMessage;
    }

    public FieldMonitorSchedule addFieldMonitorSchedule(FieldMonitorSchedule fieldMonitorSchedule) throws Exception {
        fieldMonitorScheduleRepository.save(fieldMonitorSchedule);
        LOGGER.info(E.LEAF.concat(E.LEAF).concat("FieldMonitorSchedule added: " + fieldMonitorSchedule.getFieldMonitorId()));
        messageService.sendMessage(fieldMonitorSchedule);

        return fieldMonitorSchedule;
    }

    public ProjectPosition addProjectPosition(ProjectPosition projectPosition) throws Exception {
        LOGGER.info(E.RAIN.concat(E.RAIN).concat("addProjectPosition: "
                .concat(E.YELLOW)));

        ProjectPosition m = projectPositionRepository.save(projectPosition);
        LOGGER.info(E.YELLOW_BIRD + E.YELLOW_BIRD +
                "ProjectPosition added to: " + m.getProjectName()
                + " " + E.RAIN);

        messageService.sendMessage(m);

        ActivityModel am = new ActivityModel();
        projectPosition.setNearestCities(null);
        am.setActivityType(ActivityType.positionAdded);
        am.setActivityModelId(UUID.randomUUID().toString());
        am.setDate(DateTime.now().toDateTimeISO().toString());
        am.setProjectId(projectPosition.getProjectId());
        am.setUserId(projectPosition.getUserId());
        am.setOrganizationName(null);
        am.setOrganizationId(projectPosition.getOrganizationId());
        am.setUserName(projectPosition.getUserName());
        am.setProjectName(projectPosition.getProjectName());
        am.setProjectPosition(projectPosition);

        addActivityModel(am);

        m = projectPositionRepository.findByProjectPositionId(projectPosition.getProjectPositionId());
        return m;
    }
    public ProjectPolygon addProjectPolygon(ProjectPolygon projectPolygon) throws Exception {
        LOGGER.info(E.RAIN.concat(E.RAIN).concat("addProjectPolygon: "
                .concat(E.YELLOW)));

        ProjectPolygon m = projectPolygonRepository.save(projectPolygon);
        LOGGER.info(E.YELLOW_BIRD + E.YELLOW_BIRD +
                "ProjectPolygon added to: " + m.getProjectName()
                + " " + E.RAIN);

        m.setNearestCities(null);
        messageService.sendMessage(m);

        ActivityModel am = new ActivityModel();
        projectPolygon.setNearestCities(null);
        am.setActivityType(ActivityType.polygonAdded);
        am.setActivityModelId(UUID.randomUUID().toString());
        am.setDate(DateTime.now().toDateTimeISO().toString());
        am.setProjectId(projectPolygon.getProjectId());
        am.setUserId(projectPolygon.getUserId());
        am.setOrganizationName(null);
        am.setOrganizationId(projectPolygon.getOrganizationId());
        am.setUserName(projectPolygon.getUserName());
        am.setProjectName(projectPolygon.getProjectName());
        am.setProjectPolygon(projectPolygon);

        addActivityModel(am);

        m = projectPolygonRepository
                .findByProjectPolygonId(projectPolygon.getProjectPolygonId());

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

        ActivityModel am = new ActivityModel();
        am.setActivityType(ActivityType.geofenceEventAdded);
        am.setActivityModelId(UUID.randomUUID().toString());
        am.setDate(DateTime.now().toDateTimeISO().toString());
        am.setProjectId(geofenceEvent.getProjectId());
        am.setUserId(geofenceEvent.getUser().getUserId());
        am.setOrganizationName(null);
        am.setOrganizationId(geofenceEvent.getOrganizationId());
        am.setUserName(geofenceEvent.getUser().getName());
        am.setProjectName(geofenceEvent.getProjectName());
        am.setGeofenceEvent(geofenceEvent);

        addActivityModel(am);
        return m;
    }

    public Project addProject(Project project) throws Exception {
        LOGGER.info(E.RAIN.concat(E.RAIN).concat("addProject: "
                .concat(project.getName()).concat(" ")
                .concat(E.YELLOW)));

        Project m = projectRepository.insert(project);

        LOGGER.info(E.LEAF.concat(E.LEAF)
                .concat("Project added: " + m.getProjectId()));
        ActivityModel am = new ActivityModel();
        am.setActivityType(ActivityType.projectAdded);
        am.setActivityModelId(UUID.randomUUID().toString());
        am.setDate(DateTime.now().toDateTimeISO().toString());
        am.setProjectId(project.getProjectId());
        am.setUserId(null);
        am.setOrganizationName(null);
        am.setOrganizationId(project.getOrganizationId());
        am.setUserName(null);
        am.setProjectName(project.getName());
        am.setProject(project);

        addActivityModel(am);
        messageService.sendMessage(m);
        return project;
    }

    public LocationResponse addLocationResponse(LocationResponse locationResponse) throws Exception {

        LocationResponse m = locationResponseRepository.insert(locationResponse);

        LOGGER.info(E.LEAF.concat(E.LEAF)
                .concat("locationResponse added: " + locationResponse.getOrganizationName()));
        messageService.sendMessage(m);

        ActivityModel am = new ActivityModel();
        am.setActivityType(ActivityType.locationResponse);
        am.setActivityModelId(UUID.randomUUID().toString());
        am.setDate(DateTime.now().toDateTimeISO().toString());
        am.setProjectId(null);
        am.setUserId(locationResponse.getUserId());
        am.setOrganizationName(null);
        am.setOrganizationId(locationResponse.getOrganizationId());
        am.setUserName(locationResponse.getUserName());
        am.setProjectName(null);
        am.setLocationResponse(locationResponse);

        addActivityModel(am);
        return m;
    }

    public LocationRequest addLocationRequest(LocationRequest locationRequest) throws Exception {

        LocationRequest m = locationRequestRepository.insert(locationRequest);

        LOGGER.info(E.LEAF.concat(E.LEAF)
                .concat("locationRequest added: " + locationRequest.getUserName()));
        messageService.sendMessage(m);

        ActivityModel am = new ActivityModel();
        am.setActivityType(ActivityType.locationRequest);
        am.setActivityModelId(UUID.randomUUID().toString());
        am.setDate(DateTime.now().toDateTimeISO().toString());
        am.setProjectId(null);
        am.setUserId(locationRequest.getUserId());
        am.setOrganizationName(null);
        am.setOrganizationId(locationRequest.getOrganizationId());
        am.setUserName(locationRequest.getUserName());
        am.setProjectName(null);
        am.setLocationRequest(locationRequest);

        addActivityModel(am);
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
        messageService.sendMessage(model);

        ActivityModel am = new ActivityModel();
        am.setActivityType(ActivityType.settingsChanged);
        am.setActivityModelId(UUID.randomUUID().toString());
        am.setDate(DateTime.now().toDateTimeISO().toString());
        am.setProjectId(model.getProjectId());
        am.setUserId(null);
        am.setOrganizationName(null);
        am.setOrganizationId(model.getOrganizationId());
        am.setUserName(null);
        am.setProjectName(null);

        addActivityModel(am);
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
        String message = "Dear " + user.getName() +
                "      ,\n\nYou have been registered with GeoMonitor and the team is happy to send you the first time login password. '\n" +
                "      \nPlease login on the web with your email and the attached password but use your cellphone number to sign in on the phone.\n" +
                "      \n\nThank you for working with GeoMonitor. \nWelcome aboard!!\nBest Regards,\nThe GeoMonitor Team\ninfo@geomonitorapp.io\n\n";

        mailService.sendHtmlEmail( user.getEmail(), message,"Welcome to GeoMonitor" );

        ActivityModel am = new ActivityModel();
        am.setActivityType(ActivityType.userAddedOrModified);
        am.setActivityModelId(UUID.randomUUID().toString());
        am.setDate(DateTime.now().toDateTimeISO().toString());
        am.setProjectId(null);
        am.setUserId(user.getUserId());
        am.setOrganizationName(user.getOrganizationName());
        am.setOrganizationId(user.getOrganizationId());
        am.setUserName(user.getName());
        am.setProjectName(null);

        addActivityModel(am);

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
