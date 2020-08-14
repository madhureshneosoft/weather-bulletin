package com.neosoft.weatherbulletin.model;

import lombok.Data;

@Data
public class Report {
    private int day;
    private double workHoursMaxTemperature;
    private double workHoursMinTemperature;
    private double workHoursHumidity;
    private double nonWorkHoursMinTemperature;
    private double nonWorkHoursMaxTemperature;
    private double nonWorkHoursHumidity;
}
