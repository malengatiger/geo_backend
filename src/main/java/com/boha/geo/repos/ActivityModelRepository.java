package com.boha.geo.repos;

import com.boha.geo.monitor.data.ActivityModel;
import com.boha.geo.monitor.data.Photo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ActivityModelRepository extends MongoRepository<ActivityModel, String> {

    List<ActivityModel> findByProjectId(String projectId);
    List<ActivityModel> findByOrganizationId(String organizationId);
    List<ActivityModel> findByUserId(String userId);

}
