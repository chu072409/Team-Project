package com.example.weather.model;

import java.util.List;
//test
public class HourlyWeatherResponse {
    private List<HourlyData> list;

    public static class HourlyData {
        private int dt;
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

        public int getDt() {
            return dt;
        }

        public Main getMain() {
            return main;
        }

        public List<WeatherInfo> getWeather() {
            return weather;
        }
    }

    public List<HourlyData> getList() {
        return list;
    }
}
