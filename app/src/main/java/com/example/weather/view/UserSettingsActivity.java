package com.example.weather.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.weather.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserSettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_settings);

        // 하단 네비게이션
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.nav_settings);
        BottomNav.setup(bottomNav, this);

        // 기본 정보 버튼
        Button btnBasicInfo = findViewById(R.id.btn_basic_info);
        btnBasicInfo.setOnClickListener(v -> {
            Intent intent = new Intent(this, BasicInfoActivity.class);
            startActivity(intent);
        });

        // 알림 설정 버튼
        Button btnNotification = findViewById(R.id.btn_notification);
        btnNotification.setOnClickListener(v -> {
            Intent intent = new Intent(this, NotificationSettingsActivity.class);
            startActivity(intent);
        });
    }
}
