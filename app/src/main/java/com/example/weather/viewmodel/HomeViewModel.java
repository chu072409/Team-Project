package com.example.weather.viewmodel;

import android.app.Application;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.example.weather.model.Weather;
import com.example.weather.model.HourlyWeather;
import com.example.weather.model.DailyWeather;
import com.example.weather.service.WeatherService;
import android.os.Handler;
import android.os.Looper;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private final WeatherService weatherService;
    private final FusedLocationProviderClient fusedLocationClient;


    private final MutableLiveData<Weather> weatherLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<HourlyWeather>> hourlyWeatherLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<DailyWeather>> dailyWeatherLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);


    private static final int MAX_HOURLY_WEATHER = 6;
    private static final int MAX_DAILY_WEATHER = 6;

    private Handler handler = new Handler(Looper.getMainLooper());

    public HomeViewModel(@NonNull Application application) {
        super(application);
        weatherService = new WeatherService();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(application);

        handler.postDelayed(this::fetchWeatherDataPeriodically, 60000);
    }

    public LiveData<Weather> getWeather() {
        return weatherLiveData;
    }

    public LiveData<List<HourlyWeather>> getHourlyWeather() {
        return hourlyWeatherLiveData;
    }

    public LiveData<List<DailyWeather>> getDailyWeather() {
        return dailyWeatherLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    private void fetchWeatherDataPeriodically() {
        isLoading.setValue(true);

        // 위치 기반 날씨 데이터 호출
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        fetchWeather(latitude, longitude);
                        fetchHourlyWeather(latitude, longitude);
                        fetchDailyWeather(latitude, longitude);

                        // 1분마다 반복 호출
                        handler.postDelayed(this::fetchWeatherDataPeriodically, 60000);
                    } else {
                        isLoading.setValue(false);
                    }
                });
    }

    public void fetchWeatherData(double latitude, double longitude) {
        isLoading.setValue(true);

        // 날씨 데이터 요청
        weatherService.fetchCurrentWeather(latitude, longitude, new WeatherService.WeatherCallback() {
            @Override
            public void onSuccess(Weather weather) {
                weatherLiveData.setValue(weather);
                isLoading.setValue(false);
            }
            //test
            @Override
            public void onFailure() {
                isLoading.setValue(false);
            }
        });

        // 시간별 날씨 데이터 요청
        weatherService.fetchHourlyWeather(latitude, longitude, new WeatherService.HourlyWeatherCallback() {
            @Override
            public void onSuccess(List<HourlyWeather> hourlyWeatherList) {
                if (hourlyWeatherList != null && !hourlyWeatherList.isEmpty()) {
                    List<HourlyWeather> trimmed = hourlyWeatherList.subList(0, Math.min(6, hourlyWeatherList.size()));
                    hourlyWeatherLiveData.setValue(trimmed);
                }
            }

            @Override
            public void onFailure() {
                // 실패 처리
            }
        });

        // 일별 날씨 데이터 요청
        weatherService.fetchDailyWeather(latitude, longitude, new WeatherService.DailyWeatherCallback() {
            @Override
            public void onSuccess(List<DailyWeather> dailyWeatherList) {
                if (dailyWeatherList != null && !dailyWeatherList.isEmpty()) {
                    List<DailyWeather> trimmed = dailyWeatherList.subList(0, Math.min(6, dailyWeatherList.size()));
                    dailyWeatherLiveData.setValue(trimmed);
                }
            }

            @Override
            public void onFailure() {
                // 실패 처리
            }
        });
    }


    private void fetchWeather(double lat, double lon) {
        weatherService.fetchCurrentWeather(lat, lon, new WeatherService.WeatherCallback() {
            @Override
            public void onSuccess(Weather weather) {
                weatherLiveData.setValue(weather);
                isLoading.setValue(false);
            }

            @Override
            public void onFailure() {
                isLoading.setValue(false);
            }
        });
    }

    private void fetchHourlyWeather(double lat, double lon) {
        weatherService.fetchHourlyWeather(lat, lon, new WeatherService.HourlyWeatherCallback() {
            @Override
            public void onSuccess(List<HourlyWeather> hourlyWeatherList) {

                hourlyWeatherList = hourlyWeatherList.subList(0, Math.min(hourlyWeatherList.size(), MAX_HOURLY_WEATHER));
                hourlyWeatherLiveData.setValue(hourlyWeatherList);
            }

            @Override
            public void onFailure() {
                // 실패 처리
            }
        });
    }

    private void fetchDailyWeather(double lat, double lon) {
        weatherService.fetchDailyWeather(lat, lon, new WeatherService.DailyWeatherCallback() {
            @Override
            public void onSuccess(List<DailyWeather> dailyWeatherList) {
                dailyWeatherList = dailyWeatherList.subList(0, Math.min(dailyWeatherList.size(), MAX_DAILY_WEATHER));
                dailyWeatherLiveData.setValue(dailyWeatherList);
            }

            @Override
            public void onFailure() {
                // 실패 처리
            }
        });
    }
}
