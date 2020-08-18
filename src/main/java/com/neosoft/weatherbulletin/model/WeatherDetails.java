package com.neosoft.weatherbulletin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherDetails {

    @JsonProperty("main")
    private TemperatureDetails temperatureDetails;

    @JsonProperty("dt_txt")
    private String date;
}
