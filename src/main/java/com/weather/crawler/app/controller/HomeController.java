package com.weather.crawler.app.controller;

import com.weather.crawler.app.domain.LocationResultDto;
import com.weather.crawler.app.domain.WeatherDto;
import com.weather.crawler.app.service.SolrService;
import com.weather.crawler.app.service.WeatherCrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Controller
public class HomeController {

    private final WeatherCrawlerService weatherCrawlerService;
    private final SolrService solrService;

    @Autowired
    public HomeController(WeatherCrawlerService weatherCrawlerService, SolrService solrService) {
        this.weatherCrawlerService = weatherCrawlerService;
        this.solrService = solrService;
    }

    @GetMapping("/")
    public String showHomepage() {
        return "index";
    }

    @GetMapping("/weather")
    @ResponseBody
    public WeatherDto getWeatherStats(@RequestParam String country, @RequestParam String city) {
        return weatherCrawlerService.getWeatherStats(country, city);
    }

    @GetMapping()
    @ResponseBody
    public Collection<LocationResultDto> findLocation(@RequestParam(value = "query") String location) {
        return solrService.getLocations(location);
    }
}
