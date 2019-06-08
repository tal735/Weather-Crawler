package com.crawler.app.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeatherCrawlerServiceImplTest {

    @Test
    public void getCountryFromHref() {
        String href = "window.location.href='/countries/Israel'";
        int index = href.indexOf("/countries");
        String substring = href.substring(index);
        System.out.println(substring);
    }

}