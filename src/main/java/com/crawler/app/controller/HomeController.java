package com.crawler.app.controller;

import com.crawler.app.domain.WeatherDto;
import com.crawler.app.service.WeatherCrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    private final WeatherCrawlerService weatherCrawlerService;

    @Autowired
    public HomeController(@Qualifier("timeAndDateWeatherCrawlerServiceImpl") WeatherCrawlerService weatherCrawlerService) {
        this.weatherCrawlerService = weatherCrawlerService;
    }

    @GetMapping("/")
    public String showHomepage() {
        return "index";
    }

    @GetMapping("/weather")
    public @ResponseBody WeatherDto getWeatherStats(@RequestParam String country, @RequestParam String city) {
        return weatherCrawlerService.getWeatherStats(country, city);
    }
}
