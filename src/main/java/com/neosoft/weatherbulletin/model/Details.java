package com.neosoft.weatherbulletin.model;

import lombok.Data;

/**
 * Request Payload By User
 */
@Data
public class Details {
    private String workTimeFrom;
    private String workTimeTo;
    private String cityName;
    private String stateCode;
    private String countryCode;
    private String apiKey;
}
