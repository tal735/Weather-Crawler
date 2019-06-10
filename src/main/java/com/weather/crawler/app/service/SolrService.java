package com.weather.crawler.app.service;

import com.weather.crawler.app.domain.Country;
import com.weather.crawler.app.domain.LocationResultDto;

import java.util.Collection;

public interface SolrService {
    Collection<LocationResultDto> getLocations(String locationQuery);

    void addLocations(Collection<Country> countries);
}