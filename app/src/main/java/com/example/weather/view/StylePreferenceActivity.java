package com.example.weather.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.weather.R;

public class StylePreferenceActivity extends AppCompatActivity {

    CheckBox checkCasual, checkSporty, checkFormal, checkStreet, checkVintage;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style_preference);

        // 체크박스 연결
        checkCasual = findViewById(R.id.check_casual);
        checkSporty = findViewById(R.id.check_sporty);
        checkFormal = findViewById(R.id.check_formal);
        checkStreet = findViewById(R.id.check_street);
        checkVintage = findViewById(R.id.check_vintage);
        btnSave = findViewById(R.id.btn_save);

        // 저장된 데이터 불러오기
        SharedPreferences prefs = getSharedPreferences("user_profile", MODE_PRIVATE);
        String savedStyles = prefs.getString("style_preferences", "");

        if (!savedStyles.isEmpty()) {
            if (savedStyles.contains("캐주얼")) checkCasual.setChecked(true);
            if (savedStyles.contains("스포티")) checkSporty.setChecked(true);
            if (savedStyles.contains("포멀")) checkFormal.setChecked(true);
            if (savedStyles.contains("스트릿")) checkStreet.setChecked(true);
            if (savedStyles.contains("빈티지")) checkVintage.setChecked(true);
        }

        // 저장 버튼
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder selectedStyles = new StringBuilder();
                if (checkCasual.isChecked()) selectedStyles.append("캐주얼,");
                if (checkSporty.isChecked()) selectedStyles.append("스포티,");
                if (checkFormal.isChecked()) selectedStyles.append("포멀,");
                if (checkStreet.isChecked()) selectedStyles.append("스트릿,");
                if (checkVintage.isChecked()) selectedStyles.append("빈티지,");

                // 마지막 쉼표 제거
                if (selectedStyles.length() > 0) {
                    selectedStyles.setLength(selectedStyles.length() - 1);
                }

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("style_preferences", selectedStyles.toString());
                editor.apply();

                Toast.makeText(StylePreferenceActivity.this, "스타일이 저장되었습니다", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
