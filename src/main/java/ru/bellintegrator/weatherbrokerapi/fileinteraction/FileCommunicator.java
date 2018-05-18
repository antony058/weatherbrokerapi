package ru.bellintegrator.weatherbrokerapi.fileinteraction;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.bellintegrator.weatherbrokerapi.weather.view.WeatherView;

import java.io.*;
import java.util.List;

@Component
@Scope("prototype")
public class FileCommunicator {
    private String filePath;

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public List<WeatherView> readFile() throws IOException {
        return null;
    }
}
