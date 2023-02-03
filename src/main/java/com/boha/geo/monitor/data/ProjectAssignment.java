package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("projectAssignments")
public class ProjectAssignment {

    private String organizationId, projectId, projectName, userId, userName, adminId, adminName;
    private String projectAssignmentId, date, updated;
    private int active;

}
