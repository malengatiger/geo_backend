package com.boha.geo.repos;

import com.boha.geo.monitor.data.OrgMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface OrgMessageRepository extends MongoRepository<OrgMessage, String> {

    List<OrgMessage> findByOrganizationId(String organizationId);
    List<OrgMessage> findByProjectId(String projectId);
    List<OrgMessage> findByUserId(String userId);

}
