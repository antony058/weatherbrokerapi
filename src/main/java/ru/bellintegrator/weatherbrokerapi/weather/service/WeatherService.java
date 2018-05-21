package ru.bellintegrator.weatherbrokerapi.weather.service;

import javassist.NotFoundException;
import ru.bellintegrator.weatherbrokerapi.weather.view.WeatherView;

import java.util.Set;

public interface WeatherService {
    void save(WeatherView weatherView) throws NotFoundException;
    WeatherView getCityWeather(String cityName) throws NotFoundException;
    void saveBatch(Set<WeatherView> viewSet);
}
