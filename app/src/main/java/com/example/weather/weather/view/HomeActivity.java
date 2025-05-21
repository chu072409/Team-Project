package com.example.weather.weather.view;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.model.DailyWeather;
import com.example.weather.model.HourlyWeather;
import com.example.weather.model.Weather;
import com.example.weather.utils.CityNameConverter;
import com.example.weather.utils.WeatherUtils;
import com.example.weather.view.DailyWeatherAdapter;
import com.example.weather.view.HourlyWeatherAdapter;
import com.example.weather.viewmodel.HomeViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private HomeViewModel viewModel;
    private TextView cityText, temperatureText, descriptionText, dateText, timeText;
    private ImageView weatherIcon;
    private RecyclerView hourlyRecyclerView, dailyRecyclerView;
    private com.example.weather.view.HourlyWeatherAdapter hourlyWeatherAdapter;
    private com.example.weather.view.DailyWeatherAdapter dailyWeatherAdapter;

    private Handler handler = new Handler();
    private Runnable timeRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // 위치 권한 요청
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            fetchLocationData();
        }

        // UI 요소 초기화
        cityText = findViewById(R.id.cityName);
        temperatureText = findViewById(R.id.temperature);
        descriptionText = findViewById(R.id.description);
        dateText = findViewById(R.id.dateLabel);
        timeText = findViewById(R.id.timeLabel);
        weatherIcon = findViewById(R.id.weatherIcon);
        hourlyRecyclerView = findViewById(R.id.hourlyRecyclerView);
        dailyRecyclerView = findViewById(R.id.dailyRecyclerView);

        // 어댑터 초기화
        hourlyWeatherAdapter = new HourlyWeatherAdapter(new ArrayList<>());
        dailyWeatherAdapter = new DailyWeatherAdapter(new ArrayList<>());

        // ✅ 가로 4개씩 보여주고 넘기는 GridLayoutManager 적용
        GridLayoutManager hourlyLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);
        hourlyRecyclerView.setLayoutManager(hourlyLayoutManager);
        hourlyRecyclerView.setAdapter(hourlyWeatherAdapter);

        GridLayoutManager dailyLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);
        dailyRecyclerView.setLayoutManager(dailyLayoutManager);
        dailyRecyclerView.setAdapter(dailyWeatherAdapter);

        // ViewModel 초기화
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // LiveData 구독
        viewModel.getWeather().observe(this, this::updateWeatherUI);
        viewModel.getHourlyWeather().observe(this, this::updateHourlyWeatherUI);
        viewModel.getDailyWeather().observe(this, this::updateDailyWeatherUI);

        // 첫 데이터 호출
        viewModel.fetchWeatherData(1, 2);

        // 시간 자동 업데이트
        startUpdatingTime();
    }

    // 날짜, 시간 표시 업데이트
    private void startUpdatingTime() {
        timeRunnable = new Runnable() {
            @Override
            public void run() {
                updateDateTime();
                handler.postDelayed(this, 60000);
            }
        };
        handler.post(timeRunnable);
    }

    private void updateDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 (E)", Locale.KOREA);
        SimpleDateFormat timeFormat = new SimpleDateFormat("a hh:mm", Locale.KOREA);

        String currentDate = dateFormat.format(new Date());
        String currentTime = timeFormat.format(new Date());

        dateText.setText(currentDate);
        timeText.setText(currentTime);
    }

    // 위치 권한 응답 처리
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocationData();
            } else {
                Toast.makeText(this, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 위치 가져오기
    private void fetchLocationData() {
        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            fetchWeatherData(latitude, longitude);
                        }
                    });
        } catch (SecurityException e) {
            e.printStackTrace();
            Toast.makeText(this, "위치 권한을 허용해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    // ViewModel 통해 날씨 요청
    private void fetchWeatherData(double latitude, double longitude) {
        viewModel.fetchWeatherData(latitude, longitude);
    }

    // 현재 날씨 UI 업데이트
    private void updateWeatherUI(Weather weather) {
        cityText.setText(CityNameConverter.convert(weather.getCityName()));
        temperatureText.setText(String.format("%d°C", (int) weather.getTemperature()));
        descriptionText.setText(weather.getWeatherDescription());
        weatherIcon.setImageResource(WeatherUtils.getWeatherSymbolResource(weather.getWeatherIcon()));
        cityText.setText(CityNameConverter.convert(weather.getCityName()));
    }

    // 시간별 날씨 UI 업데이트
    private void updateHourlyWeatherUI(List<HourlyWeather> hourlyWeatherList) {
        hourlyWeatherAdapter.updateHourlyWeather(hourlyWeatherList);
    }

    // 일별 날씨 UI 업데이트
    private void updateDailyWeatherUI(List<DailyWeather> dailyWeatherList) {
        dailyWeatherAdapter.updateDailyWeather(dailyWeatherList);
    }
}
