package com.example.weather.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.weather.R;
import com.example.weather.model.ClothingItem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClothingAdapter extends RecyclerView.Adapter<ClothingAdapter.ViewHolder> {

    private List<ClothingItem> itemList;
    private final Context context;
    private final Runnable onItemDeleted;

    public ClothingAdapter(Context context, List<ClothingItem> itemList, Runnable onItemDeleted) {
        this.context = context;
        this.itemList = new ArrayList<>(itemList);
        this.onItemDeleted = onItemDeleted;
        sortByFavorite();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView categoryText;
        ImageView favoriteStar;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView);
            categoryText = view.findViewById(R.id.categoryText);
            favoriteStar = view.findViewById(R.id.favoriteStar);
        }
    }

    @Override
    public ClothingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_clothing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClothingAdapter.ViewHolder holder, int position) {
        ClothingItem item = itemList.get(position);
        holder.categoryText.setText(item.getSubCategory());

        File imageFile = new File(item.getImageUri());
        Uri imageUri = imageFile.exists() ? Uri.fromFile(imageFile)
                : Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.placeholder);

        Glide.with(context).load(imageUri).into(holder.imageView);

        // 즐겨찾기 아이콘 표시 여부
        if (item.isFavorite()) {
            holder.favoriteStar.setVisibility(View.VISIBLE);
            holder.favoriteStar.setImageResource(R.drawable.ic_star);
        } else {
            holder.favoriteStar.setVisibility(View.GONE);
        }

        // 항목 클릭 시 다이얼로그
        holder.itemView.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("옵션 선택");
            builder.setItems(item.isFavorite() ?
                            new CharSequence[]{"즐겨찾기 해제", "정보 수정", "삭제", "취소"} :
                            new CharSequence[]{"즐겨찾기 등록", "정보 수정", "삭제", "취소"},
                    (dialog, which) -> {
                        switch (which) {
                            case 0: // 즐겨찾기 토글
                                item.setFavorite(!item.isFavorite());
                                sortByFavorite();
                                saveAllItemsToFile();
                                notifyDataSetChanged();
                                Toast.makeText(context, item.isFavorite() ? "즐겨찾기 등록됨" : "즐겨찾기 해제됨", Toast.LENGTH_SHORT).show();
                                break;
                            case 1: // 정보 수정
                                showEditDialog(item);
                                break;
                            case 2: // 삭제
                                deleteItem(holder.getAdapterPosition());
                                break;
                            case 3: // 취소
                                dialog.dismiss();
                                break;
                        }
                    });
            builder.show();
        });
    }

    private void showEditDialog(ClothingItem item) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_clothing, null);
        Spinner categorySpinner = dialogView.findViewById(R.id.spinnerEditCategory);
        Spinner subCategorySpinner = dialogView.findViewById(R.id.spinnerEditSubCategory);

        // 카테고리 데이터
        List<String> categoryList = Arrays.asList("아우터", "상의", "하의", "신발");
        Map<String, List<String>> subMap = new HashMap<>();
        subMap.put("아우터", Arrays.asList("바람막이", "트렌치코트", "레인자켓", "두꺼운 패딩", "롱패딩", "얇은 자켓", "얇은 방수 점퍼", "레인코트", "울 코트", "야상 자켓"));
        subMap.put("상의", Arrays.asList("린넨 셔츠", "반팔 티셔츠", "민소매", "니트", "블라우스", "맨투맨", "히트텍", "긴팔 셔츠", "얇은 셔츠", "기모 셔츠"));
        subMap.put("하의", Arrays.asList("반바지", "슬랙스", "청바지", "면바지", "롱스커트", "치마", "기모바지", "두꺼운 레깅스", "긴바지", "트레이닝 팬츠"));
        subMap.put("신발", Arrays.asList("샌들", "슬리퍼", "운동화", "방수 운동화", "레인부츠", "부츠", "방한 부츠", "밝은색 운동화", "크록스", "통굽 샌들"));

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, categoryList);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setSelection(categoryList.indexOf(item.getCategory()));

        categorySpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String selected = categoryList.get(position);
                List<String> subOptions = subMap.get(selected);
                if (subOptions != null) {
                    ArrayAdapter<String> subAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, subOptions);
                    subCategorySpinner.setAdapter(subAdapter);
                    if (item.getCategory().equals(selected)) {
                        subCategorySpinner.setSelection(subOptions.indexOf(item.getSubCategory()));
                    }
                }
            }

            @Override public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("정보 수정");
        builder.setView(dialogView);
        builder.setPositiveButton("저장", (dialog, which) -> {
            String newCategory = categorySpinner.getSelectedItem().toString();
            String newSubCategory = subCategorySpinner.getSelectedItem().toString();
            item.setFavorite(item.isFavorite()); // 그대로 유지
            itemList.get(itemList.indexOf(item)).setFavorite(item.isFavorite());
            itemList.get(itemList.indexOf(item)).setCategory(newCategory);
            itemList.get(itemList.indexOf(item)).setSubCategory(newSubCategory);
            saveAllItemsToFile();
            sortByFavorite();
            notifyDataSetChanged();
            Toast.makeText(context, "정보 수정 완료", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("취소", null);
        builder.show();
    }

    private void deleteItem(int position) {
        ClothingItem item = itemList.get(position);
        itemList.remove(position);
        notifyItemRemoved(position);

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.openFileInput("clothes_data.txt")));
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith(item.getImageUri() + ",")) {
                    lines.add(line);
                }
            }
            reader.close();

            FileOutputStream fos = context.openFileOutput("clothes_data.txt", Context.MODE_PRIVATE);
            for (String l : lines) {
                fos.write((l + "\n").getBytes());
            }
            fos.close();

            File imgFile = new File(item.getImageUri());
            if (imgFile.exists()) imgFile.delete();

            Toast.makeText(context, "삭제 완료", Toast.LENGTH_SHORT).show();

            if (onItemDeleted != null) {
                onItemDeleted.run();
            }

        } catch (Exception e) {
            Toast.makeText(context, "삭제 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveAllItemsToFile() {
        try {
            FileOutputStream fos = context.openFileOutput("clothes_data.txt", Context.MODE_PRIVATE);
            for (ClothingItem item : itemList) {
                String line = item.getImageUri() + "," + item.getCategory() + "," + item.getSubCategory() + "," + item.isFavorite();
                fos.write((line + "\n").getBytes());
            }
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sortByFavorite() {
        Collections.sort(itemList, new Comparator<ClothingItem>() {
            @Override
            public int compare(ClothingItem o1, ClothingItem o2) {
                return Boolean.compare(o2.isFavorite(), o1.isFavorite());
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setItems(List<ClothingItem> list) {
        this.itemList = new ArrayList<>(list);
        sortByFavorite();
        notifyDataSetChanged();
    }
}
