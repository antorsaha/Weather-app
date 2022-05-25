package com.example.weatherapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weatherapp.databinding.ActivityDetailsBinding;
import com.example.weatherapp.models.DailyWeather;

import java.util.Objects;

public class DetailsActivity extends AppCompatActivity {

    private ActivityDetailsBinding binding;
    private static final String TAG = "DetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();

        /*ImageView weatherIcon = findViewById(R.id.iv_weather_icon);
        TextView tv_location = findViewById(R.id.tv_location);
        TextView tv_date = findViewById(R.id.tv_date);
        TextView tv_temp = findViewById(R.id.tv_temp);
        TextView tv_wind = findViewById(R.id.tv_wind);
        TextView tv_cloud = findViewById(R.id.tv_clouds);
        TextView tv_sunRise = findViewById(R.id.tv_sun_rise);
        TextView tv_sunSet = findViewById(R.id.tv_sun_set);
        TextView tv_humiadity = findViewById(R.id.tv_humiadity);*/

        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        DailyWeather weather = (DailyWeather) intent.getSerializableExtra(MainActivity.putDailyData);

        DailyWeather weatherData = getIntent().getParcelableExtra(MainActivity.putDailyData);

        //binding.tvDate.setText(weather.getWindSpeed() + "m/s");
        //Log.d(TAG, "onCreate: "+ weather.getDate());

    }
}