package com.example.weather.view;

import android.content.Intent;
import android.os.Bundle;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
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
        recyclerClothes.setLayoutManager(new GridLayoutManager(getContext(), 3)); // ✅ 3열 + 스크롤

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

        // 버튼별 필터링
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
        loadItems();
        showFiltered(selectedCategory);
    }

    private void loadItems() {
        allItems.clear();
        try {
            File file = new File(requireContext().getFilesDir(), "clothes_data.txt");
            if (!file.exists()) return;

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String imagePath = parts[0].trim();
                    String category = parts[1].trim();
                    String subCategory = parts[2].trim();
                    allItems.add(new ClothingItem(category, subCategory, imagePath));
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showFiltered(String category) {
        List<ClothingItem> filteredList = new ArrayList<>();
        for (ClothingItem item : allItems) {
            if (category.equals("전체") || item.getCategory().equals(category)) {
                filteredList.add(item);
            }
        }

        adapter = new ClothingAdapter(requireContext(), filteredList, () -> showFiltered(category));
        recyclerClothes.setAdapter(adapter);
    }
}
