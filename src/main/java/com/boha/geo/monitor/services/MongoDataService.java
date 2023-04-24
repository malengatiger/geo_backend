package com.boha.geo.monitor.services;

import com.boha.geo.monitor.data.City;
import com.boha.geo.repos.CityRepository;
import com.boha.geo.util.E;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RequiredArgsConstructor
@Service
public class MongoDataService {
    private static final Logger LOGGER = Logger.getLogger(MongoDataService.class.getSimpleName());
    private static final String xx = E.COFFEE+E.COFFEE+E.COFFEE;

    private final CityRepository cityRepository;

    public List<City> getCities() {
        return cityRepository.findAll();
    }

    public List<City> getCitiesByLocation(Point location, Distance distance) {
        GeoResults<City> cities = cityRepository.findByCityLocationNear(location,distance);
        LOGGER.info(E.DICE + "Found " + cities.getContent().size()
                + " cities by location; radiusInKM = " + distance.getValue());
        List<City> mList = new ArrayList<>();
        for (GeoResult<City> city : cities) {
            mList.add(city.getContent());
        }

        return mList;
    }
}
