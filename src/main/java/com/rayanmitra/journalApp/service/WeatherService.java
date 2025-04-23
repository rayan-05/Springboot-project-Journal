package com.rayanmitra.journalApp.service;

import com.rayanmitra.journalApp.api.response.WeatherResponse;
import com.rayanmitra.journalApp.cache.AppCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppCache appCache;

    @Autowired
    private RedisService redisService;

    public WeatherResponse getWeather(String city) {
        WeatherResponse weatherResponse = redisService.get("weather_of_" + city, WeatherResponse.class);
        if(weatherResponse == null) {
            return weatherResponse;
        }
        else{
            if (city == null || city.trim().isEmpty()) {
                throw new IllegalArgumentException("City cannot be null or empty");
            }
            String weatherApiTemplate = appCache.APP_CACHE.get("weather_api");
            if (weatherApiTemplate == null) {
                throw new IllegalStateException("Weather API template not found in cache");
            }
            String finalAPI = weatherApiTemplate.replace("<city>", city).replace("<apiKey>", apiKey);
            ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
            WeatherResponse body= response.getBody();
            if(body != null) {
                redisService.set("weather_of_" + city,body,300l);
            }
            return body;
        }

    }
}