package ru.bellintegrator.weatherbrokerapi.weather.view;

import java.util.LinkedHashMap;

public class Query {

    private Object query;

    public void setQuery(Object object) {
        this.query = object;
    }

    public Object getQuery() {
        return query;
    }

    public boolean isQueryResultExist() {
        return ((LinkedHashMap<String, Object>) query).get("results") != null;
    }
}
