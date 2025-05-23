package com.example.weather.view;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;
import com.example.weather.R;
import java.util.Calendar;

public class NotificationSettingsActivity extends AppCompatActivity {

    private Switch switchAlarm;
    private Button btnSetAlarmTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);

        switchAlarm = findViewById(R.id.switch_alarm);
        btnSetAlarmTime = findViewById(R.id.btn_set_alarm_time);

        // 🔹 시간 설정
        btnSetAlarmTime.setOnClickListener(v -> showTimePicker());

        // 🔹 뒤로가기 버튼 동작
        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            Log.d("BackButton", "뒤로가기 버튼 클릭됨");
            finish();
        });
    }

    private void showTimePicker() {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        new TimePickerDialog(this, (view, selectedHour, selectedMinute) -> {
            String time = String.format("매일 %02d : %02d", selectedHour, selectedMinute);
            btnSetAlarmTime.setText(time);
        }, hour, minute, true).show();
    }
}
