package com.boha.geo.repos;

import com.boha.geo.monitor.data.GeofenceEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface GeofenceEventRepository extends MongoRepository<GeofenceEvent, String> {

    List<GeofenceEvent> findByProjectPositionId(String projectPositionId);
    List<GeofenceEvent> findByOrganizationId(String organizationId);

    List<GeofenceEvent> findByProjectId(String projectId);

}
