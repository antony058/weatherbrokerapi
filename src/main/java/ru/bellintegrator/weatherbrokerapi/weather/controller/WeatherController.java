package ru.bellintegrator.weatherbrokerapi.weather.controller;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.bellintegrator.weatherbrokerapi.weather.service.WeatherService;
import ru.bellintegrator.weatherbrokerapi.weather.view.WeatherView;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(produces = APPLICATION_JSON_VALUE)
public class WeatherController {

    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @RequestMapping(value = "/weather/{city}", method = {RequestMethod.GET})
    public ResponseEntity<WeatherView> getWeather(@PathVariable String city) throws NotFoundException {
        WeatherView weatherView = weatherService.getCityWeather(city);

        return new ResponseEntity<WeatherView>(weatherView, HttpStatus.OK);
    }
}