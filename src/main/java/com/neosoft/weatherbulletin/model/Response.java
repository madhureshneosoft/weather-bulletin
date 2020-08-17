package com.neosoft.weatherbulletin.model;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.sql.Time;
import java.util.List;

/**
 * Final response to be sent
 */
@Data
@NoArgsConstructor
public class Response {
    private HttpStatus status;
    private String message;
    List<Report> weatherReport;
    private String responseTime;
    private String description;

    public Response(HttpStatus status, String message, List<Report> weatherReport, String responseTime, String description) {
        this.status = status;
        this.message = message;
        this.weatherReport = weatherReport;
        this.responseTime = responseTime+" ms";
        this.description = description;
    }
}
