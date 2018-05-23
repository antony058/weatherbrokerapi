package ru.bellintegrator.weatherbrokerapi.directorywatcher.task;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

public class CheckFileExistTask implements Callable<Boolean> {
    private final Path filePath;

    public CheckFileExistTask(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    /**
     * Ожидаем, пока не создастся файл
     * @return true, если файл создан
     */
    @Override
    public Boolean call() {
        while (true) {
            if (Files.exists(filePath)) {
                return true;
            }
        }
    }
}
