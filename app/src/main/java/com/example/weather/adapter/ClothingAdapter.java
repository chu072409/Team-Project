package com.example.weather.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import java.util.List;

public class ClothingAdapter extends RecyclerView.Adapter<ClothingAdapter.ViewHolder> {

    private List<ClothingItem> itemList;
    private Context context;

    public ClothingAdapter(Context context, List<ClothingItem> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView categoryText;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView);
            categoryText = view.findViewById(R.id.categoryText);
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
        holder.categoryText.setText(item.getSubCategory() + ""); // 텍스트 간결화

        Glide.with(context)
                .load(Uri.fromFile(new File(item.getImageUri())))
                .placeholder(R.drawable.placeholder)
                .into(holder.imageView);

        holder.itemView.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("삭제")
                    .setMessage("이 항목을 삭제하시겠습니까?")
                    .setPositiveButton("삭제", (dialog, which) -> deleteItem(position))
                    .setNegativeButton("취소", null)
                    .show();
        });
    }

    private void deleteItem(int position) {
        ClothingItem item = itemList.get(position);
        itemList.remove(position);
        notifyItemRemoved(position);

        try {
            File file = new File(context.getFilesDir(), "clothes_data.txt");
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

            // 이미지 파일도 삭제
            File imgFile = new File(item.getImageUri());
            if (imgFile.exists()) imgFile.delete();

            Toast.makeText(context, "삭제 완료", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "삭제 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setItems(List<ClothingItem> list) {
        this.itemList = list;
        notifyDataSetChanged();
    }
}
