package ru.bellintegrator.weatherbrokerapi.weather.utils.validation;

import ru.bellintegrator.weatherbrokerapi.exception.ValidationException;

public class WeatherValidator {
    private final static String INCORRECT_CITY = "Значение названия города не может быть таким коротким";
    private final static String INCORRECT_DATE = "Значение даты должно равняться 8 символам. Пример: dd-MM-yyyy";
    private final static String INCORRECT_TEMPERATURE = "Нельзя использовать значение температуры другой планеты";
    private final static String INCORRECT_DESCRIPTION = "Описание должно быть более полным";

    public static boolean isValidCityName(String cityName) throws ValidationException {
        cityName = cityName.trim();

        if (cityName.length() < 3) {
            throw new ValidationException(INCORRECT_CITY);
        }

        return true;
    }

    public static boolean isValidDate(String date) throws ValidationException {
        date = date.trim();

        if (date.length() < 8) {
            throw new ValidationException(INCORRECT_DATE);
        }

        return true;
    }

    public static boolean isValidTemperature(String temp) throws ValidationException {
        int temperature = Integer.valueOf(temp.trim());

        if (temperature < -70 || temperature > 70) {
            throw new ValidationException(INCORRECT_TEMPERATURE);
        }

        return true;
    }

    public static boolean isValidDescription(String description) throws ValidationException {
        description = description.trim();

        if (description.length() < 4) {
            throw new ValidationException(INCORRECT_DESCRIPTION);
        }

        return true;
    }
}
