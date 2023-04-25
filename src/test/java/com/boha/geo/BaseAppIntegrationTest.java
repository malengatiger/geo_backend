package com.boha.geo;

import com.boha.geo.monitor.data.Organization;
import com.boha.geo.monitor.data.OrganizationRegistrationBag;
import com.boha.geo.monitor.data.SettingsModel;
import com.boha.geo.monitor.data.User;
import com.google.cloud.spring.secretmanager.SecretManagerTemplate;
import org.joda.time.DateTime;
import org.junit.*;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.junit.Assume.assumeThat;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = GeoApplication.class,
        properties = {"spring.cloud.gcp.secretmanager.enabled=true"})

public class BaseAppIntegrationTest {

    private static final String SECRET_TO_DELETE = "secret-manager-sample-delete-secret";

    @Autowired
    private SecretManagerTemplate secretManagerTemplate;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeClass
    public static void prepare() {
        System.out.println("\uD83C\uDFB2\uD83C\uDFB2 prepare running here ... \uD83C\uDF00 \uD83C\uDF00 \uD83C\uDF00 \uD83C\uDF00");

    }
    @AfterClass
    public static void finish() {
        System.out.println("\uD83C\uDFB2\uD83C\uDFB2 finish running here ... \uD83C\uDF00 \uD83C\uDF00 \uD83C\uDF00 \uD83C\uDF00");
    }

    @Test
    @Order(1)
    public void testApplicationStartup() {
        ResponseEntity<String> response = this.testRestTemplate.getForEntity("/geo/v1/", String.class);
        System.out.println("\uD83C\uDFB2\uD83C\uDFB2 " + response);

        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    @Order(2)
    public void testReadSecret() {
        ResponseEntity<String> response = this.testRestTemplate.getForEntity("/geo/v1/getSecret?secretId=mongo", String.class);
        System.out.println("\uD83C\uDF50\uD83C\uDF50\uD83C\uDF50\uD83C\uDF50 " + response);

        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }
    @LocalServerPort
    int randomServerPort;

//    @Test
//    @Order(3)
//    public void testRegisterOrganizationSuccess() throws URISyntaxException
//    {
//
//        final String baseUrl = "http://localhost:"+randomServerPort+"/geo/v1/registerOrganization";
//        URI uri = new URI(baseUrl);
//
//        Organization org = new Organization();
//        org.setOrganizationId(UUID.randomUUID().toString());
//        org.setName("Fake Test Organization");
//
//        User user = new User();
//        user.setName("John Q. Testerman");
//        user.setUserId(UUID.randomUUID().toString());
//        user.setOrganizationId(org.getOrganizationId());
//
//        OrganizationRegistrationBag bag = new OrganizationRegistrationBag();
//        bag.setOrganization(org);
//        bag.setUser(user);
//
//        SettingsModel model = new SettingsModel();
//        model.setOrganizationId(org.getOrganizationId());
//        model.setCreated(DateTime.now().toDateTimeISO().toString());
//        model.setLocale("en");
//        model.setActivityStreamHours(39);
//        model.setDistanceFromProject(5000);
//        model.setThemeIndex(0);
//        model.setNumberOfDays(60);
//
//        bag.setSettings(model);
//
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("X-COM-PERSIST", "true");
//
//        HttpEntity<Object> request = new HttpEntity<>(bag, headers);
//
//        OrganizationRegistrationBag result = this.testRestTemplate.postForObject(uri, request, OrganizationRegistrationBag.class);
////        System.out.println(Objects.requireNonNull(result.getBody()));
//        //Verify request succeed
////        assertTrue(result.isn4xxClientError());
//    }
    @Test
    @Order(4)
    public void deleteTestOrganization() {
        System.out.println("\uD83D\uDD37 deleteTestOrganization up should happen here ....");
        ResponseEntity<String> response = this.testRestTemplate.getForEntity("/geo/v1/deleteTestOrganization", String.class);
        System.out.println("deleteTestOrganization response: \uD83C\uDFB2\uD83C\uDFB2 " + response);

        assertThat(response.getStatusCode().is4xxClientError()).isTrue();

    }

    @Test
    @Order(5)
    @After
    public void cleanUp() {
        System.out.println("\uD83D\uDD37 clean up should happen here ....");
        ResponseEntity<String> response = this.testRestTemplate.getForEntity("/geo/v1/deleteTestOrganization", String.class);
        System.out.println("cleanUp response: \uD83C\uDFB2\uD83C\uDFB2 " + response.getBody());

        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }

}
