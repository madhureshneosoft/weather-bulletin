package com.neosoft.weatherbulletin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Weather Forecast including average max/min temperature & humidity for a Day
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    private int day;
    private String cityName;
    private double workHoursMaxTemperature;
    private double workHoursMinTemperature;
    private double workHoursHumidity;
    private double nonWorkHoursMinTemperature;
    private double nonWorkHoursMaxTemperature;
    private double nonWorkHoursHumidity;
}
