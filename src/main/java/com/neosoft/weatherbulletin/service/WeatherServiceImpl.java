package com.neosoft.weatherbulletin.service;

import com.neosoft.weatherbulletin.model.*;
import com.neosoft.weatherbulletin.util.UrlBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class WeatherServiceImpl implements WeatherService {

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
        for (WeatherDetails details : weatherDetails) {
            String dateStamp = details.getDate().substring(0, 10);
            long daysGap = ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.parse(dateStamp));
            if (daysGap >= 1 && daysGap <= 3) {
                int index = (int) daysGap - 1;
                String timeStamp = details.getDate().substring(11, 19);
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
}
