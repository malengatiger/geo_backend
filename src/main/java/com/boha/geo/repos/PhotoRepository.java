package com.boha.geo.repos;

import com.boha.geo.monitor.data.Photo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PhotoRepository extends MongoRepository<Photo, String> {

    List<Photo> findByProjectId(String projectId);
    List<Photo> findByOrganizationId(String organizationId);
    List<Photo> findByUserId(String userId);

    Photo findByPhotoId(String photoId);

//    @Query(value = "{userId: ?0}", count = true)
//    public long countByUser(String userId);

//    @Query(value = "{userId: ?0,  created: { $gte: ?1, $lt: ?2 } }")
//    List<Photo> getByUserPeriod(String userId, String startDate, String endDate);

    //{createdAt:{$gte:ISODate("2021-01-01"),$lt:ISODate("2020-05-01"}}
//    @Query(value = "{projectId: ?0,  created: { $gte: ?1, $lt: ?2 } }")
//    List<Photo> getByProjectPeriod(String projectId, String startDate, String endDate);
//    @Query(value = "{organizationId: ?0,  created: { $gte: ?1, $lt: ?2 } }")
//    List<Photo> getByOrganizationPeriod(String organizationId, String startDate, String endDate);
//
//    @Query(value = "{projectId: ?0,  created: { $gt: ?1 } }", count = true)
//    public long countByTimeAndProject(String projectId, String created);
//
//    @Query(value = "{projectId: ?0,  created: { $gte: ?1, $lt: ?2 } }", count = true)
//    public long countByPeriod(String projectId, String startDate, String endDate);
//
//    @Query(value = "{organizationId: ?0,  created: { $gt: ?1 } }", count = true)
//    public long countByTimeAndOrganization(String organizationId, String created);
//
//    @Query(value = "{userId: ?0,  created: { $gte: ?1, $lt: ?2 } }")
//    List<Photo> findByUserPeriod(String userId, String startDate, String endDate);
//    @Query(value = "{projectId: ?0,  created: { $gte: ?1, $lt: ?2 } }")
//    List<Photo> findByProjectPeriod(String projectId, String startDate, String endDate);
//    @Query(value = "{organizationId: ?0,  created: { $gte: ?1, $lt: ?2 } }")
//    List<Photo> findByOrganizationPeriod(String organizationId, String startDate, String endDate);
//

}
