package com.boha.geo.repos;

import com.boha.geo.monitor.data.ProjectSummary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ProjectSummaryRepository extends MongoRepository<ProjectSummary, String> {

    List<ProjectSummary> findByProjectId(String projectId);

    @Query(value = "{projectId: ?0,  created: { $gte: ?1, $lt: ?2 } }", count = true)
    List<ProjectSummary> findByProjectInPeriod(String projectId, String startDate, String endDate);

    @Query(value = "{organizationId: ?0,  created: { $gte: ?1, $lt: ?2 } }", count = true)
    List<ProjectSummary> findByOrganizationInPeriod(String organizationId, String startDate, String endDate);

    List<ProjectSummary> findByOrganizationId(String organizationId);

    @Query(value = "{projectId: ?0,  created: { $gt: ?1 } }", count = true)
    long countByTimeAndProject(String projectId, String created);

    @Query(value = "{projectId: ?0 }", count = true)
    long countByProject(String projectId);

    @Query(value = "{organizationId: ?0,  created: { $gt: ?1 } }", count = true)
    long countByTimeAndOrganization(String organizationId, String created);

}
