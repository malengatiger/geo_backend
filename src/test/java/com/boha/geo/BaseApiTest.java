package com.boha.geo;

import com.boha.geo.controllers.DataController;
import com.boha.geo.controllers.ListController;
import com.boha.geo.monitor.data.Country;
import com.boha.geo.util.E;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
//@RunWith(SpringRunner.class)

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = GeoApplication.class)
@TestPropertySource(locations = "classpath:application-prod.yml")

@WebAppConfiguration
public class BaseApiTest {
    public static final Logger LOGGER = LoggerFactory.getLogger(BaseApiTest.class.getSimpleName());

    @Autowired
    private ListController listController;
    @Autowired
    private DataController dataController;
    @Value("classpath:resources")
    Resource resourceFile;

    @Test
    public void contextLoads() throws Exception {
        LOGGER.info(E.ALIEN+E.ALIEN+E.ALIEN+" contextLoads: ");

        assertThat(listController).isNotNull();
    }
    @Test
    public void ping() throws Exception {
        String hey = dataController.ping();
        LOGGER.info(E.ALIEN+E.ALIEN+E.ALIEN+" "+ hey);
        assertThat(hey.contains("PROJECT MONITOR SERVICES PLATFORM")).isTrue();
    }
    @Test
    public void getCountries() throws Exception {
        ResponseEntity<Object> res = listController.getCountries();
        LOGGER.info(E.ALIEN+E.ALIEN+E.ALIEN+" getCountries statusCode: "+E.RED_APPLE+" "+ res.getStatusCode().value());
        assertThat(res.getStatusCode().value() == 200).isTrue();
        assertThat(res.hasBody()).isTrue();
    }
    @Test
    public void getOrganizations() throws Exception {
        ResponseEntity<Object> res = listController.getOrganizations();
        LOGGER.info(E.ALIEN+E.ALIEN+E.ALIEN+" getOrganizations statusCode: "+E.RED_APPLE+" "+ res.getStatusCode().value());
        assertThat(res.getStatusCode().value() == 200).isTrue();
        assertThat(res.hasBody()).isTrue();
    }
    @Test
    public void getUsers() throws Exception {
        ResponseEntity<Object> res = listController.getUsers();
        LOGGER.info(E.ALIEN+E.ALIEN+E.ALIEN+" getUsers statusCode: "+E.RED_APPLE+" "+ res.getStatusCode().value());
        assertThat(res.getStatusCode().value() == 200).isTrue();
        assertThat(res.hasBody()).isTrue();
    }
    @Test
    public void getAllOrganizationProjects() throws Exception {
        ResponseEntity<Object> res = listController.getAllOrganizationProjects(organizationId);
        LOGGER.info(E.ALIEN+E.ALIEN+E.ALIEN+" getAllOrganizationProjects statusCode: "+E.RED_APPLE+" "+ res.getStatusCode().value());
        assertThat(res.getStatusCode().value() == 200).isTrue();
        assertThat(res.hasBody()).isTrue();
    }
    @Test
    public void getOrganizationProjectPolygons() throws Exception {
        ResponseEntity<Object> res = listController.getOrganizationProjectPolygons(organizationId);
        LOGGER.info(E.ALIEN+E.ALIEN+E.ALIEN+" getOrganizationProjectPolygons statusCode: "+E.RED_APPLE+" "+ res.getStatusCode().value());
        assertThat(res.getStatusCode().value() == 200).isTrue();
        assertThat(res.hasBody()).isTrue();
    }

    static final String organizationId = "8b6af025-1821-4f29-995a-77c80e9b0996";
}
