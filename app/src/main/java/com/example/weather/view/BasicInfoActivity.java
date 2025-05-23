package com.example.weather.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;
import com.example.weather.R;
import java.util.Calendar;

public class BasicInfoActivity extends AppCompatActivity {

    private EditText etName;
    private RadioGroup rgGender;
    private Button btnBirthdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_info);

        etName = findViewById(R.id.et_name);
        rgGender = findViewById(R.id.rg_gender);
        btnBirthdate = findViewById(R.id.btn_birthdate);

        // 생년월일 버튼 클릭
        btnBirthdate.setOnClickListener(v -> showDatePicker());

        // ✅ 뒤로가기 버튼 직접 연결
        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            String date = selectedYear + " / " +
                    String.format("%02d", selectedMonth + 1) + " / " +
                    String.format("%02d", selectedDay);
            btnBirthdate.setText(date);
        }, year, month, day).show();
    }
}
