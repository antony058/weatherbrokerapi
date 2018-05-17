package ru.bellintegrator.weatherbrokerapi.directorywatcher.task;

import javassist.NotFoundException;
import org.springframework.context.ApplicationContext;
import ru.bellintegrator.weatherbrokerapi.fileinteraction.FileCommunicator;
import ru.bellintegrator.weatherbrokerapi.weather.service.impl.WeatherServiceImpl;
import ru.bellintegrator.weatherbrokerapi.weather.view.WeatherView;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class WatchTask implements Runnable {
    private WatchService watchService;
    private Path path;
    private String directoryPath;
    private WeatherServiceImpl weatherService;

    public WatchTask(final WatchService watchService,final  String directoryPath,
                     ApplicationContext applicationContext) throws IOException {
        this.watchService = watchService;
        this.directoryPath = directoryPath;
        this.path = Paths.get(directoryPath);
        weatherService = applicationContext.getBean(WeatherServiceImpl.class);

        pathRegister();
    }

    private void pathRegister() throws IOException {
        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
    }

    @Override
    public void run() {
        try {
            WatchKey key;
            while((key = watchService.take()) != null) {
                for (WatchEvent<?> event: key.pollEvents()) {
                    String fullPath = directoryPath + "\\" + event.context();
                    addWeatherListToDatabase(FileCommunicator.readFile(fullPath));
                }

                key.reset();
            }
        } catch (InterruptedException | IOException | NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void addWeatherListToDatabase(List<WeatherView> weatherViews) throws NotFoundException {
        for (WeatherView view: weatherViews) {
            weatherService.save(view);
        }
    }
}
