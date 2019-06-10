package com.weather.crawler.app.main;

import static org.assertj.core.api.Assertions.assertThat;

import com.weather.crawler.app.schedule.ScheduledTasks;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {
    
    @Autowired
    private ScheduledTasks tasks;

    @Test
    public void contextLoads() {
        // Basic integration test that shows the context starts up properly
        assertThat(tasks).isNotNull();
    }

    /*
    @Autowired
    SolrService solrService;
    @Test
    public void addLocation() {
        Country country = Country.initialize("Israel", "israel");
        Location batYam = Location.initialize("Bat Yam", "bat-yam");
        Location telAviv = Location.initialize("Tel-Aviv", "tel-aviv");
        country.addCities(Lists.newArrayList(batYam, telAviv));
        solrService.addLocations(Lists.newArrayList(country));
    }
    */
}
