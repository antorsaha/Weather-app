package com.example.weatherapp.adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.example.weatherapp.models.DailyWeather;

import java.util.List;

public class SevenDaysForecastAdapter extends RecyclerView.Adapter<SevenDaysForecastAdapter.ItemViewHolder> {
    private static final String TAG = "SevenDaysForecastAdapter";
    private final List<DailyWeather> dailyWeatherList;
    private final ItemClickHandler itemClickHandler;

    @SuppressLint("LongLogTag")
    public SevenDaysForecastAdapter(List<DailyWeather> weatherList, ItemClickHandler clickHandler) {
        this.dailyWeatherList = weatherList;
        itemClickHandler = clickHandler;
        Log.d(TAG, "SevenDaysForecastAdapter: constructor with data size: " + dailyWeatherList.size());
    }

    public interface ItemClickHandler {
        void onClick(DailyWeather weather);
    }

    @SuppressLint("LongLogTag")
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_list_item, parent, false);
        Log.d(TAG, "onCreateViewHolder: is created");
        return new ItemViewHolder(view);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(position);
        Log.d(TAG, "onBindViewHolder: is called");
    }

    @SuppressLint("LongLogTag")
    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + dailyWeatherList.size());
        return dailyWeatherList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView date;
        TextView tempMinMax;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.item_date);
            tempMinMax = itemView.findViewById(R.id.item_min_max_temp);

            itemView.setOnClickListener(this);
        }

        void bind(int position) {
            DailyWeather weather = dailyWeatherList.get(position);
            date.setText(String.valueOf(weather.getDate()));
            tempMinMax.setText(weather.getTempMax() + ", " + weather.getTempMin());
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAbsoluteAdapterPosition();
            DailyWeather weather = dailyWeatherList.get(adapterPosition);
            itemClickHandler.onClick(weather);
        }
    }
}
