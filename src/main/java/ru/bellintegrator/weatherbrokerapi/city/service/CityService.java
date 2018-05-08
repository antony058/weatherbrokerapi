package ru.bellintegrator.weatherbrokerapi.city.service;

import javassist.NotFoundException;
import ru.bellintegrator.weatherbrokerapi.city.model.City;
import ru.bellintegrator.weatherbrokerapi.city.view.CityView;

import java.util.List;

public interface CityService {
    void save(String cityName);
    City loadByName(String cityName) throws NotFoundException;
    List<CityView> getCitiesLikeName(String cityName);
}
