package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "projectPolygons")
public class ProjectPolygon {

    private String projectId;
    private String projectPolygonId;
    private String organizationId;
    private List<Position> positions;
    private String projectName, name;
    private String created;
    private List<City> nearestCities;
    private String userId;
            private String userName;
    private String translatedMessage;
    private String translatedTitle;



}
