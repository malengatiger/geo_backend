package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "projectCounts")

public class ProjectCount {

    private String projectId;
    private int photos;
    private int videos;
    private int audios;
    private int schedules;
    private int projectPositions;
    private int projectPolygons;

    private String date, projectName, organizationId;


}
