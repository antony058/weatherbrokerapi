package ru.bellintegrator.weatherbrokerapi.weather.dao.impl;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.bellintegrator.weatherbrokerapi.weather.dao.WeatherDao;
import ru.bellintegrator.weatherbrokerapi.weather.model.Weather;

import javax.persistence.EntityManager;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class WeatherDaoTest {

    private EntityManager em;
    private WeatherDao dao;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        em = mock(EntityManager.class);
        jdbcTemplate = mock(JdbcTemplate.class);
        dao = new WeatherDaoImpl(em, jdbcTemplate);
    }

    @Test
    public void saveWeatherSuccessTest() {
        Weather weather = new Weather();
        dao.save(weather);

        verify(em).persist(weather);
    }
}
