package com.example.weather.model;

import java.util.List;
//test1
public class CurrentWeatherResponse {
    private String name;
    private Main main;
    private List<WeatherInfo> weather;

    public static class Main {
        private double temp;

        public double getTemp() {
            return temp;
        }
    }

    public static class WeatherInfo {
        private String description;
        private String icon;

        public String getDescription() {
            return description;
        }

        public String getIcon() {
            return icon;
        }
    }

    public String getName() {
        return name;
    }

    public Main getMain() {
        return main;
    }

    public List<WeatherInfo> getWeather() {
        return weather;
    }
}
