package com.weather.crawler.app.domain;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Set;

public class Country {
    private Location location;
    private Set<Location> cities = Sets.newHashSet();

    public static Country initialize(String displayName, String urlName) {
        Location location = Location.build(displayName, urlName);
        return new Country(location);
    }

    private Country(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public Set<Location> getCities() {
        return cities;
    }

    public void addCities(Collection<Location> cities) {
        for (Location city : cities) {
            city.setParent(location);
            getCities().add(city);
        }
    }

    public String getUrlName() {
        return getLocation().getUrlName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return Objects.equal(location, country.location);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(location);
    }

    @Override
    public String toString() {
        return "Country{" +
                "location=" + location +
                ", cities=" + cities +
                '}';
    }
}
