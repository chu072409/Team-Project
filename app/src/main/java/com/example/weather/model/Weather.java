package com.example.weather.model;

import com.example.weather.utils.WeatherUtils;
//test1
public class Weather {
    private String cityName;
    private double temperature;
    private String weatherIcon;
    private String weatherDescription;

    public Weather(String cityName, double temperature, String weatherIcon, String weatherDescription) {
        this.cityName = cityName;
        this.temperature = temperature;
        this.weatherIcon = weatherIcon;
        this.weatherDescription = WeatherUtils.getWeatherDescription(weatherIcon);  // 날씨 설명 추가
    }

    public String getCityName() {
        return cityName;
    }

    public double getTemperature() {
        return temperature;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }
}
