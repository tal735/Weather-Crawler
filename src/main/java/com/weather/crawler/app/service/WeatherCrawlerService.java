package com.weather.crawler.app.service;

import com.weather.crawler.app.domain.WeatherDto;

public interface WeatherCrawlerService {
    void refreshLocations();

    WeatherDto getWeatherStats(String country, String city);
}
