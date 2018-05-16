package ru.bellintegrator.weatherbrokerapi.weather.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.bellintegrator.weatherbrokerapi.WeatherbrokerapiApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WeatherbrokerapiApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles({"test"})
public class WeatherControllerTest {
    private TestRestTemplate testRestTemplate = new TestRestTemplate();
    private HttpHeaders headers = new HttpHeaders();

    private static final String baseUrl = "http://127.0.0.1:8080";

    @Test
    public void getCityWeatherSuccessTest() {
        HttpEntity<String> httpEntity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> responseEntity = testRestTemplate.exchange(
                baseUrl + "/weather/Пенза",
                HttpMethod.GET, httpEntity, String.class
        );

        String response = responseEntity.getBody();

        Assert.assertTrue(response.contains("temp"));
        Assert.assertTrue(response.contains("text"));
        Assert.assertTrue(response.contains("city"));
    }

    @Test
    public void getCityWeatherNotFoundTest() {
        HttpEntity<String> httpEntity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> responseEntity = testRestTemplate.exchange(
                baseUrl + "/weather/SomeNotExistingCity",
                HttpMethod.GET, httpEntity, String.class
        );

        int responseCode = responseEntity.getStatusCodeValue();

        Assert.assertEquals(404, responseCode);
    }
}
