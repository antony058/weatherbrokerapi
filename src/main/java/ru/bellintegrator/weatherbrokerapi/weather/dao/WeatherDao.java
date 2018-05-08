package ru.bellintegrator.weatherbrokerapi.weather.dao;

import javassist.NotFoundException;
import ru.bellintegrator.weatherbrokerapi.weather.model.Weather;

public interface WeatherDao {
    void save(Weather weather);

    Weather getWeatherByCity(String cityName) throws NotFoundException;
}
