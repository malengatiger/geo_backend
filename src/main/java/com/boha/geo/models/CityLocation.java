package com.boha.geo.models;

import java.util.List;

public class CityLocation {
    public CityLocation(String type, List<Double> coordinates) {
        this.type = type;
        this.coordinates = coordinates;
    }

    public CityLocation() {
    }

    private String type = "Point";
    private List<Double> coordinates;

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
