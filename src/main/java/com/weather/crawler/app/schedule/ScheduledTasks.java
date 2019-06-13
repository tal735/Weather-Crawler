package com.weather.crawler.app.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.weather.crawler.app.service.WeatherCrawlerService;

@Component
public class ScheduledTasks {

    private final WeatherCrawlerService weatherCrawlerService;

    @Autowired
    public ScheduledTasks(WeatherCrawlerService weatherCrawlerService) {
        this.weatherCrawlerService = weatherCrawlerService;
    }

    @Scheduled(fixedRate = 1000 * 60 * 60, initialDelay = 1000 * 60 * 60) //every 1 hour
    public synchronized void getWeatherStats() {
        System.out.println("Starting Task getWeatherStats");
        weatherCrawlerService.refreshLocations();
    }
}
