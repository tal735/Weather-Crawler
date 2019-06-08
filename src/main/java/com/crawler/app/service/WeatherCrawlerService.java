package com.crawler.app.service;

import com.crawler.app.domain.Country;
import com.crawler.app.domain.WeatherDto;

import java.util.Collection;

public interface WeatherCrawlerService {
    void refreshLocations();

    Collection<Country> getAllCountries();

    WeatherDto getWeatherStats(String country, String city);
}
