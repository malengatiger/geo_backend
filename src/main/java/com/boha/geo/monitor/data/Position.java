package com.boha.geo.monitor.data;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


public class Position {
    String type;
    List<Double> coordinates;

    public Position(String type, List<Double> coordinates) {
        this.type = type;
        this.coordinates = coordinates;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }
}
