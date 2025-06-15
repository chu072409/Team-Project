package com.example.weather.view;

import android.app.TimePickerDialog;
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

import java.util.Calendar;

public class NotificationSettingFragment extends Fragment {

    private Switch switchNotification;
    private TextView textTime;

    private Button btnSave;

    private SharedPreferences prefs;

    public NotificationSettingFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_notification_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ImageButton btnBack = view.findViewById(R.id.btn_back);
        switchNotification = view.findViewById(R.id.switch_notification);
        textTime = view.findViewById(R.id.text_time);

        btnSave = view.findViewById(R.id.btn_save);


        btnBack.setOnClickListener(v -> requireActivity().onBackPressed());


        prefs = requireContext().getSharedPreferences("notification_settings", requireContext().MODE_PRIVATE);

        // 시간 선택 다이얼로그
        textTime.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog dialog = new TimePickerDialog(requireContext(),
                    (view1, selectedHour, selectedMinute) -> {
                        String timeFormatted = String.format("%02d:%02d", selectedHour, selectedMinute);
                        textTime.setText(timeFormatted);
                    }, hour, minute, true);
            dialog.show();
        });

        // 저장 버튼 클릭
        btnSave.setOnClickListener(v -> {
            boolean isEnabled = switchNotification.isChecked();
            String time = textTime.getText().toString();


            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("enabled", isEnabled);
            editor.putString("time", time);

            editor.apply();

            Toast.makeText(requireContext(), "알림 설정이 저장되었습니다", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
        });


        loadNotificationSettings();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadNotificationSettings();
    }

    private void loadNotificationSettings() {
        boolean isEnabled = prefs.getBoolean("enabled", true);
        String time = prefs.getString("time", "08:00");
        String content = prefs.getString("content", "");

        switchNotification.setChecked(isEnabled);
        textTime.setText(time);

    }
}
