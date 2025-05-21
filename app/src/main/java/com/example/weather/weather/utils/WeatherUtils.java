package com.example.weather.weather.utils;

import com.example.weather.R;

public class WeatherUtils {

    public static int getWeatherSymbolResource(String iconCode) {
        switch (iconCode) {
            case "01d": return R.drawable.sun_max;
            case "01n": return R.drawable.moon_stars;
            case "02d": return R.drawable.cloud_sun;
            case "02n": return R.drawable.cloud_moon;
            case "03d":
            case "03n": return R.drawable.cloud;
            case "04d":
            case "04n": return R.drawable.smoke;
            case "09d":
            case "09n": return R.drawable.cloud_heavyrain;
            case "10d":
            case "10n": return R.drawable.cloud_sun_rain;
            case "11d":
            case "11n": return R.drawable.cloud_bolt_rain;
            case "13d":
            case "13n": return R.drawable.cloud_snow;
            case "50d":
            case "50n": return R.drawable.cloud_fog;
            default: return R.drawable.sun_max; // 예외 처리
        }
    }

    public static String getWeatherDescription(String iconCode) {
        switch (iconCode) {
            case "01d": return "맑음";
            case "01n": return "맑음";
            case "02d": return "구름 조금";
            case "02n": return "구름 조금";
            case "03d": return "구름 많음";
            case "03n": return "구름 많음";
            case "04d": return "흐림";
            case "04n": return "흐림";
            case "09d": return "소나기";
            case "09n": return "소나기";
            case "10d": return "비";
            case "10n": return "비";
            case "11d": return "천둥번개";
            case "11n": return "천둥번개";
            case "13d": return "눈";
            case "13n": return "눈";
            case "50d": return "안개";
            case "50n": return "안개";
            default: return "알 수 없음";
        }
    }
}
