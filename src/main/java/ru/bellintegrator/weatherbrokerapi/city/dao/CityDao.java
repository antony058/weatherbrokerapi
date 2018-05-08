package ru.bellintegrator.weatherbrokerapi.city.dao;

import javassist.NotFoundException;
import ru.bellintegrator.weatherbrokerapi.city.model.City;

import java.util.List;

public interface CityDao {
    void save(City city);
    City loadByName(String cityName) throws NotFoundException;
    List<City> citiesLikeName(String cityName);
}
