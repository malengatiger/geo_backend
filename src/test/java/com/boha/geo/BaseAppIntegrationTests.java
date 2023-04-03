package com.boha.geo;


/*
 * Copyright 2017-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import com.boha.geo.monitor.data.Organization;
import com.boha.geo.monitor.data.OrganizationRegistrationBag;
import com.boha.geo.monitor.data.SettingsModel;
import com.boha.geo.monitor.data.User;
import com.google.cloud.spring.secretmanager.SecretManagerTemplate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.junit.Assume.assumeThat;

/**
 * Application secret named "application-secret" must exist and have a value of "Hello world.".
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = GeoApplication.class,
        properties = {"spring.cloud.gcp.secretmanager.enabled=true"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class BaseAppIntegrationTests {

    private static final String SECRET_TO_DELETE = "secret-manager-sample-delete-secret";

    @Autowired
    private SecretManagerTemplate secretManagerTemplate;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeClass
    public static void prepare() {
//        assumeThat(
//                "Secret Manager integration tests are disabled. "
//                        + "Please use '-Dit.secretmanager=true' to enable them.",
//                System.getProperty("it.secretmanager"), is("true"));
    }

    @Test
    @Order(1)
    public void testApplicationStartup() {
        ResponseEntity<String> response = this.testRestTemplate.getForEntity("/geo/v1/", String.class);
        System.out.println("\uD83C\uDFB2\uD83C\uDFB2 " + response);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).contains("Khaya");
    }

    @Test
    @Order(2)
    public void testReadSecret() {
        ResponseEntity<String> response = this.testRestTemplate.getForEntity("/geo/v1/getSecret?secretId=mongo", String.class);
        System.out.println("\uD83C\uDF50\uD83C\uDF50\uD83C\uDF50\uD83C\uDF50 " + response);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).contains("geomaster");
    }
    @LocalServerPort
    int randomServerPort;

    @Test
    @Order(3)
    public void testRegisterOrganizationSuccess() throws URISyntaxException
    {

        final String baseUrl = "http://localhost:"+randomServerPort+"/geo/v1/registerOrganization";
        URI uri = new URI(baseUrl);

        Organization org = new Organization();
        org.setOrganizationId(UUID.randomUUID().toString());
        org.setName("Fake Test Organization");

        User user = new User();
        user.setName("John Q. Testerman");
        user.setUserId(UUID.randomUUID().toString());
        user.setOrganizationId(org.getOrganizationId());

        OrganizationRegistrationBag bag = new OrganizationRegistrationBag();
        bag.setOrganization(org);
        bag.setUser(user);

        SettingsModel model = new SettingsModel();
        model.setOrganizationId(org.getOrganizationId());
        model.setCreated(DateTime.now().toDateTimeISO().toString());
        model.setLocale("en");
        model.setActivityStreamHours(39);
        model.setDistanceFromProject(5000);
        model.setThemeIndex(0);
        model.setNumberOfDays(60);

        bag.setSettings(model);


        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<Object> request = new HttpEntity<>(bag, headers);

        ResponseEntity<Object> result = this.testRestTemplate.postForEntity(uri, request, Object.class);
        System.out.println(Objects.requireNonNull(result.getBody()));
        //Verify request succeed
        assertTrue(result.getStatusCode().is2xxSuccessful());
    }
    @Test
    @Order(4)
    public void deleteTestOrganization() {
        System.out.println("\uD83D\uDD37 deleteTestOrganization up should happen here ....");
        ResponseEntity<String> response = this.testRestTemplate.getForEntity("/geo/v1/deleteTestOrganization", String.class);
        System.out.println("deleteTestOrganization response: \uD83C\uDFB2\uD83C\uDFB2 " + response);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

    }

    @Test
    @After
    public void cleanUp() {
        System.out.println("\uD83D\uDD37 clean up should happen here ....");
        ResponseEntity<String> response = this.testRestTemplate.getForEntity("/geo/v1/deleteTestOrganization", String.class);
        System.out.println("cleanUp response: \uD83C\uDFB2\uD83C\uDFB2 " + response);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }

}
