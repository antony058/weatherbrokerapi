package ru.bellintegrator.weatherbrokerapi.weather.model;

import ru.bellintegrator.weatherbrokerapi.city.model.City;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "weather")
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "temperature")
    private Integer temperature;

    @Column(name = "description")
    private String description;

    @Column(name = "weather_date")
    private Date date;

    @OneToOne
    @JoinColumn(name = "city_id")
    private City city;

    public Weather() {

    }

    public Weather(Integer temperature, String description, Date date) {
        this.temperature = temperature;
        this.description = description;
        this.date = date;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "id: " + id + "\ntemperature: " + temperature + "\ndescription: " + description;
    }
}
