package com.neosoft.weatherbulletin.service;

import com.neosoft.weatherbulletin.model.Report;
import com.neosoft.weatherbulletin.model.RequestPayload;
import com.neosoft.weatherbulletin.model.Weather;
import com.neosoft.weatherbulletin.model.WeatherDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class WeatherServiceImpl implements WeatherService {

    private static final String API_KEY = "e7c69e6bedbfb287c51a138119311fec";

    @Override
    public List<Report> getResponse(RequestPayload requestPayload) throws Exception {
        List<Report> reports = new ArrayList<>();

        double[] workHoursAverageMaxTemperature = new double[5];
        double[] nonWorkHoursAverageMaxTemperature = new double[5];
        double[] workHoursAverageMinTemperature = new double[5];
        double[] nonWorkHoursAverageMinTemperature = new double[5];
        double[] workHoursAverageHumidity = new double[5];
        double[] nonWorkHoursAverageHumidity = new double[5];

        int[] workHoursCounter = new int[5];
        int[] nonWorkHoursCounter = new int[5];

        StringBuilder url = new StringBuilder();
        RestTemplate restTemplate = new RestTemplate();

        if(requestPayload.getCityName()!=null){
            if(requestPayload.getStateCode()!=null){
                if(requestPayload.getCountryCode()!=null){
                    url.append("https://pro.openweathermap.org/data/2.5/forecast/hourly?q=").append(requestPayload.getCityName()).append(",").append(requestPayload.getStateCode()).append(",").append(requestPayload.getCountryCode()).append("&appid=").append(API_KEY);
                } else {
                    url.append("https://pro.openweathermap.org/data/2.5/forecast/hourly?q=").append(requestPayload.getCityName()).append(",").append(requestPayload.getStateCode()).append("&appid=").append(API_KEY);
                }
            } else {
                url.append("https://pro.openweathermap.org/data/2.5/forecast/hourly?q=").append(requestPayload.getCityName()).append("&appid=").append(API_KEY);
            }
        } else {
            throw new Exception("City Name is Required");
        }

//        Weather weather = restTemplate.getForObject(url.toString(), Weather.class);
        Weather weather = restTemplate.getForObject("https://samples.openweathermap.org/data/2.5/forecast/hourly?q=London,us&appid=439d4b804bc8187953eb36d2a8c26a02", Weather.class);

        Date startTime = new SimpleDateFormat("HH:mm").parse(requestPayload.getFrom());
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(startTime);
        calendar1.add(Calendar.DATE, 1);

        Date endTime = new SimpleDateFormat("HH:mm").parse(requestPayload.getTo());
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(endTime);
        calendar2.add(Calendar.DATE, 1);

        if (weather != null) {
            for(WeatherDetails details: weather.getWeatherDetails()){

                /*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));*/

                Date d = new SimpleDateFormat("HH:mm:ss").parse(details.getDate().substring(11,19));
                Calendar calendar3 = Calendar.getInstance();
                calendar3.setTime(d);
                calendar3.add(Calendar.DATE, 1);
                Date x = calendar3.getTime();

                long between = ChronoUnit.DAYS.between(LocalDate.of(2019,3,26), LocalDate.parse(details.getDate().substring(0,10)));

                if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {
                    workHoursAverageMaxTemperature[(int) between -1] += details.getTemperature().getTemperatureMaximum();
                    workHoursAverageMinTemperature[(int) between -1] += details.getTemperature().getTemperatureMinimum();
                    workHoursAverageHumidity[(int) between -1] += details.getTemperature().getHumidity();

                    workHoursCounter[(int) between -1]++;

                } else {
                    nonWorkHoursAverageMaxTemperature[(int) between -1] += details.getTemperature().getTemperatureMaximum();
                    nonWorkHoursAverageMinTemperature[(int) between -1] += details.getTemperature().getTemperatureMinimum();
                    nonWorkHoursAverageHumidity[(int) between -1] += details.getTemperature().getHumidity();

                    nonWorkHoursCounter[(int) between -1]++;
                }
            }
        } else {
            throw new Exception("No Data Found");
        }

        for(int i=0;i<=4;i++){
            workHoursAverageMaxTemperature[i] /= workHoursCounter[i];
            nonWorkHoursAverageMaxTemperature[i] /= nonWorkHoursCounter[i];

            workHoursAverageMinTemperature[i] /= workHoursCounter[i];
            nonWorkHoursAverageMinTemperature[i] /= nonWorkHoursCounter[i];

            workHoursAverageHumidity[i] /= workHoursCounter[i];
            nonWorkHoursAverageHumidity[i] /= nonWorkHoursCounter[i];
        }

        for(int i=0;i<=4;i++) {
            Report report = new Report();
            report.setDay(i+1);
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
