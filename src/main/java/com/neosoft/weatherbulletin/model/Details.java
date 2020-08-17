package com.neosoft.weatherbulletin.model;

import lombok.Data;

@Data
public class RequestPayload {
    private String from;
    private String to;
    private String cityName;
    private String stateCode;
    private String countryCode;
}
