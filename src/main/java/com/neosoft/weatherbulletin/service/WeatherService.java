package com.neosoft.weatherbulletin.service;

import com.neosoft.weatherbulletin.model.Report;
import com.neosoft.weatherbulletin.model.Details;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WeatherService {

    /**
     * To call weather forecast for the next 3 days
     * @param details payload by user
     * @return list of report
     * @throws Exception if data is not found or payload isn't valid
     */
    List<Report> getResponse(Details details) throws Exception;
}
