package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "projectSummaries")

public class ProjectSummary {

    private String projectId;
    private long photos;
    private long videos;
    private long audios;
    private long schedules;
    private long projectPositions;
    private long projectPolygons;
    private int calculatedHourly;

    private String date, projectName, organizationId, organizationName;


}
