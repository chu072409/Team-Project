package com.example.weather.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.adapter.ClothingAdapter;
import com.example.weather.model.ClothingItem;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ClosetActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ClothingAdapter adapter;
    private List<ClothingItem> clothingList = new ArrayList<>();
    private Button btnAll, btnOuter, btnTop, btnBottom, btnShoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_closet); // 또는 activity_closet로 변경해도 OK

        recyclerView = findViewById(R.id.recyclerClothes);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        btnAll = findViewById(R.id.btnAll);
        btnOuter = findViewById(R.id.btnOuter);
        btnTop = findViewById(R.id.btnTop);
        btnBottom = findViewById(R.id.btnBottom);
        btnShoes = findViewById(R.id.btnShoes);

        adapter = new ClothingAdapter(this, clothingList, () -> loadClothes("전체")); // ✅ 삭제 후 새로고침 콜백 포함
        recyclerView.setAdapter(adapter);

        btnAll.setOnClickListener(v -> loadClothes("전체"));
        btnOuter.setOnClickListener(v -> loadClothes("아우터"));
        btnTop.setOnClickListener(v -> loadClothes("상의"));
        btnBottom.setOnClickListener(v -> loadClothes("하의"));
        btnShoes.setOnClickListener(v -> loadClothes("신발"));

        loadClothes("전체");
    }

    private void loadClothes(String categoryFilter) {
        clothingList.clear();
        try {
            FileInputStream fis = openFileInput("clothes_data.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String imagePath = parts[0].trim();
                    String category = parts[1].trim();
                    String subCategory = parts[2].trim();

                    if (categoryFilter.equals("전체") || category.equals(categoryFilter)) {
                        clothingList.add(new ClothingItem(imagePath, category, subCategory));
                    }
                }
            }

            reader.close();
        } catch (Exception e) {
            Toast.makeText(this, "불러오기 실패", Toast.LENGTH_SHORT).show();
        }

        if (adapter != null) {
            adapter.setItems(clothingList); // ✅ 최신 목록 갱신
        }
    }
}
