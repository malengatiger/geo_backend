package com.boha.geo.repos;

import com.boha.geo.monitor.data.AppError;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AppErrorRepository extends MongoRepository<AppError, String> {

    List<AppError> findByOrganizationId(String organizationId);

    List<AppError> findByUserId(String userId);

}
