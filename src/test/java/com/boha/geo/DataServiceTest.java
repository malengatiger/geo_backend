package com.boha.geo;

import com.boha.geo.monitor.data.*;
import com.boha.geo.monitor.services.DataService;
import com.boha.geo.monitor.services.MessageService;
import com.boha.geo.repos.*;
import com.boha.geo.services.MailService;
import com.boha.geo.util.E;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;

import static com.mongodb.assertions.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class DataServiceTest {
    public static final Logger LOGGER = LoggerFactory.getLogger(DataServiceTest.class.getSimpleName());


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
    @InjectMocks
    private DataService dataService;

    @Before
    public void initService() {
        dataService = new DataService(env, appErrorRepository, geofenceEventRepository, settingsModelRepository,
                ratingRepository, mongoTemplate, audioRepository, projectRepository, locationResponseRepository,
                locationRequestRepository, projectPolygonRepository, cityRepository, photoRepository,
                activityModelRepository, projectAssignmentRepository, videoRepository, userRepository,
                communityRepository, conditionRepository, countryRepository, organizationRepository,
                projectPositionRepository, orgMessageRepository, messageService, fieldMonitorScheduleRepository,
                projectSummaryRepository, mailService
        );
//        LOGGER.info(E.PEAR + E.PEAR + E.PEAR +"DataServiceTest: dataService initialized");
    }

    @Test
    public void savedUserIsNotNull() throws Exception {
        User user = new User();
        user.setUserId(fakeId);
        Mockito.when(userRepository.insert(user)).thenReturn(user);

        assertNotNull(user);
        LOGGER.info(E.PEAR + E.PEAR + E.PEAR +"DataServiceTest savedUserIsNotNull");
    }
    @Test
    public void savedProjectIsNotNull() throws Exception {
        Project project = new Project();
        project.setProjectId(fakeId);
        Mockito.when(projectRepository.insert(project)).thenReturn(project);

        assertNotNull(project);
        LOGGER.info(E.PEAR + E.PEAR + E.PEAR +"DataServiceTest: savedProjectIsNotNull");
    }
    @Test
    public void addProject() throws Exception {
        Project project = new Project();
        project.setProjectId(fakeId);
        int result = 0;
        Mockito.when(dataService.addProject(project)).thenReturn(result);

        Assert.assertEquals(0, result);
        LOGGER.info(E.PEAR + E.PEAR + E.PEAR +"DataServiceTest: addProject");
    }
    @Test
    public void addProjectPosition() throws Exception {
        ProjectPosition position = new ProjectPosition();
        position.setProjectId(fakeId);
        int result = 0;
        Mockito.when(dataService.addProjectPosition(position)).thenReturn(result);

        Assert.assertEquals(0, result);
        LOGGER.info(E.PEAR + E.PEAR + E.PEAR +"DataServiceTest: addProjectPosition");
    }
    @Test
    public void addProjectPolygon() throws Exception {
        ProjectPolygon position = new ProjectPolygon();
        position.setProjectId(fakeId);
        int result = 0;
        Mockito.when(dataService.addProjectPolygon(position)).thenReturn(result);

        Assert.assertEquals(0, result);
        LOGGER.info(E.PEAR + E.PEAR + E.PEAR +"DataServiceTest: addProjectPolygon");
    }
    @Test
    public void addGeofenceEvent() throws Exception {
        GeofenceEvent event = new GeofenceEvent();
        event.setProjectId(fakeId);
        User user = new User();
        user.setName("Test User");
        user.setThumbnailUrl("https://tgfdjgfjdgf");
        event.setUser(user);
        int result = 0;
        Mockito.when(dataService.addGeofenceEvent(event)).thenReturn(result);

        Assert.assertEquals(0, result);
        LOGGER.info(E.PEAR + E.PEAR + E.PEAR +"DataServiceTest: addGeofenceEvent");
    }
    @Test
    public void addLocationRequest() throws Exception {
        LocationRequest event = new LocationRequest();
        event.setUserId(fakeId);
        int result = 0;
        Mockito.when(dataService.addLocationRequest(event)).thenReturn(result);

        Assert.assertEquals(0, result);
        LOGGER.info(E.PEAR + E.PEAR + E.PEAR +"DataServiceTest: addLocationRequest");
    }
    @Test
    public void addLocationResponse() throws Exception {
        LocationResponse event = new LocationResponse();
        event.setUserId(fakeId);
        int result = 0;
        Mockito.when(dataService.addLocationResponse(event)).thenReturn(result);

        Assert.assertEquals(0, result);
        LOGGER.info(E.PEAR + E.PEAR + E.PEAR +"DataServiceTest: addLocationResponse");
    }
    @Test
    public void addPhoto() throws Exception {
        Photo photo = new Photo();
        photo.setUserId(fakeId);
        int result = 0;
        Mockito.when(dataService.addPhoto(photo)).thenReturn(result);

        Assert.assertEquals(0, result);
        LOGGER.info(E.PEAR + E.PEAR + E.PEAR +"DataServiceTest: addPhoto");
    }
    @Test
    public void addVideo() throws Exception {
        Video video = new Video();
        video.setUserId(fakeId);
        int result = 0;
        Mockito.when(dataService.addVideo(video)).thenReturn(result);

        Assert.assertEquals(0, result);
        LOGGER.info(E.PEAR + E.PEAR + E.PEAR +"DataServiceTest: addVideo");
    }

    @Test
    public void addAudio() throws Exception {
        Audio audio = new Audio();
        audio.setUserId(fakeId);
        int result = 0;
        Mockito.when(dataService.addAudio(audio)).thenReturn(result);

        Assert.assertEquals(0, result);
        LOGGER.info(E.PEAR + E.PEAR + E.PEAR +"DataServiceTest: addAudio");
    }
    @Test
    public void addAppError() throws Exception {
        AppError appError = new AppError();
        appError.setUserId(fakeId);
        int result = 0;
        Mockito.when(dataService.addAppError(appError)).thenReturn(result);

        Assert.assertEquals(0, result);
        LOGGER.info(E.PEAR + E.PEAR + E.PEAR +"DataServiceTest: addAppError");
    }
    @Test
    public void addCondition() throws Exception {
        Condition condition = new Condition();
        condition.setUserId(fakeId);
        int result = 0;
        Mockito.when(dataService.addCondition(condition)).thenReturn(result);

        Assert.assertEquals(0, result);
        LOGGER.info(E.PEAR + E.PEAR + E.PEAR +"DataServiceTest: addCondition");
    }
    @Test
    public void addFieldMonitorSchedule() throws Exception {
        FieldMonitorSchedule schedule = new FieldMonitorSchedule();
        schedule.setUserId(fakeId);
        int result = 0;
        Mockito.when(dataService.addFieldMonitorSchedule(schedule)).thenReturn(result);

        Assert.assertEquals(0, result);
        LOGGER.info(E.PEAR + E.PEAR + E.PEAR +"DataServiceTest: addFieldMonitorSchedule");
    }
    @Test
    public void addOrgMessage() throws Exception {
        OrgMessage message = new OrgMessage();
        message.setUserId(fakeId);
        int result = 0;
        Mockito.when(dataService.addOrgMessage(message)).thenReturn(result);

        Assert.assertEquals(0, result);
        LOGGER.info(E.PEAR + E.PEAR + E.PEAR +"DataServiceTest: addOrgMessage");
    }
    @Test
    public void addSettings() throws Exception {
        SettingsModel settingsModel = new SettingsModel();
        settingsModel.setUserId(fakeId);
        int result = 0;
        Mockito.when(dataService.addSettings(settingsModel)).thenReturn(result);

        Assert.assertEquals(0, result);
        LOGGER.info(E.PEAR + E.PEAR + E.PEAR +"DataServiceTest: addSettings");
    }
    @Test
    public void addCountry() throws Exception {
        Country country = new Country();
        country.setName(fakeId);
        int result = 0;
        Mockito.when(dataService.addCountry(country)).thenReturn(result);

        Assert.assertEquals(0, result);
        LOGGER.info(E.PEAR + E.PEAR + E.PEAR +"DataServiceTest: addCountry");
    }

    static final String fakeId = "1234567890";
}
