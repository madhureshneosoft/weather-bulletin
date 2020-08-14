package com.neosoft.weatherbulletin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Weather {
    private int cod;

    private double message;

    private int cnt;

    @JsonProperty("list")
    private List<WeatherDetails> weatherDetails;
}
