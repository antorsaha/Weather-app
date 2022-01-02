package com.example.weatherapp.data;

import android.content.Context;

public class SunshinePreferences {
    // Weather location latitude in index 0 and longitude in index 1;
    private static final double[] DEFAULT_WEATHER_COORDINATES = {22.8456, 89.5403};
    private static final String DEFAULT_WEATHER_LOCATION = "Khulna";

    /**
     * This helper method return the weather location
     * As latitude and longitude
     *
     * @param context takes context
     * @return weather location as latitude and longitude
     */
    public static double[] getPreferredWeatherCoordinates(Context context) {
        return getDefaultWeatherCoordinates();
    }

    /**
     * This helper method return the weather location
     *
     * @param context takes context
     * @return weather location as String
     */
    public static String getPreferredWeatherLocation(Context context) {
        return getDefaultWeatherLocation();
    }

    /**
     * Accessor method for default weather location
     *
     * @return Default weather location.
     */
    public static String getDefaultWeatherLocation() {
        return DEFAULT_WEATHER_LOCATION;
    }

    /**
     * Accessor method for default weather coordinates
     *
     * @return default weather coordinates as latitude and longitude
     */
    public static double[] getDefaultWeatherCoordinates() {
        return DEFAULT_WEATHER_COORDINATES;
    }
}
