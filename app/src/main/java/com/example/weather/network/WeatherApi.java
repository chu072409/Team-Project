package com.example.weather.network;

import com.example.weather.model.CurrentWeatherResponse;
import com.example.weather.model.HourlyWeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
//test
public interface WeatherApi {

    @GET("data/2.5/weather")
    Call<CurrentWeatherResponse> getCurrentWeather(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("units") String units,
            @Query("appid") String apiKey
    );

    @GET("data/2.5/forecast")
    Call<HourlyWeatherResponse> getHourlyWeather(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("units") String units,
            @Query("appid") String apiKey
    );
}
