package com.boha.geo.repos;

import com.boha.geo.monitor.data.Rating;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RatingRepository extends MongoRepository<Rating, String> {

    List<Rating> findByProjectId(String projectId);
    List<Rating> findByUserId(String userId);
    List<Rating> findByOrganizationId(String organizationId);
    List<Rating> findByPhotoId(String photoId);
    List<Rating> findByVideoId(String videoId);
    List<Rating> findByAudioId(String audioId);

}
