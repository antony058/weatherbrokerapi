package ru.bellintegrator.weatherbrokerapi.weather.utils.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.bellintegrator.weatherbrokerapi.exception.ValidationException;
import ru.bellintegrator.weatherbrokerapi.weather.utils.validation.WeatherValidator;
import ru.bellintegrator.weatherbrokerapi.weather.view.WeatherView;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class WeatherMapper {
    private final static String INCORRECT_ATTR_LENGTH = "Количество атрибутов не может быть не равным 4";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public static WeatherView fromString(String weatherLine) throws ValidationException, ParseException {
        String[] weatherAttr = weatherLine.split(",");
        if (weatherAttr.length != 4) {
            throw new ValidationException(INCORRECT_ATTR_LENGTH);
        }

        WeatherView view = new WeatherView();

        if (WeatherValidator.isValidCityName(weatherAttr[0]))
            view.setCity(weatherAttr[0]);

        if (WeatherValidator.isValidDate(weatherAttr[1]))
            view.setDate(dateFormat.parse(weatherAttr[1]));

        if (WeatherValidator.isValidTemperature(weatherAttr[2]))
            view.setTemp(Integer.valueOf(weatherAttr[2]));

        if (WeatherValidator.isValidDescription(weatherAttr[3]))
            view.setText(weatherAttr[3]);

        return view;
    }

    public static List<WeatherView> fromJsonString(String json) throws IOException, ValidationException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(dateFormat);
        List<WeatherView> weatherViews = objectMapper.readValue(json, new TypeReference<List<WeatherView>>(){});

        for (WeatherView view: weatherViews) {
            WeatherValidator.isValidCityName(view.getCity());
            WeatherValidator.isValidDate(view.getDate().toString());
            WeatherValidator.isValidTemperature(String.valueOf(view.getTemp()));
            WeatherValidator.isValidDescription(view.getText());
        }

        return weatherViews;
    }
}
