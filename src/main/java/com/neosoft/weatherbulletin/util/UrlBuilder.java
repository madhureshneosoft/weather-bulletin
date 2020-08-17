package com.neosoft.weatherbulletin.util;

import com.neosoft.weatherbulletin.model.Details;

public class UrlBuilder {

    private static final String API_KEY = "e7c69e6bedbfb287c51a138119311fec";

    public static String urlBuilder(Details requestPayload) {
        StringBuilder url = new StringBuilder();
        if (requestPayload.getStateCode() != null) {
            if (requestPayload.getCountryCode() != null) {
                url.append("https://api.openweathermap.org/data/2.5/forecast?q=").append(requestPayload.getCityName()).append(",").append(requestPayload.getStateCode()).append(",").append(requestPayload.getCountryCode()).append("&appid=").append(API_KEY);
            } else {
                url.append("https://api.openweathermap.org/data/2.5/forecast?q=").append(requestPayload.getCityName()).append(",").append(requestPayload.getStateCode()).append("&appid=").append(API_KEY);
            }
        } else {
            url.append("https://api.openweathermap.org/data/2.5/forecast?q=").append(requestPayload.getCityName()).append("&appid=").append(API_KEY);
        }
        return url.toString();
    }
}
