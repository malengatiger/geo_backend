package com.boha.geo.repos;

import com.boha.geo.models.User;
import com.boha.geo.monitor.data.City;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepo extends MongoRepository<User, String> {
    public City findByLastName(String name);
    public List<User> findByCityId(String cityId);
}

