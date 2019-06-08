/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.crawler.app.main;

import com.crawler.app.schedule.ScheduledTasks;
import org.apache.commons.lang3.StringUtils;
import org.awaitility.Duration;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ScheduledTasksTest {

    @SpyBean
    ScheduledTasks tasks;

    @Test
    public void reportCurrentTime() {
        await().atMost(Duration.TEN_SECONDS).untilAsserted(() -> {
            verify(tasks, atLeast(2)).getWeatherStats();
        });
    }

    @Test
    public void getCountryFromHref() {
        String href = "/countries/Afghanistan";
        Pattern regex = Pattern.compile("/countries/(\\w+)$");
        Matcher regexMatcher = regex.matcher(href);
        if (regexMatcher.find()) {
            String country = regexMatcher.group(1);
            System.out.println(country);
        }
    }

    @Test
    public void getCityFromHref() {
        String href = "/locations/West-Jerusalem/forecasts/latest";
        Pattern regex = Pattern.compile("/locations/(.*)/forecasts/latest$");
        Matcher regexMatcher = regex.matcher(href);
        if (regexMatcher.find()) {
            String country = regexMatcher.group(1);
            System.out.println(country);
        } else {
            System.out.println("No :(");
        }
    }

    @Test
    public void splitByRegex() {
        String name = "Saudi Arabia - Riyadh";
        int i = name.lastIndexOf('-');
        String substring = name.substring(0, i);
        System.out.println(substring);
        String[] split = StringUtils.split(name, "(.+)-(.+)");
        System.out.println(split[0].trim());
    }

    @Test
    public void getWindFromParagraph() {
        String p = "Feels Like: 34 °C Forecast: 36 / 22 °C Wind: 19 km/h ↑ from Northwest";
        int begin = p.indexOf("Wind: ") + "Wind: ".length();
        int end = p.indexOf("km/h") + "km/h".length();
        String substring = StringUtils.substring(p, begin, end);
        System.out.println(substring);
    }
}