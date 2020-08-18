package com.neosoft.weatherbulletin.util;

import com.neosoft.weatherbulletin.model.Details;

/**
 * URL builder depending upon given parameters in payload
 */
public class UrlBuilder {

    private UrlBuilder() { }

    private static final String API_KEY = "e7c69e6bedbfb287c51a138119311fec";
    private static final String URL = "https://api.openweathermap.org/data/2.5/forecast?q=";

    public static String urlBuilder(Details requestPayload) {
        StringBuilder url = new StringBuilder().append(URL);
        String apiKey = "&appid="+API_KEY;
        String cityName = requestPayload.getCityName();
        String stateCode = requestPayload.getStateCode();
        String countryCode = requestPayload.getCountryCode();

        if (requestPayload.getStateCode() != null) {
            if (requestPayload.getCountryCode() != null) {
                url.append(cityName).append(",").append(stateCode).append(",").append(countryCode).append(apiKey);
            } else {
                url.append(cityName).append(",").append(stateCode).append(",").append(apiKey);
            }
        } else {
            url.append(requestPayload.getCityName()).append(apiKey);
        }
        return url.toString();
    }
}
