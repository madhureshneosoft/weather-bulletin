package com.neosoft.weatherbulletin.model;

import lombok.Data;

/**
 * Request Payload By User
 */
@Data
public class Details {
    private String workTimeStart;
    private String workTimeEnd;
    private String cityName;
    private String stateCode;
    private String countryCode;
    private String apiKey;
}
