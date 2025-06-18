package com.example.weather.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.weather.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddClothesActivity extends AppCompatActivity {

    private Button btnAddImage, btnSave, btnBack;
    private ImageView imagePreview;
    private Spinner spinnerCategory, spinnerSubCategory;
    private CheckBox checkBoxFavorite;  // ⭐ 추가
    private String savedImagePath = null;
    private Map<String, List<String>> subCategoryMap;

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selected = result.getData().getData();
                    savedImagePath = copyImageToInternalStorage(selected);
                    if (savedImagePath != null) {
                        imagePreview.setImageURI(Uri.fromFile(new File(savedImagePath)));
                    } else {
                        Toast.makeText(this, "이미지 저장 실패", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clothes);

        btnBack = findViewById(R.id.btnBack);
        btnAddImage = findViewById(R.id.btnAddImage);
        btnSave = findViewById(R.id.btnSave);
        imagePreview = findViewById(R.id.imagePreview);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerSubCategory = findViewById(R.id.spinnerSubCategory);
        checkBoxFavorite = findViewById(R.id.checkBoxFavorite); // ⭐ 추가

        btnBack.setOnClickListener(v -> finish());

        String[] categories = {"아우터", "상의", "하의", "신발"};
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(categoryAdapter);

        subCategoryMap = new HashMap<>();
        subCategoryMap.put("아우터", List.of("바람막이", "트렌치코트", "레인자켓", "두꺼운 패딩", "롱패딩", "얇은 자켓", "얇은 방수 점퍼", "레인코트", "울 코트", "야상 자켓"));
        subCategoryMap.put("상의", List.of("린넨 셔츠", "반팔 티셔츠", "민소매", "니트", "블라우스", "맨투맨", "히트텍", "긴팔 셔츠", "얇은 셔츠", "기모 셔츠"));
        subCategoryMap.put("하의", List.of("반바지", "슬랙스", "청바지", "면바지", "롱스커트", "치마", "기모바지", "두꺼운 레깅스", "긴바지", "트레이닝 팬츠"));
        subCategoryMap.put("신발", List.of("샌들", "슬리퍼", "운동화", "방수 운동화", "레인부츠", "부츠", "방한 부츠", "밝은색 운동화", "크록스", "통굽 샌들"));

        spinnerCategory.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                String selectedCategory = spinnerCategory.getSelectedItem().toString().trim();
                List<String> subOptions = subCategoryMap.get(selectedCategory);
                if (subOptions != null) {
                    ArrayAdapter<String> subAdapter = new ArrayAdapter<>(AddClothesActivity.this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<>(subOptions));
                    spinnerSubCategory.setAdapter(subAdapter);
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        btnAddImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        btnSave.setOnClickListener(v -> {
            if (savedImagePath == null) {
                Toast.makeText(this, "이미지를 선택하세요", Toast.LENGTH_SHORT).show();
                return;
            }

            String category = spinnerCategory.getSelectedItem().toString();
            String subCategory = spinnerSubCategory.getSelectedItem().toString();
            boolean isFavorite = checkBoxFavorite.isChecked(); // ⭐ 추가

            try {
                FileOutputStream fos = openFileOutput("clothes_data.txt", MODE_APPEND);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
                writer.write(savedImagePath + "," + category + "," + subCategory + "," + isFavorite); // ⭐ 즐겨찾기 저장
                writer.newLine();
                writer.close();
                Toast.makeText(this, "저장 완료", Toast.LENGTH_SHORT).show();
                finish();
            } catch (Exception e) {
                Toast.makeText(this, "저장 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String copyImageToInternalStorage(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            File imageDir = new File(getFilesDir(), "clothes");
            if (!imageDir.exists()) imageDir.mkdirs();

            String fileName = "img_" + System.currentTimeMillis() + ".jpg";
            File imageFile = new File(imageDir, fileName);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            return imageFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
