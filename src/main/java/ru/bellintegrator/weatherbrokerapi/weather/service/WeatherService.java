package ru.bellintegrator.weatherbrokerapi.weather.service;

import javassist.NotFoundException;
import ru.bellintegrator.weatherbrokerapi.weather.view.WeatherView;

public interface WeatherService {
    void save(WeatherView weatherView) throws NotFoundException;
    WeatherView getCityWeather(String cityName) throws NotFoundException;
}
