package com.example.closetfeaturetest;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.io.File;


public class ClothingAdapter extends RecyclerView.Adapter<ClothingAdapter.ViewHolder> {

    private final Context context;
    private final List<ClothingItem> clothingList;

    public ClothingAdapter(Context context, List<ClothingItem> clothingList) {
        this.context = context;
        this.clothingList = clothingList;
    }

    @NonNull
    @Override
    public ClothingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_clothing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClothingAdapter.ViewHolder holder, int position) {
        ClothingItem item = clothingList.get(position);
        holder.categoryText.setText(item.category + " / " + item.subCategory);
        holder.imageView.setImageURI(Uri.fromFile(new File(item.imageUri)));

    }

    @Override
    public int getItemCount() {
        return clothingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView categoryText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.clothingImage);
            categoryText = itemView.findViewById(R.id.clothingText);
        }
    }
}
