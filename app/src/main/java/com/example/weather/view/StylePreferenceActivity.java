package com.example.weather.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.weather.R;

public class StylePreferenceActivity extends AppCompatActivity {

    CheckBox checkCasual, checkSporty, checkFormal, checkStreet, checkVintage;
    ImageView imageCasual, imageSporty, imageFormal, imageStreet, imageVintage;
    TextView textCasual, textSporty, textFormal, textStreet, textVintage;
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

        // 이미지뷰와 텍스트뷰 연결 추가 🔽
        imageCasual = findViewById(R.id.image_casual);
        imageSporty = findViewById(R.id.image_sporty);
        imageFormal = findViewById(R.id.image_formal);
        imageStreet = findViewById(R.id.image_street);
        imageVintage = findViewById(R.id.image_vintage);

        textCasual = findViewById(R.id.text_casual_desc);
        textSporty = findViewById(R.id.text_sporty_desc);
        textFormal = findViewById(R.id.text_formal_desc);
        textStreet = findViewById(R.id.text_street_desc);
        textVintage = findViewById(R.id.text_vintage_desc);

        btnSave = findViewById(R.id.btn_save);

        // 뒤로가기 버튼
        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        // 저장된 데이터 불러오기
        SharedPreferences prefs = getSharedPreferences("user_profile", MODE_PRIVATE);
        String savedStyles = prefs.getString("style_preferences", "");

        if (!savedStyles.isEmpty()) {
            if (savedStyles.contains("캐주얼")) {
                checkCasual.setChecked(true);
                toggleVisibility(true, imageCasual, textCasual); // 이미지/설명 표시
            }
            if (savedStyles.contains("스포티")) {
                checkSporty.setChecked(true);
                toggleVisibility(true, imageSporty, textSporty);
            }
            if (savedStyles.contains("포멀")) {
                checkFormal.setChecked(true);
                toggleVisibility(true, imageFormal, textFormal);
            }
            if (savedStyles.contains("스트릿")) {
                checkStreet.setChecked(true);
                toggleVisibility(true, imageStreet, textStreet);
            }
            if (savedStyles.contains("빈티지")) {
                checkVintage.setChecked(true);
                toggleVisibility(true, imageVintage, textVintage);
            }
        }

        // 체크박스 클릭 시 이미지/설명 표시 연결 🔽
        setupCheckBox(checkCasual, imageCasual, textCasual);
        setupCheckBox(checkSporty, imageSporty, textSporty);
        setupCheckBox(checkFormal, imageFormal, textFormal);
        setupCheckBox(checkStreet, imageStreet, textStreet);
        setupCheckBox(checkVintage, imageVintage, textVintage);

        // 저장 버튼 클릭
        btnSave.setOnClickListener(v -> {
            StringBuilder selectedStyles = new StringBuilder();
            if (checkCasual.isChecked()) selectedStyles.append("캐주얼,");
            if (checkSporty.isChecked()) selectedStyles.append("스포티,");
            if (checkFormal.isChecked()) selectedStyles.append("포멀,");
            if (checkStreet.isChecked()) selectedStyles.append("스트릿,");
            if (checkVintage.isChecked()) selectedStyles.append("빈티지,");

            if (selectedStyles.length() > 0) {
                selectedStyles.setLength(selectedStyles.length() - 1); // 마지막 쉼표 제거
            }

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("style_preferences", selectedStyles.toString());
            editor.apply();

            Toast.makeText(StylePreferenceActivity.this, "스타일이 저장되었습니다", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    // 체크박스 상태 변화 시 이미지/텍스트 표시 토글
    private void setupCheckBox(CheckBox checkBox, ImageView imageView, TextView textView) {
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            toggleVisibility(isChecked, imageView, textView);
        });
    }

    private void toggleVisibility(boolean visible, ImageView imageView, TextView textView) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        imageView.setVisibility(visibility);
        textView.setVisibility(visibility);
    }
}
