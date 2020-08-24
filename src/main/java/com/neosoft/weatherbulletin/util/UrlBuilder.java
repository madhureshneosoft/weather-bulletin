package com.neosoft.weatherbulletin.util;

import com.neosoft.weatherbulletin.exception.InvalidException;
import com.neosoft.weatherbulletin.model.Details;

/**
 * URL builder depending upon given parameters in payload
 */
public class UrlBuilder {

    private UrlBuilder() { }

    private static final String URL = "https://api.openweathermap.org/data/2.5/forecast?q=";

    public static String urlBuilder(Details requestPayload) {
        StringBuilder url = new StringBuilder().append(URL);

        String apiKey = "&appid="+requestPayload.getApiKey();
        String cityName = requestPayload.getCityName();
        String stateCode = requestPayload.getStateCode();
        String countryCode = requestPayload.getCountryCode();

        if(stateCode!=null && countryCode!=null && cityName!=null){
            url.append(cityName).append(",").append(stateCode).append(",").append(countryCode).append(apiKey);
        } else {
            if(cityName!=null){
                url.append(cityName).append(",");
            }
            if(stateCode!=null){
                url.append(stateCode).append(",");
            }
            if(countryCode!=null){
                url.append(countryCode).append(",");
            }
            url.append(apiKey);
        }
        return url.toString();
    }
}
