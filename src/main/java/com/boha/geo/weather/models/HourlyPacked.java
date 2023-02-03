package com.boha.geo.weather.models;

import lombok.Data;

@Data
public class HourlyPacked {
    public HourlyPacked() {
    }

    Hourly_units hourlyUnits;
    String time;
    double temperature;
    int relativeHumidity;
    double rain;
    double showers;
    double pressure, surfacePressure;
    int cloudCover;
    double windSpeed;


}
