package ru.bellintegrator.weatherbrokerapi.weather.dao.impl;

import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.bellintegrator.weatherbrokerapi.weather.dao.WeatherDao;
import ru.bellintegrator.weatherbrokerapi.weather.model.Weather;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
public class WeatherDaoImpl implements WeatherDao {
    private final Logger log = LoggerFactory.getLogger(WeatherDao.class);

    private final EntityManager em;

    @Autowired
    public WeatherDaoImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void save(Weather weather) {
        em.persist(weather);
    }

    @Override
    public Weather getWeatherByCity(String cityName) throws NotFoundException {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Weather.class);

        Root<Weather> weatherRoot = criteriaQuery.from(Weather.class);
        criteriaQuery.where(
                criteriaBuilder.equal(weatherRoot.join("city").get("cityName"), cityName)
        );

        TypedQuery<Weather> query = em.createQuery(criteriaQuery);

        try {
            return query.getSingleResult();
        } catch (NoResultException ex) {
            log.error("Погода для города " + cityName + " не найдена");
            throw new NotFoundException("Weather not founded");
        }
    }
}
