package com.weather.crawler.app.service;

import com.weather.crawler.app.domain.Country;
import com.weather.crawler.app.domain.WeatherDto;

import java.util.Collection;

public interface WeatherCrawlerService {
    void refreshLocations();

    Collection<Country> getAllCountries();

    WeatherDto getWeatherStats(String country, String city);
}
