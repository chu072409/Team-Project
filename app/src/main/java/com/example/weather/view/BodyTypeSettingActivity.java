package com.example.weather.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.weather.R;

public class BodyTypeSettingActivity extends AppCompatActivity {

    Spinner spinnerBodyType;
    EditText editHeight, editWeight;
    Button btnCalculateBmi, btnSave;
    TextView textBmiResult;

    String[] bodyTypes = {"마름", "보통", "통통"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_type_setting);

        spinnerBodyType = findViewById(R.id.spinner_body_type);
        editHeight = findViewById(R.id.edit_height);
        editWeight = findViewById(R.id.edit_weight);
        btnCalculateBmi = findViewById(R.id.btn_calculate_bmi);
        btnSave = findViewById(R.id.btn_save);
        textBmiResult = findViewById(R.id.text_bmi_result);

        // Spinner 어댑터 설정
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bodyTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBodyType.setAdapter(adapter);

        // 저장된 값 불러오기
        SharedPreferences prefs = getSharedPreferences("user_profile", MODE_PRIVATE);
        String savedBodyType = prefs.getString("body_type", "");
        editHeight.setText(prefs.getString("height", ""));
        editWeight.setText(prefs.getString("weight", ""));

        if (!savedBodyType.isEmpty()) {
            int index = java.util.Arrays.asList(bodyTypes).indexOf(savedBodyType);
            if (index >= 0) spinnerBodyType.setSelection(index);
        }

        // BMI 계산 버튼 클릭 시
        btnCalculateBmi.setOnClickListener(v -> {
            String heightStr = editHeight.getText().toString();
            String weightStr = editWeight.getText().toString();

            if (heightStr.isEmpty() || weightStr.isEmpty()) {
                Toast.makeText(this, "키와 몸무게를 모두 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            double height = Double.parseDouble(heightStr) / 100.0;
            double weight = Double.parseDouble(weightStr);
            double bmi = weight / (height * height);

            String bmiText = String.format("BMI: %.1f", bmi);
            String bodyType = getBodyTypeFromBmi(bmi);

            textBmiResult.setText(bmiText + " → 추천 체형: " + bodyType);

            // Spinner에서 자동 선택
            int index = java.util.Arrays.asList(bodyTypes).indexOf(bodyType);
            if (index >= 0) spinnerBodyType.setSelection(index);
        });

        // 저장 버튼 클릭 시
        btnSave.setOnClickListener(v -> {
            String selectedType = spinnerBodyType.getSelectedItem().toString();
            String height = editHeight.getText().toString();
            String weight = editWeight.getText().toString();

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("body_type", selectedType);
            editor.putString("height", height);
            editor.putString("weight", weight);
            editor.apply();

            Toast.makeText(this, "체형이 저장되었습니다", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private String getBodyTypeFromBmi(double bmi) {
        if (bmi < 18.5) return "마름";
        else if (bmi < 23.0) return "보통";
        else return "통통";
    }
}
