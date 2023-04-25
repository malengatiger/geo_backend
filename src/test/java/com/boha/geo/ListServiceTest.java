package com.boha.geo;

import com.boha.geo.monitor.data.*;
import com.boha.geo.monitor.services.DataService;
import com.boha.geo.monitor.services.ListService;
import com.boha.geo.monitor.services.MessageService;
import com.boha.geo.repos.*;
import com.boha.geo.services.MailService;
import com.boha.geo.util.E;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ListServiceTest {
    public static final Logger LOGGER = LoggerFactory.getLogger(ListServiceTest.class.getSimpleName());


    @Mock
    private Environment env = Mockito.mock(Environment.class);
    @Mock
    private AppErrorRepository appErrorRepository = Mockito.mock(AppErrorRepository.class);

    @Mock
    private GeofenceEventRepository geofenceEventRepository = Mockito.mock(GeofenceEventRepository.class);

    @Mock
    private SettingsModelRepository settingsModelRepository = Mockito.mock(SettingsModelRepository.class);

    @Mock
    private RatingRepository ratingRepository = Mockito.mock(RatingRepository.class);

    @Mock
    private MongoTemplate mongoTemplate = Mockito.mock(MongoTemplate.class);

    @Mock
    private AudioRepository audioRepository = Mockito.mock(AudioRepository.class);
    @Mock
    private ProjectRepository projectRepository = Mockito.mock(ProjectRepository.class);
    @Mock
    private LocationResponseRepository locationResponseRepository = Mockito.mock(LocationResponseRepository.class);
    @Mock
    private LocationRequestRepository locationRequestRepository = Mockito.mock(LocationRequestRepository.class);

    @Mock
    private ProjectPolygonRepository projectPolygonRepository = Mockito.mock(ProjectPolygonRepository.class);

    @Mock
    private CityRepository cityRepository = Mockito.mock(CityRepository.class);
    @Mock
    private PhotoRepository photoRepository = Mockito.mock(PhotoRepository.class);
    @Mock
    private ActivityModelRepository activityModelRepository = Mockito.mock(ActivityModelRepository.class);

    @Mock
    private ProjectAssignmentRepository projectAssignmentRepository = Mockito.mock(ProjectAssignmentRepository.class);
    @Mock
    private VideoRepository videoRepository = Mockito.mock(VideoRepository.class);
    @Mock
    private UserRepository userRepository = Mockito.mock(UserRepository.class);

    @Mock
    private CommunityRepository communityRepository = Mockito.mock(CommunityRepository.class);
    @Mock
    private ConditionRepository conditionRepository = Mockito.mock(ConditionRepository.class);
    @Mock
    private CountryRepository countryRepository = Mockito.mock(CountryRepository.class);
    @Mock
    private OrganizationRepository organizationRepository = Mockito.mock(OrganizationRepository.class);
    @Mock
    private ProjectPositionRepository projectPositionRepository = Mockito.mock(ProjectPositionRepository.class);
    @Mock
    private OrgMessageRepository orgMessageRepository = Mockito.mock(OrgMessageRepository.class);
    @Mock
    private MessageService messageService = Mockito.mock(MessageService.class);
    @Mock
    private FieldMonitorScheduleRepository fieldMonitorScheduleRepository = Mockito.mock(FieldMonitorScheduleRepository.class);

    @Mock
    private ProjectSummaryRepository projectSummaryRepository = Mockito.mock(ProjectSummaryRepository.class);
    @Mock
    private MailService mailService = Mockito.mock(MailService.class);

    @Mock
    private MonitorReportRepository monitorReportRepository = Mockito.mock(MonitorReportRepository.class);

    @Mock
    private QuestionnaireRepository questionnaireRepository = Mockito.mock(QuestionnaireRepository.class);

    @Mock
    private DataService dataService = Mockito.mock(DataService.class);
    @InjectMocks
    private ListService listService;

    @Before
    public void initService() {
        listService = new ListService(geofenceEventRepository,appErrorRepository,activityModelRepository,
                projectSummaryRepository,countryRepository,cityRepository,organizationRepository,
                projectPolygonRepository,projectRepository,communityRepository,
                settingsModelRepository,userRepository,monitorReportRepository,questionnaireRepository,photoRepository,
                projectAssignmentRepository,videoRepository,dataService,audioRepository,conditionRepository,
                fieldMonitorScheduleRepository, mongoTemplate);
    }
    static final String fakeId = "1234567890";
    static final String mm = E.RED_APPLE+E.RED_APPLE+E.RED_APPLE;

    static final String organizationId = "12345";
    static final String startDate = DateTime.now().toString();
    static final String endDate = DateTime.now().toString();
    static final String projectId = "12345";
    static final String userId = "12345";
    static final String projectPositionId = "975";
    static final String email = "aub@email.com";
    static final String countryId = "975";
    static final String photoId = "975";
    static final String audioId = "975";
    static final String videoId = "975";
    static final int hours = 2;
    static final double latitude = 0.0, longitude = 0.0, radiusInKM = 0.0;

//    @Test
//    public void getOrganizationProjectPositions()  throws Exception {
//        List<ProjectPosition>  list = new ArrayList<>();
//        Mockito.when(listService.getOrganizationProjectPositions (organizationId) ).thenReturn(list);
//        Assert.assertEquals(0, list.size());
//        LOGGER.info(mm +"ListService: getOrganizationProjectPositions");
//    }

    @Test
    public void getOrganizationProjectPositionsByPeriod( )   throws Exception {
        List<ProjectPosition>  list = new ArrayList<>();

        Mockito.when(listService.getOrganizationProjectPositions (organizationId, startDate, endDate) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getOrganizationProjectPositions");
    }

    @Test
    public void getOrganizationProjectPolygons( )  throws Exception {
        List<ProjectPolygon>  list = new ArrayList<>();
        Mockito.when(listService.getOrganizationProjectPolygons (organizationId) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getOrganizationProjectPolygons");
    }

    @Test
    public void getOrganizationProjectPolygonsByPeriod( )   throws Exception {
        List<ProjectPolygon>  list = new ArrayList<>();
        Mockito.when(listService.getOrganizationProjectPolygons (organizationId, startDate, endDate) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getOrganizationProjectPolygons");
    }

    @Test
    public void getOrgFieldMonitorSchedulesByPeriod( )   throws Exception {
        List<FieldMonitorSchedule>  list = new ArrayList<>();
        Mockito.when(listService.getOrgFieldMonitorSchedules (organizationId, startDate, endDate) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getOrgFieldMonitorSchedules");
    }

    @Test
    public void getProjectFieldMonitorSchedules()   throws Exception {
        List<FieldMonitorSchedule>  list = new ArrayList<>();
        Mockito.when(listService.getProjectFieldMonitorSchedules (projectId, startDate, endDate) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getProjectFieldMonitorSchedules");
    }

    @Test
    public void getOrganizationProjectAssignments()  throws Exception {
        List<ProjectAssignment>  list = new ArrayList<>();
        Mockito.when(listService.getOrganizationProjectAssignments (organizationId) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getOrganizationProjectAssignments");
    }

    @Test
    public void getUserFieldMonitorSchedules( )  throws Exception {
        List<FieldMonitorSchedule>  list = new ArrayList<>();
        Mockito.when(listService.getUserFieldMonitorSchedules (userId) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getUserFieldMonitorSchedules");
    }

    @Test
    public void getMonitorFieldMonitorSchedules()  throws Exception {
        List<FieldMonitorSchedule>  list = new ArrayList<>();
        Mockito.when(listService.getMonitorFieldMonitorSchedules (userId) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getMonitorFieldMonitorSchedules");
    }

    @Test
    public void getAdminFieldMonitorSchedules( )  throws Exception {
        List<FieldMonitorSchedule>  list = new ArrayList<>();
        Mockito.when(listService.getAdminFieldMonitorSchedules (userId) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getAdminFieldMonitorSchedules");
    }

//    @Test
//    public void getOrganizationDataZippedFile( )   throws Exception {
//        File  file = new File("data.zip");
//        Mockito.when(listService.getOrganizationDataZippedFile (organizationId, startDate, endDate) ).thenReturn(file);
//        Assert.assertNotNull(file);
//        LOGGER.info(mm +"ListService: getOrganizationDataZippedFile");
//    }

    @Test
    public void getOrganizationActivityPeriod( )   throws Exception {
        List<ActivityModel>  list = new ArrayList<>();
        Mockito.when(listService.getOrganizationActivityPeriod (organizationId, startDate, endDate) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getOrganizationActivityPeriod");
    }

//    @Test
//    public void findProjectPositionsByLocation()   throws Exception {
//        List<ProjectPosition>  list = new ArrayList<>();
//        Mockito.when(listService.findProjectPositionsByLocation (
//                organizationId, latitude, longitude, radiusInKM) ).thenReturn(list);
//        Assert.assertEquals(0, list.size());
//        LOGGER.info(mm +"ListService: findProjectPositionsByLocation");
//    }

    @Test
    public void getGeofenceEventsByProjectPosition()  throws Exception {
        List<GeofenceEvent>  list = new ArrayList<>();
        Mockito.when(listService.getGeofenceEventsByProjectPosition (projectPositionId) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getGeofenceEventsByProjectPosition");
    }

    @Test
    public void getQuestionnairesByOrganization()  throws Exception {
        List<Questionnaire>  list = new ArrayList<>();
        Mockito.when(listService.getQuestionnairesByOrganization (organizationId) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getQuestionnairesByOrganization");
    }

    @Test
    public void findAppErrorsByOrganization()  throws Exception {
        List<AppError>  list = new ArrayList<>();
        Mockito.when(listService.findAppErrorsByOrganization (organizationId) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: findAppErrorsByOrganization");
    }

    @Test
    public void findUserByEmail()  throws Exception {
        User  user = new User();
        Mockito.when(listService.findUserByEmail (email) ).thenReturn(user);
        Assert.assertNotNull(user);
        LOGGER.info(mm +"ListService: findUserByEmail");
    }

    @Test
    public void getCountryOrganizations()  throws Exception {
        List<Organization>  list = new ArrayList<>();
        Mockito.when(listService.getCountryOrganizations (countryId) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getCountryOrganizations");
    }

    @Test
    public void getOrganizationActivity()   throws Exception {
        List<ActivityModel>  list = new ArrayList<>();
        Mockito.when(listService.getOrganizationActivity (organizationId, hours) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getOrganizationActivity");
    }

    @Test
    public void getProjectActivity()   throws Exception {
        List<ActivityModel>  list = new ArrayList<>();
        Mockito.when(listService.getProjectActivity (projectId, hours) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getProjectActivity");
    }

    @Test
    public void getUserActivity( )   throws Exception {
        List<ActivityModel>  list = new ArrayList<>();
        Mockito.when(listService.getUserActivity (userId, hours) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getUserActivity");
    }

    @Test
    public void findPhotoById()  throws Exception {
        Photo  photo = new Photo();
        Mockito.when(listService.findPhotoById (photoId) ).thenReturn(photo);
        Assert.assertNotNull(photo);
        LOGGER.info(mm +"ListService: findPhotoById");
    }

    @Test
    public void findVideoById( )  throws Exception {
        Video  video = new Video();
        Mockito.when(listService.findVideoById (videoId) ).thenReturn(video);
        Assert.assertNotNull( video);
        LOGGER.info(mm +"ListService: findVideoById");
    }

    @Test
    public void findAudioById( )  throws Exception {
        Audio  list = new Audio();
        Mockito.when(listService.findAudioById (audioId) ).thenReturn(list);
        Assert.assertNotNull(list);
        LOGGER.info(mm +"ListService: findAudioById");
    }

    @Test
    public void getOrganizationSummary( )   throws Exception {
        List<ProjectSummary>  list = new ArrayList<>();
        Mockito.when(listService.getOrganizationSummary (organizationId, startDate, endDate) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getOrganizationSummary");
    }



//    @Test
//    public void getProjectDataZippedFile()   throws Exception {
//        File  file = new File("data.zip");
//        Mockito.when(listService.getProjectDataZippedFile (projectId, startDate, endDate) ).thenReturn(file);
//        Assert.assertNotNull(file);
//        LOGGER.info(mm +"ListService: getProjectDataZippedFile");
//    }

//    @Test
//    public void getUserDataZippedFile( )   throws Exception {
//        File zipFile = new File("data.zip");
//        Mockito.when(listService.getUserDataZippedFile (userId, startDate, endDate) ).thenReturn(zipFile);
//        Assert.assertNotNull(zipFile);
//        LOGGER.info(mm +"ListService: getUserDataZippedFile");
//    }

    @Test
    public void getProjectSummaries()   throws Exception {
        List<ProjectSummary>  list = new ArrayList<>();
        Mockito.when(listService.getProjectSummaries (projectId, startDate, endDate) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getProjectSummaries");
    }

    @Test
    public void getProjectActivityPeriod()   throws Exception {
        List<ActivityModel>  list = new ArrayList<>();
        Mockito.when(listService.getProjectActivityPeriod (projectId, startDate, endDate) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getProjectActivityPeriod");
    }

    @Test
    public void getOrganizationSummaries( )   throws Exception {
        List<ProjectSummary>  list = new ArrayList<>();
        Mockito.when(listService.getOrganizationSummaries (organizationId, startDate, endDate) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getOrganizationSummaries");
    }

    @Test
    public void getUserActivityPeriod( )   throws Exception {
        List<ActivityModel>  list = new ArrayList<>();
        Mockito.when(listService.getUserActivityPeriod (userId, startDate, endDate) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getUserActivityPeriod");
    }

    @Test
    public void getUserProjectAudios( )   throws Exception {
        List<Audio>  list = new ArrayList<>();
        Mockito.when(listService.getUserProjectAudios (userId, startDate, endDate) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getUserProjectAudios");
    }

    @Test
    public void getProjectConditions()   throws Exception {
        List<Condition>  list = new ArrayList<>();
        Mockito.when(listService.getProjectConditions (projectId, startDate, endDate) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getProjectConditions");
    }

//    @Test
//    public void findProjectsByLocation( )   throws Exception {
//        List<Project>  list = new ArrayList<>();
//        Mockito.when(listService.findProjectsByLocation (organizationId, latitude, longitude, radiusInKM) ).thenReturn(list);
//        Assert.assertEquals(0, list.size());
//        LOGGER.info(mm +"ListService: findProjectsByLocation");
//    }

    @Spy List<City> cities = new ArrayList<>();
//    @Test
//    public void findCitiesByLocation()   throws Exception {
//
//        City city = new City();
//        city.setName("TestCity");
//        cities.add(city);
//        Mockito.when(listService.findCitiesByLocation (latitude, longitude, radiusInKM) ).thenReturn(cities);
//        Assert.assertEquals(1, cities.size());
//        LOGGER.info(mm +"ListService: findCitiesByLocation");
//    }

//    @Test
//    public void countPhotosByProject( )  throws Exception {
//        int  count = 0;
//        Mockito.when(listService.countPhotosByProject (projectId) ).thenReturn(count);
//        Assert.assertEquals(0, count);
//        LOGGER.info(mm +"ListService: countPhotosByProject");
//    }
//
//    @Test
//    public void getCountsByProject()  throws Exception {
//        ProjectSummary  projectSummary = new ProjectSummary();
//        Mockito.when(listService.getCountsByProject (projectId) ).thenReturn(projectSummary);
//        Assert.assertNotNull(projectSummary);
//        LOGGER.info(mm +"ListService: getCountsByProject");
//    }
//
//    @Test
//    public void getCountsByUser()  throws Exception {
//        UserCount  count = new UserCount();
//        Mockito.when(listService.getCountsByUser (userId) ).thenReturn(count);
//        Assert.assertNotNull(count);
//        LOGGER.info(mm +"ListService: getCountsByUser");
//    }
//
//    @Test
//    public void countVideosByProject( )  throws Exception {
//        int  count = 0;
//        Mockito.when(listService.countVideosByProject (projectId) ).thenReturn(count);
//        Assert.assertEquals(0, count);
//        LOGGER.info(mm +"ListService: countVideosByProject");
//    }
//
//    @Test
//    public void countPhotosByUser()  throws Exception {
//        int  count = 0;
//        Mockito.when(listService.countPhotosByUser (userId) ).thenReturn(count);
//        Assert.assertEquals(0, count);
//        LOGGER.info(mm +"ListService: countPhotosByUser");
//    }
//
//    @Test
//    public void countVideosByUser()  throws Exception {
//        int  count = 0;
//        Mockito.when(listService.countVideosByUser (userId) ).thenReturn(count);
//        Assert.assertEquals(0, count);
//        LOGGER.info(mm +"ListService: countVideosByUser");
//    }

//    @Test
//    public void getNearbyCities( )   throws Exception {
//        List<City>  list = new ArrayList<>();
//        Mockito.when(listService.getNearbyCities (latitude, longitude, radiusInKM) ).thenReturn(list);
//        Assert.assertEquals(0, list.size());
//        LOGGER.info(mm +"ListService: getNearbyCities");
//    }

    @Test
    public void getAllOrganizationProjects()  throws Exception {
        List<Project>  list = new ArrayList<>();
        Mockito.when(listService.getAllOrganizationProjects (organizationId) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getAllOrganizationProjects");
    }

    @Test
    public void getOrganizationSettings()  throws Exception {
        List<SettingsModel>  list = new ArrayList<>();
        Mockito.when(listService.getOrganizationSettings (organizationId) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getOrganizationSettings");
    }

    @Test
    public void getUserById()  throws Exception {
        User  user = new User();
        Mockito.when(listService.getUserById (userId) ).thenReturn(user);
        Assert.assertNotNull(user);
        LOGGER.info(mm +"ListService: getUserById");
    }

    @Test
    public void findCommunitiesByCountry( )  throws Exception {
        List<Community>  list = new ArrayList<>();
        Mockito.when(listService.findCommunitiesByCountry (countryId) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: findCommunitiesByCountry");
    }

    @Test
    public void findProjectsByOrganization()  throws Exception {
        List<Project>  list = new ArrayList<>();
        Mockito.when(listService.findProjectsByOrganization (organizationId) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: findProjectsByOrganization");
    }

    @Test
    public void findAppErrorsByUser()  throws Exception {
        List<AppError>  list = new ArrayList<>();
        Mockito.when(listService.findAppErrorsByUser (userId) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: findAppErrorsByUser");
    }

    @Test
    public void getUserProjectPhotos()  throws Exception {
        List<Photo>  list = new ArrayList<>();
        Mockito.when(listService.getUserProjectPhotos (userId) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getUserProjectPhotos");
    }

    @Test
    public void getUserProjectVideos()  throws Exception {
        List<Video>  list = new ArrayList<>();
        Mockito.when(listService.getUserProjectVideos (userId) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getUserProjectVideos");
    }

    @Test
    public void getUserProjectAssignments()  throws Exception {
        List<ProjectAssignment>  list = new ArrayList<>();
        Mockito.when(listService.getUserProjectAssignments (userId) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getUserProjectAssignments");
    }

    @Test
    public void getOrganizationUsersByPeriod( )   throws Exception {
        List<User>  list = new ArrayList<>();
        Mockito.when(listService.getOrganizationUsers (organizationId, startDate, endDate) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getOrganizationUsers");
    }

    @Test
    public void getOrganizationUsers()  throws Exception {
        List<User>  list = new ArrayList<>();
        Mockito.when(listService.getOrganizationUsers (organizationId) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getOrganizationUsers");
    }

    @Test
    public void getProjectPhotos()   throws Exception {
        List<Photo>  list = new ArrayList<>();
        Mockito.when(listService.getProjectPhotos (projectId, startDate, endDate) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getProjectPhotos");
    }

    @Test
    public void getProjectVideos()   throws Exception {
        List<Video>  list = new ArrayList<>();
        Mockito.when(listService.getProjectVideos (projectId, startDate, endDate) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getProjectVideos");
    }

    @Test
    public void getProjectAudios()   throws Exception {
        List<Audio>  list = new ArrayList<>();
        Mockito.when(listService.getProjectAudios (projectId, startDate, endDate) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getProjectAudios");
    }

    @Test
    public void getProjectAssignments()   throws Exception {
        List<ProjectAssignment>  list = new ArrayList<>();
        Mockito.when(listService.getProjectAssignments (projectId, startDate, endDate) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getProjectAssignments");
    }

    @Test
    public void getProjectPositions()   throws Exception {
        List<ProjectPosition>  list = new ArrayList<>();
        Mockito.when(listService.getProjectPositions (projectId, startDate, endDate) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getProjectPositions");
    }

    @Test
    public void getProjectPolygons()   throws Exception {
        List<ProjectPolygon>  list = new ArrayList<>();
        Mockito.when(listService.getProjectPolygons (projectId, startDate, endDate) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getProjectPolygons");
    }

    @Test
    public void getProjectSettings( )  throws Exception {
        List<SettingsModel>  list = new ArrayList<>();
        Mockito.when(listService.getProjectSettings (projectId) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getProjectSettings");
    }

    @Test
    public void getOrganizationProjects( )   throws Exception {
        List<Project>  list = new ArrayList<>();
        Mockito.when(listService.getOrganizationProjects (organizationId, startDate, endDate) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getOrganizationProjects");
    }

    @Test
    public void getOrganizationPhotos( )   throws Exception {
        List<Photo>  list = new ArrayList<>();
        Mockito.when(listService.getOrganizationPhotos (organizationId, startDate, endDate) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getOrganizationPhotos");
    }

    @Test
    public void getOrganizationVideos( )   throws Exception {
        List<Video>  list = new ArrayList<>();
        Mockito.when(listService.getOrganizationVideos (organizationId, startDate, endDate) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getOrganizationVideos");
    }

    @Test
    public void getOrganizationAudios( )   throws Exception {
        List<Audio>  list = new ArrayList<>();
        Mockito.when(listService.getOrganizationAudios (organizationId, startDate, endDate) ).thenReturn(list);
        Assert.assertEquals(0, list.size());
        LOGGER.info(mm +"ListService: getOrganizationAudios");
    }

//    @Test
//    public void getOrganizationData( )   throws Exception {
//        DataBag  dataBag = new DataBag();
//        Mockito.when(listService.getOrganizationData (organizationId, startDate, endDate) ).thenReturn(dataBag);
//        Assert.assertNotNull(dataBag);
//        LOGGER.info(mm +"ListService: getOrganizationData");
//    }
//
//    @Test
//    public void getProjectData()   throws Exception {
//        DataBag  dataBag = new DataBag();
//        Mockito.when(listService.getProjectData (projectId, startDate, endDate) ).thenReturn(dataBag);
//        Assert.assertNotNull(dataBag);
//        LOGGER.info(mm +"ListService: getProjectData");
//    }
//
//    @Test
//    public void getUserData()   throws Exception {
//        DataBag  dataBag = new DataBag();
//        Mockito.when(listService.getUserData (userId, startDate, endDate) ).thenReturn(dataBag);
//        Assert.assertNotNull(dataBag);
//        LOGGER.info(mm +"ListService: getUserData");
//    }


}
