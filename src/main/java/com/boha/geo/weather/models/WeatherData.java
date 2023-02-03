package com.boha.geo.weather.models;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Data
public class WeatherData {
    private static final Logger LOGGER = Logger.getLogger(WeatherData.class.getSimpleName());
    static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private float latitude;
    private float longitude;
    private float generationtime_ms;
    private float utc_offset_seconds;
    private String timezone;
    private String timezone_abbreviation;
    private float elevation;
    Hourly_units hourly_units;
    Hourly hourly;
    Daily_units daily_units;
    Daily daily;

    public List<DailyPacked> getDailyPacked() {
        List<DailyPacked> list = new ArrayList<DailyPacked>();

        for (String s : daily.time) {
            DailyPacked m = new DailyPacked();
            m.setTime(s);
            m.setDailyUnits(daily_units);
            list.add(m);
        }


        int index = 0;
        for (Integer wc : daily.weathercode) {
            DailyPacked m = list.get(index);
            m.setWeatherCode(wc);
            index++;
        }
        index = 0;
        for (Double wc : daily.temperature_2m_max) {
            DailyPacked m = list.get(index);
            m.setMaxTemperature(wc);
            index++;
        }
        index = 0;
        for (Double wc : daily.temperature_2m_min) {
            DailyPacked m = list.get(index);
            m.setMinTemperature(wc);
            index++;
        }
        index = 0;
        for (String wc : daily.sunrise) {
            DailyPacked m = list.get(index);
            m.setSunrise(wc);
            index++;
        }
        index = 0;
        for (String wc : daily.sunset) {
            DailyPacked m = list.get(index);
            m.setSunset(wc);
            index++;
        }
        index = 0;
        for (Double wc : daily.precipitation_sum) {
            DailyPacked m = list.get(index);
            m.setPrecipitationSum(wc);
            index++;
        }
        index = 0;
        for (Double wc : daily.precipitation_hours) {
            DailyPacked m = list.get(index);
            m.setPrecipitationHours(wc);
            index++;
        }
        index = 0;
        for (Double wc : daily.windspeed_10m_max) {
            DailyPacked m = list.get(index);
            m.setWindSpeedMax(wc);
            index++;
        }
        index = 0;
        for (Double wc : daily.rain_sum) {
            DailyPacked m = list.get(index);
            m.setRainSum(wc);
            index++;
        }
        index = 0;
        for (Double wc : daily.showers_sum) {
            DailyPacked m = list.get(index);
            m.setShowersSum(wc);
            index++;
        }
        index = 0;
        for (Double wc : daily.winddirection_10m_dominant) {
            DailyPacked m = list.get(index);
            m.setWindDirectionDominant(wc);
            index++;
        }
        index = 0;
        for (Double wc : daily.apparent_temperature_max) {
            DailyPacked m = list.get(index);
            m.setApparentMaxTemp(wc);
            index++;
        }
        index = 0;
        for (Double wc : daily.apparent_temperature_min) {
            DailyPacked m = list.get(index);
            m.setApparentMinTemp(wc);
            index++;
        }
        index = 0;
        for (Double wc : daily.shortwave_radiation_sum) {
            DailyPacked m = list.get(index);
            m.setShortwaveRadiation(wc);
            index++;
        }
        index = 0;
        for (Double wc : daily.et0_fao_evapotranspiration) {
            DailyPacked m = list.get(index);
            m.setEvapoTranspiration(wc);
            index++;
        }



        return list;
    }

    public List<HourlyPacked> getHourlyPacked() {
        List<HourlyPacked> list = new ArrayList<>();
        for (Double o : hourly.getTemperature_2m()) {
            HourlyPacked hp = new HourlyPacked();
            hp.hourlyUnits = hourly_units;
            hp.setTemperature(o);
            list.add(hp);

        }
        int index = 0;
        for (Double o : hourly.getRain()) {
            HourlyPacked hp = list.get(index);
            hp.setRain(o);
            index++;
        }
        index = 0;
        for (Double o : hourly.getShowers()) {
            HourlyPacked hp = list.get(index);
            hp.setShowers(o);
            index++;
        }
        index = 0;
        for (String o : hourly.getTime()) {
            HourlyPacked hp = list.get(index);
            hp.setTime(o);
            index++;
        }
        index = 0;
        for (Integer o : hourly.getCloudcover()) {
            HourlyPacked hp = list.get(index);
            hp.setCloudCover(o);
            index++;
        }
        index = 0;
        for (Double o : hourly.getWindspeed_10m()) {
            HourlyPacked hp = list.get(index);
            hp.setWindSpeed(o);
            index++;
        }
        index = 0;
        for (Integer o : hourly.getRelativehumidity_2m()) {
            HourlyPacked hp = list.get(index);
            hp.setRelativeHumidity(o);
            index++;
        }
        index = 0;
        for (Double o : hourly.getPressure_msl()) {
            HourlyPacked hp = list.get(index);
            hp.setPressure(o);
            index++;
        }

        return list;

    }


}
