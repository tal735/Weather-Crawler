package com.crawler.app.service;

import com.crawler.app.dao.LocationDao;
import com.crawler.app.domain.Country;
import com.crawler.app.domain.Location;
import com.crawler.app.domain.WeatherDto;
import com.crawler.app.service.constants.WeatherForecastConstants;
import com.google.common.collect.Sets;
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

@Service
public class WeatherCrawlerServiceImpl implements WeatherCrawlerService {

    private static final Logger log = LoggerFactory.getLogger(WeatherCrawlerServiceImpl.class);

    private final static Pattern COUNTRY_URL_REGEX = Pattern.compile("/countries/(.+)$");
    private final static Pattern CITY_URL_REGEX = Pattern.compile("/locations/(.+)/forecasts/latest$");
    private final static String A_HREF_COUNTRIES_SELECTOR = "a[href^=/countries/]";
    private final static String A_HREF_LOCATIONS_SELECTOR = "a[href^=/locations/]";
    private final static String HREF = "href";

    private final LocationDao locationDao;

    @Autowired
    public WeatherCrawlerServiceImpl(LocationDao locationDao) {
        this.locationDao = locationDao;
    }

    @Override
    public void refreshLocations() {
        Collection<Country> countries = getLatestCountries();
        addLatestCities(countries);
        locationDao.addCountries(countries);
        for (Country country : countries) {
            System.out.println(country);
        }

    }

    private Collection<Country> getLatestCountries() {
        Set<Country> countries = Sets.newHashSet();
        try {
            Document document = Jsoup.connect(WeatherForecastConstants.COUNTRIES_URL).get();
            Elements countryElements = document.select(A_HREF_COUNTRIES_SELECTOR);
            for (Element countryElement : countryElements) {
                String href = countryElement.attr(HREF);
                Matcher regexMatcher = COUNTRY_URL_REGEX.matcher(href);
                if (regexMatcher.find()) {
                    String displayName = countryElement.text();
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

    private void addLatestCities(Collection<Country> countries) {
        for (Country country : countries) {
            try {
                Document document = Jsoup.connect(WeatherForecastConstants.COUNTRIES_URL + "/" + country.getLocation().getUrlName()).get();
                Elements cities = document.select(A_HREF_LOCATIONS_SELECTOR);
                for (Element cityElement : cities) {
                    String href = cityElement.attr(HREF);
                    Matcher regexMatcher = CITY_URL_REGEX.matcher(href);
                    if (regexMatcher.find()) {
                        String displayName = cityElement.text();
                        String urlName = regexMatcher.group(1);
                        Location cityLocation = Location.build(displayName, urlName);
                        //country.addCity(cityLocation);
                    }
                }
            } catch (Exception e) {
                log.error("Error accessing cities page for country: " + country);
            }
        }
    }

    @Override
    public Collection<Country> getAllCountries() {
        return null;
    }

    @Override
    public WeatherDto getWeatherStats(String country, String city) {
        return null;
    }
}
