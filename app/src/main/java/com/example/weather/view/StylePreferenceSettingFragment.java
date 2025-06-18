package com.example.weather.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.weather.R;

public class StylePreferenceSettingFragment extends Fragment {

    private RadioGroup radioGroup;
    private RadioButton radioCasual, radioSporty, radioFormal, radioStreet, radioVintage;
    private ImageView imageCasual, imageSporty, imageFormal, imageStreet, imageVintage;
    private TextView textCasual, textSporty, textFormal, textStreet, textVintage;
    private Button btnSave;
    private SharedPreferences prefs;

    public StylePreferenceSettingFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_style_preference, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        radioGroup = view.findViewById(R.id.style_radio_group);
        radioCasual = view.findViewById(R.id.radio_casual);
        radioSporty = view.findViewById(R.id.radio_sporty);
        radioFormal = view.findViewById(R.id.radio_formal);
        radioStreet = view.findViewById(R.id.radio_street);
        radioVintage = view.findViewById(R.id.radio_vintage);

        imageCasual = view.findViewById(R.id.image_casual);
        imageSporty = view.findViewById(R.id.image_sporty);
        imageFormal = view.findViewById(R.id.image_formal);
        imageStreet = view.findViewById(R.id.image_street);
        imageVintage = view.findViewById(R.id.image_vintage);

        textCasual = view.findViewById(R.id.text_casual_desc);
        textSporty = view.findViewById(R.id.text_sporty_desc);
        textFormal = view.findViewById(R.id.text_formal_desc);
        textStreet = view.findViewById(R.id.text_street_desc);
        textVintage = view.findViewById(R.id.text_vintage_desc);

        btnSave = view.findViewById(R.id.btn_save);
        prefs = requireContext().getSharedPreferences("user_profile", getContext().MODE_PRIVATE);

        ImageButton btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        // 초기화: 저장된 스타일 반영
        hideAll(); // 모두 숨기기
        String savedStyle = prefs.getString("style_preferences", "");
        switch (savedStyle) {
            case "캐주얼":
                radioCasual.setChecked(true);
                showOnly(imageCasual, textCasual);
                break;
            case "스포티":
                radioSporty.setChecked(true);
                showOnly(imageSporty, textSporty);
                break;
            case "포멀":
                radioFormal.setChecked(true);
                showOnly(imageFormal, textFormal);
                break;
            case "스트릿":
                radioStreet.setChecked(true);
                showOnly(imageStreet, textStreet);
                break;
            case "빈티지":
                radioVintage.setChecked(true);
                showOnly(imageVintage, textVintage);
                break;
        }

        // 라디오 그룹 리스너
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            hideAll(); // 모두 숨기기

            if (checkedId == R.id.radio_casual) {
                showOnly(imageCasual, textCasual);
            } else if (checkedId == R.id.radio_sporty) {
                showOnly(imageSporty, textSporty);
            } else if (checkedId == R.id.radio_formal) {
                showOnly(imageFormal, textFormal);
            } else if (checkedId == R.id.radio_street) {
                showOnly(imageStreet, textStreet);
            } else if (checkedId == R.id.radio_vintage) {
                showOnly(imageVintage, textVintage);
            }

        });

        btnSave.setOnClickListener(v -> {
            String selectedStyle = "";
            int checkedId = radioGroup.getCheckedRadioButtonId();
            if (checkedId == R.id.radio_casual) selectedStyle = "캐주얼";
            else if (checkedId == R.id.radio_sporty) selectedStyle = "스포티";
            else if (checkedId == R.id.radio_formal) selectedStyle = "포멀";
            else if (checkedId == R.id.radio_street) selectedStyle = "스트릿";
            else if (checkedId == R.id.radio_vintage) selectedStyle = "빈티지";

            if (!selectedStyle.isEmpty()) {
                prefs.edit().putString("style_preferences", selectedStyle).apply();
                Toast.makeText(getContext(), "스타일이 저장되었습니다", Toast.LENGTH_SHORT).show();
                requireActivity().getSupportFragmentManager().popBackStack();
            } else {
                Toast.makeText(getContext(), "스타일을 선택해주세요", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hideAll() {
        imageCasual.setVisibility(View.GONE);
        textCasual.setVisibility(View.GONE);
        imageSporty.setVisibility(View.GONE);
        textSporty.setVisibility(View.GONE);
        imageFormal.setVisibility(View.GONE);
        textFormal.setVisibility(View.GONE);
        imageStreet.setVisibility(View.GONE);
        textStreet.setVisibility(View.GONE);
        imageVintage.setVisibility(View.GONE);
        textVintage.setVisibility(View.GONE);
    }

    private void showOnly(ImageView image, TextView text) {
        image.setVisibility(View.VISIBLE);
        text.setVisibility(View.VISIBLE);
    }
}