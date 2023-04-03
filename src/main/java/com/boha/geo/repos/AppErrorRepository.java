package com.boha.geo.repos;

import com.boha.geo.models.AppError;
import com.boha.geo.monitor.data.ActivityModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface AppErrorRepository extends MongoRepository<AppError, String> {

    List<AppError> findByOrganizationId(String organizationId);

    List<AppError> findByUserId(String userId);

}
