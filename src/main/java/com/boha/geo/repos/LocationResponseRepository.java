package com.boha.geo.repos;

import com.boha.geo.monitor.data.City;
import com.boha.geo.monitor.data.LocationResponse;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LocationResponseRepository extends MongoRepository<LocationResponse, String> {


    GeoResults<LocationResponse> findByPositionNear(Point location, Distance distance);
    List<LocationResponse> findByUserId(String userId);
    List<LocationResponse> findByOrganizationId(String organizationId);
}
