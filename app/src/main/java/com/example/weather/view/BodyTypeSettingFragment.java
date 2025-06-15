package com.example.weather.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.weather.R;

public class BodyTypeSettingFragment extends Fragment {

    private Spinner spinnerBodyType;
    private EditText editHeight, editWeight;
    private Button btnCalculateBmi, btnSave;
    private TextView textBmiResult;
    private ImageButton btnBack;

    private final String[] bodyTypes = {"마름", "보통", "통통"};

    public BodyTypeSettingFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_body_type_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinnerBodyType = view.findViewById(R.id.spinner_body_type);
        editHeight = view.findViewById(R.id.edit_height);
        editWeight = view.findViewById(R.id.edit_weight);
        btnCalculateBmi = view.findViewById(R.id.btn_calculate_bmi);
        btnSave = view.findViewById(R.id.btn_save);
        textBmiResult = view.findViewById(R.id.text_bmi_result);
        btnBack = view.findViewById(R.id.btn_back);

        Context context = requireContext();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, bodyTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBodyType.setAdapter(adapter);

        SharedPreferences prefs = context.getSharedPreferences("user_profile", Context.MODE_PRIVATE);
        String savedBodyType = prefs.getString("body_type", "");
        editHeight.setText(prefs.getString("height", ""));
        editWeight.setText(prefs.getString("weight", ""));

        if (!savedBodyType.isEmpty()) {
            int index = java.util.Arrays.asList(bodyTypes).indexOf(savedBodyType);
            if (index >= 0) spinnerBodyType.setSelection(index);
        }

        btnCalculateBmi.setOnClickListener(v -> {
            String heightStr = editHeight.getText().toString();
            String weightStr = editWeight.getText().toString();

            if (heightStr.isEmpty() || weightStr.isEmpty()) {
                Toast.makeText(context, "키와 몸무게를 모두 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            double height = Double.parseDouble(heightStr) / 100.0;
            double weight = Double.parseDouble(weightStr);
            double bmi = weight / (height * height);

            String bmiText = String.format("BMI: %.1f", bmi);
            String bodyType = getBodyTypeFromBmi(bmi);

            textBmiResult.setText(bmiText + " → 추천 체형: " + bodyType);

            int index = java.util.Arrays.asList(bodyTypes).indexOf(bodyType);
            if (index >= 0) spinnerBodyType.setSelection(index);
        });

        btnSave.setOnClickListener(v -> {
            String selectedType = spinnerBodyType.getSelectedItem().toString();
            String height = editHeight.getText().toString();
            String weight = editWeight.getText().toString();

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("body_type", selectedType);
            editor.putString("height", height);
            editor.putString("weight", weight);
            editor.apply();

            Toast.makeText(context, "체형이 저장되었습니다", Toast.LENGTH_SHORT).show();

            requireActivity().getSupportFragmentManager().popBackStack();
        });

        btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
    }

    private String getBodyTypeFromBmi(double bmi) {
        if (bmi < 18.5) return "마름";
        else if (bmi < 23.0) return "보통";
        else return "통통";
    }
}
