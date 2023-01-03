package com.boha.geo.controllers;

import com.boha.geo.GeoApplication;
import com.boha.geo.models.CityPlace;
import com.boha.geo.monitor.data.City;
import com.boha.geo.services.CityService;
import com.boha.geo.services.MongoService;
import com.boha.geo.services.PlacesService;
import com.boha.geo.services.UserService;
import com.boha.geo.util.E;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

@RestController
public class MainController {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger logger = Logger.getLogger(GeoApplication.class.getSimpleName());
    private static final String alien = E.AMP+E.AMP+E.AMP;
    private static final String xx = E.COFFEE+E.COFFEE+E.COFFEE;


    final PlacesService placesService;
    final MongoService mongoService;
    final UserService userService;
    final CityService cityService;

    public MainController(PlacesService placesService, MongoService mongoService,
                          UserService userService, CityService cityService) {
        this.placesService = placesService;
        this.mongoService = mongoService;
        this.userService = userService;
        this.cityService = cityService;

        logger.info(xx+" MainController constructed and services injected");
    }

    @GetMapping("/addUsers")
    private ResponseEntity<Object> addUsers() {
        try {
            int users = userService.addUsers();
            logger.info(E.BLUE_HEART + E.BLUE_HEART + E.CHECK +
                    " MainController Returning, " + users + " users");
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }
    @GetMapping("/getPlacesNear")
    private ResponseEntity<Object> getPlacesNear(@RequestParam double latitude,
                                                 @RequestParam double longitude,
                                                 @RequestParam double minDistanceInMetres,
                                                 @RequestParam double maxDistanceInMetres) {
        try {
            List<CityPlace> places = placesService.getPlacesNear(latitude,longitude,
                    minDistanceInMetres,maxDistanceInMetres);
            logger.info(E.BLUE_HEART + E.BLUE_HEART + E.CHECK +
                    " MainController Returning, " + places.size() + " places");
            return ResponseEntity.ok(places);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/getCitiesNear")
    private ResponseEntity<Object> getCitiesNear(@RequestParam double latitude,
                                                 @RequestParam double longitude,
                                                 @RequestParam double minDistanceInMetres,
                                                 @RequestParam double maxDistanceInMetres) {
        try {
            List<City> citiesNear = cityService.getCitiesNear(latitude,longitude,
                    minDistanceInMetres,maxDistanceInMetres);
            logger.info(E.BLUE_HEART + E.BLUE_HEART + E.CHECK +
                    " MainController Returning, " + citiesNear.size() + " cities");
            return ResponseEntity.ok(citiesNear);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/getCitiesInProvince")
    private ResponseEntity<Object> getCitiesInProvince(@RequestParam String province) {
        try {
            List<City> citiesInProvince
                    = cityService.getCitiesInProvince(province);
            logger.info(E.BLUE_HEART + E.BLUE_HEART + E.CHECK +
                    " MainController Returning, " + citiesInProvince.size() + " Cities In Province");
            return ResponseEntity.ok(citiesInProvince);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/getPlacesByCityName")
    private ResponseEntity<Object> getPlacesByCityName(@RequestParam String city) {
        try {
            List<CityPlace> placesByCityName
                    = placesService.getPlacesByCityName(city);
            logger.info(E.BLUE_HEART + E.BLUE_HEART + E.CHECK +
                    " MainController Returning, " + placesByCityName.size() + " places in " + city);
            return ResponseEntity.ok(placesByCityName);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/getPlacesByCityNameAndType")
    private ResponseEntity<Object> getPlacesByCityNameAndType(
            @RequestParam String city, @RequestParam String type) {
        try {
            List<CityPlace> placesByCityName
                    = placesService.getPlacesByCityNameAndType(city,type);
            logger.info(E.BLUE_HEART + E.BLUE_HEART + E.CHECK +
                    " MainController Returning, " + placesByCityName.size() + " places in "
                    + city + " - type: " + type);
            return ResponseEntity.ok(placesByCityName);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/countPlaces")
    private ResponseEntity<Object> countPlaces() {
        try {
            long placesByCount
                    = placesService.countPlaces();
            logger.info(E.BLUE_HEART + E.BLUE_HEART + E.CHECK +
                    " MainController Returning, places counted: " + placesByCount);
            return ResponseEntity.ok(placesByCount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/addCities")
    private ResponseEntity<Object> addCities() {
        try {
            List<City> cities = mongoService.addCitiesToDB();
            logger.info(E.BLUE_HEART + E.BLUE_HEART + E.CHECK +
                    " MainController Returning " + cities.size() + " cities");
            return ResponseEntity.ok(cities);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }
    @GetMapping("/addCityPlaces")
    private ResponseEntity<Object> addCityPlaces() {
        try {
            String result = placesService.loadCityPlaces();
            logger.info(E.BLUE_HEART + E.BLUE_HEART + E.CHECK +
                    " MainController Returning " + result + " " + E.RED_APPLE);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    @GetMapping("/loadSpecialCityPlaces")
    private ResponseEntity<Object> loadSpecialCityPlaces() {
        try {
            String result = placesService.loadSpecialCityPlaces();
            logger.info(E.BLUE_HEART + E.BLUE_HEART + E.CHECK +
                    " MainController Returning " + result + " " + E.RED_APPLE);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

}
