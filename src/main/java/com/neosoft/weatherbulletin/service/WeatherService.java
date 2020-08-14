package com.neosoft.weatherbulletin.service;

import com.neosoft.weatherbulletin.model.Report;
import com.neosoft.weatherbulletin.model.RequestPayload;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
public interface WeatherService {
    List<Report> getResponse(RequestPayload requestPayload) throws Exception;
}
