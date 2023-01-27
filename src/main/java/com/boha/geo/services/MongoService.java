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
import java.util.*;
import java.util.logging.Logger;

@Service
public class MongoService {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger logger = Logger.getLogger(MongoService.class.getSimpleName());
    private static final String xx = E.COFFEE+E.COFFEE+E.COFFEE;
    @Autowired
    private CityRepository cityRepo;

    public MongoService(CityRepository cityRepo, MongoClient mongoClient, ResourceLoader resourceLoader, UserRepository userRepository, OrganizationRepository organizationRepository) {
        this.cityRepo = cityRepo;
        this.mongoClient = mongoClient;
        this.resourceLoader = resourceLoader;
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        logger.info(xx + " MongoService constructed, will set database and initializeIndexes ........... " + E.BELL+E.BELL);
        setDatabase();
        initializeIndexes();
        logger.info(xx + " MongoService has completed setup of database and indexes ........... " + E.BELL+E.BELL);

    }
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

            createUserIndexes();
            createOrganizationIndexes();
            createProjectIndexes();
            createCommunityIndexes();

            createAudioIndexes();
            createSchedulesIndexes();
            createUniqueCityIndex();

        } catch (Exception e) {
            logger.severe(E.RED_DOT+E.RED_DOT+" Index building failed: " + e.getMessage());
        }
    }
    private void createOrganizationIndexes() {
        //add index
        MongoCollection<Document> dbCollection = db.getCollection("organizations");

        String result2 = dbCollection.createIndex(Indexes.ascending("name"),
                new IndexOptions().unique(true));
        logger.info(mm +
                " unique name index on organizations collection: " +
                E.RED_APPLE + result2);

        String result3 = dbCollection.createIndex(Indexes.ascending("organizationId"));
        logger.info(mm +
                " organizationId index on organizations collection: " +
                E.RED_APPLE + result3);

    }
    private void createProjectIndexes() {
        //add index
        MongoCollection<Document> dbCollection = db.getCollection("projects");

        String result2 = dbCollection.createIndex(Indexes.ascending("organizationId", "name"),
                new IndexOptions().unique(true));
        logger.info(mm +
                " unique name index on projects collection: " +
                E.RED_APPLE + result2);

        String result3 = dbCollection.createIndex(Indexes.ascending("organizationId"));
        logger.info(mm +
                " organizationId index on projects collection: " +
                E.RED_APPLE + result3);

    }
    private void createUserIndexes() {
        //add index
        MongoCollection<Document> dbCollection = db.getCollection("users");

        String result2 = dbCollection.createIndex(Indexes.ascending("email"),
                new IndexOptions().unique(false));
        logger.info(mm +
                " user unique email index on users collection: " +
                E.RED_APPLE + result2);

        String result = dbCollection.createIndex(Indexes.ascending("cellphone"));
        logger.info(mm +
                " user cellphone index on users collection: " +
                E.RED_APPLE + result);

        String result3 = dbCollection.createIndex(Indexes.ascending("organizationId"));
        logger.info(mm +
                " organizationId index on users collection: " +
                E.RED_APPLE + result3);
    }
    private void createCommunityIndexes() {
        //add index
        MongoCollection<Document> dbCollection = db.getCollection("communities");

        String result2 = dbCollection.createIndex(Indexes.ascending("name", "countryId"),
                new IndexOptions().unique(true));
        logger.info(mm +
                " user unique name index on community collection: " +
                E.RED_APPLE + result2);

        String result = dbCollection.createIndex(Indexes.ascending("countryId"));
        logger.info(mm +
                " user countryId index on community collection: " +
                E.RED_APPLE + result);
    }
    private void createAudioIndexes() {
        //add index
        MongoCollection<Document> dbCollection = db.getCollection("audios");

        String result2 = dbCollection.createIndex(Indexes.ascending("projectId"),
                new IndexOptions().unique(false));
        logger.info(mm +
                " projectId index on audios collection: " +
                E.RED_APPLE + result2);

        String result = dbCollection.createIndex(Indexes.ascending("userId"));
        logger.info(mm+
                " userId index on audios collection: " +
                E.RED_APPLE + result);

        String result3 = dbCollection.createIndex(Indexes.geo2dsphere("position"));
        logger.info(mm+
                " position 2dSphere index on audios collection: " +
                E.RED_APPLE + result3);
        String result4 = dbCollection.createIndex(Indexes.ascending("organizationId"));
        logger.info(mm+
                " organizationId index on audios collection: " +
                E.RED_APPLE + result4);
    }
    private void createSchedulesIndexes() {
        //add index
        MongoCollection<Document> dbCollection = db.getCollection("fieldMonitorSchedules");

        String result2 = dbCollection.createIndex(Indexes.ascending("projectId"),
                new IndexOptions().unique(false));
        logger.info(mm +
                " projectId index on fieldMonitorSchedules collection: " +
                E.RED_APPLE + result2);

        String result = dbCollection.createIndex(Indexes.ascending("userId"));
        logger.info(mm+
                " userId index on fieldMonitorSchedules collection: " +
                E.RED_APPLE + result);

        String result3 = dbCollection.createIndex(Indexes.geo2dsphere("position"));
        logger.info(mm+
                " position 2dSphere index on fieldMonitorSchedules collection: " +
                E.RED_APPLE + result3);
    }
    private void createUniqueCityIndex() {
        MongoCollection<Document> dbCollection = db.getCollection("cities");

        String result = dbCollection.createIndex(Indexes.ascending( "province", "name"),
                new IndexOptions().unique(true));

        logger.info(mm +
                " unique index : province & name - cities collection: " +
                E.RED_APPLE + result);
    }

    private static final String mm = E.LEAF + " ";
    private void createCityIndexes() {
        //add index
        MongoCollection<Document> dbCollection = db.getCollection("cities");
        String result = dbCollection.createIndex(Indexes.geo2dsphere("cityLocation"));
        logger.info(mm +
                " cityLocation 2dSphere index on city collection: " +
                E.RED_APPLE + result);

        String result2 = dbCollection.createIndex(Indexes.ascending("name"));
        logger.info(mm +
                " name index on city collection: " +
                E.RED_APPLE + result2);

        String result3 = dbCollection.createIndex(Indexes.ascending("cityId"));
        logger.info(mm +
                " cityId index on city collection: " +
                E.RED_APPLE + result3);
    }

    private void createProjectPositionIndexes() {
        //add index
        MongoCollection<Document> dbCollection = db.getCollection("projectPositions");
        String result = dbCollection.createIndex(Indexes.geo2dsphere("position"));
        logger.info(mm +
                " projectPosition 2dSphere index on projectPositions collection: " +
                E.RED_APPLE + result);

        String result2 = dbCollection.createIndex(Indexes.ascending("projectId"));
        logger.info(mm +
                " projectId index on projectPositions collection: " +
                E.RED_APPLE + result2);

        String result3 = dbCollection.createIndex(Indexes.ascending("organizationId"));
        logger.info(mm +
                " organizationId index on projectPositions collection: " +
                E.RED_APPLE + result3);

    }
    private void createLocationResponseIndexes() {
        //add index
        MongoCollection<Document> dbCollection = db.getCollection("locationResponses");
        String result = dbCollection.createIndex(Indexes.geo2dsphere("position"));
        logger.info(mm +
                " projectPosition 2dSphere index on locationResponses collection: " +
                E.RED_APPLE + result);

        String result2 = dbCollection.createIndex(Indexes.ascending("userId"));
        logger.info(mm +
                " userId index on locationResponses collection: " +
                E.RED_APPLE + result2);

        String result3 = dbCollection.createIndex(Indexes.ascending("organizationId"));
        logger.info(mm +
                " organizationId index on locationResponses collection: " +
                E.RED_APPLE + result3);

    }
    private void createGeofenceEventIndexes() {
        //add index
        MongoCollection<Document> dbCollection = db.getCollection("geofenceEvents");
        String result = dbCollection.createIndex(Indexes.geo2dsphere("position"));
        logger.info(mm +
                " position 2dSphere index on geofenceEvents collection: " +
                E.RED_APPLE + result);

        String result2 = dbCollection.createIndex(Indexes.ascending("projectId"));
        logger.info(mm +
                " projectId index on geofenceEvents collection: " +
                E.RED_APPLE + result2);

        String result3 = dbCollection.createIndex(Indexes.ascending("organizationId"));
        logger.info(mm +
                " organizationId index on geofenceEvents collection: " +
                E.RED_APPLE + result3);

    }
    private void createRatingIndexes() {
        //add index
        MongoCollection<Document> dbCollection = db.getCollection("ratings");
        String result = dbCollection.createIndex(Indexes.geo2dsphere("position"));
        logger.info(mm +
                " position 2dSphere index on ratings collection: " +
                E.RED_APPLE + result);

        String result2 = dbCollection.createIndex(Indexes.ascending("projectId"));
        logger.info(mm +
                " projectId index on ratings collection: " +
                E.RED_APPLE + result2);

        String result3 = dbCollection.createIndex(Indexes.ascending("organizationId"));
        logger.info(mm +
                " organizationId index on ratings collection: " +
                E.RED_APPLE + result3);

    }
    private void createProjectPolygonIndexes() {
        //add index
        MongoCollection<Document> dbCollection = db.getCollection("projectPolygons");
        String result = dbCollection.createIndex(Indexes.geo2dsphere("positions.coordinates"));
        logger.info(mm +
                " positions 2dSphere index on projectPolygons collection: " +
                E.RED_APPLE + result);

        String result2 = dbCollection.createIndex(Indexes.ascending("projectId"));
        logger.info(mm +
                " projectId index on projectPolygons collection: " +
                E.RED_APPLE + result2);

        String result3 = dbCollection.createIndex(Indexes.ascending("organizationId"));
        logger.info(mm +
                " organizationId index on projectPolygons collection: " +
                E.RED_APPLE + result3);

    }

    private void createPhotoIndexes() {
        //add index
        MongoCollection<Document> dbCollection = db.getCollection("photos");
        String result = dbCollection.createIndex(Indexes.geo2dsphere("projectPosition"));
        logger.info(mm +
                " photo 2dSphere index on photos collection: " +
                E.RED_APPLE + result);

        String result2 = dbCollection.createIndex(Indexes.ascending("projectId"));
        logger.info(mm +
                " projectId index on photos collection: " +
                E.RED_APPLE + result2);

        String result3 = dbCollection.createIndex(Indexes.ascending("organizationId"));
        logger.info(mm +
                " organizationId index on photos collection: " +
                E.RED_APPLE + result3);

    }

    private MongoDatabase db;
    private void setDatabase() {
        if (db == null) {
            db = mongoClient.getDatabase("geo");
            logger.info(mm+" Mongo Database set up, name: " + db.getName());
        }
    }
    private void createVideoIndexes() {
        //add index
        MongoCollection<Document> dbCollection = db.getCollection("videos");
        String result = dbCollection.createIndex(Indexes.geo2dsphere("projectPosition"));
        logger.info(mm +
                " photo 2dSphere index on videos collection: " +
                E.RED_APPLE + result);

        String result2 = dbCollection.createIndex(Indexes.ascending("projectId"));
        logger.info(mm +
                " projectId index on videos collection: " +
                E.RED_APPLE + result2);

        String result3 = dbCollection.createIndex(Indexes.ascending("organizationId"));
        logger.info(mm +
                " organizationId index on videos collection: " +
                E.RED_APPLE + result3);

    }

    public void printOrganizations() {
        List<Organization> orgs = organizationRepository.findAll();
        for (Organization org : orgs) {
            List<User> users = userRepository.findByOrganizationId(org.getOrganizationId());
            logger.info(E.PEAR+E.PEAR+E.PEAR+ " Organization: " + org.getName());
            logger.info(E.PEAR+E.PEAR+E.PEAR+ " organizationId: " + org.getOrganizationId());
            for (User user : users) {
                logger.info(E.PEAR+E.PEAR + " user: " + user.getUserType() + " " + E.RED_APPLE
                        + "  " + user.getEmail() + " " + E.BLUE_DOT +"  " + user.getName());
                logger.info(E.PEAR+E.PEAR + " \tuserId: " + user.getUserId() + " " + E.RED_APPLE);
            }
            logger.info("\n\n");
        }
    }

}
