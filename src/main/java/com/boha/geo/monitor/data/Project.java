package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Data
@Document(collection = "projects")
public class Project {
    private String _partitionKey;
    private String projectId;
    private String name;
    private String organizationId;
    private String description;
    private String organizationName;
    private double monitorMaxDistanceInMetres;
    private String created;
    private List<City> nearestCities;

    public Project() {
    }

    public Project(String _partitionKey, String projectId, String name, String organizationId,
                   String description, String organizationName, double monitorMaxDistanceInMetres,
                   String created, List<City> nearestCities) {
        this._partitionKey = _partitionKey;
        this.projectId = projectId;
        this.name = name;
        this.organizationId = organizationId;
        this.description = description;
        this.organizationName = organizationName;
        this.monitorMaxDistanceInMetres = monitorMaxDistanceInMetres;
        this.created = created;
        this.nearestCities = nearestCities;
    }
}
