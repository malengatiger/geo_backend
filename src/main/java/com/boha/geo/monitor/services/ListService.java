package com.boha.geo.monitor.services;


import com.boha.geo.monitor.data.*;
import com.boha.geo.repos.*;
import com.boha.geo.util.E;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoClient;
import lombok.val;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

;

@Service
public class ListService {
    public static final Logger LOGGER = LoggerFactory.getLogger(ListService.class.getSimpleName());
    private static final String xx = E.COFFEE + E.COFFEE + E.COFFEE;

    private static final Gson G = new GsonBuilder().setPrettyPrinting().create();
    @Autowired
    GeofenceEventRepository geofenceEventRepository;

    @Autowired
    ActivityModelRepository activityModelRepository;

    @Autowired
    ProjectSummaryRepository projectSummaryRepository;
    @Autowired
    CountryRepository countryRepository;
    @Autowired
    CityRepository cityRepository;
    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    ProjectPolygonRepository projectPolygonRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    CommunityRepository communityRepository;

    @Autowired
    SettingsModelRepository settingsModelRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MonitorReportRepository monitorReportRepository;
    @Autowired
    QuestionnaireRepository questionnaireRepository;
    @Autowired
    PhotoRepository photoRepository;

    @Autowired
    ProjectAssignmentRepository projectAssignmentRepository;
    @Autowired
    VideoRepository videoRepository;
    @Autowired
    DataService dataService;
    @Autowired
    AudioRepository audioRepository;
    @Autowired
    ConditionRepository conditionRepository;

    @Autowired
    FieldMonitorScheduleRepository fieldMonitorScheduleRepository;
    @Autowired
    MongoTemplate mongoTemplate;

    public ListService() {

        LOGGER.info(xx + " ListService constructed ");
    }

    static final double lat = 0.0144927536231884; // degrees latitude per mile
    static final double lon = 0.0181818181818182; // degrees longitude per mile

    public User findUserByEmail(String email) throws Exception {
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("findUserByEmail ...".concat(email)));

        User user = userRepository.findByEmail(email);

        if (user != null) {
            LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("findUserByEmail ... found: \uD83D\uDC24 " + G.toJson(user)));
        } else {
            throw new Exception(E.ALIEN + "User " + email + " not found, probably not registered yet ".concat(E.NOT_OK));
        }
        return user;
    }

    public List<FieldMonitorSchedule> getProjectFieldMonitorSchedules(String projectId, String startDate, String endDate) {

        Criteria c = Criteria.where("projectId").is(projectId)
                .and("created").gte(startDate).lte(endDate);
        Query query = new Query(c);
        List<FieldMonitorSchedule> m = mongoTemplate.find(query, FieldMonitorSchedule.class);

        return m;
    }

    public List<FieldMonitorSchedule> getUserFieldMonitorSchedules(String userId) {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS).concat("getUserFieldMonitorSchedules: "
                .concat(E.FLOWER_YELLOW)));

        List<FieldMonitorSchedule> m = fieldMonitorScheduleRepository.findByUserId(userId);

        LOGGER.info(E.LEAF.concat(E.LEAF).concat("getUserFieldMonitorSchedules found: " + m.size()));
        return m;
    }

    public List<FieldMonitorSchedule> getOrgFieldMonitorSchedules(String organizationId, String startDate, String endDate) {
        Criteria c = Criteria.where("organizationId").is(organizationId)
                .and("created").gte(startDate).lte(endDate);
        Query query = new Query(c);
        List<FieldMonitorSchedule> mList = mongoTemplate.find(query, FieldMonitorSchedule.class);


        return mList;
    }

    public List<FieldMonitorSchedule> getMonitorFieldMonitorSchedules(String userId) {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS).concat("getMonitorFieldMonitorSchedules: "
                .concat(E.FLOWER_YELLOW)));

        List<FieldMonitorSchedule> m = fieldMonitorScheduleRepository.findByFieldMonitorId(userId);

        LOGGER.info(E.LEAF.concat(E.LEAF).concat("getMonitorFieldMonitorSchedules found: " + m.size()));
        return m;
    }

    public List<FieldMonitorSchedule> getAdminFieldMonitorSchedules(String userId) {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS).concat("getOrgFieldMonitorSchedules: "
                .concat(E.FLOWER_YELLOW)));

        List<FieldMonitorSchedule> m = fieldMonitorScheduleRepository.findByAdminId(userId);

        LOGGER.info(E.LEAF.concat(E.LEAF).concat("getOrgFieldMonitorSchedules found: " + m.size()));
        return m;
    }

    public List<Organization> getOrganizations() {

        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getOrganizations ..."));
        List<Organization> mList = (List<Organization>) organizationRepository.findAll();
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getOrganizations ... found: " + mList.size()));
        return mList;
    }

    public List<Community> getCommunities() {

        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getCommunities ..."));
        List<Community> mList = (List<Community>) communityRepository.findAll();
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getCommunities ... found: " + mList.size()));

        return mList;
    }

    public List<Project> getProjects() {

        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("ListService: getProjects ..."));
        List<Project> mList = (List<Project>) projectRepository.findAll();

        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("ListService: getProjects ... found:" +
                " \uD83D\uDC24 " + mList.size()));

        return mList;
    }


    public List<Organization> getCountryOrganizations(String countryId) {

        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getOrganizations ..."));
        List<Organization> mList = organizationRepository.findByCountryId(countryId);
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getOrganizations ... found: " + mList.size()));

        return mList;
    }

    public List<Photo> getProjectPhotos(String projectId, String startDate, String endDate) {

        Criteria c = Criteria.where("projectId").is(projectId)
                .and("created").gte(startDate).lte(endDate);
        Query query = new Query(c);
        List<Photo> mList = mongoTemplate.find(query, Photo.class);

        return mList;
    }

    public List<ProjectAssignment> getProjectAssignments(String projectId, String startDate, String endDate) {
        Criteria c = Criteria.where("projectId").is(projectId)
                .and("created").gte(startDate).lte(endDate);
        Query query = new Query(c);
        List<ProjectAssignment> mList = mongoTemplate.find(query, ProjectAssignment.class);
        return mList;
    }

    public List<ProjectAssignment> getUserProjectAssignments(String userId) {

        return projectAssignmentRepository.findByUserId(userId);
    }

    public List<ProjectAssignment> getOrganizationProjectAssignments(String organizationId) {

        return projectAssignmentRepository.findByOrganizationId(organizationId);
    }

    public List<ActivityModel> getOrganizationActivity(String organizationId, int hours) {

        DateTime then = DateTime.now().minusHours(hours);
        String date = then.toDateTimeISO().toString();

        Criteria criteria = new Criteria();
        criteria = criteria.and("organizationId").is(organizationId);
        criteria = criteria.and("date").gt(date);

        Query query = new Query(criteria);
        List<ActivityModel> activities = mongoTemplate.find(query, ActivityModel.class);
        LOGGER.info(E.PANDA + E.PANDA + " activities found " + activities.size() + " hours: " + hours);
        return activities;
    }

    public List<ActivityModel> getProjectActivity(String projectId, int hours) {

        DateTime then = DateTime.now().minusHours(hours);
        String date = then.toDateTimeISO().toString();

        Criteria criteria = new Criteria();
        criteria = criteria.and("projectId").is(projectId);
        criteria = criteria.and("date").gt(date);

        Query query = new Query(criteria);
        List<ActivityModel> activities = mongoTemplate.find(query, ActivityModel.class);
        LOGGER.info(E.PANDA + E.PANDA + " project activities found " + activities.size() + " hours: " + hours);
        return activities;
    }

    public List<ActivityModel> getUserActivity(String userId, int hours) {

        DateTime then = DateTime.now().minusHours(hours);
        String date = then.toDateTimeISO().toString();

        Criteria criteria = new Criteria();
        criteria = criteria.and("userId").is(userId);
        criteria = criteria.and("date").gt(date);

        Query query = new Query(criteria);
        List<ActivityModel> activities = mongoTemplate.find(query, ActivityModel.class);
        LOGGER.info(E.PANDA + E.PANDA + " user activities found " + activities.size() + " hours: " + hours);
        return activities;
    }

    public DataBag getUserData(String userId, String startDate, String endDate) {
        DataBag bag = new DataBag();
        User user = userRepository.findByUserId(userId);
        List<Project> projects = projectRepository.findByOrganizationId(user.getOrganizationId());
        List<Photo> photos = getUserProjectPhotos(userId);
        List<Video> videos = getUserProjectVideos(userId);
        List<ProjectAssignment> assignments = getUserProjectAssignments(userId);

        List<ProjectPosition> projectPositions = getOrganizationProjectPositions(user.getOrganizationId(), startDate, endDate);
        List<ProjectPolygon> projectPolygons = getOrganizationProjectPolygons(user.getOrganizationId(), startDate, endDate);
        List<FieldMonitorSchedule> fieldMonitorSchedules = getOrgFieldMonitorSchedules(user.getOrganizationId(), startDate, endDate);
        List<User> users = getOrganizationUsers(user.getOrganizationId(), startDate, endDate);

        bag.setDate(DateTime.now().toDateTimeISO().toString());
        bag.setProjects(projects);
        bag.setFieldMonitorSchedules(fieldMonitorSchedules);
        bag.setProjectPositions(projectPositions);
        bag.setProjectPolygons(projectPolygons);
        bag.setPhotos(photos);
        bag.setVideos(videos);
        bag.setUsers(users);
        bag.setProjectAssignments(assignments);


        LOGGER.info(E.RED_APPLE + " User data found: photos: " + bag.getPhotos().size() + " videos: " + bag.getVideos().size()
                + " schedules: " + bag.getFieldMonitorSchedules().size());

        return bag;
    }

    public DataBag getProjectData(String projectId, String startDate, String endDate) {
        DataBag bag = new DataBag();
        Project project = projectRepository.findByProjectId(projectId);
        List<Project> projects = new ArrayList<>();
        projects.add(project);

        List<Photo> photos = getProjectPhotos(projectId, startDate, endDate);
        List<Video> videos = getProjectVideos(projectId, startDate, endDate);
        List<Audio> audios = getProjectAudios(projectId, startDate, endDate);
        List<ProjectAssignment> assignments = getProjectAssignments(projectId, startDate, endDate);
        List<ProjectPosition> projectPositions = getProjectPositions(projectId, startDate, endDate);
        List<ProjectPolygon> projectPolygons = getProjectPolygons(projectId, startDate, endDate);
        List<FieldMonitorSchedule> fieldMonitorSchedules = getProjectFieldMonitorSchedules(projectId, startDate, endDate);
        List<SettingsModel> settings = getProjectSettings(projectId);

        bag.setDate(DateTime.now().toDateTimeISO().toString());
        bag.setProjects(projects);
        bag.setFieldMonitorSchedules(fieldMonitorSchedules);
        bag.setProjectPositions(projectPositions);
        bag.setProjectPolygons(projectPolygons);
        bag.setPhotos(photos);
        bag.setVideos(videos);
        bag.setAudios(audios);
        bag.setSettings(settings);
        bag.setProjectAssignments(assignments);

        LOGGER.info(E.RED_APPLE + " Project data found: photos: " + bag.getPhotos().size() + " videos: " + bag.getVideos().size()
                + " schedules: " + bag.getFieldMonitorSchedules().size() + " polygons: " + bag.getProjectPolygons().size());

        return bag;
    }

    public Photo findPhotoById(String photoId) {
        return photoRepository.findByPhotoId(photoId);
    }

    public Video findVideoById(String photoId) {
        return videoRepository.findByVideoId(photoId);
    }

    public Audio findAudioById(String photoId) {
        return audioRepository.findByAudioId(photoId);
    }

    public DataBag getOrganizationData(String organizationId, String startDate, String endDate) throws Exception {
        DataBag bag = new DataBag();

        List<Project> projects = getOrganizationProjects(organizationId, startDate, endDate);
        List<Photo> photos = getOrganizationPhotos(organizationId, startDate, endDate);
        List<Video> videos = getOrganizationVideos(organizationId, startDate, endDate);
        List<Audio> audios = getOrganizationAudios(organizationId, startDate, endDate);

        List<ProjectAssignment> assignments = getOrganizationProjectAssignments(organizationId);

        List<ProjectPosition> projectPositions = getOrganizationProjectPositions(organizationId, startDate, endDate);
        List<FieldMonitorSchedule> fieldMonitorSchedules = getOrgFieldMonitorSchedules(organizationId, startDate, endDate);
        List<ProjectPolygon> polygons = getOrganizationProjectPolygons(organizationId, startDate, endDate);

        List<User> users = getOrganizationUsers(organizationId, startDate, endDate);

        bag.setDate(DateTime.now().toDateTimeISO().toString());
        bag.setProjects(projects);
        bag.setFieldMonitorSchedules(fieldMonitorSchedules);
        bag.setProjectPositions(projectPositions);
        bag.setProjectPolygons(polygons);
        bag.setPhotos(photos);
        bag.setVideos(videos);
        bag.setAudios(audios);
        bag.setUsers(users);
        bag.setProjectAssignments(assignments);

        LOGGER.info(E.RED_APPLE + " Organization data found: photos: " + bag.getPhotos().size() + " videos: " + bag.getVideos().size()
                + " schedules: " + bag.getFieldMonitorSchedules().size() + " polygons: " + bag.getProjectPolygons().size());

//        File zipped = getOrganizationDataZippedFile(organizationId);
//        LOGGER.info(" zipped file with org data: " + zipped.length() + " bytes");

        return bag;
    }

    public List<ProjectSummary> getOrganizationSummary(
            String organizationId, String startDate, String endDate) throws Exception {

        List<ProjectSummary> list = dataService.createDailyOrganizationSummaries(organizationId, startDate, endDate);

        LOGGER.info(E.RED_APPLE + " Organization summaries found:  " + list.size());

        return list;
    }

    public List<ProjectSummary> getProjectSummary(
            String projectId, String startDate, String endDate) throws Exception {


        return new ArrayList<>();
    }

    static final String mm = "" + E.BLUE_DOT + E.BLUE_DOT + E.BLUE_DOT + " Zipping Org data: ";

    public File getOrganizationDataZippedFile(String organizationId, String startDate, String endDate) throws Exception {

        long start = System.currentTimeMillis();

        DataBag bag = getOrganizationData(organizationId, startDate, endDate);
        String json = G.toJson(bag);
        LOGGER.info(mm + " Size of json file before zipping: " + json.length() + " bytes");
        File dir = new File("zipDirectory");
        if (!dir.exists()) {
            boolean ok = dir.mkdir();
            LOGGER.info(mm + " Zip directory has been created: path: " + dir.getAbsolutePath() + " created: " + ok);
        }
        File zippedFile = new File(dir, "" + DateTime.now().getMillis() + ".zip");
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zippedFile));
        ZipEntry e = new ZipEntry("dataBag");
        out.putNextEntry(e);

        byte[] data = json.getBytes();
        out.write(data, 0, data.length);
        out.closeEntry();

        out.close();
        long end = System.currentTimeMillis();
        long seconds = ((end - start) / 1000) % 60;

        LOGGER.info(mm + " Size of zipped json file after zipping: "
                + zippedFile.length() + " bytes, elapsed: " + seconds + " seconds");
        return zippedFile;
    }

    public File getProjectDataZippedFile(String projectId, String startDate, String endDate) throws Exception {
        long start = System.currentTimeMillis();
        DataBag bag = getProjectData(projectId, startDate, endDate);
        String json = G.toJson(bag);
        LOGGER.info(mm + " Size of json file before zipping: " + json.length() + " bytes");

        File dir = new File("zipDirectory");
        if (!dir.exists()) {
            boolean ok = dir.mkdir();
            LOGGER.info(mm + " Zip directory has been created: path: " + dir.getAbsolutePath() + " created: " + ok);
        }
        File zippedFile = new File(dir, "" + DateTime.now().getMillis() + ".zip");
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zippedFile));
        ZipEntry e = new ZipEntry("dataBag");
        out.putNextEntry(e);

        byte[] data = json.getBytes();
        out.write(data, 0, data.length);
        out.closeEntry();

        out.close();
        long end = System.currentTimeMillis();
        long seconds = ((end - start) / 1000) % 60;

        LOGGER.info(mm + " Size of zipped json file after zipping: "
                + zippedFile.length() + " bytes, elapsed: " + seconds + " seconds");
        return zippedFile;
    }

    public File getUserDataZippedFile(String userId, String startDate, String endDate) throws Exception {

        DataBag bag = getUserData(userId, startDate, endDate);
        String json = G.toJson(bag);
        LOGGER.info(mm + " getUserDataZippedFile: Size of json file before zipping: " + json.length() + " bytes");

        File dir = new File("zipDirectory");
        if (!dir.exists()) {
            boolean ok = dir.mkdir();
            LOGGER.info(mm + " Zip directory has been created: path: " + dir.getAbsolutePath() + " created: " + ok);
        }
        File zippedFile = new File(dir, "" + DateTime.now().getMillis() + ".zip");
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zippedFile));
        ZipEntry e = new ZipEntry("dataBag");
        out.putNextEntry(e);

        byte[] data = json.getBytes();
        out.write(data, 0, data.length);
        out.closeEntry();

        out.close();
        LOGGER.info(mm + " Size of zipped json file after zipping: " + zippedFile.length() + " bytes");
        return zippedFile;
    }

    public List<Photo> getUserProjectPhotos(String userId) {

        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getUserProjectPhotos ...userId: " + userId));
        List<Photo> mList = photoRepository.findByUserId(userId);
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getUserProjectPhotos ... found: " + mList.size()));

        return mList;
    }

    public List<Video> getUserProjectVideos(String userId) {

        List<Video> mList = videoRepository.findByUserId(userId);
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getUserProjectVideos ... found: " + mList.size()));

        return mList;
    }

    public List<ProjectSummary> getProjectSummaries(String projectId, String startDate, String endDate) {

        List<ProjectSummary> mList = projectSummaryRepository.findByProjectInPeriod(projectId, startDate, endDate);
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getProjectSummaries ... found: " + mList.size()
                + " startDate: " + startDate + " endDate: " + endDate));

        return mList;
    }

    public List<ActivityModel> getProjectActivityPeriod(String projectId, String startDate, String endDate) {

        List<ActivityModel> mList = activityModelRepository.findByProjectPeriod(projectId, startDate, endDate);
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getProjectActivityPeriod ... found: " + mList.size()
                + " startDate: " + startDate + " endDate: " + endDate));

        return mList;
    }

    public List<ProjectSummary> getOrganizationSummaries(String organizationId, String startDate, String endDate) {

        List<ProjectSummary> mList = projectSummaryRepository.findByOrganizationInPeriod(organizationId, startDate, endDate);
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getProjectSummaries ... found: " + mList.size()
                + " startDate: " + startDate + " endDate: " + endDate));

        return mList;
    }

    public List<ActivityModel> getOrganizationActivityPeriod(String organizationId, String startDate, String endDate) {

        List<ActivityModel> mList = activityModelRepository.findByOrganizationPeriod(organizationId, startDate, endDate);
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getOrganizationActivityPeriod ... found: " + mList.size()
                + " startDate: " + startDate + " endDate: " + endDate));

        return mList;
    }

    public List<ActivityModel> getUserActivityPeriod(String userId, String startDate, String endDate) {

        List<ActivityModel> mList = activityModelRepository.findByUserPeriod(userId, startDate, endDate);
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getUserActivityPeriod ... found: " + mList.size()
                + " startDate: " + startDate + " endDate: " + endDate));

        return mList;
    }

    public List<Audio> getUserProjectAudios(String userId, String startDate, String endDate) {
        Criteria c = Criteria.where("userId").is(userId)
                .and("created").gte(startDate).lte(endDate);
        Query query = new Query(c);
        List<Audio> mList = mongoTemplate.find(query, Audio.class);
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getUserProjectAudios ... found: " + mList.size()));

        return mList;
    }

    public List<Video> getProjectVideos(String projectId, String startDate, String endDate) {

        Criteria c = Criteria.where("projectId").is(projectId)
                .and("created").gte(startDate).lte(endDate);
        Query query = new Query(c);
        List<Video> mList = mongoTemplate.find(query, Video.class);

        return mList;
    }

    public List<Audio> getProjectAudios(String projectId, String startDate, String endDate) {

        Criteria c = Criteria.where("projectId").is(projectId)
                .and("created").gte(startDate).lte(endDate);
        Query query = new Query(c);
        List<Audio> mList = mongoTemplate.find(query, Audio.class);

        return mList;
    }

    public List<SettingsModel> getProjectSettings(String projectId) {

        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getProjectSettings ..."));
        List<SettingsModel> mList = settingsModelRepository.findByProjectId(projectId);
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getProjectSettings ... found: " + mList.size()));

        return mList;
    }

    public List<Condition> getProjectConditions(String projectId, String startDate, String endDate) {

        Criteria c = Criteria.where("projectId").is(projectId)
                .and("created").gte(startDate).lte(endDate);
        Query query = new Query(c);
        List<Condition> mList = mongoTemplate.find(query, Condition.class);

        return mList;
    }

    @Autowired
    ProjectPositionRepository projectPositionRepository;

    public List<Project> findProjectsByLocation(String organizationId, double latitude, double longitude, double radiusInKM) {
        LOGGER.info(E.DICE.concat(E.DICE).concat(" findProjectsByLocation ..."));
        Point point = new Point(longitude, latitude);
        Distance distance = new Distance(radiusInKM, Metrics.KILOMETERS);
        List<ProjectPosition> projects = projectPositionRepository.findByPositionNear(point, distance);
        LOGGER.info(E.DOLPHIN.concat(E.DOLPHIN).concat(E.DOLPHIN)
                + " Nearby Projects found: " + projects.size() + " : " + E.RED_APPLE + " radius: " + radiusInKM);
        List<Project> fList = new ArrayList<>();
        for (ProjectPosition projectPosition : projects) {
            LOGGER.info(E.DOLPHIN.concat(E.DOLPHIN) + projectPosition.getProjectName() + ", "
                    + E.COFFEE);
            if (projectPosition.getOrganizationId().equalsIgnoreCase(organizationId)) {
                Project p = projectRepository.findByProjectId(projectPosition.getProjectId());
                fList.add(p);
            }
        }
        LOGGER.info(E.HEART_ORANGE.concat(E.HEART_ORANGE).concat(
                "findProjectsByLocation: Nearby Projects found: " + projects.size() + " \uD83C\uDF3F"));
        return fList;
    }

    public List<City> findCitiesByLocation(double latitude, double longitude, double radiusInKM) {
        LOGGER.info(E.DICE.concat(E.DICE).concat(" findCitiesByLocation ... radiusInKM: " + radiusInKM));
        Point point = new Point(longitude, latitude);
        Distance distance = new Distance(radiusInKM, Metrics.KILOMETERS);
        GeoResults<City> cities = cityRepository.findByCityLocationNear(point, distance);
        LOGGER.info(E.DOLPHIN.concat(E.DOLPHIN).concat(E.DOLPHIN)
                + " Nearby Cities found: " + E.RED_APPLE + E.RED_APPLE + cities.getContent().size() + " : "
                + E.RED_APPLE + E.RED_APPLE + " radiusInKM: " + radiusInKM);

        LOGGER.info(E.DOLPHIN.concat(E.DOLPHIN + E.RED_APPLE + E.RED_APPLE)
                + "Total cities found: " + cities.getContent().size());
        List<City> mList = new ArrayList<>();
        for (GeoResult<City> city : cities) {
            mList.add(city.getContent());
        }
        return mList;
    }

    @Autowired
    MongoClient mongoClient;

    public int countPhotosByProject(String projectId) {

        int cnt = photoRepository.findByProjectId(projectId).size();
        LOGGER.info(E.HEART_ORANGE.concat(E.HEART_ORANGE)
                + " countPhotosByProject, \uD83C\uDF3F found: " + cnt);
        return cnt;
    }

    public ProjectSummary getCountsByProject(String projectId) {

        int photos = photoRepository.findByProjectId(projectId).size();
        int videos = videoRepository.findByProjectId(projectId).size();
        int audios = audioRepository.findByProjectId(projectId).size();
        int schedules = fieldMonitorScheduleRepository.findByProjectId(projectId).size();
        int positions = projectPositionRepository.findByProjectId(projectId).size();
        int polygons = projectPolygonRepository.findByProjectId(projectId).size();

        Project project = projectRepository.findByProjectId(projectId);
        val pc = new ProjectSummary();
        pc.setPhotos(photos);
        pc.setDate(new DateTime().toDateTimeISO().toString());
        pc.setVideos(videos);
        pc.setProjectId(projectId);
        pc.setOrganizationId(project.getOrganizationId());
        pc.setAudios(audios);
        pc.setSchedules(schedules);
        pc.setProjectName(project.getName());
        pc.setOrganizationName(project.getOrganizationName());
        pc.setProjectPositions(positions);
        pc.setProjectPolygons(polygons);

        LOGGER.info(E.HEART_ORANGE.concat(E.HEART_ORANGE)
                + " getCountsByProject, \uD83C\uDF3F found: " + G.toJson(pc));
        return pc;
    }

    public UserCount getCountsByUser(String userId) {

        List<Photo> photos = photoRepository.findByUserId(userId);
        List<Video> videos = videoRepository.findByUserId(userId);

        HashMap<String, String> map = new HashMap<>();
        for (Photo photo : photos) {
            map.put(photo.getProjectId(), photo.getProjectId());
        }
        for (Video video : videos) {
            map.put(video.getProjectId(), video.getProjectId());
        }

        User user = userRepository.findByUserId(userId);
        val pc = new UserCount();
        pc.setUser(user);
        pc.setVideos(videos.size());
        pc.setPhotos(photos.size());
        pc.setDate(DateTime.now().toDateTimeISO().toString());

        LOGGER.info(E.HEART_ORANGE.concat(E.HEART_ORANGE)
                + " getCountsByUser, \uD83C\uDF3F found: " + G.toJson(pc));
        return pc;
    }

    public int countVideosByProject(String projectId) {

        int cnt = videoRepository.findByProjectId(projectId).size();
        LOGGER.info(E.HEART_ORANGE.concat(E.HEART_ORANGE)
                + " countVideosByProject, \uD83C\uDF3F found: " + cnt);
        return cnt;
    }

    public int countPhotosByUser(String userId) {

        int cnt = photoRepository.findByUserId(userId).size();
        LOGGER.info(E.HEART_ORANGE.concat(E.HEART_ORANGE)
                + " countPhotosByUser, \uD83C\uDF3F found: " + cnt);
        return cnt;
    }

    public int countVideosByUser(String userId) {

        int cnt = videoRepository.findByUserId(userId).size();
        LOGGER.info(E.HEART_ORANGE.concat(E.HEART_ORANGE)
                + " countVideosByUser, \uD83C\uDF3F found: " + cnt);
        return cnt;
    }

    public List<ProjectPosition> findProjectPositionsByLocation(String organizationId, double latitude, double longitude, double radiusInKM) {
        LOGGER.info(E.DICE.concat(E.DICE).concat(" findProjectPositionsByLocation ..."));
        Point point = new Point(longitude, latitude);
        Distance distance = new Distance(radiusInKM, Metrics.KILOMETERS);

        List<ProjectPosition> positions = projectPositionRepository.findByPositionNear(point, distance);
        List<ProjectPosition> mPositions = new ArrayList<>();
        for (ProjectPosition position : positions) {
            if (position.getOrganizationId().equalsIgnoreCase(organizationId)) {
                mPositions.add(position);
            }
        }

        LOGGER.info(E.DOLPHIN.concat(E.DOLPHIN).concat(E.DOLPHIN)
                + " Nearby Projects found: " + mPositions.size() + " : " + E.RED_APPLE + " radius: " + radiusInKM);


        return mPositions;
    }

    public List<ProjectPosition> getProjectPositions(String projectId, String startDate, String endDate) {
        Criteria c = Criteria.where("projectId").is(projectId)
                .and("created").gte(startDate).lte(endDate);
        Query query = new Query(c);
        List<ProjectPosition> mList = mongoTemplate.find(query, ProjectPosition.class);

        LOGGER.info(E.LEAF.concat(E.LEAF).concat("ProjectPositions found: " + mList.size()));
        return mList;
    }

    public List<ProjectPolygon> getProjectPolygons(String projectId, String startDate, String endDate) {
        Criteria c = Criteria.where("projectId").is(projectId)
                .and("created").gte(startDate).lte(endDate);
        Query query = new Query(c);
        List<ProjectPolygon> mList = mongoTemplate.find(query, ProjectPolygon.class);

        LOGGER.info(E.LEAF.concat(E.LEAF).concat("ProjectPolygons found: " + mList.size()));
        return mList;
    }

    public List<ProjectPosition> getOrganizationProjectPositions(String organizationId, String startDate, String endDate) {
        Criteria c = Criteria.where("organizationId").is(organizationId)
                .and("created").gte(startDate).lte(endDate);
        Query query = new Query(c);
        List<ProjectPosition> mList = mongoTemplate.find(query, ProjectPosition.class);
        LOGGER.info(E.LEAF.concat(E.LEAF).concat(" OrgProjectPositions found: " + mList.size()
                + " for organizationId: " + organizationId));
        return mList;
    }

    public List<ProjectPolygon> getOrganizationProjectPolygons(String organizationId, String startDate, String endDate) {
        Criteria c = Criteria.where("organizationId").is(organizationId)
                .and("created").gte(startDate).lte(endDate);
        Query query = new Query(c);
        List<ProjectPolygon> mList = mongoTemplate.find(query, ProjectPolygon.class);

        LOGGER.info(E.LEAF.concat(E.LEAF).concat(" OrgProjectPolygons found: " + mList.size()
                + " for organizationId: " + organizationId));
        return mList;
    }

    //    public List<GeofenceEvent> getGeofenceEventsByUser(String userId)  {
//        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS).concat("getGeofenceEventsByUser: "
//                .concat(E.FLOWER_YELLOW)));
//
//        List<GeofenceEvent> events = geofenceEventRepository.findByUserId(userId);
//        LOGGER.info(E.LEAF.concat(E.LEAF).concat("GeofenceEvents found: " + events.size()));
//        return events;
//    }
    public List<GeofenceEvent> getGeofenceEventsByProjectPosition(String projectPositionId) {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS).concat("getGeofenceEventsByProjectPosition: "
                .concat(E.FLOWER_YELLOW)));

        List<GeofenceEvent> events = geofenceEventRepository.findByProjectPositionId(projectPositionId);
        LOGGER.info(E.LEAF.concat(E.LEAF).concat("GeofenceEvents found: " + events.size()));
        return events;
    }

    public List<City> getNearbyCities(double latitude, double longitude, double radiusInKM) {

        LOGGER.info(E.DICE.concat(E.DICE).concat(" getNearbyCities ..."));
        Point point = new Point(longitude, latitude);
        Distance distance = new Distance(radiusInKM, Metrics.KILOMETERS);
        GeoResults<City> cities = cityRepository.findByCityLocationNear(point, distance);
        LOGGER.info(E.DOLPHIN.concat(E.DOLPHIN).concat(E.DOLPHIN)
                + " Nearby Cities found: " + cities.getContent().size() + " : "
                + E.RED_APPLE + " radius: " + radiusInKM);
        List<City> mList = new ArrayList<>();
        for (GeoResult<City> city : cities) {
            mList.add(city.getContent());
        }
        return mList;
    }

    public List<Project> getOrganizationProjects(String organizationId, String startDate, String endDate) {

        Criteria c = Criteria.where("organizationId").is(organizationId)
                .and("created").gte(startDate).lte(endDate);
        Query query = new Query(c);
        List<Project> mList = mongoTemplate.find(query, Project.class);
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getOrganizationProjects ... found: " + mList.size()));

        return mList;
    }

    public List<SettingsModel> getOrganizationSettings(String organizationId) {

        List<SettingsModel> mList = settingsModelRepository.findByOrganizationId(organizationId);
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getOrganizationSettings ... found: " + mList.size()));

        return mList;
    }


    public List<User> getOrganizationUsers(String organizationId, String startDate, String endDate) {
        List<User> filteredList = new ArrayList<>();
        Criteria c = Criteria.where("organizationId").is(organizationId)
                .and("created").gte(startDate).lte(endDate);
        Query query = new Query(c);
        List<User> mList = mongoTemplate.find(query, User.class);
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getOrganizationUsers ... found: " + mList.size()));
        for (User user : mList) {
            if (user.getActive() == 0) {
                filteredList.add(user);
            }
        }
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getOrganizationUsers ... found after filtering for inactive users: " + filteredList.size()));
        return filteredList;
    }

    public List<Photo> getOrganizationPhotos(String organizationId, String startDate, String endDate) {

        Criteria c = Criteria.where("organizationId").is(organizationId)
                .and("created").gte(startDate).lte(endDate);
        Query query = new Query(c);
        List<Photo> mList = mongoTemplate.find(query, Photo.class);
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getOrganizationPhotos ... found: " + mList.size()));

        return mList;
    }

    public List<Video> getOrganizationVideos(String organizationId, String startDate, String endDate) {

        Criteria c = Criteria.where("organizationId").is(organizationId)
                .and("created").gte(startDate).lte(endDate);
        Query query = new Query(c);
        List<Video> mList = mongoTemplate.find(query, Video.class);
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getOrganizationVideos ... found: " + mList.size()));

        return mList;
    }

    public List<Audio> getOrganizationAudios(String organizationId, String startDate, String endDate) {

        Criteria c = Criteria.where("organizationId").is(organizationId)
                .and("created").gte(startDate).lte(endDate);
        Query query = new Query(c);
        List<Audio> mList = mongoTemplate.find(query, Audio.class);
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getOrganizationAudios ... found: " + mList.size()));

        return mList;
    }

    public List<User> getUsers() {

        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getUsers ..."));
        List<User> mList = userRepository.findAll();
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getUsers ... found: " + mList.size()));

        return mList;
    }

    public User getUserById(String userId) {

        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getUserById ..."));
        User user = userRepository.findByUserId(userId);
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getUserById ... found, active: " + user.getActive()));

        if (user.getActive() > 0) {
            return null;
        }

        return user;
    }

    public List<City> getCities() {

        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getCities ..."));

        List<City> mList = (List<City>) cityRepository.findAll();
        LOGGER.info(E.RED_CAR.concat(E.RED_CAR).concat("getCities ... found: " + mList.size()));

        return mList;
    }

    public List<Community> findCommunitiesByCountry(String countryId) {

        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("findCommunitiesByCountry ..."));

        List<Community> mList = communityRepository.findByCountryId(countryId);
        LOGGER.info(E.RED_CAR.concat(E.RED_CAR).concat("findCommunitiesByCountry ... found: " + mList.size()));

        return mList;
    }

    public List<Questionnaire> getQuestionnairesByOrganization(String organizationId) {

        List<Questionnaire> list = questionnaireRepository.findByOrganizationId(organizationId);
        return list;
    }

    public List<Project> findProjectsByOrganization(String organizationId) {

        List<Project> list = projectRepository.findByOrganizationId(organizationId);
        return list;
    }

    //
    public List<Country> getCountries() {

        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getCountries ..."));

        List<Country> mList = countryRepository.findAll();
        LOGGER.info(E.RED_CAR.concat(E.RED_CAR).concat("getCountries ... found: " + mList.size()));

        return mList;
    }


}
