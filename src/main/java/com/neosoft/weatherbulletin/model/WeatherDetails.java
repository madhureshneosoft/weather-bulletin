package com.neosoft.weatherbulletin.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherDetails {

    @JsonProperty("main")
    private Temperature temperature;

    @JsonProperty("dt_txt")
    private String date;
}
