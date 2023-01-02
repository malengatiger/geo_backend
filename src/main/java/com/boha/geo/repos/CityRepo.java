package com.boha.geo.repos;

import com.boha.geo.models.City;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CityRepo extends MongoRepository<City, String> {
    public City findByName(String name);
    public List<City> findByProvince(String province);
}

