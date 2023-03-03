package com.boha.geo.repos;

import com.boha.geo.monitor.data.ProjectPosition;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ProjectPositionRepository extends MongoRepository<ProjectPosition, String> {

    List<ProjectPosition> findByPositionNear(Point location, Distance distance);
    List<ProjectPosition> findByProjectId(String projectId);
    ProjectPosition findByProjectPositionId(String projectPositionId);
    List<ProjectPosition> findByOrganizationId(String organizationId);

    @Query(value = "{userId: ?0}", count = true)
    public long countByUser(String userId);
    @Query(value = "{projectId: ?0,  created: { $gt: ?1 } }", count = true)
    public long countByTimeAndProject(String projectId, String created);

    @Query(value = "{organizationId: ?0,  created: { $gt: ?1 } }", count = true)
    public long countByTimeAndOrganization(String organizationId, String created);

}
