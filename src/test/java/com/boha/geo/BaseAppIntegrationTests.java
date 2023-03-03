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
import com.google.cloud.spring.secretmanager.SecretManagerTemplate;
import org.junit.BeforeClass;
import org.junit.Test;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assume.assumeThat;

/**
 * Application secret named "application-secret" must exist and have a value of "Hello world.".
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = GeoApplication.class,
        properties = {"spring.cloud.gcp.secretmanager.enabled=true"})
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
    public void testApplicationStartup() {
        ResponseEntity<String> response = this.testRestTemplate.getForEntity("/geo/v1/", String.class);
        System.out.println(response);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).contains("Khaya");
    }

    @Test
    public void testReadSecret() {
        ResponseEntity<String> response = this.testRestTemplate.getForEntity("/geo/v1/getSecret?secretId=mongo", String.class);
        System.out.println("\uD83C\uDF50\uD83C\uDF50\uD83C\uDF50\uD83C\uDF50 " + response);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).contains("geomaster");
    }
    @LocalServerPort
    int randomServerPort;

    @Test
    public void testRegisterOrganizationSuccess() throws URISyntaxException
    {
        final String baseUrl = "http://localhost:"+randomServerPort+"/registerOrganization/";
        URI uri = new URI(baseUrl);
        Organization org = new Organization();
        org.setName("Test Org");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<Organization> request = new HttpEntity<>(org, headers);

        URI createdEmployeeURL = testRestTemplate.postForLocation(uri, org);
        System.out.println("\uD83C\uDF50\uD83C\uDF50\uD83C\uDF50\uD83C\uDF50 " +
                "createdEmployeeURL: " + createdEmployeeURL.toString());

        assertNotNull(createdEmployeeURL);

        //ResponseEntity<Employee> result = restTemplate.postForEntity(uri, employee, Employee.class);
        //
        //Assertions.assertEquals(201, result.getStatusCodeValue());
        //Assertions.assertNotNull(result.getBody().getId());
        //ResponseEntity<?> result = this.testRestTemplate.postForEntity(uri, org, Organization.class);
//        System.out.println("\uD83C\uDF50\uD83C\uDF50\uD83C\uDF50\uD83C\uDF50 " + result.getBody());

        //Verify request succeed
//        assertEquals(200, result.getStatusCode().value());
    }

}
