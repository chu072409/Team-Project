package com.example.weather.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.model.HourlyWeather;
import com.example.weather.utils.WeatherUtils;

import java.util.List;

// HourlyWeatherAdapter.java
public class HourlyWeatherAdapter extends RecyclerView.Adapter<HourlyWeatherAdapter.HourlyViewHolder> {

    private List<HourlyWeather> hourlyList;
    //test
    // 생성자에서 List<HourlyWeather> 인자를 받도록 수정
    public HourlyWeatherAdapter(List<HourlyWeather> hourlyList) {
        this.hourlyList = hourlyList;
    }

    @NonNull
    @Override
    public HourlyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hourly_weather, parent, false);
        return new HourlyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyViewHolder holder, int position) {
        HourlyWeather item = hourlyList.get(position);
        holder.hourText.setText(item.getTime());
        holder.hourTemp.setText(item.getTemperature() + "°");
        holder.hourIcon.setImageResource(WeatherUtils.getWeatherSymbolResource(item.getIconCode()));
    }

    @Override
    public int getItemCount() {
        return hourlyList.size();
    }

    // 데이터를 업데이트하는 메서드 추가
    public void updateHourlyWeather(List<HourlyWeather> newData) {
        this.hourlyList = newData;
        notifyDataSetChanged();
    }

    static class HourlyViewHolder extends RecyclerView.ViewHolder {
        TextView hourText, hourTemp;
        ImageView hourIcon;

        HourlyViewHolder(View itemView) {
            super(itemView);
            hourText = itemView.findViewById(R.id.hourText);
            hourTemp = itemView.findViewById(R.id.hourTemp);
            hourIcon = itemView.findViewById(R.id.hourIcon);
        }
    }
}
