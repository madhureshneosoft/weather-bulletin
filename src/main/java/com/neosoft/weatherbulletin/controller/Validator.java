package com.neosoft.weatherbulletin.controller;

import com.neosoft.weatherbulletin.model.Details;
import com.neosoft.weatherbulletin.model.Report;
import com.neosoft.weatherbulletin.model.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Abstract class for validation and to build Response Entity
 */
@Component
public abstract class Validator {

    protected boolean valid(Details payload){
        return payloadValidation(payload) &&
                timeValidation(payload.getWorkTimeFrom()) &&
                timeValidation(payload.getWorkTimeTo());
    }

    private boolean timeValidation(String time){
        String pattern = "^([01]\\d|2[0-3]):?([0-5]\\d)$";
        return time.matches(pattern);
    }

    private boolean payloadValidation(Details payload){
        return ((!(payload.getCityName()==null && payload.getStateCode()==null && payload.getCountryCode()==null))
                &&(isNotNullOrEmpty(payload.getApiKey()) && isNotNullOrEmpty(payload.getWorkTimeTo()) && isNotNullOrEmpty(payload.getWorkTimeFrom())));
    }

    private boolean isNotNullOrEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    protected ResponseEntity<Response> responseBuilder(List<Report> reports,String cityName ,long responseTime){
        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date start = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 2);
        Date end = calendar.getTime();
        Response response = new Response(HttpStatus.OK,
                "Success",
                reports,
                String.valueOf(responseTime),
                "Weather Forecast for "+cityName+" dated from "+format.format(start)+" to "+format.format(end));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    protected ResponseEntity<Response> exceptionResponseBuilder(long responseTime,String exception){
        Response response = new Response(HttpStatus.BAD_REQUEST,"Failed",null,String.valueOf(responseTime),exception);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
