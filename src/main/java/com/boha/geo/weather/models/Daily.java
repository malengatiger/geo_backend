package com.boha.geo.weather.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
@Data
public class Daily {
    private static final Logger LOGGER = Logger.getLogger(Daily.class.getSimpleName());
    static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    Daily_units daily_units;
    ArrayList<String> time = new ArrayList<>();
    ArrayList<Integer> weathercode = new ArrayList<>();
    ArrayList<Double> temperature_2m_max = new ArrayList<>();
    ArrayList<Double> temperature_2m_min = new ArrayList<>();
    ArrayList<Double> apparent_temperature_max = new ArrayList<>();
    ArrayList<Double> apparent_temperature_min = new ArrayList<>();
    ArrayList<String> sunrise = new ArrayList<>();
    ArrayList<String> sunset = new ArrayList<>();
    ArrayList<Double> precipitation_sum = new ArrayList<>();
    ArrayList<Double> rain_sum = new ArrayList<>();
    ArrayList<Double> showers_sum = new ArrayList<>();
    ArrayList<Double> snowfall_sum = new ArrayList<>();
    ArrayList<Double> precipitation_hours = new ArrayList<>();
    ArrayList<Double> windspeed_10m_max = new ArrayList<>();
    ArrayList<Double> windgusts_10m_max = new ArrayList<>();
    ArrayList<Double> winddirection_10m_dominant = new ArrayList<Double>();
    ArrayList<Double> shortwave_radiation_sum = new ArrayList<>();
    ArrayList<Double> et0_fao_evapotranspiration = new ArrayList<>();



}
