package com.example.weatherapp.data;

import android.content.Context;

public class SunshinePreferences {
    // Weather location latitude in index 0 and longitude in index 1;
    private static final double[] DEFAULT_WEATHER_LOCATION = {22.82, 89.55};

    /**
     * This helper method return the weather location
     * As latitude and longitude
     * @param context takes context
     * @return weather location as latitude and longitude
     */
    public static double[] getPreferredWeatherLocation(Context context){
        return DEFAULT_WEATHER_LOCATION;
    }
}
