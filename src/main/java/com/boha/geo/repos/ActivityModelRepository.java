package com.boha.geo.repos;

import com.boha.geo.monitor.data.ActivityModel;
import com.boha.geo.monitor.data.Photo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ActivityModelRepository extends MongoRepository<ActivityModel, String> {

    List<ActivityModel> findByProjectId(String projectId);
    List<ActivityModel> findByOrganizationId(String organizationId);
    List<ActivityModel> findByUserId(String userId);

    @Query(value = "{userId: ?0}", count = true)
    public long countByUser(String userId);

    @Query(value = "{userId: ?0,  date: { $gte: ?1, $lt: ?2 } }", count = true)
    long countByUserPeriod(String userId, String startDate, String endDate);

    @Query(value = "{projectId: ?0,  date: { $gte: ?1, $lt: ?2 } }", count = true)
     long countByProjectPeriod(String projectId, String startDate, String endDate);
    @Query(value = "{organizationId: ?0,  date: { $gte: ?1, $lt: ?2 } }", count = true)
     long countByOrganizationPeriod(String organizationId, String startDate, String endDate);

    @Query(value = "{userId: ?0,  date: { $gte: ?1, $lt: ?2 } }")
    List<ActivityModel> findByUserPeriod(String userId, String startDate, String endDate);
    @Query(value = "{projectId: ?0,  date: { $gte: ?1, $lt: ?2 } }")
    List<ActivityModel> findByProjectPeriod(String projectId, String startDate, String endDate);
    @Query(value = "{organizationId: ?0,  date: { $gte: ?1, $lt: ?2 } }")
    List<ActivityModel> findByOrganizationPeriod(String organizationId, String startDate, String endDate);
}
