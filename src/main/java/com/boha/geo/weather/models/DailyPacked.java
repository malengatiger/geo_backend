package com.boha.geo.weather.models;

import lombok.Data;

@Data
public class DailyPacked {
    public DailyPacked() {
    }

    Daily_units dailyUnits;
    String time;
    int weatherCode;
    double minTemperature, maxTemperature;
    double apparentMinTemp, apparentMaxTemp;
    String sunrise, sunset;
    double precipitationSum, rainSum, showersSum;
    double precipitationHours, windSpeedMax;
    Double windDirectionDominant;
    double shortwaveRadiation, evapoTranspiration;


}
