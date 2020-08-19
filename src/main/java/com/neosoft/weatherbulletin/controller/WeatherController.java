package com.neosoft.weatherbulletin.controller;

import com.neosoft.weatherbulletin.exception.InvalidException;
import com.neosoft.weatherbulletin.model.Details;
import com.neosoft.weatherbulletin.model.Report;
import com.neosoft.weatherbulletin.model.Response;
import com.neosoft.weatherbulletin.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller
 */
@RestController
@RequestMapping("/api/weather")
public class WeatherController extends Validator {

    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /**
     * Method to call weather forecast for next 3 days
     * @param details request payload by user
     * @return response entity
     */
    @PostMapping("/forecast")
    public ResponseEntity<Response> weatherForecast(@RequestBody Details details) {
        long time = System.currentTimeMillis();
        long responseTime;
        List<Report> reports;
        try {
            if(valid(details)) {
                reports = weatherService.getResponse(details);
            } else {
                throw new InvalidException("Invalid payload, please ensure the payload is in correct format as shown below," +
                        "eg:" +
                        "{" +
                        "'from':'HH:mm' [24 HOURS FORMAT], " +
                        "'to':'HH:mm' [24 HOURS FORMAT], " +
                        "'cityName':'YOUR_CITY' [MANDATORY]," +
                        "'stateCode':'STATE_CODE' [OPTIONAL]," +
                        "'countryCode':'COUNTRY_CODE' [OPTIONAL]" +
                        "}");
            }
        } catch (Exception e) {
            responseTime = (System.currentTimeMillis() - time);
            return exceptionResponseBuilder(responseTime,e.getMessage());
        }
        responseTime = (System.currentTimeMillis() - time);
        return responseBuilder(reports,responseTime);
    }
}
