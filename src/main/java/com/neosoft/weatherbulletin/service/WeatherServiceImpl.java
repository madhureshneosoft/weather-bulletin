package com.neosoft.weatherbulletin.service;

import com.neosoft.weatherbulletin.exception.InvalidException;
import com.neosoft.weatherbulletin.model.*;
import com.neosoft.weatherbulletin.util.UrlBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class WeatherServiceImpl implements WeatherService {

    @Value("${first.day}")
    private int firstDay;

    @Value("${last.day}")
    private int lastDay;

    @Override
    public List<Report> getResponse(Details requestPayload) {
        List<DayReport> reportList = Arrays.asList(new DayReport(), new DayReport(), new DayReport());
        LocalTime workTimeStart = LocalTime.parse(requestPayload.getWorkTimeStart());
        LocalTime workTimeEnd = LocalTime.parse(requestPayload.getWorkTimeEnd());
        String url = UrlBuilder.urlBuilder(requestPayload);
        WeatherForecast weatherForecast = new RestTemplate().getForObject(url, WeatherForecast.class);
        checkDaysGap(weatherForecast.getWeatherDetails(), workTimeStart, workTimeEnd, reportList);
        return calculateAverage(reportList);
    }

    /**
     * Checks days gap between two dates and add temperature and humidity in the respective arraylist
     * @param weatherDetails contains temperature and time stamp
     * @param workTimeFrom starting of the work time
     * @param workTimeTo end of the work time
     * @param reportList next 3 days report
     */
    private void checkDaysGap(List<WeatherDetails> weatherDetails, LocalTime workTimeFrom, LocalTime workTimeTo, List<DayReport> reportList) {
        weatherDetails.forEach(details -> {
            try {
                String dateStamp = UrlBuilder.extractDateStamp(details.getDate());
                long daysGap = ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.parse(dateStamp));
                if (daysGap >= firstDay && daysGap <= lastDay) {
                    int index = (int) daysGap - 1;
                    String timeStamp = UrlBuilder.extractTimeStamp(details.getDate());
                    LocalTime givenTime = LocalTime.parse(timeStamp);
                    if (givenTime.isAfter(workTimeFrom) && givenTime.isBefore(workTimeTo)) {
                        reportList.get(index).getAvgWorkHourMaxTemp().add(details.getTemperatureDetails().getTemperatureMaximum());
                        reportList.get(index).getAvgWorkHourMinTemp().add(details.getTemperatureDetails().getTemperatureMinimum());
                        reportList.get(index).getAvgWorkHourHumidity().add(details.getTemperatureDetails().getHumidity());
                    } else {
                        reportList.get(index).getAvgNonWorkHourMaxTemp().add(details.getTemperatureDetails().getTemperatureMaximum());
                        reportList.get(index).getAvgNonWorkHourMinTemp().add(details.getTemperatureDetails().getTemperatureMinimum());
                        reportList.get(index).getAvgNonWorkHourHumidity().add(details.getTemperatureDetails().getHumidity());
                    }
                    reportList.get(index).setDay((int) daysGap);
                }
            } catch (ParseException e){
                throw new InvalidException("Date not parsable");
            }
        });
    }

    /**
     * Calculates average and converts it into Report object
     * @param report next 3 days report
     * @return list of Report
     */
    private List<Report> calculateAverage(List<DayReport> report) {
        List<Report> reports = new ArrayList<>();
        report.forEach(dayReport -> {
            Report reportObj = new Report();
            reportObj.setDay(dayReport.getDay());
            reportObj.setNonWorkHoursMaxTemperature(dayReport.getAvgNonWorkHourMaxTemp().stream().mapToDouble(Double::doubleValue).sum() / dayReport.getAvgNonWorkHourMaxTemp().size());
            reportObj.setNonWorkHoursMinTemperature(dayReport.getAvgNonWorkHourMinTemp().stream().mapToDouble(Double::doubleValue).sum() / dayReport.getAvgNonWorkHourMinTemp().size());
            reportObj.setNonWorkHoursHumidity(dayReport.getAvgNonWorkHourHumidity().stream().mapToDouble(Double::doubleValue).sum() / dayReport.getAvgNonWorkHourHumidity().size());
            reportObj.setWorkHoursMaxTemperature(dayReport.getAvgWorkHourMaxTemp().stream().mapToDouble(Double::doubleValue).sum() / dayReport.getAvgWorkHourMaxTemp().size());
            reportObj.setWorkHoursMinTemperature(dayReport.getAvgWorkHourMinTemp().stream().mapToDouble(Double::doubleValue).sum() / dayReport.getAvgWorkHourMinTemp().size());
            reportObj.setWorkHoursHumidity(dayReport.getAvgWorkHourHumidity().stream().mapToDouble(Double::doubleValue).sum() / dayReport.getAvgWorkHourHumidity().size());
            reports.add(reportObj);
        });
        return reports;
    }
}
