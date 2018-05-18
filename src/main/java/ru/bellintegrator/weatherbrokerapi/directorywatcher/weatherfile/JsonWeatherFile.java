package ru.bellintegrator.weatherbrokerapi.directorywatcher.weatherfile;

import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.bellintegrator.weatherbrokerapi.exception.ValidationException;
import ru.bellintegrator.weatherbrokerapi.weather.utils.mapper.WeatherMapper;
import ru.bellintegrator.weatherbrokerapi.weather.view.WeatherView;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Component
@Scope("prototype")
public class JsonWeatherFile implements WeatherFile {

    @Override
    public List<WeatherView> readFile(String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream inputStream = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        List<WeatherView> weatherViews;

        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        try {
            weatherViews = WeatherMapper.fromJsonString(builder.toString());
        } catch (ValidationException | JsonParseException e) {
            e.printStackTrace();
            weatherViews = Collections.emptyList();
        }

        reader.close();
        inputStream.close();

        boolean isSuccessDeleted = file.delete();
        System.out.println("Success deleted \'" + filePath + "\': " + isSuccessDeleted);

        return weatherViews;
    }
}
