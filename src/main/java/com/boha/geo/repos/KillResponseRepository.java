package com.boha.geo.repos;

import com.boha.geo.models.KillResponse;
import com.boha.geo.monitor.data.Audio;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface KillResponseRepository extends MongoRepository<KillResponse, String> {

    List<KillResponse> findByOrganizationId(String organizationId);

}
