package com.neosoft.weatherbulletin.controller;

import com.neosoft.weatherbulletin.model.Report;
import com.neosoft.weatherbulletin.model.RequestPayload;
import com.neosoft.weatherbulletin.service.WeatherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @PostMapping("/forecast")
    public ResponseEntity<?> weatherForecast(@RequestBody RequestPayload requestPayload) {
        List<Report> reports;
        try {
            reports = weatherService.getResponse(requestPayload);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }

}
