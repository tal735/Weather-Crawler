package com.weather.crawler.app.dao;

import com.weather.crawler.app.domain.Country;

import java.util.Collection;

public interface LocationDao {
    Collection<Country> getAllCountries();

    void addCountries(Collection<Country> countries);
}
