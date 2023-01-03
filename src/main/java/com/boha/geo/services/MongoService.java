package com.boha.geo.services;

import com.boha.geo.models.City;
import com.boha.geo.models.CityLocation;
import com.boha.geo.repos.CityRepo;
import com.boha.geo.util.E;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
    private static final String mm = E.RAIN + E.RAIN + E.RAIN;
    private static final String xx = E.COFFEE+E.COFFEE+E.COFFEE;
    @Autowired
    private CityRepo cityRepo;

    public MongoService() {
        logger.info(xx + " MongoService constructed ");
    }

    @Autowired
    private ResourceLoader resourceLoader;


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
            CityLocation loc = new CityLocation();
            loc.setType("Point");
            List<Double> list = new ArrayList<>();
            list.add(longitude);
            list.add(latitude);
            loc.setCoordinates(list);
            city.setCityLocation(loc);
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


}
