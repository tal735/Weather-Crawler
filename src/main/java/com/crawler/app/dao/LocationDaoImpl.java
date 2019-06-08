package com.crawler.app.dao;

import com.crawler.app.domain.Country;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

@Component
public class LocationDaoImpl implements LocationDao {

    private Set<Country> countries = Sets.newHashSet();

    @Override
    public Collection<Country> getAllCountries() {
        return countries;
    }

    @Override
    public void addCountries(Collection<Country> countries) {
        this.countries.addAll(countries);
    }
}
