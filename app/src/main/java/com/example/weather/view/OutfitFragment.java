package com.example.weather.view;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.weather.R;
import com.example.weather.model.ClothingItem;
import com.example.weather.model.Weather;
import com.example.weather.recommend.OutfitRecommender;
import com.example.weather.viewmodel.HomeViewModel;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class OutfitFragment extends Fragment {

    private ImageView outerIcon, topIcon, bottomIcon, shoesIcon;
    private TextView outerDesc, topDesc, bottomDesc, shoesDesc;
    private TextView todayTipBtn;


    public OutfitFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_outfit, container, false);

        outerIcon = view.findViewById(R.id.outerIcon);
        topIcon = view.findViewById(R.id.topIcon);
        bottomIcon = view.findViewById(R.id.bottomIcon);
        shoesIcon = view.findViewById(R.id.shoesIcon);

        outerDesc = view.findViewById(R.id.outerDesc);
        topDesc = view.findViewById(R.id.topDesc);
        bottomDesc = view.findViewById(R.id.bottomDesc);
        shoesDesc = view.findViewById(R.id.shoesDesc);
        todayTipBtn = view.findViewById(R.id.todayTipBtn);

        CardView btnClosetCard = view.findViewById(R.id.btnClosetCard);
        btnClosetCard.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new ClosetFragment())
                    .addToBackStack(null)
                    .commit();
        });

        HomeViewModel homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        homeViewModel.getWeather().observe(getViewLifecycleOwner(), weather -> {
            if (weather == null) return;
            int temp = (int) weather.getTemperature();
            String desc = weather.getWeatherDescription();

            List<ClothingItem> clothes = loadClothesFromFile();
            recommendClothes(temp, desc, clothes);
        });

        return view;
    }

    private List<ClothingItem> loadClothesFromFile() {
        List<ClothingItem> clothes = new ArrayList<>();
        try {
            FileInputStream fis = requireContext().openFileInput("clothes_data.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String imageUri = parts[0].trim();
                    String category = parts[1].trim();
                    String subCategory = parts[2].trim();
                    clothes.add(new ClothingItem(imageUri, category, subCategory));
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clothes;
    }

    private void recommendClothes(int temperature, String weatherDescription, List<ClothingItem> clothes) {
        ClothingItem top = OutfitRecommender.recommend("상의", temperature, weatherDescription, clothes);
        ClothingItem bottom = OutfitRecommender.recommend("하의", temperature, weatherDescription, clothes);
        ClothingItem outer = OutfitRecommender.recommend("아우터", temperature, weatherDescription, clothes);
        ClothingItem shoes = OutfitRecommender.recommend("신발", temperature, weatherDescription, clothes);

        loadClothingImage(outerIcon, outer);
        loadClothingImage(topIcon, top);
        loadClothingImage(bottomIcon, bottom);
        loadClothingImage(shoesIcon, shoes);

        outerDesc.setText(outer.getSubCategory() + " 추천 - " + getReason(outer.getSubCategory(), temperature, weatherDescription));
        topDesc.setText(top.getSubCategory() + " 추천 - " + getReason(top.getSubCategory(), temperature, weatherDescription));
        bottomDesc.setText(bottom.getSubCategory() + " 추천 - " + getReason(bottom.getSubCategory(), temperature, weatherDescription));
        shoesDesc.setText(shoes.getSubCategory() + " 추천 - " + getReason(shoes.getSubCategory(), temperature, weatherDescription));

        todayTipBtn.setOnClickListener(v -> showTodayTipDialog(temperature, weatherDescription));
    }

    private void showTodayTipDialog(int temp, String desc) {
        String tip;
        if (desc.contains("맑음") && temp >= 20) {
            tip = "자외선이 강하니 썬크림을 꼭 챙기세요!";
        } else if (desc.contains("비") || desc.contains("소나기")) {
            tip = "비가 오니 우산을 꼭 챙기시고, 미끄러짐 주의하세요.";
        } else if (desc.contains("천둥") || desc.contains("번개")) {
            tip = "천둥 번개가 예상됩니다. 실외 활동은 주의하세요.";
        } else if (desc.contains("눈")) {
            tip = "눈이 내려요! 미끄럼 사고 조심하고 따뜻하게 입으세요.";
        } else {
            tip = "쾌적한 날씨입니다. 외출을 즐기세요!";
        }

        new AlertDialog.Builder(requireContext())
                .setTitle("오늘의 날씨 팁")
                .setMessage(tip)
                .setPositiveButton("확인", null)
                .show();
    }

    private String getReason(String subCategory, int temp, String desc) {
        if (subCategory.contains("패딩")) return "추운 날씨에 보온이 좋아요.";
        if (subCategory.contains("셔츠")) return "간절기에 가볍고 실용적이에요.";
        if (subCategory.contains("반팔") || subCategory.contains("민소매")) return "더운 날씨에 시원하게 입기 좋아요.";
        if (subCategory.contains("바지") || subCategory.contains("슬랙스")) return "활동성이 좋고 편해요.";
        if (subCategory.contains("샌들") || subCategory.contains("슬리퍼")) return "더운 날씨에 통풍이 잘돼요.";
        if (subCategory.contains("부츠")) return "비 또는 눈길에 좋고 따뜻해요.";
        if (subCategory.contains("바람막이")) return "옵션으로 챙기시면 좋아요!";
        return "날씨에 맞춰 적절한 선택이에요.";
    }

    private void loadClothingImage(ImageView view, ClothingItem item) {
        if (item.getImageUri() == null || item.getImageUri().isEmpty()) {
            int resId = com.example.weather.utils.ClothingImageMapper.getImageResId(item.getSubCategory());
            view.setImageResource(resId);
        } else {
            Glide.with(requireContext())
                    .load(item.getImageUri())
                    .into(view);
        }
    }
}
