package ru.bellintegrator.weatherbrokerapi.weather.service.impl;

import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bellintegrator.weatherbrokerapi.city.model.City;
import ru.bellintegrator.weatherbrokerapi.city.service.CityService;
import ru.bellintegrator.weatherbrokerapi.weather.dao.WeatherDao;
import ru.bellintegrator.weatherbrokerapi.weather.model.Weather;
import ru.bellintegrator.weatherbrokerapi.weather.service.WeatherService;
import ru.bellintegrator.weatherbrokerapi.weather.view.WeatherView;

@Service
public class WeatherServiceImpl implements WeatherService {

    private Logger log = LoggerFactory.getLogger(WeatherServiceImpl.class);

    private final WeatherDao weatherDao;
    private final CityService cityService;

    @Autowired
    public WeatherServiceImpl(WeatherDao weatherDao, CityService cityService) {
        this.weatherDao = weatherDao;
        this.cityService = cityService;
    }

    @Override
    @Transactional
    public void save(WeatherView weatherView) throws NotFoundException {
        City city = cityService.loadByName(weatherView.getCity());

        Weather weather = new Weather(weatherView.getTemp(), weatherView.getText());
        weather.setCity(city);

        city.setWeather(weather);

        log.info("Погода добавлена в бд");
    }

    @Override
    @Transactional(readOnly = true)
    public WeatherView getCityWeather(String cityName) throws NotFoundException {
        Weather weather = weatherDao.getWeatherByCity(cityName);

        return WeatherView.mapToWeatherView(weather, cityName);
    }
}
