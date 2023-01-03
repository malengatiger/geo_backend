package com.boha.geo.repos;

import com.boha.geo.monitor.data.Condition;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ConditionRepository extends MongoRepository<Condition, String> {

    List<Condition> findByProjectId(String projectId);

}
