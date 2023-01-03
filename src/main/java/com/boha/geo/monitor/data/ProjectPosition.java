package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Data
@Document(collection = "projectPositions")
public class ProjectPosition {

    private String projectId, projectPositionId,
            organizationId;
    private Position position;
    private String projectName;
    private String caption;
    private String created;
    private PlaceMark placemark;
    private List<City> nearestCities;

    public ProjectPosition() {
    }

    public ProjectPosition(String projectId, String projectPositionId, String organizationId,
                           Position position, String projectName, String caption,
                           String created, PlaceMark placemark, List<City> nearestCities) {
        this.projectId = projectId;
        this.projectPositionId = projectPositionId;
        this.organizationId = organizationId;
        this.position = position;
        this.projectName = projectName;
        this.caption = caption;
        this.created = created;
        this.placemark = placemark;
        this.nearestCities = nearestCities;
    }
}
