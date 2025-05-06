package com.example.weather.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.model.DailyWeather;
import com.example.weather.model.Weather;
import com.example.weather.utils.WeatherUtils;

import java.util.List;
//test
// DailyWeatherAdapter.java
public class DailyWeatherAdapter extends RecyclerView.Adapter<DailyWeatherAdapter.DailyViewHolder> {

    private List<DailyWeather> dailyList;

    // 생성자에서 List<DailyWeather> 인자를 받도록 수정
    public DailyWeatherAdapter(List<DailyWeather> dailyList) {
        this.dailyList = dailyList;
    }

    @NonNull
    @Override
    public DailyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_daily_weather, parent, false);
        return new DailyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyViewHolder holder, int position) {
        DailyWeather item = dailyList.get(position);
        holder.dateText.setText(item.getDate());
        holder.tempText.setText(item.getTemperature() + "°C");
        holder.descText.setText(item.getWeatherDescription());
        holder.iconView.setImageResource(WeatherUtils.getWeatherSymbolResource(item.getIconCode()));
    }

    @Override
    public int getItemCount() {
        return dailyList.size();
    }

    // 데이터를 업데이트하는 메서드 추가
    public void updateDailyWeather(List<DailyWeather> newData) {
        this.dailyList = newData;
        notifyDataSetChanged();
    }

    static class DailyViewHolder extends RecyclerView.ViewHolder {
        TextView dateText, tempText, descText;
        ImageView iconView;

        public DailyViewHolder(@NonNull View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.dailyDate);
            tempText = itemView.findViewById(R.id.dailyTemp);
            descText = itemView.findViewById(R.id.dailyDesc);
            iconView = itemView.findViewById(R.id.dailyIcon);
        }
    }
}
