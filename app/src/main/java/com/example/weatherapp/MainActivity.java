package com.example.weatherapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.weatherapp.data.SunshinePreferences;
import com.example.weatherapp.utilities.NetworkUtils;
import com.example.weatherapp.utilities.WeatherJsonResponseUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    TextView displayTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Display network call result...
        displayTextView = findViewById(R.id.tv_display);

        loadData();
    }

    private void loadData() {
        double[] location = SunshinePreferences.getPreferredWeatherCoordinates(this);
        new FetchWeatherTask().execute(location);
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

    class FetchWeatherTask extends AsyncTask<double[], Void, String> {

        @Override
        protected String doInBackground(double[]... doubles) {
            if (doubles.length == 0)
                return null;

            double[] location = doubles[0];
            URL requestUrl = NetworkUtils.buildUrl(location);

            try {
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(requestUrl);

                return WeatherJsonResponseUtils.getCurrentWeatherString(MainActivity.this, jsonResponse);

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
            displayTextView.setText(s);
            Toast.makeText(getApplicationContext(), "Data loaded", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onPostExecute: data response " + s);
        }
    }
}