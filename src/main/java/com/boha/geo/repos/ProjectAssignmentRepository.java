package com.boha.geo.repos;

import com.boha.geo.monitor.data.ProjectAssignment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProjectAssignmentRepository extends MongoRepository<ProjectAssignment, String> {

    List<ProjectAssignment> findByProjectId(String projectId);
    List<ProjectAssignment> findByOrganizationId(String organizationId);
    List<ProjectAssignment> findByUserId(String userId);

}
