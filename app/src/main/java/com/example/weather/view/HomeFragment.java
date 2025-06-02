package com.example.weather.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.view.DailyWeatherAdapter;
import com.example.weather.view.HourlyWeatherAdapter;
import com.example.weather.model.DailyWeather;
import com.example.weather.model.HourlyWeather;
import com.example.weather.model.Weather;
import com.example.weather.utils.CityNameConverter;
import com.example.weather.utils.WeatherUtils;
import com.example.weather.viewmodel.HomeViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private HomeViewModel viewModel;
    private TextView cityText, temperatureText, descriptionText, dateText, timeText;
    private ImageView weatherIcon;
    private RecyclerView hourlyRecyclerView, dailyRecyclerView;
    private HourlyWeatherAdapter hourlyWeatherAdapter;
    private DailyWeatherAdapter dailyWeatherAdapter;

    private Handler handler = new Handler();
    private Runnable timeRunnable;

    public HomeFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmet_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        cityText = view.findViewById(R.id.cityName);
        temperatureText = view.findViewById(R.id.temperature);
        descriptionText = view.findViewById(R.id.description);
        dateText = view.findViewById(R.id.dateLabel);
        timeText = view.findViewById(R.id.timeLabel);
        weatherIcon = view.findViewById(R.id.weatherIcon);
        hourlyRecyclerView = view.findViewById(R.id.hourlyRecyclerView);
        dailyRecyclerView = view.findViewById(R.id.dailyRecyclerView);

        hourlyWeatherAdapter = new HourlyWeatherAdapter(new ArrayList<>());
        dailyWeatherAdapter = new DailyWeatherAdapter(new ArrayList<>());

        GridLayoutManager hourlyLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        hourlyRecyclerView.setLayoutManager(hourlyLayoutManager);
        hourlyRecyclerView.setAdapter(hourlyWeatherAdapter);

        GridLayoutManager dailyLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        dailyRecyclerView.setLayoutManager(dailyLayoutManager);
        dailyRecyclerView.setAdapter(dailyWeatherAdapter);

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        viewModel.getWeather().observe(getViewLifecycleOwner(), this::updateWeatherUI);
        viewModel.getHourlyWeather().observe(getViewLifecycleOwner(), this::updateHourlyWeatherUI);
        viewModel.getDailyWeather().observe(getViewLifecycleOwner(), this::updateDailyWeatherUI);

        viewModel.fetchWeatherData(1, 2);
        startUpdatingTime();

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            fetchLocationData();
        }
    }

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

    private void fetchLocationData() {
        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(requireActivity(), location -> {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            fetchWeatherData(latitude, longitude);
                        }
                    });
        } catch (SecurityException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "위치 권한을 허용해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchWeatherData(double latitude, double longitude) {
        viewModel.fetchWeatherData(latitude, longitude);
    }

    private void updateWeatherUI(Weather weather) {
        cityText.setText(CityNameConverter.convert(weather.getCityName()));
        temperatureText.setText(String.format("%d°C", (int) weather.getTemperature()));
        descriptionText.setText(weather.getWeatherDescription());
        weatherIcon.setImageResource(WeatherUtils.getWeatherSymbolResource(weather.getWeatherIcon()));
    }

    private void updateHourlyWeatherUI(List<HourlyWeather> hourlyWeatherList) {
        hourlyWeatherAdapter.updateHourlyWeather(hourlyWeatherList);
    }

    private void updateDailyWeatherUI(List<DailyWeather> dailyWeatherList) {
        dailyWeatherAdapter.updateDailyWeather(dailyWeatherList);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocationData();
            } else {
                Toast.makeText(getContext(), "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
