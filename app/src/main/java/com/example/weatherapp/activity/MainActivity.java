package com.example.weatherapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.example.weatherapp.adapters.SevenDaysForecastAdapter;
import com.example.weatherapp.data.SunshinePreferences;
import com.example.weatherapp.models.DailyWeather;
import com.example.weatherapp.utilities.NetworkUtils;
import com.example.weatherapp.utilities.WeatherJsonResponseUtils;

import org.json.JSONException;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements SevenDaysForecastAdapter.ItemClickHandler,
        SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String putDailyData = "dailyData";
    private static final String TAG = "MainActivity";

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).
                unregisterOnSharedPreferenceChangeListener(this);
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
    private void setupSharedPreference(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    /**
     * Display seven days weather in RecyclerView
     */
    public void loadSevenDaysWeather(List<DailyWeather> sevenDayWeatherList) {
        SevenDaysForecastAdapter adapter = new SevenDaysForecastAdapter(sevenDayWeatherList, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        itemsRV.setLayoutManager(layoutManager);
        itemsRV.setAdapter(adapter);
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
     * @param weather is the weather forecast which is click in list
     */
    @Override
    public void onClick(DailyWeather weather) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(putDailyData, weather);
        startActivity(intent);
    }

    /**
     * This function triggered when a value change in shared preference
     * @param sharedPreferences the sharedPreference object
     * @param key is the key which value is updated
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_units_key))){
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
            List<DailyWeather> sevenDayWeather = null;

            try {
                //Current weather from json response
                currentWeatherString = WeatherJsonResponseUtils.getCurrentWeatherString(getApplicationContext(), s);

                //Seven days weather forecast as arrayList
                sevenDayWeather = WeatherJsonResponseUtils.getDailyWeather(getApplicationContext(), s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //loadSevenDaysWeather in RecyclerView
            loadSevenDaysWeather(sevenDayWeather);
            displayTextView.setText(currentWeatherString);
            Toast.makeText(getApplicationContext(), "Data loaded", Toast.LENGTH_SHORT).show();
        }
    }
}