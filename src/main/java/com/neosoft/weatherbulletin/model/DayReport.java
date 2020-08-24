package com.neosoft.weatherbulletin.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DayReport {
    private int day;

    private List<Double> avgWorkHourMaxTemp = new ArrayList<>();
    private List<Double> avgWorkHourMinTemp = new ArrayList<>();
    private List<Double> avgWorkHourHumidity = new ArrayList<>();

    private List<Double> avgNonWorkHourMaxTemp = new ArrayList<>();
    private List<Double> avgNonWorkHourMinTemp = new ArrayList<>();
    private List<Double> avgNonWorkHourHumidity = new ArrayList<>();
}
