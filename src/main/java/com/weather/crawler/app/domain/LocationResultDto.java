package com.weather.crawler.app.domain;

public class LocationResultDto {
    private String country;
    private String countryUrl;
    private String city;
    private String cityUrl;

    public static LocationResultDto create(String country, String countryUrl, String city, String cityUrl) {
        LocationResultDto dto = new LocationResultDto();
        dto.country = country;
        dto.countryUrl = countryUrl;
        dto.city = city;
        dto.cityUrl = cityUrl;
        return dto;
    }

    public String getCountry() {
        return country;
    }

    public String getCountryUrl() {
        return countryUrl;
    }

    public String getCity() {
        return city;
    }

    public String getCityUrl() {
        return cityUrl;
    }
}