package com.example.weatherapp.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.weatherapp.R;
import com.example.weatherapp.data.WeatherContract.WeatherEntity;
import com.example.weatherapp.databinding.ActivityDetailsBinding;
import com.example.weatherapp.models.DailyWeather;
import com.example.weatherapp.utilities.SunshineDateUtils;

import java.util.Objects;

public class DetailsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{
    private static final String TAG = "DetailsActivity";

    private static final int LOADER_ID = 103;

    private static final String[] WEATHER_PROJECTION ={
            WeatherEntity.DATE,
            WeatherEntity.TEMP_MAX,
            WeatherEntity.WIND_SPEED,
            WeatherEntity.CLOUDINESS,
            WeatherEntity.SUNRISE,
            WeatherEntity.SUNSET,
            WeatherEntity.HUMIDITY,
            WeatherEntity.WEATHER_ICON
    };
    private final int POSITION_DATE = 0;
    private final int POSITION_TEMP_MAX = 1;
    private final int POSITION_WIND_SPEED = 2;
    private final int POSITION_CLOUDINESS = 3;
    private final int POSITION_SUNRISE = 4;
    private final int POSITION_SUNSET = 5;
    private final int POSITION_HUMIDITY = 6;
    private final int POSITION_WEATHER_ICON = 7;


    private ActivityDetailsBinding binding;

    // This is uri for the data pass through intent
    Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();

        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        mUri = intent.getData();

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);


        //DailyWeather weatherData = getIntent().getParcelableExtra(MainActivity.putDailyData);

        /*
        binding.tvDate.setText(weather.getDate() + "");
        Log.d(TAG, "onCreate: date: " + weather.getDate());
        binding.tvTemp.setText(weather.getTempMax() + "");
        binding.tvWind.setText(weather.getWindSpeed() + "");
        binding.tvClouds.setText(weather.getClouds() +  "%");
        binding.tvSunRise.setText(weather.getSunrise() + "");
        binding.tvSunSet.setText(weather.getSunset() + "");
        binding.tvHumiadity.setText(weather.getHumidity() + "");

        Log.d(TAG, "onCreate: date: " + weather.getDate());

         */

    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id){
            case LOADER_ID:
                return new CursorLoader(this,
                        mUri,
                        WEATHER_PROJECTION,
                        null, null, null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        boolean cursorHasValidData = false;
        if (data != null && data.moveToFirst()) {
            /* We have valid data, continue on to bind the data to the UI */
            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {
            /* No data to display, simply return and do nothing */
            return;
        }

        //String OUTPUT_DATE_FORMAT="dd MMMM, EEEE - hh:mm a";
        long dateInMills = data.getLong(POSITION_DATE);
        String dateString = SunshineDateUtils.getDateFromUTCTimestamp(dateInMills);
        binding.tvDate.setText(dateString);

        double temp = data.getDouble(POSITION_TEMP_MAX);
        binding.tvTemp.setText(String.valueOf(temp));

        double windSpeed = data.getDouble(POSITION_WIND_SPEED);
        binding.tvWind.setText(windSpeed + " m/s");

        double clouds = data.getDouble(POSITION_CLOUDINESS);
        binding.tvClouds.setText(clouds + "%");

        long sunRiseInMills = data.getLong(POSITION_SUNRISE);
        String sunRise = SunshineDateUtils.getTimeFromUTCTimestamp(sunRiseInMills);
        binding.tvSunRise.setText(sunRise);

        long sunSetInMills = data.getLong(POSITION_SUNSET);
        String sunSet = SunshineDateUtils.getTimeFromUTCTimestamp(sunSetInMills);
        binding.tvSunSet.setText(sunSet);

        double humidity = data.getDouble(POSITION_HUMIDITY);
        binding.tvHumiadity.setText(humidity + "%");

        String iconId = "i" + data.getString(POSITION_WEATHER_ICON);
        int drawableResourceId = this.getResources().getIdentifier(iconId, "drawable", this.getPackageName());
        Log.d(TAG, "onLoadFinished: " + drawableResourceId);

        binding.ivWeatherIcon.setImageResource(drawableResourceId);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}