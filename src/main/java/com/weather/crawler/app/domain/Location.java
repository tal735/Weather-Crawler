package com.weather.crawler.app.domain;

import com.google.common.base.Objects;

public class Location {

    private Location parent;
    private final String displayName;
    private final String urlName;

    public static Location build(String displayName, String urlName) {
        return new Location(displayName,  urlName);
    }

    private Location(String displayName, String urlName) {
        this.displayName = displayName;
        this.urlName = urlName;
    }

    public Location getParent() {
        return parent;
    }

    public void setParent(Location parent) {
        this.parent = parent;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUrlName() {
        return urlName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equal(displayName, location.displayName) &&
                Objects.equal(urlName, location.urlName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(displayName, urlName);
    }

    @Override
    public String toString() {
        return "Location{" +
                "displayName='" + displayName + '\'' +
                ", urlName='" + urlName + '\'' +
                '}';
    }
}
