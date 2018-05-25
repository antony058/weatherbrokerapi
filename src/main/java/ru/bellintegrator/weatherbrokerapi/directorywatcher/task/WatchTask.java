package ru.bellintegrator.weatherbrokerapi.directorywatcher.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.bellintegrator.weatherbrokerapi.directorywatcher.handler.WeatherHandler;
import ru.bellintegrator.weatherbrokerapi.directorywatcher.weatherfile.WeatherFile;
import ru.bellintegrator.weatherbrokerapi.weather.view.WeatherView;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.*;

@Component
@Scope("prototype")
public class WatchTask implements Runnable {
    private final WatchService watchService;
    private final WeatherHandler weatherHandler;

    private Path path;
    private WeatherFile weatherFile;
    private String directoryPath;
    private ExecutorService executorService = Executors.newScheduledThreadPool(1);

    private static final int WAIT_FILE_EXIST_TIMEOUT = 5;

    @Autowired
    public WatchTask(WatchService watchService, WeatherHandler weatherHandler) {
        this.watchService = watchService;
        this.weatherHandler = weatherHandler;
    }

    /**
     * Установка полного пути отслеживаемой директории.
     * Путь берется относительно каталога <i>bin</i> сервера приложений
     * @param directoryPath - полный путь каталога
     * @throws IOException
     */
    public void setDirectoryPath(String directoryPath) throws IOException {
        this.directoryPath = directoryPath;

        pathRegister();
    }

    /**
     * Установка объекта работы(чтение файла) с добавляемыми файлами в отслеживамый каталог
     * @param weatherFile - объект с файлами определенного формата
     */
    public void setWeatherFile(WeatherFile weatherFile) {
        this.weatherFile = weatherFile;
    }

    /**
     * Регистрация сервиса наблюдения и событий, на которые он будет реагировать
     * @throws IOException
     */
    private void pathRegister() throws IOException {
        path = Paths.get(directoryPath);
        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
    }

    /**
     * Отслеживание событий добавления новых файлов, их обработка и дальнейшее руководство на
     * чтение добавленных файлов, с дальнейшей записью результатов в {@link WeatherHandler#weatherViews}
     */
    @Override
    public void run() {
        WatchKey key = null;
        try {
            while((key = watchService.take()) != null) {
                for (WatchEvent<?> event: key.pollEvents()) {
                    String filePath = directoryPath + "\\" + event.context();
                    try {
                        waitFileExist(filePath);
                        addWeatherListToHandlerSet(weatherFile.readFile(filePath));
                    } catch (IOException | ExecutionException | TimeoutException e) {
                        e.printStackTrace();
                    }
                }

                key.reset();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Последовательное добавление списка объектов погоды в {@link WeatherHandler#weatherViews}
     * @param weatherViews
     */
    private void addWeatherListToHandlerSet(List<WeatherView> weatherViews)  {
        for (WeatherView view: weatherViews) {
            weatherHandler.addWeatherView(view);
        }
    }

    /**
     * Ожидание создания и готовности файла
     * @param filePath - полный путь файла
     * @throws InterruptedException Если прерван поток
     * @throws ExecutionException Если ошибка во время ожидания результата из дочернего потока
     * @throws TimeoutException Если результат не был получен до завершения таймаута
     */
    private void waitFileExist(String filePath) throws InterruptedException, ExecutionException, TimeoutException {
        Future<Boolean> future = executorService.submit(new CheckFileExistTask(filePath));
        future.get(WAIT_FILE_EXIST_TIMEOUT, TimeUnit.SECONDS);
    }
}
