package com.boha.geo.repos;

import com.boha.geo.monitor.data.Video;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface VideoRepository extends MongoRepository<Video, String> {

    List<Video> findByProjectId(String projectId);
    List<Video> findByUserId(String userId);
    List<Video> findByOrganizationId(String organizationId);

}
