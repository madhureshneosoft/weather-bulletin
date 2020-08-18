package com.neosoft.weatherbulletin.service;

import com.neosoft.weatherbulletin.exception.InvalidException;
import com.neosoft.weatherbulletin.model.Details;
import com.neosoft.weatherbulletin.model.Report;
import com.neosoft.weatherbulletin.model.WeatherDetails;
import com.neosoft.weatherbulletin.model.WeatherForecast;
import com.neosoft.weatherbulletin.util.UrlBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

        WeatherForecast weatherForecast = restTemplate.getForObject(url, WeatherForecast.class);

        sumOfTemperatureDayWise(requestPayload, workHoursAverageMaxTemperature, nonWorkHoursAverageMaxTemperature, workHoursAverageMinTemperature, nonWorkHoursAverageMinTemperature, workHoursAverageHumidity, nonWorkHoursAverageHumidity, workHoursCounter, nonWorkHoursCounter, weatherForecast);
        sumTheAverage(workHoursAverageMaxTemperature, nonWorkHoursAverageMaxTemperature, workHoursAverageMinTemperature, nonWorkHoursAverageMinTemperature, workHoursAverageHumidity, nonWorkHoursAverageHumidity, workHoursCounter, nonWorkHoursCounter);
        convertToPerDayReport(requestPayload, reports, workHoursAverageMaxTemperature, nonWorkHoursAverageMaxTemperature, workHoursAverageMinTemperature, nonWorkHoursAverageMinTemperature, workHoursAverageHumidity, nonWorkHoursAverageHumidity);
        return reports;
    }

    /**
     * Adds temperature for the following day in workHour/nonWorkHour array depending upon time stamp
     * @param requestPayload given by the user
     * @param workHoursAverageMaxTemperature array
     * @param nonWorkHoursAverageMaxTemperature array
     * @param workHoursAverageMinTemperature array
     * @param nonWorkHoursAverageMinTemperature array
     * @param workHoursAverageHumidity array
     * @param nonWorkHoursAverageHumidity array
     * @param workHoursCounter array
     * @param nonWorkHoursCounter array
     * @param weatherForecast object
     * @throws ParseException if date cannot be parsed
     */
    private void sumOfTemperatureDayWise(Details requestPayload, double[] workHoursAverageMaxTemperature, double[] nonWorkHoursAverageMaxTemperature, double[] workHoursAverageMinTemperature, double[] nonWorkHoursAverageMinTemperature, double[] workHoursAverageHumidity, double[] nonWorkHoursAverageHumidity, int[] workHoursCounter, int[] nonWorkHoursCounter, WeatherForecast weatherForecast) throws ParseException {
        if (weatherForecast != null) {
            Calendar workTimeFrom = getTimeAsCalenderObject(requestPayload.getFrom());
            Calendar workTimeTo = getTimeAsCalenderObject(requestPayload.getTo());
            for(WeatherDetails details: weatherForecast.getWeatherDetails()){
                Date givenTime = getTimeAsCalenderObject(details.getDate().substring(11,19)).getTime();
                long daysGap = ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.parse(details.getDate().substring(0,10)));
                if(daysGap>=1 && daysGap<=3) {
                    if (givenTime.after(workTimeFrom.getTime()) && givenTime.before(workTimeTo.getTime())) {
                        addForAverage(workHoursAverageMaxTemperature, workHoursAverageMinTemperature, workHoursAverageHumidity, workHoursCounter, details, (int) daysGap);

                    } else {
                        addForAverage(nonWorkHoursAverageMaxTemperature, nonWorkHoursAverageMinTemperature, nonWorkHoursAverageHumidity, nonWorkHoursCounter, details, (int) daysGap);
                    }
                }
            }
        } else {
            throw new InvalidException("No data found for the given city, please ensure the city you have entered is correct.");
        }
    }

    /**
     * Converts the average min/max temperature and humidity to an Object of Report class
     * @param requestPayload given by user
     * @param reports list of report
     * @param workHoursAverageMaxTemperature array
     * @param nonWorkHoursAverageMaxTemperature array
     * @param workHoursAverageMinTemperature array
     * @param nonWorkHoursAverageMinTemperature array
     * @param workHoursAverageHumidity array
     * @param nonWorkHoursAverageHumidity array
     */
    private void convertToPerDayReport(Details requestPayload, List<Report> reports, double[] workHoursAverageMaxTemperature, double[] nonWorkHoursAverageMaxTemperature, double[] workHoursAverageMinTemperature, double[] nonWorkHoursAverageMinTemperature, double[] workHoursAverageHumidity, double[] nonWorkHoursAverageHumidity) {
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
    }

    /**
     * Divides the added temperatures by the counter for the given day to find the average temperature
     * @param workHoursAverageMaxTemperature array
     * @param nonWorkHoursAverageMaxTemperature array
     * @param workHoursAverageMinTemperature array
     * @param nonWorkHoursAverageMinTemperature array
     * @param workHoursAverageHumidity array
     * @param nonWorkHoursAverageHumidity array
     * @param workHoursCounter array
     * @param nonWorkHoursCounter array
     */
    private void sumTheAverage(double[] workHoursAverageMaxTemperature, double[] nonWorkHoursAverageMaxTemperature, double[] workHoursAverageMinTemperature, double[] nonWorkHoursAverageMinTemperature, double[] workHoursAverageHumidity, double[] nonWorkHoursAverageHumidity, int[] workHoursCounter, int[] nonWorkHoursCounter) {
        for(int i=0;i<=2;i++){
            workHoursAverageMaxTemperature[i] /= workHoursCounter[i];
            nonWorkHoursAverageMaxTemperature[i] /= nonWorkHoursCounter[i];

            workHoursAverageMinTemperature[i] /= workHoursCounter[i];
            nonWorkHoursAverageMinTemperature[i] /= nonWorkHoursCounter[i];

            workHoursAverageHumidity[i] /= workHoursCounter[i];
            nonWorkHoursAverageHumidity[i] /= nonWorkHoursCounter[i];
        }
    }

    /**
     * Adds temperature in workHour/nonWorkHour array depending upon timestamp
     * @param averageMaxTemperature array
     * @param averageMinTemperature array
     * @param averageHumidity array
     * @param counter array to counter total number of entries in a particular day
     * @param details weather details
     * @param daysGap gap between current date and time stamp
     */
    private void addForAverage(double[] averageMaxTemperature, double[] averageMinTemperature, double[] averageHumidity, int[] counter, WeatherDetails details, int daysGap) {
        averageMaxTemperature[daysGap - 1] += details.getTemperatureDetails().getTemperatureMaximum();
        averageMinTemperature[daysGap - 1] += details.getTemperatureDetails().getTemperatureMinimum();
        averageHumidity[daysGap - 1] += details.getTemperatureDetails().getHumidity();

        counter[daysGap - 1]++;
    }

    /**
     * Return the give time in the form of Calender Object
     * @param time in HH:mm format
     * @return Calender object
     * @throws ParseException if time isn't in proper format
     */
    private Calendar getTimeAsCalenderObject(String time) throws ParseException {
        Date date = new SimpleDateFormat("HH:mm").parse(time);
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        calender.add(Calendar.DATE, 1);
        return calender;
    }
}
