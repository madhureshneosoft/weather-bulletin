package com.neosoft.weatherbulletin.util;

import com.neosoft.weatherbulletin.model.Details;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * URL builder depending upon given parameters in payload
 */
public class UrlBuilder {

    private UrlBuilder() {
    }

    private static final String URL = "https://api.openweathermap.org/data/2.5/forecast?q=";

    /**
     * Builds the url depending upon number of parameters given by user in the payload
     *
     * @param requestPayload given by user as input
     * @return url of the api to be called
     */
    public static String urlBuilder(Details requestPayload) {
        StringBuilder url = new StringBuilder().append(URL);

        String apiKey = "&appid=" + requestPayload.getApiKey();
        String cityName = requestPayload.getCityName();
        String stateCode = requestPayload.getStateCode();
        String countryCode = requestPayload.getCountryCode();

        if (cityName != null) {
            url.append(cityName).append(",");
        }
        if (stateCode != null) {
            url.append(stateCode).append(",");
        }
        if (countryCode != null) {
            url.append(countryCode).append(",");
        }

        url.append(apiKey);
        return url.toString();
    }

    public static String extractTimeStamp(String dateAndTime) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateAndTime);
        return new SimpleDateFormat("HH:mm").format(date);
    }

    public static String extractDateStamp(String dateAndTime) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateAndTime);
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
}
