package com.example.weatherapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weatherapp.data.SunshinePreferences;
import com.example.weatherapp.utilities.NetworkUtils;
import com.example.weatherapp.utilities.WeatherJsonResponseUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView displayTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Display network call result...
        displayTextView = findViewById(R.id.tv_display);
    }

    private void loadData() {
        double[] location = SunshinePreferences.getPreferredWeatherLocation(this);
        FetchWeatherTask task = new FetchWeatherTask().execute(location);
    }

    class FetchWeatherTask extends AsyncTask<Double[], Void, String> {

        @Override
        protected String doInBackground(Double[]... doubles) {
            if (doubles.length == 0)
                return null;

            URL requestUrl = NetworkUtils.buildUrl(doubles[0]);

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
            if (s == null){
                displayTextView.setText("null");
            }
            displayTextView.setText(s);
        }
    }
}