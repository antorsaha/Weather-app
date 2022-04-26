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
    private final List<DailyWeather> dailyWeatherList;
    private static final String TAG = "SevenDaysForecastAdapter";

    @SuppressLint("LongLogTag")
    public SevenDaysForecastAdapter(List<DailyWeather> weatherList) {
        this.dailyWeatherList = weatherList;
        Log.d(TAG, "SevenDaysForecastAdapter: constructor with data size: " + dailyWeatherList.size());

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
        //return 10;
        Log.d(TAG, "getItemCount: "+ dailyWeatherList.size());
        return dailyWeatherList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView tempMinMax;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.item_date);
            tempMinMax = itemView.findViewById(R.id.item_min_max_temp);
        }

        @SuppressLint("LongLogTag")
        void bind(int position) {
            DailyWeather weather = dailyWeatherList.get(position);
            date.setText(String.valueOf(weather.getDate()));
            tempMinMax.setText(weather.getTempMax() + ", " + weather.getTempMin());
            //date.setText(position);
            Log.d(TAG, "bind: is called and bind successful");
        }
    }
}
