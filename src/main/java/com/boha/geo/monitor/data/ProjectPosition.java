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
    private String caption, name;
    private String created;
    private PlaceMark placemark;
    private List<City> nearestCities;

    public ProjectPosition() {
    }

}
