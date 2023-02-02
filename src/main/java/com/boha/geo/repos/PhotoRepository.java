package com.boha.geo.repos;

import com.boha.geo.monitor.data.Photo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PhotoRepository extends MongoRepository<Photo, String> {

    List<Photo> findByProjectId(String projectId);
    List<Photo> findByOrganizationId(String organizationId);
    List<Photo> findByUserId(String userId);

    Photo findByPhotoId(String photoId);


}
