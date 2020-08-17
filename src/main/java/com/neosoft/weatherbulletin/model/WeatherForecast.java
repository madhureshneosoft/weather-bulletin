package com.neosoft.weatherbulletin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Model class for the response give by the weather API
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherForecast {
    private int cod;
    private double message;
    private int cnt;
    @JsonProperty("list")
    private List<WeatherDetails> weatherDetails;
}
