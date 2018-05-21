package ru.bellintegrator.weatherbrokerapi.directorywatcher.task;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.bellintegrator.weatherbrokerapi.directorywatcher.handler.WeatherHandler;
import ru.bellintegrator.weatherbrokerapi.directorywatcher.weatherfile.WeatherFile;
import ru.bellintegrator.weatherbrokerapi.weather.service.WeatherService;
import ru.bellintegrator.weatherbrokerapi.weather.view.WeatherView;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@Component
@Scope("prototype")
public class WatchTask implements Runnable {
    private final WatchService watchService;
    private final WeatherService weatherService;
    private final WeatherHandler weatherHandler;

    private Path path;
    private WeatherFile weatherFile;
    private String directoryPath;

    @Autowired
    public WatchTask(WatchService watchService, WeatherService weatherService, WeatherHandler weatherHandler) {
        this.watchService = watchService;
        this.weatherService = weatherService;
        this.weatherHandler = weatherHandler;
    }

    public void setDirectoryPath(String directoryPath) throws IOException {
        this.directoryPath = directoryPath;

        pathRegister();
    }

    public void setWeatherFile(WeatherFile weatherFile) {
        this.weatherFile = weatherFile;
    }

    private void pathRegister() throws IOException {
        path = Paths.get(directoryPath);
        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
    }

    @Override
    public void run() {
        WatchKey key = null;
        try {
            while((key = watchService.take()) != null) {
                for (WatchEvent<?> event: key.pollEvents()) {
                    String filePath = directoryPath + "\\" + event.context();
                    try {
                        addWeatherListToHandlerSet(weatherFile.readFile(filePath));
                    } catch (IOException | NotFoundException e) {
                        e.printStackTrace();
                    }
                }

                key.reset();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void addWeatherListToHandlerSet(List<WeatherView> weatherViews) throws NotFoundException {
        for (WeatherView view: weatherViews) {
            weatherHandler.addWeatherView(view);
        }
    }
}
