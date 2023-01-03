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
    ProjectRepository projectRepository;
    @Autowired
    CommunityRepository communityRepository;
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
    ConditionRepository conditionRepository;

    @Autowired
    FieldMonitorScheduleRepository fieldMonitorScheduleRepository;

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

    public List<ProjectPosition> findProjectPositionsByLocation(double latitude, double longitude, double radiusInKM)  {
        LOGGER.info(E.DICE.concat(E.DICE).concat(" findProjectPositionsByLocation ..."));
        Point point = new Point(longitude, latitude);
        Distance distance = new Distance(radiusInKM, Metrics.KILOMETERS);
        List<ProjectPosition> positions = projectPositionRepository.findByPositionNear(point, distance);
        LOGGER.info(E.DOLPHIN.concat(E.DOLPHIN).concat(E.DOLPHIN)
                + " Nearby Projects found: " + positions.size() + " : " + E.RED_APPLE + " radius: " + radiusInKM);

        LOGGER.info(E.HEART_ORANGE.concat(E.HEART_ORANGE).concat(
                "findProjectsByLocation: Nearby ProjectPositions found: " + positions.size() + " \uD83C\uDF3F"));
        return positions;
    }

    public List<ProjectPosition> getProjectPositions(String projectId)  {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS).concat("getProjectPositions: "
                .concat(E.FLOWER_YELLOW)));

        List<ProjectPosition> m = projectPositionRepository.findByProjectId(projectId);

        LOGGER.info(E.LEAF.concat(E.LEAF).concat("ProjectPositions found: " + m.size()));
        return m;
    }
    public List<ProjectPosition> getOrganizationProjectPositions(String organizationId)  {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS).concat("getOrganizationProjectPositions: "
                .concat(E.FLOWER_YELLOW)));

        List<Project> projects = projectRepository.findByOrganizationId(organizationId);
        List<ProjectPosition> mList = new ArrayList<>();
        for (Project project : projects) {
            List<ProjectPosition> m = projectPositionRepository.findByProjectId(project.getProjectId());
            mList.addAll(m);
        }



        LOGGER.info(E.LEAF.concat(E.LEAF).concat("OrgProjectPositions found: " + mList.size()));
        return mList;
    }
    public List<GeofenceEvent> getGeofenceEventsByUser(String userId)  {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS).concat("getGeofenceEventsByUser: "
                .concat(E.FLOWER_YELLOW)));

        List<GeofenceEvent> events = geofenceEventRepository.findByUserId(userId);
        LOGGER.info(E.LEAF.concat(E.LEAF).concat("GeofenceEvents found: " + events.size()));
        return events;
    }
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

        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getOrganizationReports ..."));
        List<Project> mList = projectRepository.findByOrganizationId(organizationId);
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getOrganizationProjects ... found: " + mList.size()));

        return mList;
    }

    public List<User> getOrganizationUsers(String organizationId)  {

        List<User> mList = userRepository.findByOrganizationId(organizationId);
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getOrganizationUsers ... found: " + mList.size()));

        return mList;
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
    public List<User> getUsers()  {

        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getUsers ..."));
        List<User> mList = (List<User>) userRepository.findAll();
        LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("getUsers ... found: " + mList.size()));

        return mList;
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
