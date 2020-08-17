package com.neosoft.weatherbulletin.service;

import com.neosoft.weatherbulletin.exception.InvalidException;
import com.neosoft.weatherbulletin.model.Report;
import com.neosoft.weatherbulletin.model.Details;
import com.neosoft.weatherbulletin.model.WeatherForecast;
import com.neosoft.weatherbulletin.model.WeatherDetails;
import com.neosoft.weatherbulletin.util.UrlBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class WeatherServiceImpl implements WeatherService {

    @Override
    public List<Report> getResponse(Details requestPayload) throws Exception {
        List<Report> reports = new ArrayList<>();

        double[] workHoursAverageMaxTemperature = new double[3];
        double[] nonWorkHoursAverageMaxTemperature = new double[3];
        double[] workHoursAverageMinTemperature = new double[3];
        double[] nonWorkHoursAverageMinTemperature = new double[3];
        double[] workHoursAverageHumidity = new double[3];
        double[] nonWorkHoursAverageHumidity = new double[3];

        int[] workHoursCounter = new int[3];
        int[] nonWorkHoursCounter = new int[3];

        String url = UrlBuilder.urlBuilder(requestPayload);
        RestTemplate restTemplate = new RestTemplate();

        /*if(requestPayload.getCityName()!=null){
            if(requestPayload.getStateCode()!=null){
                if(requestPayload.getCountryCode()!=null){
                    url.append("https://api.openweathermap.org/data/2.5/forecast?q=").append(requestPayload.getCityName()).append(",").append(requestPayload.getStateCode()).append(",").append(requestPayload.getCountryCode()).append("&appid=").append(API_KEY);
                } else {
                    url.append("https://api.openweathermap.org/data/2.5/forecast?q=").append(requestPayload.getCityName()).append(",").append(requestPayload.getStateCode()).append("&appid=").append(API_KEY);
                }
            } else {
                url.append("https://api.openweathermap.org/data/2.5/forecast?q=").append(requestPayload.getCityName()).append("&appid=").append(API_KEY);
            }
        } else {
            throw new InvalidException("Invalid payload, please ensure the payload is in correct format as shown below," +
                    "eg:" +
                    "{" +
                    "'from':'HH:mm', " +
                    "'to':'HH:mm', " +
                    "'cityName':'YOUR_CITY' [MANDATORY]," +
                    "'stateCode':'STATE_CODE' [OPTIONAL]," +
                    "'countryCode':'COUNTRY_CODE' [OPTIONAL]" +
                    "}");
        }*/

        WeatherForecast weatherForecast = restTemplate.getForObject(url, WeatherForecast.class);

        if (weatherForecast != null) {

            Date startTime = new SimpleDateFormat("HH:mm").parse(requestPayload.getFrom());
            Calendar workTimeFrom = Calendar.getInstance();
            workTimeFrom.setTime(startTime);
            workTimeFrom.add(Calendar.DATE, 1);

            Date endTime = new SimpleDateFormat("HH:mm").parse(requestPayload.getTo());
            Calendar workTimeTo = Calendar.getInstance();
            workTimeTo.setTime(endTime);
            workTimeTo.add(Calendar.DATE, 1);

            for(WeatherDetails details: weatherForecast.getWeatherDetails()){

                Date d = new SimpleDateFormat("HH:mm:ss").parse(details.getDate().substring(11,19));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(d);
                calendar.add(Calendar.DATE, 1);
                Date givenTime = calendar.getTime();

                long daysGap = ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.parse(details.getDate().substring(0,10)));
                if(daysGap>=1 && daysGap<=3) {
                    if (givenTime.after(workTimeFrom.getTime()) && givenTime.before(workTimeTo.getTime())) {
                        workHoursAverageMaxTemperature[(int) daysGap - 1] += details.getTemperature().getTemperatureMaximum();
                        workHoursAverageMinTemperature[(int) daysGap - 1] += details.getTemperature().getTemperatureMinimum();
                        workHoursAverageHumidity[(int) daysGap - 1] += details.getTemperature().getHumidity();

                        workHoursCounter[(int) daysGap - 1]++;

                    } else {
                        nonWorkHoursAverageMaxTemperature[(int) daysGap - 1] += details.getTemperature().getTemperatureMaximum();
                        nonWorkHoursAverageMinTemperature[(int) daysGap - 1] += details.getTemperature().getTemperatureMinimum();
                        nonWorkHoursAverageHumidity[(int) daysGap - 1] += details.getTemperature().getHumidity();

                        nonWorkHoursCounter[(int) daysGap - 1]++;
                    }
                }
            }
        } else {
            throw new InvalidException("No data found for the given city, please ensure the city you have entered is correct.");
        }

        for(int i=0;i<=2;i++){
            workHoursAverageMaxTemperature[i] /= workHoursCounter[i];
            nonWorkHoursAverageMaxTemperature[i] /= nonWorkHoursCounter[i];

            workHoursAverageMinTemperature[i] /= workHoursCounter[i];
            nonWorkHoursAverageMinTemperature[i] /= nonWorkHoursCounter[i];

            workHoursAverageHumidity[i] /= workHoursCounter[i];
            nonWorkHoursAverageHumidity[i] /= nonWorkHoursCounter[i];
        }

        for(int i=0;i<=2;i++) {
            Report report = new Report();
            report.setDay(i+1);
            report.setCityName(requestPayload.getCityName());
            report.setWorkHoursMaxTemperature(workHoursAverageMaxTemperature[i]);
            report.setWorkHoursMinTemperature(workHoursAverageMinTemperature[i]);
            report.setWorkHoursHumidity(workHoursAverageHumidity[i]);
            report.setNonWorkHoursMaxTemperature(nonWorkHoursAverageMaxTemperature[i]);
            report.setNonWorkHoursMinTemperature(nonWorkHoursAverageMinTemperature[i]);
            report.setNonWorkHoursHumidity(nonWorkHoursAverageHumidity[i]);
            reports.add(report);
        }
        return reports;
    }
}
