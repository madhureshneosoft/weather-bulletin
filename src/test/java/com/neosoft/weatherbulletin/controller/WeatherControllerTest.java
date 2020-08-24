package com.neosoft.weatherbulletin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.neosoft.weatherbulletin.exception.InvalidException;
import com.neosoft.weatherbulletin.model.Details;
import com.neosoft.weatherbulletin.model.Report;
import com.neosoft.weatherbulletin.service.WeatherServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = WeatherController.class)
public class WeatherControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    WeatherServiceImpl weatherService;

    WeatherController weatherController;

    Details details;

    String url;

    @Before
    public void setup() {
        weatherController = new WeatherController(weatherService);
        details = new Details();
        url = "https://localhost:8080/api/weather/forecast";
    }

    @Test
    public void cityNotFound() throws Exception {
        String payload = "{\"from\" : \"09:30\", \"to\" : \"18:30\", \"cityName\" : \"ASDASD\"}";

        doThrow(InvalidException.class).when(weatherService).getResponse(any());

        mockMvc.perform(MockMvcRequestBuilders.post(url)
            .content(payload)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        verify(weatherService,times(0)).getResponse(any());
    }

    @Test
    public void invalidPayload() throws Exception {
        String payload = "{\"from\" : \"09:30\", \"to\" : \"18:30\"}";
        doThrow(InvalidException.class).when(weatherService).getResponse(any());

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                .content(payload)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        verify(weatherService,times(0)).getResponse(any());
    }

    @Test
    public void valid() throws Exception {
        String payload = "{\"workTimeFrom\" : \"09:30\", \"workTimeTo\" : \"18:30\", \"cityName\" : \"London\", \"apiKey\" : \"e7c69e6bedbfb287c51a138119311fec\"}";
        List<Report> reports = Arrays.asList(new Report(),new Report(),new Report());

        doReturn(reports).when(weatherService).getResponse(any());

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                .content(payload)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        verify(weatherService,times(1)).getResponse(any());
    }

    @Test
    public void serviceValidTest() throws Exception {
        String payload = "{\"workTimeFrom\" : \"09:30\", \"workTimeTo\" : \"18:30\", \"cityName\" : \"London\", \"apiKey\" : \"e7c69e6bedbfb287c51a138119311fec\"}";

        Gson gson = new Gson();

        when(weatherService.getResponse(gson.fromJson(payload,Details.class))).thenCallRealMethod();

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                .content(payload)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    public void serviceInvalidTest() throws Exception {
        String payload = "{\"workTimeFrom\" : \"09:30\", \"workTimeTo\" : \"18:30\", \"cityName\" : \"ASJHDAJ\", \"apiKey\" : \"e7c69e6bedbfb287c51a138119311fec\"}";

        Gson gson = new Gson();

        when(weatherService.getResponse(gson.fromJson(payload,Details.class))).thenCallRealMethod();

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                .content(payload)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void serviceValidTest2() throws Exception {
        String payload = "{\n" +
                "    \"workTimeFrom\" : \"09:30\"," +
                "    \"workTimeTo\" : \"18:30\"," +
                "    \"countryCode\" : \"India\"," +
                "    \"apiKey\" : \"e7c69e6bedbfb287c51a138119311fec\"" +
                "}";

        Gson gson = new Gson();

        when(weatherService.getResponse(gson.fromJson(payload,Details.class))).thenCallRealMethod();

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                .content(payload)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void serviceValidTest3() throws Exception {
        String payload = "{\n" +
                "    \"workTimeFrom\" : \"09:30\"," +
                "    \"workTimeTo\" : \"18:30\"," +
                "    \"stateCode\" : \"Maharashtra\"," +
                "    \"apiKey\" : \"e7c69e6bedbfb287c51a138119311fec\"" +
                "}";

        Gson gson = new Gson();

        when(weatherService.getResponse(gson.fromJson(payload,Details.class))).thenCallRealMethod();

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                .content(payload)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void serviceValidTest4() throws Exception {
        String payload = "{\n" +
                "    \"workTimeFrom\" : \"09:30\"," +
                "    \"workTimeTo\" : \"18:30\"," +
                "    \"cityName\" : \"Mumbai\"," +
                "    \"stateCode\" : \"Maharashtra\"," +
                "    \"countryCode\" : \"India\"," +
                "    \"apiKey\" : \"e7c69e6bedbfb287c51a138119311fec\"" +
                "}";

        Gson gson = new Gson();

        when(weatherService.getResponse(gson.fromJson(payload,Details.class))).thenCallRealMethod();

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                .content(payload)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
