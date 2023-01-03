package com.boha.geo.repos;

import com.boha.geo.monitor.data.Project;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface ProjectRepository extends MongoRepository<Project, String> {

    List<Project> findByOrganizationId(String organizationId);
    Project findByProjectId(String projectId);

}
