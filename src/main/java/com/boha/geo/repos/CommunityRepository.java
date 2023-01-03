package com.boha.geo.repos;

import com.boha.geo.monitor.data.Community;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CommunityRepository extends MongoRepository<Community, String> {

    List<Community> findByCountryId(String countryId);
}
