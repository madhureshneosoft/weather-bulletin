package com.neosoft.weatherbulletin.model;

import lombok.Data;

/**
 * Request Payload By User
 */
@Data
public class Details {
    private String from;
    private String to;
    private String cityName;
    private String stateCode;
    private String countryCode;
}
