package com.boha.geo.repos;

import com.boha.geo.monitor.data.ActivityModel;
import com.boha.geo.monitor.data.LocationRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LocationRequestRepository extends MongoRepository<LocationRequest, String> {

    List<LocationRequest> findByRequesterId(String requesterId);
    List<LocationRequest> findByOrganizationId(String organizationId);
    List<LocationRequest> findByUserId(String userId);

}
