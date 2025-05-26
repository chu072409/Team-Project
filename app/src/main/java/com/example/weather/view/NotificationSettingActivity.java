package com.example.weather.view;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.weather.R;


import java.util.Calendar;

public class NotificationSettingActivity extends AppCompatActivity {

    Switch switchNotification;
    TextView textTime;
    EditText editContent;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_setting);

        // ① 연결
        switchNotification = findViewById(R.id.switch_notification);
        textTime = findViewById(R.id.text_time);
        editContent = findViewById(R.id.edit_content);
        btnSave = findViewById(R.id.btn_save);

        // ② 저장된 데이터 불러오기
        SharedPreferences prefs = getSharedPreferences("notification_settings", MODE_PRIVATE);
        switchNotification.setChecked(prefs.getBoolean("enabled", true));
        textTime.setText(prefs.getString("time", "08:00"));
        editContent.setText(prefs.getString("content", ""));

        // ③ 시간 설정 클릭 → TimePickerDialog
        textTime.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog dialog = new TimePickerDialog(this,
                    (view, selectedHour, selectedMinute) -> {
                        String timeFormatted = String.format("%02d:%02d", selectedHour, selectedMinute);
                        textTime.setText(timeFormatted);
                    },
                    hour, minute, true);
            dialog.show();
        });

        // ④ 저장 버튼
        btnSave.setOnClickListener(v -> {
            boolean isEnabled = switchNotification.isChecked();
            String time = textTime.getText().toString();
            String content = editContent.getText().toString();

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("enabled", isEnabled);
            editor.putString("time", time);
            editor.putString("content", content);
            editor.apply();

            Toast.makeText(this, "알림 설정이 저장되었습니다", Toast.LENGTH_SHORT).show();
            finish(); // 저장 후 종료
        });
    }
}