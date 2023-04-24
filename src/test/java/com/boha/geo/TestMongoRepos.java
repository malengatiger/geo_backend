package com.boha.geo;


import com.boha.geo.monitor.data.Country;
import com.boha.geo.monitor.data.Position;
import com.boha.geo.repos.CountryRepository;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = GeoApplication.class,
        properties = {"spring.cloud.gcp.secretmanager.enabled=true"})
@DataMongoTest
@Import(PopulatorConfiguration.class)

class TestMongoRepos {

    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void bootstrapTestDataWithMongoRepository() {
        var country = new Country();
        country.setName("Test Country");
        country.setCountryCode("TC");
        country.setCountryId("123456");
        Position pos = new Position();
        List<Double> co = new ArrayList<>();
        co.add(0.0);
        co.add(0.0);
        pos.setCoordinates(co);
        country.setPosition(pos);
        Country c = countryRepository.insert(country);

        assertNotNull(c);
    }
    @AfterEach
    void cleanUpDatabase() {
        mongoTemplate.getDb().drop();
    }
}
