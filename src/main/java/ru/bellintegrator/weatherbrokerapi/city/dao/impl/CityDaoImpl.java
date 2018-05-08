package ru.bellintegrator.weatherbrokerapi.city.dao.impl;

import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.bellintegrator.weatherbrokerapi.city.dao.CityDao;
import ru.bellintegrator.weatherbrokerapi.city.model.City;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class CityDaoImpl implements CityDao {
    private Logger log = LoggerFactory.getLogger(CityDaoImpl.class);

    private final EntityManager em;

    @Autowired
    public CityDaoImpl(EntityManager em) {
        this.em = em;
    }


    @Override
    public void save(City city) {
        em.persist(city);
    }

    @Override
    public City loadByName(String cityName) throws NotFoundException {
        TypedQuery<City> query = em.createQuery("SELECT c from City c WHERE c.cityName='" + cityName + "'", City.class);

        try {
            return query.getSingleResult();
        } catch (NoResultException ex) {
            log.error("Город " + cityName + " не найден");
            throw new NotFoundException("City not founded");
        }
    }

    @Override
    public List<City> citiesLikeName(String cityName) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(City.class);

        Root<City> cityRoot = criteriaQuery.from(City.class);

        criteriaQuery.where(
                criteriaBuilder.like(
                        criteriaBuilder.lower(cityRoot.<String>get("cityName")),
                        "%" + cityName.toLowerCase() + "%")
        );

        TypedQuery<City> query = em.createQuery(criteriaQuery);
        return query.getResultList();
    }
}
