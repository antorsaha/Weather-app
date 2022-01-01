package com.example.weatherapp.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    /**
     * Query parameter
     */
    final static String QUERY_PRAM = "?";
    final static String LAT_PRAM = "lat";
    final static String LOT_PRAM = "lon";
    final static String APP_ID_PRAM = "appid";
    final static String UNITS_PRAM = "units";
    final static String EXCLUDE_PRAM = "exclude";
    final static String FORMAT_PRAM = "format";
    private static final String TAG = "NetworkUtils";
    //api base url
    private static final String WEATHER_FORECAST_BASE_URL =
            "https://api.openweathermap.org/data/2.5/onecall?";
    // values for api call
    private static final String FORMAT = "json";
    private static final String APP_ID = "a242f3acd8adac5febdb6ee2df3bb784";
    private static final String UNITS = "metric";
    private static final String EXCLUDE = "minutely";

    /**
     * build api query url
     *
     * @param location gives lat at index 0 and lot at index 1...
     * @return query url
     */
    public static URL buildUrl(double[] location) {
        Uri buildUri = Uri.parse(WEATHER_FORECAST_BASE_URL).buildUpon()
                .appendQueryParameter(LAT_PRAM, location[0] + "")
                .appendQueryParameter(LOT_PRAM, location[1] + "")
                .appendQueryParameter(APP_ID_PRAM, APP_ID)
                .appendQueryParameter(UNITS_PRAM, UNITS)
                .appendQueryParameter(FORMAT_PRAM, FORMAT)
                .appendQueryParameter(EXCLUDE_PRAM, EXCLUDE)
                .build();

        URL url = null;
        try {
            url = new URL(buildUri.toString());
            Log.d(TAG, "buildUrl: url build successful\n" + url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method return entire result from HTTP response
     *
     * @param url the url to fetch url response
     * @return HTTP response in a string
     * @throws IOException Related to network and inputStream
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
