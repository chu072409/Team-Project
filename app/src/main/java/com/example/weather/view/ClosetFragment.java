package com.example.weather.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.adapter.ClothingAdapter;
import com.example.weather.model.ClothingItem;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClosetFragment extends Fragment {

    private RecyclerView recyclerClothes;
    private ClothingAdapter adapter;
    private List<ClothingItem> allItems = new ArrayList<>();
    private String selectedCategory = "전체";

    private Button btnAll, btnOuter, btnTop, btnBottom, btnShoes, btnAddImage, btnBack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_closet, container, false);

        recyclerClothes = view.findViewById(R.id.recyclerClothes);
        recyclerClothes.setLayoutManager(new GridLayoutManager(getContext(), 3));

        btnBack = view.findViewById(R.id.btnBack);
        btnAddImage = view.findViewById(R.id.btnAddImage);
        btnAll = view.findViewById(R.id.btnAll);
        btnOuter = view.findViewById(R.id.btnOuter);
        btnTop = view.findViewById(R.id.btnTop);
        btnBottom = view.findViewById(R.id.btnBottom);
        btnShoes = view.findViewById(R.id.btnShoes);

        btnBack.setOnClickListener(v -> requireActivity().onBackPressed());

        btnAddImage.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddClothesActivity.class);
            startActivity(intent);
        });

        btnAll.setOnClickListener(v -> {
            selectedCategory = "전체";
            showFiltered("전체");
        });
        btnOuter.setOnClickListener(v -> {
            selectedCategory = "아우터";
            showFiltered("아우터");
        });
        btnTop.setOnClickListener(v -> {
            selectedCategory = "상의";
            showFiltered("상의");
        });
        btnBottom.setOnClickListener(v -> {
            selectedCategory = "하의";
            showFiltered("하의");
        });
        btnShoes.setOnClickListener(v -> {
            selectedCategory = "신발";
            showFiltered("신발");
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
        showFiltered(selectedCategory);
    }

    private void loadData() {
        allItems.clear();
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(requireContext().openFileInput("clothes_data.txt"))
            );
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String imagePath = parts[0];
                    String category = parts[1];
                    String subCategory = parts[2];

                    // ⭐ 즐겨찾기 정보 읽기
                    boolean isFavorite = parts.length >= 4 && Boolean.parseBoolean(parts[3]);

                    File file = new File(imagePath);
                    Log.d("IMAGE_DEBUG", "경로: " + imagePath + " | 존재함?: " + file.exists());

                    allItems.add(new ClothingItem(imagePath, category, subCategory, isFavorite)); // ⭐ 수정된 생성자 사용
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showFiltered(String category) {
        List<ClothingItem> filtered = new ArrayList<>();
        if (category.equals("전체")) {
            filtered.addAll(allItems);
        } else {
            for (ClothingItem item : allItems) {
                if (item.getCategory().equals(category)) {
                    filtered.add(item);
                }
            }
        }
        adapter = new ClothingAdapter(requireContext(), filtered, this::onItemDeleted);
        recyclerClothes.setAdapter(adapter);
    }

    private void onItemDeleted() {
        loadData();
        showFiltered(selectedCategory);
    }
}
