package ru.bellintegrator.weatherbrokerapi.weather.dao.impl;

import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.bellintegrator.weatherbrokerapi.weather.dao.WeatherDao;
import ru.bellintegrator.weatherbrokerapi.weather.model.Weather;
import ru.bellintegrator.weatherbrokerapi.weather.view.WeatherView;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class WeatherDaoImpl implements WeatherDao {
    private final Logger log = LoggerFactory.getLogger(WeatherDao.class);
    private static final String CALL_UPSERT_WEATHER_FUNC = "SELECT set_weather_desc(?, ?, ?, ?)";

    private final EntityManager em;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public WeatherDaoImpl(EntityManager em, JdbcTemplate jdbcTemplate) {
        this.em = em;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Сохраняет погоду в БД
     * @param weather - entity-объект погоды
     */
    @Override
    public void save(Weather weather) {
        em.persist(weather);
    }

    /**
     * Ищет погоду в БД для заданного города
     * @param cityName - город
     * @return возвращает найденую погоду (entity-объект)
     * @throws NotFoundException Если погода не найдена
     */
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

    /**
     * Добавляет пакетно список объектов погоды в БД через хранимую процедуру
     * @param viewSet - список объектов погоды
     */
    @Override
    @Transactional
    public void saveBatch(List<WeatherView> viewSet) {
        jdbcTemplate.batchUpdate(CALL_UPSERT_WEATHER_FUNC, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                WeatherView view = viewSet.get(i);
                preparedStatement.setInt(1, view.getTemp());
                preparedStatement.setString(2, view.getText());
                preparedStatement.setDate(3, new Date(view.getDate().getTime()));
                preparedStatement.setString(4, view.getCity());
            }

            @Override
            public int getBatchSize() {
                return viewSet.size();
            }
        });
    }
}
