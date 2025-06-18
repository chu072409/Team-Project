package com.example.weather.service;

import android.util.Log;

import com.example.weather.model.CurrentWeatherResponse;
import com.example.weather.model.DailyWeather;
import com.example.weather.model.HourlyWeather;
import com.example.weather.model.HourlyWeatherResponse;
import com.example.weather.model.Weather;
import com.example.weather.network.RetrofitClient;
import com.example.weather.network.WeatherApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherService {

    private static final String API_KEY = "e847a9ef8e5a39f939cbbc72b30a6c5d";

    public void fetchCurrentWeather(double lat, double lon, final WeatherCallback callback) {
        WeatherApi api = RetrofitClient.getClient().create(WeatherApi.class);
        api.getCurrentWeather(lat, lon, "metric", API_KEY).enqueue(new Callback<CurrentWeatherResponse>() {
            @Override
            public void onResponse(Call<CurrentWeatherResponse> call, Response<CurrentWeatherResponse> response) {
                if (response.isSuccessful()) {
                    CurrentWeatherResponse data = response.body();
                    String iconCode = data.getWeather().get(0).getIcon();
                    String description = data.getWeather().get(0).getDescription();
                    String cityName = data.getName();
                    Weather weather = new Weather(cityName, data.getMain().getTemp(), iconCode, description);
                    callback.onSuccess(weather);
                } else {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<CurrentWeatherResponse> call, Throwable t) {
                callback.onFailure();
            }
        });
    }


    public void fetchHourlyWeather(double lat, double lon, final HourlyWeatherCallback callback) {
        WeatherApi api = RetrofitClient.getClient().create(WeatherApi.class);
        api.getHourlyWeather(lat, lon, "metric", API_KEY).enqueue(new Callback<HourlyWeatherResponse>() {
            @Override
            public void onResponse(Call<HourlyWeatherResponse> call, Response<HourlyWeatherResponse> response) {
                if (response.isSuccessful()) {
                    List<HourlyWeatherResponse.HourlyData> list = response.body().getList();

                    // ✅ 현재 시간 이후만 필터링
                    long now = System.currentTimeMillis() / 1000L; // 현재 시각(초)


                    List<HourlyWeather> hourlyWeatherList = new ArrayList<>();
                    for (HourlyWeatherResponse.HourlyData data : list) {
                        long timestamp = data.getDt();

                        Log.d("HourlyDebug", "timestamp: " + timestamp + " → " + convertTimestampToTime(timestamp));  // 그다음 로그

                        if (timestamp < now) continue; // 과거는 스킵

                        String time = convertTimestampToTime(timestamp); // 예: "오전 9시"
                        String iconCode = data.getWeather().get(0).getIcon();
                        int temperature = (int) data.getMain().getTemp();

                        hourlyWeatherList.add(new HourlyWeather(time, temperature, iconCode));
                    }

                    // ✅ 시간순 정렬
                    hourlyWeatherList.sort((a, b) -> {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("a h시", Locale.KOREA);
                            Date d1 = sdf.parse(a.getTime());
                            Date d2 = sdf.parse(b.getTime());
                            return d1.compareTo(d2);
                        } catch (Exception e) {
                            return 0;
                        }
                    });

                    callback.onSuccess(hourlyWeatherList);
                } else {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<HourlyWeatherResponse> call, Throwable t) {
                callback.onFailure();
            }
        });
    }

    private String convertTimestampToTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH시", Locale.KOREA); // ✔ 24시간제: 09시, 15시
        Date date = new Date(timestamp * 1000L);
        return sdf.format(date);
    }

    public void fetchDailyWeather(double lat, double lon, final DailyWeatherCallback callback) {
        WeatherApi api = RetrofitClient.getClient().create(WeatherApi.class);
        api.getHourlyWeather(lat, lon, "metric", API_KEY).enqueue(new Callback<HourlyWeatherResponse>() {
            @Override
            public void onResponse(Call<HourlyWeatherResponse> call, Response<HourlyWeatherResponse> response) {
                if (response.isSuccessful()) {
                    List<HourlyWeatherResponse.HourlyData> list = response.body().getList();

                    // 날짜별로 데이터를 그룹화
                    Map<String, List<HourlyWeatherResponse.HourlyData>> groupedData = new HashMap<>();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA); // "2025-03-28" 형식

                    for (HourlyWeatherResponse.HourlyData data : list) {
                        Date date = new Date(data.getDt() * 1000L); // 타임스탬프 -> Date로 변환
                        String dateKey = dateFormat.format(date); // 날짜 키 생성 (예: "2025-03-28")

                        groupedData.putIfAbsent(dateKey, new ArrayList<>());
                        groupedData.get(dateKey).add(data); // 같은 날짜에 속하는 데이터 추가
                    }

                    // DailyWeather 객체로 변환
                    List<DailyWeather> dailyWeatherList = new ArrayList<>();
                    for (String dateKey : groupedData.keySet()) {
                        List<HourlyWeatherResponse.HourlyData> dailyData = groupedData.get(dateKey);
                        int totalTemp = 0;
                        String iconCode = dailyData.get(0).getWeather().get(0).getIcon(); // 첫 번째 날씨 아이콘 사용
                        String description = dailyData.get(0).getWeather().get(0).getDescription();

                        for (HourlyWeatherResponse.HourlyData data : dailyData) {
                            totalTemp += data.getMain().getTemp();
                        }

                        int avgTemp = totalTemp / dailyData.size();
                        String displayDate = convertDateToDisplayFormat(dateKey); // 날짜 형식 변경 (예: "3월 28일")

                        dailyWeatherList.add(new DailyWeather(displayDate, avgTemp, iconCode, description));
                    }
                    Collections.sort(dailyWeatherList, (dw1, dw2) -> {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("M월 d일", Locale.KOREA);
                            return sdf.parse(dw1.getDate()).compareTo(sdf.parse(dw2.getDate()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return 0;
                        }
                    });
                    callback.onSuccess(dailyWeatherList);
                } else {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<HourlyWeatherResponse> call, Throwable t) {
                callback.onFailure();
            }
        });
    }

    private String convertDateToDisplayFormat(String dateKey) {
        // "2025-03-28" -> "3월 28일" 형식으로 변환
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
            SimpleDateFormat outputFormat = new SimpleDateFormat("M월 d일", Locale.KOREA);
            Date date = inputFormat.parse(dateKey);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateKey;
        }
    }


    public interface WeatherCallback {
        void onSuccess(Weather weather);
        void onFailure();
    }

    public interface HourlyWeatherCallback {
        void onSuccess(List<HourlyWeather> hourlyWeatherList);
        void onFailure();
    }

    public interface DailyWeatherCallback {
        void onSuccess(List<DailyWeather> dailyWeatherList);
        void onFailure();
    }
}
