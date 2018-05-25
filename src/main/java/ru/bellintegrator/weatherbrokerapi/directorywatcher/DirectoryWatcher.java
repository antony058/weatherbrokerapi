package ru.bellintegrator.weatherbrokerapi.directorywatcher;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.bellintegrator.weatherbrokerapi.directorywatcher.task.WatchTask;
import ru.bellintegrator.weatherbrokerapi.directorywatcher.weatherfile.JsonWeatherFile;
import ru.bellintegrator.weatherbrokerapi.directorywatcher.weatherfile.TextWeatherFile;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Component
public class DirectoryWatcher {

    private final WatchTask textFileWatchTask;
    private final WatchTask jsonFileWatchTask;
    private final ExecutorService watchServiceThreadPool;
    private final TextWeatherFile textWeatherFile;
    private final JsonWeatherFile jsonWeatherFile;

    @Autowired
    private ObjectFactory<WatchTask> watchServiceObjectFactory;

    @Value("${weatherfiles.path.text}")
    private String weatherTextFilesPath;

    @Value("${weatherfiles.path.json}")
    private String weatherJsonFilesPath;

    @Autowired
    public DirectoryWatcher(WatchTask textFileWatchTask, WatchTask jsonFileWatchTask,
                            TextWeatherFile textWeatherFile, JsonWeatherFile jsonWeatherFile,
                            ExecutorService executorService) {
        this.textFileWatchTask = textFileWatchTask;
        this.jsonFileWatchTask = jsonFileWatchTask;
        this.textWeatherFile = textWeatherFile;
        this.jsonWeatherFile = jsonWeatherFile;
        this.watchServiceThreadPool = executorService;
    }

    @PostConstruct
    public void initialize() throws IOException {
        textFileWatchTask.setDirectoryPath(weatherTextFilesPath);
        textFileWatchTask.setWeatherFile(textWeatherFile);

        jsonFileWatchTask.setDirectoryPath(weatherJsonFilesPath);
        jsonFileWatchTask.setWeatherFile(jsonWeatherFile);

        watchServiceThreadPool.execute(textFileWatchTask);
        watchServiceThreadPool.execute(jsonFileWatchTask);

        List<WatchTask> watchServices = new ArrayList<>();
        for (int i=0;i<10;i++) {
            watchServices.add(watchServiceObjectFactory.getObject());
        }
        System.out.println(watchServices);
    }

    @PreDestroy
    public void shutdown() {
        watchServiceThreadPool.shutdown();
    }
}
