package com.boha.geo.repos;

import com.boha.geo.monitor.data.FieldMonitorSchedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface FieldMonitorScheduleRepository extends MongoRepository<FieldMonitorSchedule, String> {

    List<FieldMonitorSchedule> findByProjectId(String projectId);

    List<FieldMonitorSchedule> findByUserId(String userId);
    List<FieldMonitorSchedule> findByOrganizationId(String organizationId);
    List<FieldMonitorSchedule> findByFieldMonitorId(String userId);
    List<FieldMonitorSchedule> findByAdminId(String userId);

}
