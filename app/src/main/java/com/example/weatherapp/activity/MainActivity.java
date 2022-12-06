package com.example.weatherapp.activity;

import static com.example.weatherapp.data.WeatherContract.WeatherEntity.getSqlSelectForTodayOnwards;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.example.weatherapp.adapters.SevenDaysForecastAdapter;
import com.example.weatherapp.data.SunshinePreferences;
import com.example.weatherapp.data.WeatherContract;
import com.example.weatherapp.data.WeatherContract.WeatherEntity;
import com.example.weatherapp.models.DailyWeather;
import com.example.weatherapp.utilities.NetworkUtils;
import com.example.weatherapp.utilities.WeatherJsonResponseUtils;

import org.json.JSONException;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements SevenDaysForecastAdapter.ItemClickHandler,
        LoaderManager.LoaderCallbacks<Cursor>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String[] MAIN_FORECAST_PROJECTION = {
            WeatherEntity.DATE,
            WeatherEntity.TEMP_MAX,
            WeatherEntity.TEMP_MIN,
            WeatherEntity.SUNRISE,
            WeatherEntity.SUNSET,
            WeatherEntity.ATMOSPHERIC_PRESSURE,
            WeatherEntity.HUMIDITY,
            WeatherEntity.CLOUDINESS,
            WeatherEntity.UV_INDEX,
            WeatherEntity.WIND_SPEED,
            WeatherEntity.WEATHER,
            WeatherEntity.DESCRIPTION,

    };
    public static final int POSITION_DATE = 0;
    public static final int POSITION_TEMP_MAX = 1;
    public static final int POSITION_TEMP_MIN = 2;
    public static final int POSITION_SUNRISE = 3;
    public static final int POSITION_SUNSET = 4;
    public static final int POSITION_ATMOSPHERIC_PRESSURE = 5;
    public static final int POSITION_HUMIDITY = 6;
    public static final int POSITION_CLOUDINESS = 7;
    public static final int POSITION_UV_INDEX = 8;
    public static final int POSITION_WIND_SPEED = 9;
    public static final int POSITION_WEATHER = 10;
    public static final int POSITION_DESCRIPTION = 11;


    private static final String TAG = "MainActivity";
    private static final int ID_FORECAST_LOADER = 30;
    SevenDaysForecastAdapter adapter;

    //view declaration
    private TextView displayTextView;
    private RecyclerView itemsRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize views
        displayTextView = findViewById(R.id.tv_today_forecast);
        itemsRV = findViewById(R.id.rv_forecast);

        loadData();
        setupSharedPreference();

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        itemsRV.setLayoutManager(layoutManager);
        adapter = new SevenDaysForecastAdapter(this, this);
        itemsRV.setAdapter(adapter);

        /*
         * Ensures a loader is initialized and active. If the loader doesn't already exist, one is
         * created and (if the activity/fragment is currently started) starts the loader. Otherwise
         * the last created loader is re-used.
         */
        getSupportLoaderManager().initLoader(ID_FORECAST_LOADER, null, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*
        PreferenceManager.getDefaultSharedPreferences(this).
                unregisterOnSharedPreferenceChangeListener(this);
         */
    }

    /**
     * load data using background thread
     */
    private void loadData() {
        //Load location coordinates
        double[] location = SunshinePreferences.getPreferredWeatherCoordinates(this);

        //create a background thread
        new FetchWeatherTask().execute(location);
    }

    /**
     * setup the shared preferences and
     * register all sharedPreferences change listener
     */
    private void setupSharedPreference() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(this);


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_refresh)
            loadData();
        else if (itemId == R.id.action_setting) {
            Intent settingIntent = new Intent(this, SettingActivity.class);
            startActivity(settingIntent);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.forecust_menu, menu);
        return true;
    }

    /**
     * This is SevenDaysForecastAdapter onclick listener
     *
     * @param date is which date is clicked
     */
    @Override
    public void onClick(long date) {

        Intent intent = new Intent(this, DetailsActivity.class);
        Uri uriForDateClicked = WeatherEntity.buildWeatherUriWithDate(date);
        intent.setData(uriForDateClicked);
        startActivity(intent);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id) {
            case ID_FORECAST_LOADER:
                Uri forecastQueryUri = WeatherContract.WeatherEntity.CONTENT_URI;
                String sortOrder = WeatherContract.WeatherEntity.DATE + " ASC";

                return new CursorLoader(this,
                        forecastQueryUri,
                        MAIN_FORECAST_PROJECTION,
                        null,
                        null,
                        sortOrder);
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data == null) {
            return;
        }

        //Setting new data from loader to adapter
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    /**
     * This function triggered when a value change in shared preference
     *
     * @param sharedPreferences the sharedPreference object
     * @param key               is the key which value is updated
     */

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_units_key))) {
            String unit = sharedPreferences.getString(getString(R.string.pref_units_key),
                    getString(R.string.pref_units_def_val));
            NetworkUtils.setUnits(unit);
        }
    }


    /**
     * Creating a background thread using AsyncTask
     * This manage network call in background
     */
    class FetchWeatherTask extends AsyncTask<double[], Void, String> {

        /**
         * work in background thread
         *
         * @param doubles is weather coordinate
         * @return the json response from the api as String
         */

        @Override
        protected String doInBackground(double[]... doubles) {
            if (doubles.length == 0) return null;

            //doubles is a array of double array
            double[] location = doubles[0];

            //Building url using location
            URL requestUrl = NetworkUtils.buildUrl(location);

            try {
                return NetworkUtils.getResponseFromHttpUrl(requestUrl);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * work in UI thread
         *
         * @param s response from background thread
         */

        @Override
        protected void onPostExecute(String s) {
            if (s == null) {
                displayTextView.setText("");
                Toast.makeText(getApplicationContext(), "Data is null", Toast.LENGTH_SHORT).show();
                return;
            }
            String currentWeatherString = null;

            try {
                //Current weather from json response
                currentWeatherString = WeatherJsonResponseUtils.getCurrentWeatherString(getApplicationContext(), s);


                WeatherJsonResponseUtils.loadDailyWeather(getApplicationContext(), s);
                Log.d(TAG, "onPostExecute: loadDailyWeather is called");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            displayTextView.setText(currentWeatherString);

        }
    }
}