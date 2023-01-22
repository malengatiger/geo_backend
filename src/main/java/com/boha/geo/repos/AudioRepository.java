package com.boha.geo.repos;

import com.boha.geo.monitor.data.Audio;
import com.boha.geo.monitor.data.Video;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AudioRepository extends MongoRepository<Audio, String> {

    List<Audio> findByProjectId(String projectId);
    List<Audio> findByUserId(String userId);
    List<Audio> findByOrganizationId(String organizationId);

}
