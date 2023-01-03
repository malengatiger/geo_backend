package com.boha.geo.repos;

import com.boha.geo.monitor.data.Questionnaire;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface QuestionnaireRepository extends MongoRepository<Questionnaire, String> {

    List<Questionnaire> findByProjectId(String projectId);
    List<Questionnaire> findByOrganizationId(String organizationId);
}
