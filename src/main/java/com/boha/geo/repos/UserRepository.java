package com.boha.geo.repos;

import com.boha.geo.monitor.data.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {

    User findByEmail(String email);
    User findByUserId(String userId);
    List<User> findByOrganizationId(String organizationId);


    void deleteByUserId(String userId);
}
