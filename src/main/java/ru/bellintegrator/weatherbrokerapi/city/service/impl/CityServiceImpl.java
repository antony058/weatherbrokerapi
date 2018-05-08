package ru.bellintegrator.weatherbrokerapi.city.service.impl;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bellintegrator.weatherbrokerapi.city.dao.CityDao;
import ru.bellintegrator.weatherbrokerapi.city.model.City;
import ru.bellintegrator.weatherbrokerapi.city.service.CityService;
import ru.bellintegrator.weatherbrokerapi.city.view.CityView;

import java.util.ArrayList;
import java.util.List;

@Service
public class CityServiceImpl implements CityService {

    private final CityDao cityDao;

    @Autowired
    public CityServiceImpl(CityDao cityDao) {
        this.cityDao = cityDao;
    }

    @Override
    @Transactional
    public void save(String cityName) {
        City city = new City(cityName);

        cityDao.save(city);
    }

    @Override
    @Transactional
    public City loadByName(String cityName) throws NotFoundException {
        return cityDao.loadByName(cityName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CityView> getCitiesLikeName(String cityName) {
        List<City> cities = cityDao.citiesLikeName(cityName);
        List<CityView> cityViews = new ArrayList<CityView>(cities.size());

        for (City city: cities) {
            cityViews.add(CityView.mapToCityView(city));
        }

        return cityViews;
    }
}
