package ru.bellintegrator.weatherbrokerapi.weather;

import javassist.NotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.bellintegrator.weatherbrokerapi.city.model.City;
import ru.bellintegrator.weatherbrokerapi.city.service.CityService;
import ru.bellintegrator.weatherbrokerapi.weather.dao.WeatherDao;
import ru.bellintegrator.weatherbrokerapi.weather.model.Weather;
import ru.bellintegrator.weatherbrokerapi.weather.service.WeatherService;
import ru.bellintegrator.weatherbrokerapi.weather.service.impl.WeatherServiceImpl;
import ru.bellintegrator.weatherbrokerapi.weather.view.WeatherView;

import javax.persistence.NoResultException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WeatherServiceTest {
    private WeatherDao weatherDao;
    private CityService cityService;
    private WeatherService weatherService;

    private WeatherView weatherView;

    @Before
    public void setUp() {
        weatherDao = mock(WeatherDao.class);
        cityService = mock(CityService.class);
        weatherService = new WeatherServiceImpl(weatherDao, cityService);
    }

    @Before
    public void setUpView() {
        weatherView = new WeatherView("Penza", "01-01-2019", 22, "Sunny");
    }

    @Test
    public void getCityWeather() throws NotFoundException {
        Weather weather = new Weather(22, "Sunny");
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
