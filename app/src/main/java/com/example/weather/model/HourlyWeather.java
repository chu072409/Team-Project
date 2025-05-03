package com.example.weather.model;

import com.example.weather.utils.WeatherUtils;

public class HourlyWeather {
    private String time;
    private String iconCode;
    private String weatherDescription;
    private int temperature;

    public HourlyWeather(String time, int temperature, String iconCode, String weatherDescription) {
        this.time = time;
        this.temperature = temperature;
        this.iconCode = iconCode;
        this.weatherDescription = WeatherUtils.getWeatherDescription(iconCode);  // 날씨 설명 추가
    }

    public String getTime() {
        return time;
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
