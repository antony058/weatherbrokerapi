package ru.bellintegrator.weatherbrokerapi.directorywatcher.handler;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bellintegrator.weatherbrokerapi.weather.service.WeatherService;
import ru.bellintegrator.weatherbrokerapi.weather.view.WeatherView;

import java.util.Iterator;
import java.util.Set;

@Component
public class WeatherHandler {
    private final WeatherService weatherService;
    private Set<WeatherView> weatherViews;

    private final static int WEATHER_VIEWS_MAX_SIZE = 2;

    @Autowired
    public WeatherHandler(Set<WeatherView> weatherViews, WeatherService weatherService) {
        this.weatherViews = weatherViews;
        this.weatherService = weatherService;
    }

    /**
     * Добавляет переданную в качестве параметра погоду в список, если список пуст
     * или существующая погода в списке для такого же города является более старой(сравнение по дате).
     * Если количество элементов в списке достигло установленного количества -
     * данные из списка записываются в базу данных, и список очищается.
     *
     * @param view
     */
    public void addWeatherView(WeatherView view) {
        if (removeWeatherIfLess(view)) {
            weatherViews.add(view);
        }

        if (weatherViews.size() >= WEATHER_VIEWS_MAX_SIZE) {
            addWeatherSetToDatabase();
            weatherViews.clear();
        }
    }

    /**
     * Удаляет погоду из списка, если она более старая (сравнение по дате), нежели погода, переданная
     * в качестве входного параметра.
     *
     * @param view
     * @return
     */
    private boolean removeWeatherIfLess(WeatherView view) {
        Iterator<WeatherView> iterator = weatherViews.iterator();

        while (iterator.hasNext()) {
            WeatherView weatherView = iterator.next();

            if (weatherView.getCity().equals(view.getCity())) {
                if (weatherView.getDate().compareTo(view.getDate()) < 0) {
                    iterator.remove();

                    return true; // Если в списке был найден такой же город и его погода была устаревшей (по дате)
                }

                return false; // Если в списке найден такой же город и его погода более новая (по дате)
            }
        }

        return true; // Если список пуст или погода для переданного города в этом списке не найдена
    }

    /**
     * Добавляет все данные о погоде из списка в базу данных.
     */
    private void addWeatherSetToDatabase() {
        weatherService.saveBatch(weatherViews);
    }

}
