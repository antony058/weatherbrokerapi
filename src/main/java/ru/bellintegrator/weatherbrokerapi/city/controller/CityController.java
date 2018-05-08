package ru.bellintegrator.weatherbrokerapi.city.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bellintegrator.weatherbrokerapi.city.service.CityService;
import ru.bellintegrator.weatherbrokerapi.city.view.CityView;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/city", produces = APPLICATION_JSON_VALUE)
public class CityController {

    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @RequestMapping(value = "/list/{city}")
    public ResponseEntity<List<CityView>> getCitiesLikeName(@PathVariable(value = "city") String city) {
        List<CityView> cityViews = cityService.getCitiesLikeName(city);

        return new ResponseEntity<List<CityView>>(cityViews, HttpStatus.OK);
    }
}
