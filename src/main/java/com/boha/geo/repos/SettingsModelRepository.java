package com.boha.geo.repos;

import com.boha.geo.monitor.data.Audio;
import com.boha.geo.monitor.data.SettingsModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SettingsModelRepository extends MongoRepository<SettingsModel, String> {

    List<SettingsModel> findByProjectId(String projectId);
    List<SettingsModel> findByOrganizationId(String organizationId);

}
