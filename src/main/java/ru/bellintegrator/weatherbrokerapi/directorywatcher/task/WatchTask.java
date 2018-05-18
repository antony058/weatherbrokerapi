package ru.bellintegrator.weatherbrokerapi.directorywatcher.task;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
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

    private Path path;
    private WeatherFile weatherFile;
    private String directoryPath;

    @Autowired
    public WatchTask(WatchService watchService, WeatherService weatherService) {
        this.watchService = watchService;
        this.weatherService = weatherService;
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
                        addWeatherListToDatabase(weatherFile.readFile(filePath));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                key.reset();
            }
        } catch (InterruptedException | NotFoundException e) {
            e.printStackTrace();
        } finally {
            if (key != null) {
                key.reset();
            }
        }
    }

    private void addWeatherListToDatabase(List<WeatherView> weatherViews) throws NotFoundException {
        for (WeatherView view: weatherViews) {
            weatherService.save(view);
        }
    }
}
