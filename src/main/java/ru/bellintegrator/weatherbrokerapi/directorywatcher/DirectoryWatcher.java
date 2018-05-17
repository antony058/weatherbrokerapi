package ru.bellintegrator.weatherbrokerapi.directorywatcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.bellintegrator.weatherbrokerapi.directorywatcher.task.WatchTask;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.nio.file.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class DirectoryWatcher {

    private final WatchService watchService;
    private final ExecutorService watchServiceSingleThread = Executors.newSingleThreadExecutor();
    private final ApplicationContext applicationContext;

    @Value("${weatherfiles.path}")
    private String weatherFilesPath;

    @Autowired
    public DirectoryWatcher(WatchService watchService, ApplicationContext applicationContext) {
        this.watchService = watchService;
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void initialize() throws IOException {
        WatchTask watchTask = new WatchTask(watchService, weatherFilesPath, applicationContext);
        watchServiceSingleThread.execute(watchTask);
    }

    @PreDestroy
    public void shutdown() {
        watchServiceSingleThread.shutdown();
    }
}
