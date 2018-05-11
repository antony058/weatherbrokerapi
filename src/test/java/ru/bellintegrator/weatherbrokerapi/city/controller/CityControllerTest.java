package ru.bellintegrator.weatherbrokerapi.city.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import ru.bellintegrator.weatherbrokerapi.WeatherbrokerapiApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WeatherbrokerapiApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CityControllerTest {
    private TestRestTemplate testRestTemplate = new TestRestTemplate();
    private HttpHeaders headers = new HttpHeaders();

    private static final String baseUrl = "http://127.0.0.1:8080";

    @Test
    public void getCitiesListEmptyListTest() {
        HttpEntity<String> httpEntity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> responseEntity = testRestTemplate.exchange(
                baseUrl + "/city/list/Несуществующий_город",
                HttpMethod.GET, httpEntity, String.class
        );

        String response = responseEntity.getBody();
        String correctResult = "[]";

        Assert.assertEquals(response, correctResult);
    }

    @Test
    public void getCitiesListSingleCityTest() {
        HttpEntity<String> httpEntity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> responseEntity = testRestTemplate.exchange(
                baseUrl + "/city/list/Пенза",
                HttpMethod.GET, httpEntity, String.class
        );

        String response = responseEntity.getBody();
        String correctResult = "[{\"city\":\"Пенза\"}]";

        Assert.assertEquals(response, correctResult);
    }

    @Test
    public void getCitiesListMultipleCityTest() {
        HttpEntity<String> httpEntity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> responseEntity = testRestTemplate.exchange(
                baseUrl + "/city/list/Пе",
                HttpMethod.GET, httpEntity, String.class
        );

        String response = responseEntity.getBody();
        String correctResult = "[{\"city\":\"Пенза\"},{\"city\":\"Санкт-Петербург\"},{\"city\":\"Пермь\"}]";

        Assert.assertEquals(response, correctResult);
    }
}
