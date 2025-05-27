package com.example.weather.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.weather.R;

public class SettingActivity extends AppCompatActivity {

    LinearLayout btnProfileEdit;
    LinearLayout btnNotificationSetting;
    LinearLayout btnBodyTypeSetting;  // 체형 설정 버튼 추가
    LinearLayout btnStylePreferenceSetting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_settings);  // 설정 화면 XML

        // XML 항목 연결
        btnProfileEdit = findViewById(R.id.btn_profile_edit);
        btnNotificationSetting = findViewById(R.id.btn_notification_setting);
        btnBodyTypeSetting = findViewById(R.id.btn_body_setting);  // 체형 설정 버튼 연결

        // 프로필 수정 클릭 시 이동
        btnProfileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, ProfileEditActivity.class);
                startActivity(intent);
            }
        });

        // 알림 설정 클릭 시 이동
        btnNotificationSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, NotificationSettingActivity.class);
                startActivity(intent);
            }
        });

        // 체형 설정 클릭 시 이동
        btnBodyTypeSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, BodyTypeSettingActivity.class);
                startActivity(intent);
            }
        });
        btnStylePreferenceSetting = findViewById(R.id.btn_style_setting);

        btnStylePreferenceSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, StylePreferenceActivity.class);
                startActivity(intent);
            }
        });
    }
}
