package com.example.weather.view;


import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.weather.R;

import java.io.*;
import java.util.Calendar;
import java.util.Locale;

public class ProfileEditFragment extends Fragment {

    private ImageView profileImage;
    private TextView textName, textBirth, textGender, textJob;
    private ImageButton btnBack;
    private Uri selectedImageUri;
    private Button btnEdit;

    private static final int PERMISSION_REQUEST_CODE = 101;
    private ActivityResultLauncher<Intent> pickImageLauncher;

    public ProfileEditFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_profile_edit, container, false); // 기존 레이아웃 그대로 사용
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        profileImage = view.findViewById(R.id.profile_image);
        textName = view.findViewById(R.id.text_name);
        textBirth = view.findViewById(R.id.text_birth);
        textGender = view.findViewById(R.id.text_gender);
        textJob = view.findViewById(R.id.text_job);
        btnEdit = view.findViewById(R.id.btn_profile_edit);
        btnBack = view.findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> requireActivity().onBackPressed());

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        profileImage.setImageURI(selectedImageUri);
                        saveImageToInternalStorage();
                    }
                });

        profileImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageLauncher.launch(intent);
        });

        btnEdit.setOnClickListener(v -> showEditDialog());

        checkAndLoadProfile();
    }
    @Override
    public void onResume() {
        super.onResume();
        loadProfile();
    }

    private void checkAndLoadProfile() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, PERMISSION_REQUEST_CODE);
            } else {
                loadProfile();
            }
        } else {
            loadProfile();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadProfile();
            } else {
                Toast.makeText(requireContext(), "이미지를 표시하려면 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.RoundedDialog);
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.form_dialog, null);
        builder.setView(view);

        EditText editName = view.findViewById(R.id.edit_name);
        EditText editBirth = view.findViewById(R.id.edit_birth);
        Spinner spinnerJob = view.findViewById(R.id.spinner_job);
        RadioGroup radioGender = view.findViewById(R.id.radio_gender);
        RadioButton radioMale = view.findViewById(R.id.radio_male);
        RadioButton radioFemale = view.findViewById(R.id.radio_female);
        Button btnSave = view.findViewById(R.id.btn_save);

        String[] jobOptions = {"학교", "회사", "운동", "데이트", "일상"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, jobOptions);
        spinnerJob.setAdapter(adapter);

        SharedPreferences prefs = requireContext().getSharedPreferences("user_profile", getContext().MODE_PRIVATE);
        editName.setText(prefs.getString("name", ""));
        editBirth.setText(prefs.getString("birth", ""));
        String savedJob = prefs.getString("job", "");
        for (int i = 0; i < jobOptions.length; i++) {
            if (jobOptions[i].equals(savedJob)) {
                spinnerJob.setSelection(i);
                break;
            }
        }

        String gender = prefs.getString("gender", "");
        if (gender.equals("남자")) radioMale.setChecked(true);
        else if (gender.equals("여자")) radioFemale.setChecked(true);

        editBirth.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                    (view1, year1, month1, dayOfMonth) -> {
                        String formattedDate = String.format(Locale.KOREA, "%04d-%02d-%02d",
                                year1, month1 + 1, dayOfMonth);
                        editBirth.setText(formattedDate);
                    }, year, month, day);

            datePickerDialog.show();
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        btnSave.setOnClickListener(v -> {
            String name = editName.getText().toString();
            String birth = editBirth.getText().toString();
            String job = spinnerJob.getSelectedItem().toString();
            String genderStr = (radioGender.getCheckedRadioButtonId() == R.id.radio_male) ? "남자" : "여자";

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("name", name);
            editor.putString("birth", birth);
            editor.putString("job", job);
            editor.putString("gender", genderStr);
            editor.apply();

            loadProfile();
            dialog.dismiss();
        });
    }

    private void loadProfile() {
        SharedPreferences prefs = requireContext().getSharedPreferences("user_profile", getContext().MODE_PRIVATE);
        textName.setText(prefs.getString("name", "이름 없음"));
        textBirth.setText(prefs.getString("birth", "생년월일 없음"));
        textGender.setText(prefs.getString("gender", "성별 없음"));
        textJob.setText(prefs.getString("job", "직무 없음"));

        String imagePath = prefs.getString("profile_image_path", null);
        if (imagePath != null) {
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                profileImage.setImageURI(Uri.fromFile(imgFile));
            } else {
                Toast.makeText(requireContext(), "프로필 이미지 파일이 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveImageToInternalStorage() {
        if (selectedImageUri == null) return;

        try {
            InputStream inputStream = requireActivity().getContentResolver().openInputStream(selectedImageUri);
            File file = new File(requireContext().getFilesDir(), "profile_image.jpg");
            OutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }

            inputStream.close();
            outputStream.close();

            SharedPreferences.Editor editor = requireContext()
                    .getSharedPreferences("user_profile", getContext().MODE_PRIVATE).edit();
            editor.putString("profile_image_path", file.getAbsolutePath());
            editor.apply();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "이미지 저장 실패", Toast.LENGTH_SHORT).show();
        }
    }
}