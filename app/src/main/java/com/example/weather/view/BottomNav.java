package com.example.weather.view;


import com.example.weather.R;
import android.app.Activity;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNav {

    public static void setup(@NonNull BottomNavigationView bottomNav, @NonNull Activity activity) {
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_settings && !(activity instanceof SettingActivity)) {
                activity.startActivity(new Intent(activity, SettingActivity.class));
                return true;
            }

            return true; // 설정 외 항목은 일단 무시
        });
    }
}
