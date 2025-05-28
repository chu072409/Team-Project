package com.example.closetfeaturetest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddClothesActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_IMAGE = 101;
    private Button btnAddImage, btnSave;
    private ImageView imagePreview;
    private Spinner spinnerCategory, spinnerSubCategory;
    private Uri selectedImageUri;

    private Map<String, List<String>> subCategoryMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clothes);

        // 뒤로가기 버튼
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // 뷰 연결
        btnAddImage = findViewById(R.id.btnAddImage);
        imagePreview = findViewById(R.id.imagePreview);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerSubCategory = findViewById(R.id.spinnerSubCategory);
        btnSave = findViewById(R.id.btnSave);

        // 카테고리 리스트
        String[] categories = {"아우터", "상의", "하의", "신발"};
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(categoryAdapter);

        // 세부 분류 리스트 구성
        subCategoryMap = new HashMap<>();
        subCategoryMap.put("아우터", List.of("바람막이", "트렌치코트", "레인자켓", "두꺼운 패딩", "롱패딩", "얇은 자켓", "얇은 방수 점퍼", "레인코트", "울 코트", "야상 자켓"));
        subCategoryMap.put("상의", List.of("린넨 셔츠", "반팔 티셔츠", "민소매", "니트", "블라우스", "맨투맨", "히트텍", "긴팔 셔츠", "얇은 셔츠", "기모 셔츠"));
        subCategoryMap.put("하의", List.of("반바지", "슬랙스", "청바지", "면바지", "롱스커트", "치마", "기모바지", "두꺼운 레깅스", "긴바지", "트레이닝 팬츠"));
        subCategoryMap.put("신발", List.of("샌들", "슬리퍼", "운동화", "방수 운동화", "레인부츠", "부츠", "방한 부츠", "밝은색 운동화", "크록스", "통굽 샌들"));

        // 카테고리 선택 시 세부 분류 동적 설정
        spinnerCategory.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                String selectedCategory = categories[position];
                List<String> subOptions = subCategoryMap.get(selectedCategory);
                if (subOptions != null) {
                    ArrayAdapter<String> subAdapter = new ArrayAdapter<>(AddClothesActivity.this, android.R.layout.simple_spinner_dropdown_item, subOptions);
                    spinnerSubCategory.setAdapter(subAdapter);
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        // 이미지 선택
        btnAddImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_IMAGE);
        });

        // 저장 버튼
        btnSave.setOnClickListener(v -> {
            if (selectedImageUri == null) {
                Toast.makeText(this, "이미지를 선택하세요", Toast.LENGTH_SHORT).show();
                return;
            }

            String category = spinnerCategory.getSelectedItem().toString();
            String subCategory = spinnerSubCategory.getSelectedItem().toString();
            String imagePath = selectedImageUri.toString(); // 내부 저장소 경로

            try {
                FileOutputStream fos = openFileOutput("clothes_data.txt", MODE_APPEND);
                OutputStreamWriter writer = new OutputStreamWriter(fos);
                writer.write(imagePath + "," + category + "," + subCategory + "\n");
                writer.close();

                Toast.makeText(this, "저장 완료!", Toast.LENGTH_SHORT).show();
                finish();

            } catch (Exception e) {
                Toast.makeText(this, "저장 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 이미지 선택 결과 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selected = data.getData();

            // 내부 저장소에 이미지 복사
            String savedPath = copyImageToInternalStorage(selected);

            if (savedPath != null) {
                selectedImageUri = Uri.fromFile(new File(savedPath));
                imagePreview.setImageURI(selectedImageUri);
            } else {
                Toast.makeText(this, "이미지 저장 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 이미지 복사 함수
    private String copyImageToInternalStorage(Uri uri) {
        try {
            File dir = new File(getFilesDir(), "clothes");
            if (!dir.exists()) dir.mkdirs();

            String fileName = "img_" + System.currentTimeMillis() + ".jpg";
            File destFile = new File(dir, fileName);

            InputStream in = getContentResolver().openInputStream(uri);
            FileOutputStream out = new FileOutputStream(destFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }

            in.close();
            out.close();

            return destFile.getAbsolutePath();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
