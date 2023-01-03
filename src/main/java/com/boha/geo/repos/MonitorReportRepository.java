package com.boha.geo.repos;

import com.boha.geo.monitor.data.MonitorReport;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MonitorReportRepository extends MongoRepository<MonitorReport, String> {

    List<MonitorReport> findByPositionNear(Point location, Distance distance);
    List<MonitorReport> findByProjectId(String projectId);

}
