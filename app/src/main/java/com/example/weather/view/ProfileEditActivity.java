package com.example.weather.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.weather.R;


public class ProfileEditActivity extends AppCompatActivity {

    EditText editName, editBirth, editJob;
    RadioGroup radioGender;
    RadioButton radioMale, radioFemale;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);  // XML 연결

        // XML 요소들 연결
        editName = findViewById(R.id.edit_name);
        editBirth = findViewById(R.id.edit_birth);
        editJob = findViewById(R.id.edit_job);
        radioGender = findViewById(R.id.radio_gender);
        radioMale = findViewById(R.id.radio_male);
        radioFemale = findViewById(R.id.radio_female);
        btnSave = findViewById(R.id.btn_save);

        // SharedPreferences 불러오기
        SharedPreferences prefs = getSharedPreferences("user_profile", MODE_PRIVATE);
        editName.setText(prefs.getString("name", ""));
        editBirth.setText(prefs.getString("birth", ""));
        editJob.setText(prefs.getString("job", ""));

        String gender = prefs.getString("gender", "");
        if (gender.equals("male")) {
            radioMale.setChecked(true);
        } else if (gender.equals("female")) {
            radioFemale.setChecked(true);
        }

        // 저장 버튼 클릭 시
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });
    }

    private void saveProfile() {
        String name = editName.getText().toString();
        String birth = editBirth.getText().toString();
        String job = editJob.getText().toString();

        int selectedGenderId = radioGender.getCheckedRadioButtonId();
        String gender = (selectedGenderId == R.id.radio_male) ? "male" : "female";

        SharedPreferences prefs = getSharedPreferences("user_profile", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("name", name);
        editor.putString("birth", birth);
        editor.putString("job", job);
        editor.putString("gender", gender);
        editor.apply();

        Toast.makeText(this, "저장되었습니다", Toast.LENGTH_SHORT).show();
        finish(); // 저장 후 뒤로 가기
    }
}