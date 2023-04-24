package com.boha.geo.services;

import com.boha.geo.monitor.data.City;
import com.boha.geo.repos.CityRepo;
import com.boha.geo.repos.CityRepository;
import com.boha.geo.util.E;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.near;
@RequiredArgsConstructor
@Service
public class CityService {

    final MongoClient mongoClient;
    final CityRepository cityRepo;


    private static final String xx = E.COFFEE+E.COFFEE+E.COFFEE;

    private static final Logger logger
            = Logger.getLogger(CityService.class.getSimpleName());
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting().create();

    public List<City> getCitiesNear(double latitude, double longitude,
                                    double minDistanceInMetres,
                                    double maxDistanceInMetres) throws Exception {

        MongoDatabase mongoDb = mongoClient.getDatabase("geo");
        MongoCollection<Document> cityCollection = mongoDb.getCollection("cities");
        Point myPoint = new Point(new Position(longitude, latitude));
        Bson query = near("cityLocation", myPoint,
                maxDistanceInMetres, minDistanceInMetres);
        final List<City> cities = new ArrayList<>();

        cityCollection.find(query)
                .forEach(doc -> {
                    String json = doc.toJson();
                    City city = gson.fromJson(json, City.class);
                    cities.add(city);
                });

        logger.info(E.PINK+E.PINK+E.PINK+"" + cities.size()
                + " cities found with min: " + minDistanceInMetres
                + " max: " + maxDistanceInMetres);

        Collections.sort(cities);
        HashMap<String, City> map = filter(cities);
        List<City> filteredCities = map.values().stream().toList();
        int count = 0;
        for (City place : filteredCities) {
            count++;
            logger.info(E.LEAF+E.LEAF+" City: #" + count + " " + E.RED_APPLE + " " + place.getName()
                    + ", " + place.getProvince());
        }


        return filteredCities;

    }

    public List<City> getCitiesInProvince(String province) throws Exception {

        List<City> cities = cityRepo.findByProvince(province);
        logger.info(E.GREEN_APPLE+E.GREEN_APPLE+E.GREEN_APPLE+
                " Cities found in province: " + province + " " + E.GREEN_APPLE);
        int count = 0;
        for (City place : cities) {
            count++;
            logger.info(E.LEAF+E.LEAF+" City: #" + count
                    + " " + E.RED_APPLE + " " + place.getName()
                    + ", " + place.getProvince());
        }
        return cities;
    }

    private static HashMap<String, City> filter(List<City> cities) {
        HashMap<String,City> map = new HashMap<>();
        for (City city : cities) {
            if (!map.containsKey(city.getName())) {
                map.put(city.getName(), city);
            }
        }
        return map;
    }
}
