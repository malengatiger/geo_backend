package com.boha.geo.repos;

import com.boha.geo.monitor.data.ProjectPosition;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProjectPositionRepository extends MongoRepository<ProjectPosition, String> {

    List<ProjectPosition> findByPositionNear(Point location, Distance distance);
    List<ProjectPosition> findByProjectId(String projectId);

    List<ProjectPosition> findByOrganizationId(String organizationId);

}
