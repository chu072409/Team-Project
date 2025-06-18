package com.example.weather.model;

import com.example.weather.utils.WeatherUtils;

public class HourlyWeather {
    private String time;
    private String iconCode;
    private String weatherDescription;
    private int temperature;

    public HourlyWeather(String time, int temperature, String iconCode) {
        this.time = time;
        this.temperature = temperature;
        this.iconCode = iconCode;
        this.weatherDescription = WeatherUtils.getWeatherDescription(iconCode);  // 아이콘 기반 설명
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
