package com.boha.geo.weather.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
@Data
public class Hourly {
    private static final Logger LOGGER = Logger.getLogger(WeatherData.class.getSimpleName());
    static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    Hourly_units hourly_units;
    ArrayList<String> time = new ArrayList<>();
    ArrayList<Double> temperature_2m = new ArrayList<>();
    ArrayList<Integer> relativehumidity_2m = new ArrayList<>();
    ArrayList<Double> rain = new ArrayList<>();
    ArrayList<Double> showers = new ArrayList<>();
    ArrayList<Double> pressure_msl = new ArrayList<>();
    ArrayList<Double> surface_pressure = new ArrayList<>();
    ArrayList<Integer> cloudcover = new ArrayList<>();
    ArrayList<Integer> cloudcover_low = new ArrayList<>();
    ArrayList<Integer> cloudcover_mid = new ArrayList<>();
    ArrayList<Integer> cloudcover_high = new ArrayList<>();
    ArrayList<Double> windspeed_10m = new ArrayList<>();
    ArrayList<Double> windspeed_80m = new ArrayList<>();
    ArrayList<Double> windspeed_120m = new ArrayList<>();
    ArrayList<Double> windspeed_180m = new ArrayList<>();
    ArrayList<Integer> winddirection_80m = new ArrayList<>();
    ArrayList<Double> temperature_80m = new ArrayList<>();
    ArrayList<Double> temperature_120m = new ArrayList<>();
    ArrayList<Double> temperature_180m = new ArrayList<>();



}
