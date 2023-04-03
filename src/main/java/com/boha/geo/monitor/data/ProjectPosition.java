package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Data
@Document(collection = "projectPositions")
public class ProjectPosition {

    private String projectId;
    private String projectPositionId;
    private String organizationId;
    private Position position;
    private String projectName;
    private String caption;
    private String name;
    private String created;
    private PlaceMark placemark;
    private List<City> nearestCities;
    private String userId;
    private String userName;
    private String possibleAddress;
    private String translatedMessage;
    private String translatedTitle;


    public ProjectPosition() {
    }

}
