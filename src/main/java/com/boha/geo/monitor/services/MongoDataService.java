package com.boha.geo.monitor.services;

import com.boha.geo.monitor.data.City;
import com.boha.geo.repos.CityRepository;
import com.boha.geo.util.E;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;


@Service
public class MongoDataService {
    private static final Logger LOGGER = Logger.getLogger(MongoDataService.class.getSimpleName());
    private static final String xx = E.COFFEE+E.COFFEE+E.COFFEE;

    public MongoDataService() {
        LOGGER.info(xx+" MessageService constructed");
    }

    @Autowired
    CityRepository cityRepository;

    public List<City> getCities() {
        return cityRepository.findAll();
    }

    public List<City> getCitiesByLocation(Point location, Distance distance) {
        List<City> cities = cityRepository.findByPositionNear(location,distance);
        LOGGER.info(E.DICE + "Found " + cities.size()
                + " cities by location; radiusInKM = " + distance.getValue());

        return cities;
    }
}
