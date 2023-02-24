package com.boha.geo.repos;

import com.boha.geo.monitor.data.ProjectPolygon;
import com.boha.geo.monitor.data.ProjectPosition;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProjectPolygonRepository extends MongoRepository<ProjectPolygon, String> {

    List<ProjectPolygon> findByProjectId(String projectId);

    List<ProjectPolygon> findByOrganizationId(String organizationId);

    ProjectPolygon findByProjectPolygonId(String projectPolygonId);

}
