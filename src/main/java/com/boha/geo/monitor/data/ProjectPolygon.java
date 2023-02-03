package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "projectPolygons")
public class ProjectPolygon {

    private String projectId, projectPolygonId,
            organizationId;
    private List<Position> positions;
    private String projectName, name;
    private String created;
    private List<City> nearestCities;

    public ProjectPolygon() {
    }


}
