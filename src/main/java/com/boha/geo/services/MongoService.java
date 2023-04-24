package com.boha.geo.services;

import com.boha.geo.models.CityLocation;
import com.boha.geo.monitor.data.City;
import com.boha.geo.monitor.data.Organization;
import com.boha.geo.monitor.data.User;
import com.boha.geo.repos.CityRepository;
import com.boha.geo.repos.OrganizationRepository;
import com.boha.geo.repos.UserRepository;
import com.boha.geo.util.E;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
@RequiredArgsConstructor

@Service
public class MongoService {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger logger = Logger.getLogger(MongoService.class.getSimpleName());
    private static final String xx = E.COFFEE + E.COFFEE + E.COFFEE;

    private final CityRepository cityRepo;
    private final MongoClient mongoClient;
    private final ResourceLoader resourceLoader;
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;


    private List<City> getCitiesFromFile() throws IOException {
        logger.info(mm + " Getting cities from file ... ");
        Resource resource = resourceLoader.getResource("classpath:cities.json");
        File file = resource.getFile();
        logger.info(mm + " Cities json file length: " + file.length());

        Path p = Paths.get(file.toURI());
        String json = Files.readString(p);
        List<City> cities = new ArrayList<>();
        JSONArray arr = new JSONArray(json);
        for (Object o : arr) {
            JSONObject jo = (JSONObject) o;
            String name = jo.getString("AccentCity");
            String province = jo.getString("ProvinceName");
            double latitude = jo.getDouble("Latitude");
            double longitude = jo.getDouble("Longitude");
            City city = new City();
            city.setName(name);
            city.setCityId(UUID.randomUUID().toString());
            city.setProvince(province);
            CityLocation position = new CityLocation();
            position.setType("Point");
            List<Double> list = new ArrayList<>();
            list.add(longitude);
            list.add(latitude);
            position.setCoordinates(list);
            city.setCityLocation(position);
            city.setCountry("South Africa");
            cities.add(city);
        }

        logger.info(mm + " Found " + cities.size() + " cities in file");
        Collections.sort(cities);
        return cities;
    }

    public List<City> addCitiesToDB() throws Exception {
        logger.info(mm + " ... adding cities to MongoDB ...");
        long start = System.currentTimeMillis();

        List<City> cities = getCitiesFromFile();

        long end1 = System.currentTimeMillis();
        long elapsed = (end1 - start) / 1000;
        logger.info(E.RED_APPLE + E.RED_APPLE + E.RED_APPLE
                + " it took " + elapsed + " seconds to build list");

        long start2 = System.currentTimeMillis();
        cityRepo.insert(cities);

        long end2 = System.currentTimeMillis();
        long elapsed2 = (end2 - start2) / 1000;
        logger.info(E.RED_APPLE + E.RED_APPLE + E.RED_APPLE
                + " it took " + elapsed2 + " seconds to save in MongoDB");


        logger.info(mm + " " + cities.size() + " cities saved to MongoDB "
                + E.LEAF + E.LEAF);

        return cities;
    }

    public List<City> getCities() throws Exception {
        List<City> cities = cityRepo.findAll();
        logger.info(E.LEAF + E.LEAF + "Cities found on MongoDB: " + cities.size());
        Collections.sort(cities);
        return cities;
    }

    public void initializeIndexes() {
        try {
            createCityIndexes();
            createProjectPositionIndexes();
            createProjectPolygonIndexes();
            createGeofenceEventIndexes();
            createRatingIndexes();
            createLocationResponseIndexes();

            createPhotoIndexes();
            createVideoIndexes();
            createActivityIndexes();

            createUserIndexes();
            createOrganizationIndexes();
            createProjectIndexes();
            createCommunityIndexes();

            createAudioIndexes();
            createSchedulesIndexes();
            createUniqueCityIndex();

            createSettingsIndexes();
            createProjectSummaryIndexes();
            logger.info(xx + " MongoService has completed setup of indexes " + E.BELL + E.BELL);

        } catch (Exception e) {
            logger.severe(E.RED_DOT + E.RED_DOT + " Index building failed: " + e.getMessage());
        }
    }

    private void createOrganizationIndexes() {
        //add index
        MongoCollection<Document> dbCollection = db.getCollection("organizations");

        String result2 = dbCollection.createIndex(Indexes.ascending("name"),
                new IndexOptions().unique(true));

        dbCollection.createIndex(Indexes.ascending("organizationId"));

    }

    private void createSettingsIndexes() {
        //add index
        MongoCollection<Document> dbCollection = db.getCollection("settings");

        dbCollection.createIndex(Indexes.ascending("organizationId"));
        dbCollection.createIndex(Indexes.ascending("projectId"));

    }

    private void createProjectIndexes() {
        //add index
        MongoCollection<Document> dbCollection = db.getCollection("projects");

        dbCollection.createIndex(Indexes.ascending("organizationId", "name"),
                new IndexOptions().unique(true));

        dbCollection.createIndex(Indexes.ascending("organizationId"));

    }

    private void createProjectSummaryIndexes() {
        //add index
        MongoCollection<Document> dbCollection = db.getCollection("projectSummaries");

        dbCollection.createIndex(Indexes.ascending("organizationId", "date"),
                new IndexOptions().unique(false));

        dbCollection.createIndex(Indexes.ascending("projectId", "date"),
                new IndexOptions().unique(false));

        dbCollection.createIndex(Indexes.ascending("organizationId"));

        dbCollection.createIndex(Indexes.ascending("projectId"));

        dbCollection.createIndex(Indexes.ascending("date"));

    }

    private void createUserIndexes() {
        //add index
        MongoCollection<Document> dbCollection = db.getCollection("users");

        dbCollection.createIndex(Indexes.ascending("email"),
                new IndexOptions().unique(false));


        dbCollection.createIndex(Indexes.ascending("cellphone"));


        dbCollection.createIndex(Indexes.ascending("organizationId"));

    }

    private void createCommunityIndexes() {
        //add index
        MongoCollection<Document> dbCollection = db.getCollection("communities");

        dbCollection.createIndex(Indexes.ascending("name", "countryId"),
                new IndexOptions().unique(true));

        dbCollection.createIndex(Indexes.ascending("countryId"));

    }

    private void createAudioIndexes() {
        //add index
        MongoCollection<Document> dbCollection = db.getCollection("audios");

        dbCollection.createIndex(Indexes.ascending("projectId"),
                new IndexOptions().unique(false));

        dbCollection.createIndex(Indexes.ascending("userId"));

        dbCollection.createIndex(Indexes.geo2dsphere("position"));
        dbCollection.createIndex(Indexes.ascending("organizationId"));

    }

    private void createSchedulesIndexes() {
        //add index
        MongoCollection<Document> dbCollection = db.getCollection("fieldMonitorSchedules");

        dbCollection.createIndex(Indexes.ascending("projectId"),
                new IndexOptions().unique(false));

        dbCollection.createIndex(Indexes.ascending("userId"));

        dbCollection.createIndex(Indexes.geo2dsphere("position"));

    }

    private void createUniqueCityIndex() {
        MongoCollection<Document> dbCollection = db.getCollection("cities");

        dbCollection.createIndex(Indexes.ascending("province", "name"),
                new IndexOptions().unique(true));

    }

    private static final String mm = E.LEAF + " ";

    private void createCityIndexes() {
        //add index
        MongoCollection<Document> dbCollection = db.getCollection("cities");
        dbCollection.createIndex(Indexes.geo2dsphere("cityLocation"));

        dbCollection.createIndex(Indexes.ascending("name"));

        dbCollection.createIndex(Indexes.ascending("cityId"));

    }

    private void createProjectPositionIndexes() {
        //add index
        MongoCollection<Document> dbCollection = db.getCollection("projectPositions");
        dbCollection.createIndex(Indexes.geo2dsphere("position"));

        dbCollection.createIndex(Indexes.ascending("projectId"));

        dbCollection.createIndex(Indexes.ascending("organizationId"));


    }

    private void createLocationResponseIndexes() {
        //add index
        MongoCollection<Document> dbCollection = db.getCollection("locationResponses");
        dbCollection.createIndex(Indexes.geo2dsphere("position"));

        dbCollection.createIndex(Indexes.ascending("userId"));


        dbCollection.createIndex(Indexes.ascending("organizationId"));


    }

    private void createGeofenceEventIndexes() {
        //add index
        MongoCollection<Document> dbCollection = db.getCollection("geofenceEvents");
        dbCollection.createIndex(Indexes.geo2dsphere("position"));

        dbCollection.createIndex(Indexes.ascending("projectId"));

        dbCollection.createIndex(Indexes.ascending("organizationId"));


    }

    private void createRatingIndexes() {
        //add index
        MongoCollection<Document> dbCollection = db.getCollection("ratings");
        dbCollection.createIndex(Indexes.geo2dsphere("position"));

        dbCollection.createIndex(Indexes.ascending("projectId"));

        dbCollection.createIndex(Indexes.ascending("organizationId"));


    }

    private void createProjectPolygonIndexes() {
        //add index
        MongoCollection<Document> dbCollection = db.getCollection("projectPolygons");
        dbCollection.createIndex(Indexes.geo2dsphere("positions.coordinates"));

         dbCollection.createIndex(Indexes.ascending("projectId"));

        dbCollection.createIndex(Indexes.ascending("organizationId"));


    }

    private void createPhotoIndexes() {
        //add index
        MongoCollection<Document> dbCollection = db.getCollection("photos");
        dbCollection.createIndex(Indexes.geo2dsphere("projectPosition"));

         dbCollection.createIndex(Indexes.ascending("projectId"));

         dbCollection.createIndex(Indexes.ascending("organizationId"));


    }

    private void createActivityIndexes() {
        //add index
        MongoCollection<Document> dbCollection = db.getCollection("activities");

         dbCollection.createIndex(Indexes.ascending("projectId"));

        dbCollection.createIndex(Indexes.ascending("organizationId"));

        dbCollection.createIndex(Indexes.ascending("userId"));


  dbCollection.createIndex(Indexes.ascending("date"));


    }


    private MongoDatabase db;

    private void setDatabase() {
        if (db == null) {
            db = mongoClient.getDatabase("geo");
            logger.info(mm + " Mongo Database set up, name: " + db.getName());
        }
    }

    private void createVideoIndexes() {
        //add index
        MongoCollection<Document> dbCollection = db.getCollection("videos");
         dbCollection.createIndex(Indexes.geo2dsphere("projectPosition"));

         dbCollection.createIndex(Indexes.ascending("projectId"));


         dbCollection.createIndex(Indexes.ascending("organizationId"));


    }

    public void printOrganizations() {
        List<Organization> orgs = organizationRepository.findAll();
        for (Organization org : orgs) {
            List<User> users = userRepository.findByOrganizationId(org.getOrganizationId());
            logger.info(E.PEAR + E.PEAR + E.PEAR + " Organization: " + org.getName());
            logger.info(E.PEAR + E.PEAR + E.PEAR + " organizationId: " + org.getOrganizationId());
            for (User user : users) {
                logger.info(E.PEAR + E.PEAR + " user: " + user.getUserType() + " " + E.RED_APPLE
                        + "  " + user.getEmail() + " " + E.BLUE_DOT + "  " + user.getName());
                logger.info(E.PEAR + E.PEAR + " \tuserId: " + user.getUserId() + " " + E.RED_APPLE);
            }
            logger.info("\n\n");
        }
    }

}
