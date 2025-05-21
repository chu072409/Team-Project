package com.example.weather.weather.model;

import com.example.weather.utils.WeatherUtils;

public class DailyWeather {
    private String date;
    private String iconCode;
    private String weatherDescription;
    private int temperature;

    public DailyWeather(String date, int temperature, String iconCode, String weatherDescription) {
        this.date = date;
        this.temperature = temperature;
        this.iconCode = iconCode;
        this.weatherDescription = WeatherUtils.getWeatherDescription(iconCode);  // 날씨 설명 추가
    }

    public String getDate() {
        return date;
    }

    public int getTemperature() {
        return temperature;
    }

    public String getIconCode() {
        return iconCode;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }
}
