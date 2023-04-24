package com.boha.geo;

import com.boha.geo.monitor.data.*;
import com.boha.geo.monitor.services.DataService;
import com.boha.geo.monitor.services.ListService;
import com.boha.geo.monitor.services.MessageService;
import com.boha.geo.monitor.services.RegistrationService;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;

import static com.mongodb.assertions.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {
    public static final Logger LOGGER = LoggerFactory.getLogger(RegistrationServiceTest.class.getSimpleName());

    @Mock
    private ProjectRepository projectRepository = Mockito.mock(ProjectRepository.class);

    @Mock
    private UserRepository userRepository = Mockito.mock(UserRepository.class);

    @Mock
    private OrganizationRepository organizationRepository = Mockito.mock(OrganizationRepository.class);
    @Mock
    private ProjectPositionRepository projectPositionRepository = Mockito.mock(ProjectPositionRepository.class);

    @Mock
    private DataService dataService = Mockito.mock(DataService.class);
    @Mock
    private ListService listService = Mockito.mock(ListService.class);
    @InjectMocks
    private RegistrationService registrationService;

    @Before
    public void initService() {
        registrationService = new RegistrationService(organizationRepository,userRepository,
                projectRepository, projectPositionRepository, dataService, listService);
    }


    @Test
    public void registerOrganization() throws Exception {
        OrganizationRegistrationBag bag = new OrganizationRegistrationBag();
        bag.setOrganization(new Organization());

        OrganizationRegistrationBag res = new OrganizationRegistrationBag();
        SettingsModel settingsModel = new SettingsModel();
        settingsModel.setOrganizationId("12345");
        res.setSettings(settingsModel);
        res.setDate(DateTime.now().toDateTimeISO().toString());
        Mockito.when(registrationService.registerOrganization(bag)).thenReturn(res);

        Assert.assertNotNull(res.getSettings());
        Assert.assertNotNull(res.getDate());
        LOGGER.info(E.PEAR + E.PEAR + E.PEAR +"RegistrationServiceTest: registerOrganization");
    }

}
