package com.boha.geo.repos;

import com.boha.geo.monitor.data.FieldMonitorSchedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface FieldMonitorScheduleRepository extends MongoRepository<FieldMonitorSchedule, String> {

    List<FieldMonitorSchedule> findByProjectId(String projectId);

    List<FieldMonitorSchedule> findByUserId(String userId);
    List<FieldMonitorSchedule> findByOrganizationId(String organizationId);
    List<FieldMonitorSchedule> findByFieldMonitorId(String userId);
    List<FieldMonitorSchedule> findByAdminId(String userId);

    @Query(value = "{userId: ?0}", count = true)
    public long countByUser(String userId);
    @Query(value = "{userId: ?0,  created: { $gte: ?1, $lt: ?2 } }", count = true)
    long countByUserPeriod(String userId, String startDate, String endDate);

    @Query(value = "{projectId: ?0,  created: { $gte: ?1, $lt: ?2 } }", count = true)
    public long countByProjectPeriod(String projectId, String startDate, String endDate);
    @Query(value = "{organizationId: ?0,  created: { $gte: ?1, $lt: ?2 } }", count = true)
    public long countByOrganizationPeriod(String organizationId, String startDate, String endDate);

    @Query(value = "{projectId: ?0,  created: { $gt: ?1 } }", count = true)
    public long countByTimeAndProject(String projectId, String created);

    @Query(value = "{organizationId: ?0,  created: { $gt: ?1 } }", count = true)
    public long countByTimeAndOrganization(String organizationId, String created);

    @Query(value = "{projectId: ?0 }", count = true)
    public long countByProject(String projectId);

}
