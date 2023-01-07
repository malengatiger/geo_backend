package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "fieldMonitorSchedules")
public class FieldMonitorSchedule {
    private String adminId, projectId, fieldMonitorScheduleId,
            date, organizationId, fieldMonitorId, fieldMonitorName;
    private String projectName, organizationName, userId;
    private int perDay, perWeek, perMonth;


}
