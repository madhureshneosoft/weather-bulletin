package com.neosoft.weatherbulletin.service;

import com.neosoft.weatherbulletin.exception.InvalidException;
import com.neosoft.weatherbulletin.model.*;
import com.neosoft.weatherbulletin.util.UrlBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class WeatherServiceImpl implements WeatherService {

    @Override
    public List<Report> getResponse(Details requestPayload) throws Exception {
        List<DayReport> reportList = Arrays.asList(new DayReport(), new DayReport(), new DayReport());
        Calendar workTimeFrom = getTimeAsCalenderObject(requestPayload.getWorkTimeFrom());
        Calendar workTimeTo = getTimeAsCalenderObject(requestPayload.getWorkTimeTo());
        String url = UrlBuilder.urlBuilder(requestPayload);
        WeatherForecast weatherForecast = new RestTemplate().getForObject(url, WeatherForecast.class);
        if (weatherForecast != null) {
            checkDaysGap(weatherForecast.getWeatherDetails(), workTimeFrom, workTimeTo, reportList);
            return calculateAverage(reportList);
        } else {
            throw new InvalidException("No data found for the given city, please ensure the city you have entered is correct.");
        }
    }

    /**
     * Checks days gap between two dates and add temperature and humidity in the respective arraylist
     * @param weatherDetails contains temperature and time stamp
     * @param workTimeFrom starting of the work time
     * @param workTimeTo end of the work time
     * @param reportList next 3 days report
     * @throws ParseException if date given is not parsable
     */
    private void checkDaysGap(List<WeatherDetails> weatherDetails, Calendar workTimeFrom, Calendar workTimeTo, List<DayReport> reportList) throws ParseException {
        for (WeatherDetails details : weatherDetails) {
            long daysGap = ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.parse(details.getDate().substring(0, 10)));
            if (daysGap >= 1 && daysGap <= 3) {
                Date givenTime = getTimeAsCalenderObject(details.getDate().substring(11, 19)).getTime();
                if (givenTime.after(workTimeFrom.getTime()) && givenTime.before(workTimeTo.getTime())) {
                    reportList.get((int) daysGap - 1).getAvgWorkHourMaxTemp().add(details.getTemperatureDetails().getTemperatureMaximum());
                    reportList.get((int) daysGap - 1).getAvgWorkHourMinTemp().add(details.getTemperatureDetails().getTemperatureMinimum());
                    reportList.get((int) daysGap - 1).getAvgWorkHourHumidity().add(details.getTemperatureDetails().getHumidity());
                } else {
                    reportList.get((int) daysGap - 1).getAvgNonWorkHourMaxTemp().add(details.getTemperatureDetails().getTemperatureMaximum());
                    reportList.get((int) daysGap - 1).getAvgNonWorkHourMinTemp().add(details.getTemperatureDetails().getTemperatureMinimum());
                    reportList.get((int) daysGap - 1).getAvgNonWorkHourHumidity().add(details.getTemperatureDetails().getHumidity());
                }
                reportList.get((int) daysGap - 1).setDay((int) daysGap);
            } else if (daysGap > 3){
                break;
            }
        }
    }

    /**
     * Calculates average and converts it into Report object
     * @param report next 3 days report
     * @return list of Report
     */
    private List<Report> calculateAverage(List<DayReport> report) {
        List<Report> reports = new ArrayList<>();
        for (DayReport dayReport : report) {
            Report reportObj = new Report();
            reportObj.setDay(dayReport.getDay());
            reportObj.setNonWorkHoursMaxTemperature(dayReport.getAvgNonWorkHourMaxTemp().stream().mapToDouble(Double::doubleValue).sum() / dayReport.getAvgNonWorkHourMaxTemp().size());
            reportObj.setNonWorkHoursMinTemperature(dayReport.getAvgNonWorkHourMinTemp().stream().mapToDouble(Double::doubleValue).sum() / dayReport.getAvgNonWorkHourMinTemp().size());
            reportObj.setNonWorkHoursHumidity(dayReport.getAvgNonWorkHourHumidity().stream().mapToDouble(Double::doubleValue).sum() / dayReport.getAvgNonWorkHourHumidity().size());
            reportObj.setWorkHoursMaxTemperature(dayReport.getAvgWorkHourMaxTemp().stream().mapToDouble(Double::doubleValue).sum() / dayReport.getAvgWorkHourMaxTemp().size());
            reportObj.setWorkHoursMinTemperature(dayReport.getAvgWorkHourMinTemp().stream().mapToDouble(Double::doubleValue).sum() / dayReport.getAvgWorkHourMinTemp().size());
            reportObj.setWorkHoursHumidity(dayReport.getAvgWorkHourHumidity().stream().mapToDouble(Double::doubleValue).sum() / dayReport.getAvgWorkHourHumidity().size());
            reports.add(reportObj);
        }
        return reports;
    }

    /**
     * Get the given time as a Calender object
     * @param time given by user
     * @return calender object
     * @throws ParseException if given time is not parsable
     */
    private Calendar getTimeAsCalenderObject(String time) throws ParseException {
        Date date = new SimpleDateFormat("HH:mm").parse(time);
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        calender.add(Calendar.DATE, 1);
        return calender;
    }
}
