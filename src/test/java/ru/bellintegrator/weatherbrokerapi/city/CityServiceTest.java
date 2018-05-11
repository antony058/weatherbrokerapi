package ru.bellintegrator.weatherbrokerapi.city;

import javassist.NotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.bellintegrator.weatherbrokerapi.city.dao.CityDao;
import ru.bellintegrator.weatherbrokerapi.city.model.City;
import ru.bellintegrator.weatherbrokerapi.city.service.CityService;
import ru.bellintegrator.weatherbrokerapi.city.service.impl.CityServiceImpl;
import ru.bellintegrator.weatherbrokerapi.city.view.CityView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

public class CityServiceTest {
    private CityDao cityDao;
    private CityService cityService;

    @Before
    public void setUp() {
        cityDao = mock(CityDao.class);
        cityService = new CityServiceImpl(cityDao);
    }

    @Test
    public void saveCitySuccessTest() {
        cityService.save("Penza");
        verify(cityDao).save((City) any());
    }

    @Test
    public void loadByNameSuccessTest() throws NotFoundException {
        City city = new City("Penza");
        when(cityDao.loadByName("Penza")).thenReturn(city);

        City receivedCity = cityService.loadByName("Penza");

        Assert.assertEquals(city, receivedCity);
    }

    @Test(expected = NotFoundException.class)
    public void loadByNameNotFoundException() throws NotFoundException {
        when(cityDao.loadByName("Not exists city name")).thenThrow(new NotFoundException(""));

        cityService.loadByName("Not exists city name");
    }

    @Test
    public void getCitiesLikeNameNotEmptyResultListSuccessTest() {
        List<City> cities = Arrays.asList(new City("Samara"), new City("Saratov"));

        when(cityDao.citiesLikeName("Sa")).thenReturn(cities);

        List<CityView> receivedCities = cityService.getCitiesLikeName("Sa");

        Assert.assertEquals(2, receivedCities.size());
        Assert.assertEquals(cities.get(0).getCityName(), receivedCities.get(0).getCity());
        Assert.assertEquals(cities.get(1).getCityName(), receivedCities.get(1).getCity());
    }

    @Test
    public void getCitiesLikeNameEmptyResultListSuccessTest() {
        List<City> cities = Collections.emptyList();

        when(cityDao.citiesLikeName("pzx")).thenReturn(cities);

        List<CityView> receivedCities = cityService.getCitiesLikeName("pzx");

        Assert.assertEquals(0, receivedCities.size());
    }
}
