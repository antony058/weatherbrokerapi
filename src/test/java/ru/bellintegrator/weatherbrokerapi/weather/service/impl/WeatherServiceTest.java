package ru.bellintegrator.weatherbrokerapi.weather.service.impl;

import javassist.NotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.bellintegrator.weatherbrokerapi.city.model.City;
import ru.bellintegrator.weatherbrokerapi.city.service.CityService;
import ru.bellintegrator.weatherbrokerapi.weather.dao.WeatherDao;
import ru.bellintegrator.weatherbrokerapi.weather.model.Weather;
import ru.bellintegrator.weatherbrokerapi.weather.service.WeatherService;
import ru.bellintegrator.weatherbrokerapi.weather.view.WeatherView;

import javax.persistence.NoResultException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WeatherServiceTest {
    private WeatherDao weatherDao;
    private CityService cityService;
    private WeatherService weatherService;

    private WeatherView weatherView;
    private SimpleDateFormat dateFormat;

    @Before
    public void setUp() {
        weatherDao = mock(WeatherDao.class);
        cityService = mock(CityService.class);
        weatherService = new WeatherServiceImpl(weatherDao, cityService);

        dateFormat = new SimpleDateFormat("dd-MM-yy");
    }

    @Before
    public void setUpView() throws ParseException {
        weatherView = new WeatherView("Penza", dateFormat.parse("01-01-2019"), 22, "Sunny");
    }

    @Test
    public void getCityWeather() throws NotFoundException, ParseException {
        Weather weather = new Weather(22, "Sunny", dateFormat.parse("12-12-12"));
        weather.setCity(new City("Penza"));

        when(weatherDao.getWeatherByCity("Penza")).thenReturn(weather);

        WeatherView weatherView = weatherService.getCityWeather("Penza");

        Assert.assertEquals(weather.getCity().getCityName(), weatherView.getCity());
        Assert.assertEquals(weather.getTemperature(), weatherView.getTemp());
        Assert.assertEquals(weather.getDescription(), weatherView.getText());
    }

    @Test(expected = NoResultException.class)
    public void getCityWeatherNoResultException() throws NotFoundException {
        when(weatherDao.getWeatherByCity("pzx")).thenThrow(new NoResultException(""));

        weatherService.getCityWeather("pzx");
    }

    @Test(expected = NotFoundException.class)
    public void saveWeatherNotFoundException() throws NotFoundException {
        when(cityService.loadByName("pzx")).thenThrow(new NotFoundException(""));

        weatherView.setCity("pzx");
        weatherService.save(weatherView);
    }

    @Test
    public void saveWeatherSuccessTest() throws NotFoundException {
        City city = new City("Penza");
        when(cityService.loadByName("Penza")).thenReturn(city);
    }
}
