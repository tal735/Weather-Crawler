package com.weather.crawler.app.schedule;

import com.weather.crawler.app.domain.Country;
import com.weather.crawler.app.domain.Location;
import com.weather.crawler.app.domain.WeatherDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.weather.crawler.app.service.WeatherCrawlerService;

import java.util.Collection;

@Component
public class ScheduledTasks {

    private final WeatherCrawlerService weatherCrawlerService;

    @Autowired
    public ScheduledTasks(@Qualifier("timeAndDateWeatherCrawlerServiceImpl") WeatherCrawlerService weatherCrawlerService) {
        this.weatherCrawlerService = weatherCrawlerService;
    }

    @Scheduled(fixedRate = 1000 * 60 * 60, initialDelay = 1000 * 60 * 60) //every 1 hour
    public synchronized void getWeatherStats() {
        System.out.println("Starting Task getWeatherStats");
        weatherCrawlerService.refreshLocations();
        Collection<Country> countries = weatherCrawlerService.getAllCountries();
        for (Country country : countries) {
            for (Location city : country.getCities()) {
                WeatherDto weatherDto = weatherCrawlerService.getWeatherStats(country.getUrlName(), city.getUrlName());
                System.out.println(weatherDto);
            }
        }
    }
}
