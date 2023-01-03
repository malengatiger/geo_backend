package com.boha.geo.monitor.data;

import com.boha.geo.models.CityLocation;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "cities")
public class City implements Comparable<City> {

    private String  _partitionKey;
    private String  name;
    private String  cityId;
    private String country;
    private String province;
    private CityLocation cityLocation;

    public City() {
    }


    @Override
    public int compareTo(City o) {
        return this.name.compareTo(o.name);
    }
}
