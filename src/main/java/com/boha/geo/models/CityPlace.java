package com.boha.geo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "cityPlaces")

public class CityPlace implements Comparable<CityPlace>{
    private String name;
    private String placeId;
    private ArrayList<String> types;
    private String vicinity;
    private String cityId, cityName, province;
    private Geometry geometry;
    private CityPlaceLocation cityPlaceLocation;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public ArrayList<String> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<String> types) {
        this.types = types;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public CityPlaceLocation getCityPlaceLocation() {
        return cityPlaceLocation;
    }

    public void setCityPlaceLocation(CityPlaceLocation cityPlaceLocation) {
        this.cityPlaceLocation = cityPlaceLocation;
    }

    @Override
    public int compareTo(CityPlace o) {
        return this.name.compareTo(o.name);
    }
}



