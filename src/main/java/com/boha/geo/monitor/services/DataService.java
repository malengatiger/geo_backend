package com.boha.geo.monitor.services;


import com.boha.geo.monitor.data.AppError;
import com.boha.geo.models.GioMediaInterface;
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
import lombok.RequiredArgsConstructor;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service that adds data to the MongoDB database via repositories
 */
@RequiredArgsConstructor

@Service
public class DataService {
    public static final Logger LOGGER = LoggerFactory.getLogger(DataService.class.getSimpleName());
    private static final Gson G = new GsonBuilder().setPrettyPrinting().create();
    //    @Value("${databaseUrl}")
    private static final String databaseUrl = "https://monitor-2021.firebaseio.com";
    final Environment env;
    final AppErrorRepository appErrorRepository;

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




    private static final String xx = E.COFFEE + E.COFFEE + E.COFFEE;

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

                }


            } else {
                LOGGER.info("\uD83C\uDFBD \uD83C\uDFBD \uD83C\uDFBD \uD83C\uDFBD  DataService: Firebase is already initialized: ... \uD83C\uDF4F" +
                        ".... \uD83D\uDC99 \uD83D\uDC99 isInitialized: " + true + " \uD83D\uDC99 \uD83D\uDC99 "
                        + E.BLUE_DOT + E.BLUE_DOT + E.BLUE_DOT);
            }
        } catch (Exception e) {
            String msg = "Unable to initialize Firebase";
            LOGGER.error(E.RED_DOT.concat(E.RED_DOT).concat(msg));
            throw new Exception(msg, e);
        }


    }

    public List<ProjectSummary> createDailyProjectSummaries(String projectId, String startDate, String endDate) throws Exception {
        //todo - check if this range has been created before - maybe think of a key strategy to prevent duplicates
        long start = System.currentTimeMillis();
        List<ProjectSummary> summaries = new ArrayList<>();
        DateTime dt = DateTime.parse(startDate).toDateTimeISO();
        DateTime dtStart = dt.withTimeAtStartOfDay();
        DateTime dtTo = DateTime.parse(endDate).toDateTimeISO();
        String batchId = UUID.randomUUID().toString();

        Project project = projectRepository.findByProjectId(projectId);
        long daysDiff = Math.abs(Days.daysBetween(dtStart, dtTo).getDays());

        DateTime myStart = DateTime.parse(dtStart.toString());
        val positions = projectPositionRepository.findByProjectId(project.getProjectId());
        val polygons = projectPolygonRepository.findByProjectId(project.getProjectId());
        val schedules = fieldMonitorScheduleRepository.findByProjectId(project.getProjectId());
        for (int i = 0; i < daysDiff; i++) {
            val pc = createProjectSummary(project, myStart.toString(), (myStart.plusDays(1)).toString(), i + 1, -1);
            pc.setCalculatedHourly(1);
            pc.setProjectPositions(positions.size());
            pc.setProjectPolygons(polygons.size());
            pc.setSchedules(schedules.size());
            pc.setBatchId(batchId);
            summaries.add(pc);
            myStart = myStart.plusDays(1);
        }

        projectSummaryRepository.insert(summaries);

        return summaries;
    }

    public List<ProjectSummary> createDailyOrganizationSummaries(String organizationId, String startDate, String endDate) throws Exception {
        //todo - check if this range has been created before - maybe think of a key strategy to prevent duplicates
        long start = System.currentTimeMillis();
        List<ProjectSummary> summaries = new ArrayList<>();
        DateTime dt = DateTime.parse(startDate).toDateTimeISO();
        DateTime dtStart = dt.withTimeAtStartOfDay();
        DateTime dtTo = DateTime.parse(endDate).toDateTimeISO();
        String batchId = UUID.randomUUID().toString();

        List<Project> projects = projectRepository.findByOrganizationId(organizationId);
        long daysDiff = Math.abs(Days.daysBetween(dtStart, dtTo).getDays());

        for (Project project : projects) {
            DateTime myStart = DateTime.parse(dtStart.toString());
            val positions = projectPositionRepository.findByProjectId(project.getProjectId());
            val polygons = projectPolygonRepository.findByProjectId(project.getProjectId());
            val schedules = fieldMonitorScheduleRepository.findByProjectId(project.getProjectId());
            for (int i = 0; i < daysDiff; i++) {
                val pc = createProjectSummary(project, myStart.toString(), (myStart.plusDays(1)).toString(), i + 1, -1);
                pc.setCalculatedHourly(1);
                pc.setProjectPositions(positions.size());
                pc.setProjectPolygons(polygons.size());
                pc.setSchedules(schedules.size());
                pc.setBatchId(batchId);
                summaries.add(pc);
                myStart = myStart.plusDays(1);
            }

        }
        projectSummaryRepository.insert(summaries);

        return summaries;
    }

    public List<ProjectSummary> createHourlyOrganizationSummaries(String organizationId, String startDate, String endDate) throws Exception {
        //todo - check if this range has been created before - maybe think of a key strategy to prevent duplicates
        long start = System.currentTimeMillis();
        List<ProjectSummary> summaries = new ArrayList<>();
        DateTime dtFrom = DateTime.parse(startDate).toDateTimeISO();
        DateTime dtStart = dtFrom.withTimeAtStartOfDay();
        DateTime dtTo = DateTime.parse(endDate).toDateTimeISO();
        String batchId = UUID.randomUUID().toString();

        List<Project> projects = projectRepository.findByOrganizationId(organizationId);
        long hoursDiff = Math.abs(Hours.hoursBetween(dtStart, dtTo).getHours());

        for (Project project : projects) {
            DateTime myStart = DateTime.parse(dtStart.toString());
            val positions = projectPositionRepository.findByProjectId(project.getProjectId());
            val polygons = projectPolygonRepository.findByProjectId(project.getProjectId());
            val schedules = fieldMonitorScheduleRepository.findByProjectId(project.getProjectId());
            for (int i = 0; i < hoursDiff; i++) {
                val pc = createProjectSummary(project, myStart.toString(), (myStart.plusHours(1)).toString(), -1, i + 1);
                pc.setCalculatedHourly(0);
                pc.setProjectPositions(positions.size());
                pc.setProjectPolygons(polygons.size());
                pc.setSchedules(schedules.size());
                pc.setBatchId(batchId);
                summaries.add(pc);
                myStart = myStart.plusHours(1);
            }
        }
        projectSummaryRepository.insert(summaries);

        return summaries;
    }

    public List<ProjectSummary> createHourlyProjectSummaries(String projectId, String startDate, String endDate) throws Exception {
        //todo - check if this range has been created before - maybe think of a key strategy to prevent duplicates
        long start = System.currentTimeMillis();
        List<ProjectSummary> summaries = new ArrayList<>();
        DateTime dtFrom = DateTime.parse(startDate).toDateTimeISO();
        DateTime dtStart = dtFrom.withTimeAtStartOfDay();
        DateTime dtTo = DateTime.parse(endDate).toDateTimeISO();
        String batchId = UUID.randomUUID().toString();

        Project project = projectRepository.findByProjectId(projectId);
        long hoursDiff = Math.abs(Hours.hoursBetween(dtStart, dtTo).getHours());

        DateTime myStart = DateTime.parse(dtStart.toString());
        val positions = projectPositionRepository.findByProjectId(project.getProjectId());
        val polygons = projectPolygonRepository.findByProjectId(project.getProjectId());
        val schedules = fieldMonitorScheduleRepository.findByProjectId(project.getProjectId());
        for (int i = 0; i < hoursDiff; i++) {
            val pc = createProjectSummary(project, myStart.toString(), (myStart.plusHours(1)).toString(), -1, i + 1);
            pc.setCalculatedHourly(0);
            pc.setProjectPositions(positions.size());
            pc.setProjectPolygons(polygons.size());
            pc.setSchedules(schedules.size());
            pc.setBatchId(batchId);
            summaries.add(pc);
            myStart = myStart.plusHours(1);
        }

        projectSummaryRepository.insert(summaries);
        final DecimalFormat df = new DecimalFormat("0.000");
        long end = System.currentTimeMillis();
        long ms = (end - start);
        Double delta = Double.parseDouble("" + ms) / Double.parseDouble("1000");

        return summaries;
    }

    public List<Photo> getProjectPhotosInPeriod(String projectId, String startDate, String endDate) throws Exception {

        Criteria c = Criteria.where("projectId").is(projectId).and("date").gte(startDate).lte(endDate);
        Query query = new Query(c);
        return mongoTemplate.find(query, Photo.class);
    }

    public List<Video> getProjectVideosInPeriod(String projectId, String startDate, String endDate) throws Exception {
        Criteria c = Criteria.where("projectId").is(projectId).and("date").gte(startDate).lte(endDate);
        Query query = new Query(c);
        return mongoTemplate.find(query, Video.class);
    }

    public List<Audio> getProjectAudiosInPeriod(String projectId, String startDate, String endDate) throws Exception {
        Criteria c = Criteria.where("projectId").is(projectId).and("date").gte(startDate).lte(endDate);
        Query query = new Query(c);
        return mongoTemplate.find(query, Audio.class);
    }

    public ProjectSummary createProjectSummary(Project project, String startDate, String endDate, int day, int hour) throws Exception {

        long start = System.currentTimeMillis();

        val photos = getProjectPhotosInPeriod(project.getProjectId(), startDate, endDate);
        val videos = getProjectVideosInPeriod(project.getProjectId(), startDate, endDate);
        val audios = getProjectAudiosInPeriod(project.getProjectId(), startDate, endDate);

        val pc = new ProjectSummary();
        pc.setProjectId(project.getProjectId());
        pc.setProjectName(project.getName());
        pc.setOrganizationId(project.getOrganizationId());
        pc.setOrganizationName(project.getOrganizationName());
        pc.setDate(DateTime.now().toDateTimeISO().toString());
        pc.setAudios(audios.size());
        pc.setVideos(videos.size());
        pc.setPhotos(photos.size());
        pc.setDay(day);
        pc.setHour(hour);
        pc.setStartDate(startDate);
        pc.setEndDate(endDate);

        final DecimalFormat df = new DecimalFormat("0.000");

        long end = System.currentTimeMillis();
        long ms = (end - start);
        Double delta = Double.parseDouble("" + ms) / Double.parseDouble("1000");

        return pc;
    }


    public void updateAllActivities() throws Exception {

        val list = activityModelRepository.findAll();
        val users = userRepository.findAll();

        long cnt = 0;
        for (User user : users) {
            if (user.getThumbnailUrl() == null) {
                continue;
            }
            Query query = new Query(Criteria.where("userId").is(user.getUserId()));
            Update update = new Update();
            update.set("userThumbnailUrl", user.getThumbnailUrl());
            update.set("userName", user.getName());
            UpdateResult result = mongoTemplate.updateMulti(query, update, ActivityModel.class);

            cnt = cnt + result.getModifiedCount();


        }


    }

    public User updateUser(User user) throws Exception {

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

        messageService.sendMessage(user);

        String message = "Dear " + user.getName() +
                "      ,\n\nYour account has been updated with new information. '\n" +
                "      \nIf you have not changed anything yourself please contact your Administrator or your Supervisor.\n" +
                "      \n\nThank you for working with GeoMonitor. \nBest Regards,\nThe GeoMonitor Team\ninfo@geomonitorapp.io\n\n";

        mailService.sendHtmlEmail(user.getEmail(), message, "GeoMonitor Account Updated");


        ActivityModel am = new ActivityModel();
        am.setActivityType(ActivityType.userAddedOrModified);
        am.setActivityModelId(UUID.randomUUID().toString());
        am.setDate(DateTime.now().toDateTimeISO().toString());
        am.setProjectId(null);
        am.setUserId(user.getUserId());
        am.setOrganizationName(user.getOrganizationName());
        am.setOrganizationId(user.getOrganizationId());
        am.setUserName(user.getName());
        am.setUserThumbnailUrl(user.getThumbnailUrl());
        am.setProjectName(null);
        am.setUser(user);
        am.setUserType(user.getUserType());
        am.setTranslatedUserType(null);

        addActivityModel(am);
        return user;
    }

    public User addUser(User user) throws Exception {

        User mUser = userRepository.insert(user);
         messageService.sendMessage(user);

        ActivityModel am = new ActivityModel();
        am.setActivityType(ActivityType.userAddedOrModified);
        am.setActivityModelId(UUID.randomUUID().toString());
        am.setDate(DateTime.now().toDateTimeISO().toString());
        am.setProjectId(null);
        am.setUserId(user.getUserId());
        am.setOrganizationName(user.getOrganizationName());
        am.setOrganizationId(user.getOrganizationId());
        am.setUserName(user.getName());
        am.setUserThumbnailUrl(user.getThumbnailUrl());
        am.setProjectName(null);
        am.setUserType(user.getUserType());
        am.setTranslatedUserType(null);

        addActivityModel(am);

        return mUser;
    }

    public int addRating(Rating rating) {
        if (rating.getRatingId() == null) {
            rating.setRatingId(UUID.randomUUID().toString());
        }
         ratingRepository.insert(rating);
        return 0;
    }

    public int addAppError(AppError appError) {
         appErrorRepository.insert(appError);
         return 0;
    }

    public int addPhoto(Photo photo) throws Exception {
        if (photo.getPhotoId() == null) {
            photo.setPhotoId(UUID.randomUUID().toString());
        }
        photoRepository.insert(photo);
        messageService.sendMessage(photo);
        ActivityModel am2 = buildActivityModel(photo,ActivityType.photoAdded);
        addActivityModel(am2);
        return 0;
    }

    ActivityModel buildActivityModel(GioMediaInterface gio, ActivityType activityType) {
        ActivityModel am = new ActivityModel();
        am.setActivityType(activityType);
        am.setActivityModelId(UUID.randomUUID().toString());
        am.setDate(DateTime.now().toDateTimeISO().toString());
        am.setProjectId(gio.getProjectId());
        am.setUserId(gio.getUserId());
        am.setOrganizationName(null);
        am.setOrganizationId(gio.getOrganizationId());
        am.setUserName(gio.getUserName());
        am.setProjectName(gio.getProjectName());
        am.setUserThumbnailUrl(gio.getUserUrl());
        am.setUserType(gio.getUserType());
        //
        if (gio instanceof Photo) {
            am.setPhoto((Photo) gio);
        }
        if (gio instanceof Video) {
            am.setVideo((Video) gio);
        }
        if (gio instanceof Audio) {
            am.setAudio((Audio) gio);
        }

        am.setUserType(gio.getUserType());
        am.setTranslatedUserType(null);

        return am;
    }

    public void addActivityModel(ActivityModel model) throws Exception {

        activityModelRepository.insert(model);
        messageService.sendMessage(model);
    }

    public int addProjectAssignment(ProjectAssignment projectAssignment) throws Exception {
        if (projectAssignment.getProjectAssignmentId() == null) {
            projectAssignment.setProjectAssignmentId(UUID.randomUUID().toString());
        }
        projectAssignmentRepository.insert(projectAssignment);
        messageService.sendMessage(projectAssignment);
        return 0;
    }


    public int addVideo(Video video) throws Exception {
        videoRepository.insert(video);

        ActivityModel am2 = buildActivityModel(video,ActivityType.videoAdded);
        addActivityModel(am2);
        messageService.sendMessage(video);
        return 0;
    }

    public int addAudio(Audio audio) throws Exception {

        audioRepository.insert(audio);
        ActivityModel am2 = buildActivityModel(audio,ActivityType.audioAdded);
        addActivityModel(am2);
        messageService.sendMessage(audio);
        return 0;
    }

    public int addCondition(Condition condition) throws Exception {
        conditionRepository.insert(condition);
         messageService.sendMessage(condition);
        return 0;
    }

    public int addOrgMessage(OrgMessage orgMessage) throws Exception {
        orgMessageRepository.insert(orgMessage);
        messageService.sendMessage(orgMessage);
        orgMessage.setResult(null);
        User user = userRepository.findByUserId(orgMessage.getAdminId());

        ActivityModel am = new ActivityModel();
        am.setActivityType(ActivityType.messageAdded);
        am.setActivityModelId(UUID.randomUUID().toString());
        am.setDate(DateTime.now().toDateTimeISO().toString());
        am.setProjectId(orgMessage.getProjectId());
        am.setUserId(orgMessage.getUserId());
        if (user != null) {
            am.setUserThumbnailUrl(user.getThumbnailUrl());
        }
        am.setOrganizationName(null);
        am.setOrganizationId(orgMessage.getOrganizationId());
        am.setUserName(orgMessage.getAdminName());
        am.setProjectName(orgMessage.getProjectName());
        am.setOrgMessage(orgMessage);

        addActivityModel(am);
        return 0;
    }

    public int addFieldMonitorSchedule(FieldMonitorSchedule fieldMonitorSchedule) throws Exception {
        fieldMonitorScheduleRepository.insert(fieldMonitorSchedule);
        messageService.sendMessage(fieldMonitorSchedule);

        return 0;
    }

    public int addProjectPosition(ProjectPosition projectPosition) throws Exception {

        ProjectPosition m = projectPositionRepository.insert(projectPosition);

        messageService.sendMessage(m);
        User user = userRepository.findByUserId(projectPosition.getUserId());

        ActivityModel am = new ActivityModel();
        projectPosition.setNearestCities(null);
        am.setActivityType(ActivityType.positionAdded);
        am.setActivityModelId(UUID.randomUUID().toString());
        am.setDate(DateTime.now().toDateTimeISO().toString());
        am.setProjectId(projectPosition.getProjectId());
        am.setUserId(projectPosition.getUserId());
        if (user != null) {
            am.setUserThumbnailUrl(user.getThumbnailUrl());
        }
        am.setOrganizationName(null);
        am.setOrganizationId(projectPosition.getOrganizationId());
        am.setUserName(projectPosition.getUserName());
        am.setProjectName(projectPosition.getProjectName());
        am.setProjectPosition(projectPosition);

        addActivityModel(am);

        return 0;
    }

    public int addProjectPolygon(ProjectPolygon projectPolygon) throws Exception {

        ProjectPolygon m = projectPolygonRepository.insert(projectPolygon);

        if (m != null) {
            m.setNearestCities(null);
            messageService.sendMessage(m);
        }
        User user = userRepository.findByUserId(projectPolygon.getUserId());

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
        if (user != null) {
            am.setUserThumbnailUrl(user.getThumbnailUrl());
        }
        am.setProjectName(projectPolygon.getProjectName());
        am.setProjectPolygon(projectPolygon);

        addActivityModel(am);

        return 0;
    }

    public int addGeofenceEvent(GeofenceEvent geofenceEvent) throws Exception {

        GeofenceEvent m = geofenceEventRepository.insert(geofenceEvent);

        if (m != null) {
            messageService.sendMessage(m);
        }

        ActivityModel am = new ActivityModel();
        am.setActivityType(ActivityType.geofenceEventAdded);
        am.setActivityModelId(UUID.randomUUID().toString());
        am.setDate(DateTime.now().toDateTimeISO().toString());
        am.setProjectId(geofenceEvent.getProjectId());

        am.setUserId(geofenceEvent.getUser().getUserId());
        am.setOrganizationName(null);
        am.setOrganizationId(geofenceEvent.getOrganizationId());
        am.setUserName(geofenceEvent.getUser().getName());
        am.setUserThumbnailUrl(geofenceEvent.getUser().getThumbnailUrl());
        am.setProjectName(geofenceEvent.getProjectName());
        am.setGeofenceEvent(geofenceEvent);

        addActivityModel(am);
        return 0;
    }

    public int addProject(Project project) throws Exception {

        Project m = projectRepository.insert(project);

        ActivityModel am = new ActivityModel();
        am.setActivityType(ActivityType.projectAdded);
        am.setActivityModelId(UUID.randomUUID().toString());
        am.setDate(DateTime.now().toDateTimeISO().toString());
        am.setProjectId(project.getProjectId());
        am.setUserId(null);
        am.setOrganizationName(project.getOrganizationName());
        am.setOrganizationId(project.getOrganizationId());
        am.setUserName(null);
        am.setProjectName(project.getName());
        am.setProject(project);

        addActivityModel(am);
        messageService.sendMessage(m);
        return 0;
    }

    public int addLocationResponse(LocationResponse locationResponse) throws Exception {

        LocationResponse m = locationResponseRepository.insert(locationResponse);

        messageService.sendMessage(m);
        User user = userRepository.findByUserId(locationResponse.getUserId());

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
        if (user != null) {
            am.setUserThumbnailUrl(user.getThumbnailUrl());
        }

        addActivityModel(am);
        return 0;
    }

    public int addLocationRequest(LocationRequest locationRequest) throws Exception {

        LocationRequest m = locationRequestRepository.insert(locationRequest);

        if (m != null) {
            messageService.sendMessage(m);
        }
        User user = userRepository.findByUserId(locationRequest.getUserId());

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
        if (user != null) {
            am.setUserThumbnailUrl(user.getThumbnailUrl());
        }
        addActivityModel(am);
        return 0;
    }

    public Project updateProject(Project project) throws Exception {

        Project m = projectRepository.insert(project);
        return m;
    }

    public City addCity(City city) throws Exception {
        city.setCityId(UUID.randomUUID().toString());
        City c = cityRepository.insert(city);
        return c;
    }

    public Community addCommunity(Community community) throws Exception {
        community.setCommunityId(UUID.randomUUID().toString());
        Community cm = communityRepository.insert(community);

        return cm;
    }

    public int addCountry(Country country) throws Exception {
        countryRepository.insert(country);
        return 0;
    }

    public int addSettings(SettingsModel model) throws Exception {

        SettingsModel m = settingsModelRepository.insert(model);
        messageService.sendMessage(model);

        ActivityModel am = new ActivityModel();
        am.setActivityType(ActivityType.settingsChanged);
        am.setActivityModelId(UUID.randomUUID().toString());
        am.setDate(DateTime.now().toDateTimeISO().toString());
        am.setProjectId(model.getProjectId());
        am.setUserId(model.getUserId());
        am.setOrganizationId(model.getOrganizationId());
        am.setUserName(model.getUserName());
        am.setUserThumbnailUrl(model.getUserThumbnailUrl());
        am.setProjectName(null);

        addActivityModel(am);
        return 0;
    }


    public void deleteTestOrganization() {
        Query query = Query.query(Criteria.where("name").is("Fake Test Organization"));
        mongoTemplate.findAndRemove(query, Organization.class);

        Query query2 = Query.query(Criteria.where("name").is("John Q. Testerman"));
        mongoTemplate.findAndRemove(query2, User.class);

        LOGGER.info(E.BUTTERFLY+E.BUTTERFLY+ " should have deleted test organization");
    }

    public User createUser(User user) throws Exception {
        LOGGER.info("\uD83E\uDDE1\uD83E\uDDE1 create user : " + G.toJson(user));
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        LOGGER.info("\uD83E\uDDE1\uD83E\uDDE1 createRequest  .... ");
        try {
            UserRecord.CreateRequest createRequest = new UserRecord.CreateRequest();
            createRequest.setPhoneNumber(user.getCellphone());
            createRequest.setDisplayName(user.getName());
            createRequest.setPassword(user.getPassword());
            createRequest.setEmail(user.getEmail());

            LOGGER.info("\uD83E\uDDE1\uD83E\uDDE1 createUserAsync  .... ");


            ApiFuture<UserRecord> userRecord = firebaseAuth.createUserAsync(createRequest);
            LOGGER.info("\uD83E\uDDE1\uD83E\uDDE1 userRecord : " + G.toJson(userRecord));
            String uid = userRecord.get().getUid();
            user.setUserId(uid);

            user.setPassword(null);
            String message = "Dear " + user.getName() +
                    "      ,\n\nYou have been registered with GeoMonitor and the team is happy to send you the first time login password. '\n" +
                    "      \nPlease login on the web with your email and the attached password but use your cellphone number to sign in on the phone.\n" +
                    "      \n\nThank you for working with GeoMonitor. \nWelcome aboard!!\nBest Regards,\nThe GeoMonitor Team\ninfo@geomonitorapp.io\n\n";

            LOGGER.info("\uD83E\uDDE1\uD83E\uDDE1 sending email  .... ");

            mailService.sendHtmlEmail(user.getEmail(), message, "Welcome to GeoMonitor");
            LOGGER.info("\uD83E\uDDE1\uD83E\uDDE1 sending activity object  .... ");
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

            LOGGER.info("\uD83E\uDDE1\uD83E\uDDE1 create user completed. ");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }


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

            FirebaseAuth.getInstance().updateUser(request);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("User auth update failed: " + e.getMessage());
        }

    }
}
