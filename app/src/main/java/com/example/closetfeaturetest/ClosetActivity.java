package com.example.closetfeaturetest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ClosetActivity extends AppCompatActivity {

    private RecyclerView recyclerClothes;
    private List<ClothingItem> fullClothingList = new ArrayList<>();
    private List<ClothingItem> filteredList = new ArrayList<>();
    private ClothingAdapter adapter;

    private Button btnAddImage;
    private Button btnAll, btnOuter, btnTop, btnBottom, btnShoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closet);

        // 버튼 연결
        btnAddImage = findViewById(R.id.btnAddImage);
        btnAll = findViewById(R.id.btnAll);
        btnOuter = findViewById(R.id.btnOuter);
        btnTop = findViewById(R.id.btnTop);
        btnBottom = findViewById(R.id.btnBottom);
        btnShoes = findViewById(R.id.btnShoes);

        // + 버튼 클릭 시 AddClothesActivity로 이동
        btnAddImage.setOnClickListener(v -> {
            Intent intent = new Intent(ClosetActivity.this, AddClothesActivity.class);
            startActivity(intent);
        });

        // RecyclerView 설정
        recyclerClothes = findViewById(R.id.recyclerClothes);
        recyclerClothes.setLayoutManager(new GridLayoutManager(this, 2));

        // 데이터 불러오기
        loadClothesData();

        // 기본 전체 목록 표시
        filteredList.addAll(fullClothingList);
        adapter = new ClothingAdapter(this, filteredList);
        recyclerClothes.setAdapter(adapter);

        // 필터 버튼들 클릭 리스너
        btnAll.setOnClickListener(v -> showFiltered("전체"));
        btnOuter.setOnClickListener(v -> showFiltered("아우터"));
        btnTop.setOnClickListener(v -> showFiltered("상의"));
        btnBottom.setOnClickListener(v -> showFiltered("하의"));
        btnShoes.setOnClickListener(v -> showFiltered("신발"));
    }

    // txt 파일에서 데이터 불러오기
    private void loadClothesData() {
        fullClothingList.clear();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput("clothes_data.txt")));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    fullClothingList.add(new ClothingItem(parts[0], parts[1], parts[2]));
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 카테고리별 필터링 적용
    private void showFiltered(String category) {
        filteredList.clear();

        if (category.equals("전체")) {
            filteredList.addAll(fullClothingList);
        } else {
            for (ClothingItem item : fullClothingList) {
                if (item.category.equals(category)) {
                    filteredList.add(item);
                }
            }
        }

        adapter.notifyDataSetChanged();
    }
}
