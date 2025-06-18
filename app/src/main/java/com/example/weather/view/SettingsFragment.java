package com.example.weather.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.weather.R;

public class SettingsFragment extends Fragment {

    private LinearLayout btnProfileEdit;
    private LinearLayout btnNotificationSetting;
    private LinearLayout btnBodyTypeSetting;
    private LinearLayout btnStylePreferenceSetting;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_basic_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnProfileEdit = view.findViewById(R.id.btn_profile_edit);
        btnNotificationSetting = view.findViewById(R.id.btn_notification_setting);
        btnBodyTypeSetting = view.findViewById(R.id.btn_body_setting);
        btnStylePreferenceSetting = view.findViewById(R.id.btn_style_setting);

        btnProfileEdit.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new ProfileEditFragment())
                        .addToBackStack(null)
                        .commit()
        );

        btnNotificationSetting.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new NotificationSettingFragment())
                        .addToBackStack(null)
                        .commit()
        );

        btnBodyTypeSetting.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new BodyTypeSettingFragment())
                        .addToBackStack(null)
                        .commit()
        );

        btnStylePreferenceSetting.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new StylePreferenceSettingFragment())
                        .addToBackStack(null)
                        .commit()
        );
    }
}