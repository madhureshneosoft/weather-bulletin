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
public class Validator {

    /**
     * Checks whether or not the payload is valid
     * @param payload input by user
     * @return true/false
     */
    protected static boolean valid(Details payload){
        return payloadValidation(payload) &&
                timeValidation(payload.getWorkTimeStart()) &&
                timeValidation(payload.getWorkTimeEnd());
    }

    /**
     * To check if the time mentioned is in correct format and is valid
     * @param time given by user in payload
     * @return true/false
     */
    private static boolean timeValidation(String time){
        String pattern = "^([01]\\d|2[0-3]):?([0-5]\\d)$";
        return time.matches(pattern);
    }

    /**
     * Checks if mandatory fields in payload is present or not
     * @param payload input by the user
     * @return true/false
     */
    private static boolean payloadValidation(Details payload){
        return ((!(payload.getCityName()==null && payload.getStateCode()==null && payload.getCountryCode()==null))
                &&(isNotNullOrEmpty(payload.getApiKey()) && isNotNullOrEmpty(payload.getWorkTimeEnd()) && isNotNullOrEmpty(payload.getWorkTimeStart())));
    }

    /**
     * To check whether a string is not null or empty
     * @param str to be checked
     * @return true/false
     */
    private static boolean isNotNullOrEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    /**
     * Builds response that is to be sent to UI if there are no exceptions
     * @param reports for the next 3 days
     * @param responseTime of the api
     * @return ResponseEntity object
     */
    protected static ResponseEntity<Response> responseBuilder(List<Report> reports,long responseTime){
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
                "Weather Forecast dated from "+format.format(start)+" to "+format.format(end));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Builds response that is to be sent to UI if there are exceptions
     * @param responseTime of the api
     * @param exception that has occurred
     * @return ResponseEntity object
     */
    protected static ResponseEntity<Response> exceptionResponseBuilder(long responseTime,String exception){
        Response response = new Response(HttpStatus.BAD_REQUEST,"Failed",null,String.valueOf(responseTime),exception);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
