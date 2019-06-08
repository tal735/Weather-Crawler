package com.crawler.app.service.constants;

public class WeatherForecastConstants {

    private final static String PROTOCOL = "https";

    private final static String DOMAIN = PROTOCOL + "://" + "www.weather-forecast.com";

    public final static String COUNTRIES_URL = DOMAIN + "/countries";

    public final static String CITY_FORECAST_FORMAT_URL = DOMAIN + "/locations/{0}/forecasts/latest";
}
