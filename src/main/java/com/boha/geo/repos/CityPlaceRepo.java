package com.boha.geo.repos;

import com.boha.geo.models.CityPlace;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CityPlaceRepo extends MongoRepository<CityPlace, String> {
    public CityPlace findByName(String name);
    public CityPlace findByPlaceId(String placeId);
    public List<CityPlace> findByCityId(String cityId);
    public List<CityPlace> findByCityName(String name);


    public List<CityPlace> findByCityPlaceLocationNear(Point location, Distance distance);
}

