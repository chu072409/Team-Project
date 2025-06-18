package com.example.weather.view;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.weather.R;
import com.example.weather.model.Weather;
import com.example.weather.notification.NotificationReceiver;
import com.example.weather.viewmodel.HomeViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.Calendar;

public class NotificationSettingFragment extends Fragment {

    private Switch switchNotification;
    private TextView textTime;
    private Button btnSave;
    private SharedPreferences prefs;
    private HomeViewModel homeViewModel;
    private FusedLocationProviderClient fusedLocationClient;

    public NotificationSettingFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_notification_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton btnBack = view.findViewById(R.id.btn_back);
        switchNotification = view.findViewById(R.id.switch_notification);
        textTime = view.findViewById(R.id.text_time);
        btnSave = view.findViewById(R.id.btn_save);

        prefs = requireContext().getSharedPreferences("notification_settings", Context.MODE_PRIVATE);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        btnBack.setOnClickListener(v -> requireActivity().onBackPressed());

        textTime.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog dialog = new TimePickerDialog(requireContext(),
                    (view1, selectedHour, selectedMinute) -> {
                        String timeFormatted = String.format("%02d:%02d", selectedHour, selectedMinute);
                        textTime.setText(timeFormatted);
                    }, hour, minute, true);
            dialog.show();
        });

        btnSave.setOnClickListener(v -> {
            boolean isEnabled = switchNotification.isChecked();
            String time = textTime.getText().toString();

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("enabled", isEnabled);
            editor.putString("time", time);
            editor.apply();

            if (isEnabled) {
                fetchLocationAndSchedule(time);
            }

            Toast.makeText(requireContext(), "알림 설정이 저장되었습니다", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
        });

        loadNotificationSettings();
    }

    private void loadNotificationSettings() {
        boolean isEnabled = prefs.getBoolean("enabled", true);
        String time = prefs.getString("time", "08:00");

        switchNotification.setChecked(isEnabled);
        textTime.setText(time);
    }

    private void fetchLocationAndSchedule(String time) {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(requireContext(), "위치 권한이 필요합니다", Toast.LENGTH_SHORT).show();
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                double lat = location.getLatitude();
                double lon = location.getLongitude();

                // 날씨 강제 로딩
                homeViewModel.fetchWeatherData(lat, lon);

                Weather weather = homeViewModel.getWeather().getValue();
                if (weather != null) {
                    int temp = (int) weather.getTemperature();
                    String desc = weather.getWeatherDescription();
                    scheduleNotification(time, temp, desc);
                } else {
                    // LiveData가 아직 null일 경우 fallback 예약 (5초 후 다시 시도)
                    new android.os.Handler().postDelayed(() -> {
                        Weather retryWeather = homeViewModel.getWeather().getValue();
                        if (retryWeather != null) {
                            scheduleNotification(time, (int) retryWeather.getTemperature(), retryWeather.getWeatherDescription());
                        }
                    }, 5000);
                }
            } else {
                Toast.makeText(requireContext(), "위치를 가져올 수 없습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void scheduleNotification(String time, int temp, String desc) {
        String[] parts = time.split(":");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
        cal.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
        cal.set(Calendar.SECOND, 0);

        if (cal.getTimeInMillis() < System.currentTimeMillis()) {
            cal.add(Calendar.DATE, 1);
        }

        Log.d("알림예약", "예약된 시간: " + cal.getTime().toString());

        Intent intent = new Intent(requireContext(), NotificationReceiver.class);
        intent.putExtra("temp", temp);
        intent.putExtra("desc", desc);

        int requestCode = (int) System.currentTimeMillis(); // ✅ 고유한 requestCode
        PendingIntent pi = PendingIntent.getBroadcast(
                requireContext(), requestCode, intent, PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager am = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(
                AlarmManager.RTC_WAKEUP,
                cal.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pi
        );
    }

}
