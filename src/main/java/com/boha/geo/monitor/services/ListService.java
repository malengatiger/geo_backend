package com.boha.geo.monitor.services;


import com.boha.geo.monitor.data.DataBag;
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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

;

@Service
public class ListService {
    public static final Logger LOGGER = LoggerFactory.getLogger(ListService.class.getSimpleName());
    private static final String xx = E.COFFEE+E.COFFEE+E.COFFEE;

    private static final Gson G = new GsonBuilder().setPrettyPrinting().create();
    @Autowired
    GeofenceEventRepository geofenceEventRepository;
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
    VideoRepository videoRepository;

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
    public List<FieldMonitorSchedule> getProjectFieldMonitorSchedules(String projectId)  {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS).concat("getProjectFieldMonitorSchedules: "
                .concat(E.FLOWER_YELLOW)));

        List<FieldMonitorSchedule> m = fieldMonitorScheduleRepository.findByProjectId(projectId);

        LOGGER.info(E.LEAF.concat(E.LEAF).concat("getProjectFieldMonitorSchedules found: " + m.size()));
        return m;
    }
    public List<FieldMonitorSchedule> getUserFieldMonitorSchedules(String userId)  {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS).concat("getUserFieldMonitorSchedules: "
                .concat(E.FLOWER_YELLOW)));

        List<FieldMonitorSchedule> m = fieldMonitorScheduleRepository.findByUserId(userId);

        LOGGER.info(E.LEAF.concat(E.LEAF).concat("getUserFieldMonitorSchedules found: " + m.size()));
        return m;
    }
    public List<FieldMonitorSchedule> getOrgFieldMonitorSchedules(String organizationId)  {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS).concat("getOrgFieldMonitorSchedules: "
                .concat(E.FLOWER_YELLOW)));

        List<FieldMonitorSchedule> m = fieldMonitorScheduleRepository.findByOrganizationId(organizationId);

        LOGGER.info(E.LEAF.concat(E.LEAF).concat("getOrgFieldMonitorSchedules found: " + m.size()));
        return m;
    }
    public List<FieldMonitorSchedule> getMonitorFieldMonitorSchedules(String userId)  {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS).concat("getMonitorFieldMonitorSchedules: "
                .concat(E.FLOWER_YELLOW)));

        List<FieldMonitorSchedule> m = fieldMonitorScheduleRepository.findByFieldMonitorId(userId);

        LOGGER.info(E.LEAF.concat(E.LEAF).concat("getMonitorFieldMonitorSchedules found: " + m.size()));
        return m;
    }
    public List<FieldMonitorSchedule> getAdminFieldMonitorSchedules(String userId)  {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS).concat("getOrgFieldMonitorSchedules: "
                .concat(E.FLOWER_YELLOW)));

        List<FieldMonitorSchedule> m = fieldMonitorScheduleRepository.findByAdminId(userId);

        LOGGER.info(E.LEAF.concat(E.LEAF).concat("getOrgFieldMonitorSchedules found: " + m.size()));
        return m;
    }

    public List<Organization> getOrganizations()  {

        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getOrganizations ..."));
        List<Organization> mList = (List<Organization>) organizationRepository.findAll();
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getOrganizations ... found: " + mList.size()));
        return mList;
    }

    public List<Community> getCommunities()  {

        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getCommunities ..."));
        List<Community> mList = (List<Community>) communityRepository.findAll();
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getCommunities ... found: " + mList.size()));

        return mList;
    }

    public List<Project> getProjects()  {

        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("ListService: getProjects ..."));
        List<Project> mList = (List<Project>) projectRepository.findAll();

        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("ListService: getProjects ... found:" +
                " \uD83D\uDC24 " + mList.size()));

        return mList;
    }


    public List<Organization> getCountryOrganizations(String countryId)  {

        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getOrganizations ..."));
        List<Organization> mList = organizationRepository.findByCountryId(countryId);
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getOrganizations ... found: " + mList.size()));

        return mList;
    }

    public List<Photo> getProjectPhotos(String projectId)  {

        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getProjectPhotos ..."));
        List<Photo> mList = photoRepository.findByProjectId(projectId);
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getProjectPhotos ... found: " + mList.size()));

        return mList;
    }

    public DataBag getUserData(String userId)  {
        DataBag bag = new DataBag();
        User user = userRepository.findByUserId(userId);
        List<Project> projects = projectRepository.findByOrganizationId(user.getOrganizationId());
        List<Photo> photos = getUserProjectPhotos(userId);
        List<Video> videos = getUserProjectVideos(userId);
        List<ProjectPosition> projectPositions = getOrganizationProjectPositions(user.getOrganizationId());
        List<ProjectPolygon> projectPolygons = getOrganizationProjectPolygons(user.getOrganizationId());
        List<FieldMonitorSchedule> fieldMonitorSchedules = getOrgFieldMonitorSchedules(user.getOrganizationId());
        List<User> users = getOrganizationUsers(user.getOrganizationId());

        bag.setDate(DateTime.now().toDateTimeISO().toString());
        bag.setProjects(projects);
        bag.setFieldMonitorSchedules(fieldMonitorSchedules);
        bag.setProjectPositions(projectPositions);
        bag.setProjectPolygons(projectPolygons);
        bag.setPhotos(photos);
        bag.setVideos(videos);
        bag.setUsers(users);

        LOGGER.info(E.RED_APPLE+" Project data found: photos: " + bag.getPhotos().size() + " videos: " + bag.getVideos().size()
                + " schedules: " + bag.getFieldMonitorSchedules().size());

        return bag;
    }

    public DataBag getProjectData(String projectId)  {
        DataBag bag = new DataBag();
        Project project = projectRepository.findByProjectId(projectId);
        List<Project> projects = new ArrayList<>();
        projects.add(project);

        List<Photo> photos = getProjectPhotos(projectId);
        List<Video> videos = getProjectVideos(projectId);
        List<Audio> audios = getProjectAudios(projectId);
        List<ProjectPosition> projectPositions = getProjectPositions(projectId);
        List<ProjectPolygon> projectPolygons = getProjectPolygons(projectId);
        List<FieldMonitorSchedule> fieldMonitorSchedules = getProjectFieldMonitorSchedules(projectId);
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

        LOGGER.info(E.RED_APPLE+" Project data found: photos: " + bag.getPhotos().size() + " videos: " + bag.getVideos().size()
        + " schedules: " + bag.getFieldMonitorSchedules().size() + " polygons: " + bag.getProjectPolygons().size());

        return bag;
    }
    public DataBag getOrganizationData(String organizationId)  {
        DataBag bag = new DataBag();
        List<Project> projects = getOrganizationProjects(organizationId);
        List<Photo> photos = getOrganizationPhotos(organizationId);
        List<Video> videos = getOrganizationVideos(organizationId);
        List<Audio> audios = getOrganizationAudios(organizationId);

        List<ProjectPosition> projectPositions = getOrganizationProjectPositions(organizationId);
        List<FieldMonitorSchedule> fieldMonitorSchedules = getOrgFieldMonitorSchedules(organizationId);
        List<ProjectPolygon> polygons = getOrganizationProjectPolygons(organizationId);

        List<User> users = getOrganizationUsers(organizationId);

        bag.setDate(DateTime.now().toDateTimeISO().toString());
        bag.setProjects(projects);
        bag.setFieldMonitorSchedules(fieldMonitorSchedules);
        bag.setProjectPositions(projectPositions);
        bag.setProjectPolygons(polygons);
        bag.setPhotos(photos);
        bag.setVideos(videos);
        bag.setAudios(audios);
        bag.setUsers(users);

        LOGGER.info(E.RED_APPLE+" Organization data found: photos: " + bag.getPhotos().size() + " videos: " + bag.getVideos().size()
                + " schedules: " + bag.getFieldMonitorSchedules().size() + " polygons: " + bag.getProjectPolygons().size());

        return bag;
    }

        public List<Photo> getUserProjectPhotos(String userId)  {

        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getUserProjectPhotos ...userId: " + userId));
        List<Photo> mList = photoRepository.findByUserId(userId);
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getUserProjectPhotos ... found: " + mList.size()));

        return mList;
    }
    public List<Video> getUserProjectVideos(String userId)  {

        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getUserProjectVideos...userId: " + userId));
        List<Video> mList = videoRepository.findByUserId(userId);
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getUserProjectVideos ... found: " + mList.size()));

        return mList;
    }

    public List<Video> getProjectVideos(String projectId)  {

        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getProjectVideos ..."));
        List<Video> mList = videoRepository.findByProjectId(projectId);
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getProjectVideos ... found: " + mList.size()));

        return mList;
    }
    public List<Audio> getProjectAudios(String projectId)  {

        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getProjectAudios ..."));
        List<Audio> mList = audioRepository.findByProjectId(projectId);
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getProjectAudios ... found: " + mList.size()));

        return mList;
    }
    public List<SettingsModel> getProjectSettings(String projectId)  {

        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getProjectSettings ..."));
        List<SettingsModel> mList = settingsModelRepository.findByProjectId(projectId);
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getProjectSettings ... found: " + mList.size()));

        return mList;
    }

    public List<Condition> getProjectConditions(String projectId)  {

        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getProjectConditions ..."));
        List<Condition> mList = conditionRepository.findByProjectId(projectId);
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getProjectConditions ... found: " + mList.size()));

        return mList;
    }

    @Autowired
    ProjectPositionRepository projectPositionRepository;

    public List<Project> findProjectsByLocation(double latitude, double longitude, double radiusInKM)  {
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
            Project p = projectRepository.findByProjectId(projectPosition.getProjectId());
            fList.add(p);
        }
        LOGGER.info(E.HEART_ORANGE.concat(E.HEART_ORANGE).concat(
                "findProjectsByLocation: Nearby Projects found: " + projects.size() + " \uD83C\uDF3F"));
        return fList;
    }

    public List<City> findCitiesByLocation(double latitude, double longitude, double radiusInKM)  {
        LOGGER.info(E.DICE.concat(E.DICE).concat(" findCitiesByLocation ... radiusInKM: " + radiusInKM));
        Point point = new Point(longitude, latitude);
        Distance distance = new Distance(radiusInKM, Metrics.KILOMETERS);
        GeoResults<City> cities = cityRepository.findByCityLocationNear(point, distance);
        LOGGER.info(E.DOLPHIN.concat(E.DOLPHIN).concat(E.DOLPHIN)
                + " Nearby Cities found: "+ E.RED_APPLE + E.RED_APPLE + cities.getContent().size() + " : "
                + E.RED_APPLE+ E.RED_APPLE + " radiusInKM: " + radiusInKM);

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

    public ProjectCount getCountsByProject(String projectId) {

        int photos = photoRepository.findByProjectId(projectId).size();
        int videos = videoRepository.findByProjectId(projectId).size();
        val pc = new ProjectCount();
        pc.setPhotos(photos);
        pc.setDate(new DateTime().toDateTimeISO().toString());
        pc.setVideos(videos);

        LOGGER.info(E.HEART_ORANGE.concat(E.HEART_ORANGE)
                + " getCountsByProject, \uD83C\uDF3F found: " + G.toJson(pc));
        return pc;
    }
    public UserCount getCountsByUser(String userId) {

        List<Photo> photos = photoRepository.findByUserId(userId);
        List<Video> videos = videoRepository.findByUserId(userId);

        HashMap<String,String> map = new HashMap<>();
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

    public List<ProjectPosition> findProjectPositionsByLocation(String organizationId, double latitude, double longitude, double radiusInKM)  {
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

    public List<ProjectPosition> getProjectPositions(String projectId)  {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS).concat("getProjectPositions: "
                .concat(E.FLOWER_YELLOW)));

        List<ProjectPosition> m = projectPositionRepository.findByProjectId(projectId);

        LOGGER.info(E.LEAF.concat(E.LEAF).concat("ProjectPositions found: " + m.size()));
        return m;
    }
    public List<ProjectPolygon> getProjectPolygons(String projectId)  {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS).concat("getProjectPolygons: "
                .concat(E.FLOWER_YELLOW)));

        List<ProjectPolygon> m = projectPolygonRepository.findByProjectId(projectId);

        LOGGER.info(E.LEAF.concat(E.LEAF).concat("ProjectPolygons found: " + m.size()));
        return m;
    }

    public List<ProjectPosition> getOrganizationProjectPositions(String organizationId)  {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS).concat("getOrganizationProjectPositions: "
                .concat(E.FLOWER_YELLOW)));

        List<ProjectPosition> mList = projectPositionRepository.findByOrganizationId(organizationId);

        LOGGER.info(E.LEAF.concat(E.LEAF).concat(" OrgProjectPositions found: " + mList.size()
                + " for organizationId: " + organizationId));
        return mList;
    }
    public List<ProjectPolygon> getOrganizationProjectPolygons(String organizationId)  {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS).concat("getOrganizationProjectPolygons: "
                .concat(E.FLOWER_YELLOW)));

        List<ProjectPolygon> mList = projectPolygonRepository.findByOrganizationId(organizationId);

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
    public List<GeofenceEvent> getGeofenceEventsByProjectPosition(String projectPositionId)  {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS).concat("getGeofenceEventsByProjectPosition: "
                .concat(E.FLOWER_YELLOW)));

        List<GeofenceEvent> events = geofenceEventRepository.findByProjectPositionId(projectPositionId);
        LOGGER.info(E.LEAF.concat(E.LEAF).concat("GeofenceEvents found: " + events.size()));
        return events;
    }

    public List<City> getNearbyCities(double latitude, double longitude, double radiusInKM)  {

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

    public List<Project> getOrganizationProjects(String organizationId)  {

        List<Project> mList = projectRepository.findByOrganizationId(organizationId);
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getOrganizationProjects ... found: " + mList.size()));

        return mList;
    }

    public List<SettingsModel> getOrganizationSettings(String organizationId)  {

        List<SettingsModel> mList = settingsModelRepository.findByOrganizationId(organizationId);
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getOrganizationSettings ... found: " + mList.size()));

        return mList;
    }


    public List<User> getOrganizationUsers(String organizationId)  {
        List<User> filteredList = new ArrayList<>();
        List<User> mList = userRepository.findByOrganizationId(organizationId);
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getOrganizationUsers ... found: " + mList.size()));
        for (User user : mList) {
            if (user.getActive() == 0) {
                filteredList.add(user);
            }
        }
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getOrganizationUsers ... found after filtering for inactive users: " + filteredList.size()));
        return filteredList;
    }
    public List<Photo> getOrganizationPhotos(String organizationId)  {

        List<Photo> mList = photoRepository.findByOrganizationId(organizationId);
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getOrganizationPhotos ... found: " + mList.size()));

        return mList;
    }
    public List<Video> getOrganizationVideos(String organizationId)  {

        List<Video> mList = videoRepository.findByOrganizationId(organizationId);
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getOrganizationVideos ... found: " + mList.size()));

        return mList;
    }
    public List<Audio> getOrganizationAudios(String organizationId)  {

        List<Audio> mList = audioRepository.findByOrganizationId(organizationId);
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getOrganizationAudios ... found: " + mList.size()));

        return mList;
    }
    public List<User> getUsers()  {

        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getUsers ..."));
        List<User> mList = userRepository.findAll();
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getUsers ... found: " + mList.size()));

        return mList;
    }
    public User getUserById(String userId)  {

        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getUserById ..."));
        User user = userRepository.findByUserId(userId);
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getUserById ... found, active: " + user.getActive()));
        if (user.getActive() > 0) {
            return null;
        }

        return user;
    }

    public List<City> getCities()  {

        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getCities ..."));

        List<City> mList = (List<City>) cityRepository.findAll();
        LOGGER.info(E.RED_CAR.concat(E.RED_CAR).concat("getCities ... found: " + mList.size()));

        return mList;
    }

    public List<Community> findCommunitiesByCountry(String countryId)  {

        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("findCommunitiesByCountry ..."));

        List<Community> mList = communityRepository.findByCountryId(countryId);
        LOGGER.info(E.RED_CAR.concat(E.RED_CAR).concat("findCommunitiesByCountry ... found: " + mList.size()));

        return mList;
    }

    public List<Questionnaire> getQuestionnairesByOrganization(String organizationId)  {

        List<Questionnaire> list = questionnaireRepository.findByOrganizationId(organizationId);
        return list;
    }

    public List<Project> findProjectsByOrganization(String organizationId)  {

        List<Project> list = projectRepository.findByOrganizationId(organizationId);
        return list;
    }

    //
    public List<Country> getCountries()  {

        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getCountries ..."));

        List<Country> mList = countryRepository.findAll();
        LOGGER.info(E.RED_CAR.concat(E.RED_CAR).concat("getCountries ... found: " + mList.size()));

        return mList;
    }


}
