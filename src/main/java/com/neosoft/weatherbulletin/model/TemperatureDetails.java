package com.neosoft.weatherbulletin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Parameters of the Forecast like temperature and humidity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemperatureDetails {
    @JsonProperty("temp")
    private double temperature;

    @JsonProperty("temp_min")
    private double temperatureMinimum;

    @JsonProperty("temp_max")
    private double temperatureMaximum;

    @JsonProperty("pressure")
    private double pressure;

    @JsonProperty("sea_level")
    private double seaLevel;

    @JsonProperty("grnd_level")
    private double groundLevel;

    @JsonProperty("humidity")
    private double humidity;

    @JsonProperty("temp_kf")
    private double temperatureKf;
}
