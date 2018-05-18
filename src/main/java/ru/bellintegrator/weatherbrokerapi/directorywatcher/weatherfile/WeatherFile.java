package ru.bellintegrator.weatherbrokerapi.directorywatcher.weatherfile;

import ru.bellintegrator.weatherbrokerapi.weather.view.WeatherView;

import java.io.IOException;
import java.util.List;

public interface WeatherFile {
    List<WeatherView> readFile(String filPath) throws IOException;
}
