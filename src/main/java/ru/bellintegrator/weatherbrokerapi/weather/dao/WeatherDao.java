package ru.bellintegrator.weatherbrokerapi.weather.dao;

import javassist.NotFoundException;
import ru.bellintegrator.weatherbrokerapi.weather.model.Weather;
import ru.bellintegrator.weatherbrokerapi.weather.view.WeatherView;

import java.util.List;

public interface WeatherDao {
    void save(Weather weather);

    Weather getWeatherByCity(String cityName) throws NotFoundException;

    void saveBatch(List<WeatherView> viewSet);
}
