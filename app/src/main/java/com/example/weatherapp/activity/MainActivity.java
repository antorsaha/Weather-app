package com.example.weatherapp.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity implements SevenDaysForecastAdapter.ItemClickHandler {
    private static final String TAG = "MainActivity";
    public static final String putDailyData = "dailyData";

    private TextView displayTextView;
    private RecyclerView itemsRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayTextView = findViewById(R.id.tv_today_forecast);
        itemsRV = findViewById(R.id.rv_forecast);

        loadData();
    }

    private void loadData() {
        double[] location = SunshinePreferences.getPreferredWeatherCoordinates(this);
        new FetchWeatherTask().execute(location);
    }


    public void loadSevenDaysWeather(List<DailyWeather> sevenDayWeatherList) {
        SevenDaysForecastAdapter adapter = new SevenDaysForecastAdapter(sevenDayWeatherList, this);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        itemsRV.setLayoutManager(layoutManager);
        itemsRV.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.menu.forecust_menu)
            loadData();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.forecust_menu, menu);
        return true;
    }

    @Override
    public void onClick(DailyWeather weather) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(putDailyData, /*(new Gson()).toJson(weather)*/ weather);
        startActivity(intent);
    }

    class FetchWeatherTask extends AsyncTask<double[], Void, String> {

        @Override
        protected String doInBackground(double[]... doubles) {
            if (doubles.length == 0)
                return null;

            double[] location = doubles[0];
            URL requestUrl = NetworkUtils.buildUrl(location);

            try {
                return NetworkUtils.getResponseFromHttpUrl(requestUrl);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

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
                currentWeatherString = WeatherJsonResponseUtils.getCurrentWeatherString(getApplicationContext(), s);
                sevenDayWeather = WeatherJsonResponseUtils.getDailyWeather(getApplicationContext(), s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            loadSevenDaysWeather(sevenDayWeather);
            displayTextView.setText(currentWeatherString);
            Toast.makeText(getApplicationContext(), "Data loaded", Toast.LENGTH_SHORT).show();
        }
    }
}