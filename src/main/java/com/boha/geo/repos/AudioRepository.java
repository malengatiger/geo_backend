package com.boha.geo.repos;

import com.boha.geo.monitor.data.ActivityModel;
import com.boha.geo.monitor.data.Audio;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface AudioRepository extends MongoRepository<Audio, String> {

    List<Audio> findByProjectId(String projectId);
    List<Audio> findByUserId(String userId);
    List<Audio> findByOrganizationId(String organizationId);
    Audio findByAudioId(String audioId);

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

    @Query(value = "{userId: ?0,  created: { $gte: ?1, $lt: ?2 } }")
    List<Audio> findByUserPeriod(String userId, String startDate, String endDate);
    @Query(value = "{projectId: ?0,  created: { $gte: ?1, $lt: ?2 } }")
    List<Audio> findByProjectPeriod(String projectId, String startDate, String endDate);
    @Query(value = "{organizationId: ?0,  created: { $gte: ?1, $lt: ?2 } }")
    List<Audio> findByOrganizationPeriod(String organizationId, String startDate, String endDate);

}
