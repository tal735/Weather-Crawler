package com.crawler.app.service;

import com.crawler.app.dao.LocationDao;
import com.crawler.app.domain.Country;
import com.crawler.app.domain.Location;
import com.crawler.app.domain.WeatherDto;
import com.crawler.app.service.constants.TimeAndDateConstants;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("timeAndDateWeatherCrawlerServiceImpl")
public class TimeAndDateWeatherCrawlerServiceImpl implements WeatherCrawlerService {


    private static final Logger log = LoggerFactory.getLogger(TimeAndDateWeatherCrawlerServiceImpl.class);

    private final static Pattern LOCATION_URL_REGEX = Pattern.compile("/weather/(.+)/(.+)$");
    private final static String A_HREF_LOCATION_SELECTOR = "a[href^=/weather/]";
    private final static String TABLE_TR_SELECTOR = "table tr";
    private final static String HREF = "href";

    private final LocationDao locationDao;

    @Autowired
    public TimeAndDateWeatherCrawlerServiceImpl(LocationDao locationDao) {
        this.locationDao = locationDao;
    }

    @Override
    public void refreshLocations() {
        Collection<Country> countries = getLatestCountries();
        for (Country country : countries) {
            Collection<Location> cities = getCities(country);
            country.addCities(cities);
        }

        locationDao.addCountries(countries);
    }

    @Override
    public Collection<Country> getAllCountries() {
        return locationDao.getAllCountries();
    }

    @Override
    public WeatherDto getWeatherStats(String country, String city) {
        try {
            Document document = Jsoup.connect(String.format(TimeAndDateConstants.WEATHER_COUNTRY_CITY_URL, country, city)).get();
            return getWeatherStats(document);
        } catch (Exception e) {
            log.error("Exception while fetching weather for " + city, e);
            return null;
        }
    }

    private Collection<Country> getLatestCountries() {
        Set<Country> countries = Sets.newHashSet();
        try {
            Document document = Jsoup.connect(TimeAndDateConstants.WEATHER_URL).data("low", "c", "sort", "1").get(); //list of capitals
            Elements capitals = document.select(TABLE_TR_SELECTOR);
            for (Element capital : capitals) {
                Elements capitalElem = capital.select(A_HREF_LOCATION_SELECTOR);
                String href = capitalElem.attr(HREF);
                Matcher regexMatcher = LOCATION_URL_REGEX.matcher(href);
                if (regexMatcher.find()) {
                    String displayName = StringUtils.split(capitalElem.text(), "(.+)-(.+)")[0].trim();
                    String urlName = regexMatcher.group(1);
                    Country country = Country.initialize(displayName, urlName);
                    countries.add(country);
                }
            }
        } catch (Exception e) {
            log.error("Error while fetching countries", e);
        }
        return countries;
    }

    private Collection<Location> getCities(Country country) {
        Set<Location> cities = Sets.newHashSet();
        try {
            Document document = Jsoup.connect(String.format(TimeAndDateConstants.WEATHER_COUNTRY_URL, country.getUrlName())).get();
            Elements tableRows = document.select(TABLE_TR_SELECTOR);
            for (Element tableRow : tableRows) {
                Elements rowCities = tableRow.select(A_HREF_LOCATION_SELECTOR);
                for (Element colCity : rowCities) {
                    String href = colCity.attr(HREF);
                    Matcher regexMatcher = LOCATION_URL_REGEX.matcher(href);
                    if (regexMatcher.find()) {
                        String displayName = colCity.text();
                        String urlName = regexMatcher.group(2);
                        Location cityLocation = Location.build(displayName, urlName);
                        cities.add(cityLocation);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error accessing cities page for country: " + country);
        }
        return cities;
    }

    private WeatherDto getWeatherStats(Document document) {
        WeatherDto dto = new WeatherDto();

        Element leftPanel = document.getElementById("qlook");
        String location = document.select("body > div.wrapper > div.main-content-div > div:nth-child(2) > header > h1").text();
        String temperature = leftPanel.getElementsByClass("h2").get(0).text();
        String humidity = document.select("#qfacts > p:nth-child(7)").text();
        String windSpeed = extractWindSpeed(leftPanel);
        String weatherIconUrl = document.getElementById("cur-weather").attr("src");

        dto.setLocation(location);
        dto.setTemperature(temperature);
        dto.setHumidity(humidity);
        dto.setWind(windSpeed);
        dto.setIcon(weatherIconUrl);

        return dto;
    }

    private String extractWindSpeed(Element panel) {
        String p = panel.select("p").get(1).text();
        int begin = p.indexOf("Wind: ") + "Wind: ".length();
        int end = p.indexOf("km/h") + "km/h".length();
        return StringUtils.substring(p, begin, end);
    }
}