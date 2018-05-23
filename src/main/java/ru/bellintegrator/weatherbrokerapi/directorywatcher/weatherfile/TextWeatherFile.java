package ru.bellintegrator.weatherbrokerapi.directorywatcher.weatherfile;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.bellintegrator.weatherbrokerapi.exception.ValidationException;
import ru.bellintegrator.weatherbrokerapi.weather.utils.mapper.WeatherMapper;
import ru.bellintegrator.weatherbrokerapi.weather.view.WeatherView;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class TextWeatherFile implements WeatherFile {

    /**
     * Считывание TXT-файла
     * @param filePath - полный путь к файлу
     * @return список представлений погоды, считанных из файла
     * @throws IOException Если ошибка работы с файлом
     */
    @Override
    public List<WeatherView> readFile(String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream inputStream = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        WeatherView weatherView = null;
        List<WeatherView> weatherViews = new ArrayList<>();

        String line;
        while ((line = reader.readLine()) != null) {
            try {
                weatherView = WeatherMapper.fromString(line);
            } catch (ValidationException e) {
                e.printStackTrace();
                continue;
            } catch (ParseException e) {
                e.printStackTrace();
            }

            weatherViews.add(weatherView);
        }

        reader.close();
        inputStream.close();

        boolean isSuccessDeleted = file.delete();
        System.out.println("Success deleted: " + isSuccessDeleted);

        return weatherViews;
    }
}
