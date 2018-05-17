package ru.bellintegrator.weatherbrokerapi.fileinteraction;

import ru.bellintegrator.weatherbrokerapi.weather.view.WeatherView;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileCommunicator {
    public static List<WeatherView> readFile(String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream inputStream = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        WeatherView weatherView = null;
        List<WeatherView> weatherViews = new ArrayList<>();

        String line;
        while ((line = reader.readLine()) != null) {
            String[] weatherAttr = line.split(",");

            if (weatherAttr.length != 4)
                continue;

            weatherView = new WeatherView(
                    weatherAttr[0],
                    weatherAttr[1],
                    Integer.valueOf(weatherAttr[2]),
                    weatherAttr[3]);

            weatherViews.add(weatherView);
        }

        reader.close();
        inputStream.close();

        boolean isSuccessDeleted = file.delete();
        System.out.println("Success deleted: " + isSuccessDeleted);

        return weatherViews;
    }
}
