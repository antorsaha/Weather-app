package com.example.weatherapp.data;

import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.weatherapp.utilities.SunshineDateUtils;

public class WeatherContract {
    public static final String CONTENT_AUTHORITY = "com.example.weatherapp";
    public static final String PATH_WEATHER = "weather";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    private static final String TAG = "WeatherContract";

    public static final class WeatherEntity implements BaseColumns {
        //Table names
        public static final String TABLE_DAILY_WEATHER = "daily_Weather";
        public static final String TABLE_HOURLY_WEATHER = "hourly_weather";
        public static final String TABLE_CURRENT_WEATHER = "current_weather";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_WEATHER)
                .build();

        //Colum names
        public static final String DATE = "dt";
        public static final String SUNRISE = "sunrise";
        public static final String SUNSET = "sunset";
        public static final String TEMPERATURE = "temp";
        public static final String ATMOSPHERIC_PRESSURE = "pressure";
        public static final String HUMIDITY = "humidity";
        public static final String CLOUDINESS = "clouds";
        public static final String UV_INDEX = "uvi";
        public static final String WIND_SPEED = "wind_speed";
        public static final String WEATHER = "weather";
        public static final String MAIN = "main";
        public static final String DESCRIPTION = "description";
        public static final String MAXIMUM = "max";
        public static final String MINIMUM = "min";
        public static final String TEMP_MAX = TEMPERATURE + MAXIMUM;
        public static final String TEMP_MIN = TEMPERATURE + MINIMUM;
        public static final String  WEATHER_ICON = "icon";
        public static final String TEMPERATURE_FEEL_LIKE = "feels_like";

        /**
         * give selection for today and onwards
         * @return selection as string
         */
        public static String getSqlSelectForTodayOnwards() {
            long normalizedUtcNow = SunshineDateUtils.normalizeDate(System.currentTimeMillis());
            Log.d(TAG, "getSqlSelectForTodayOnwards: current time " + normalizedUtcNow);
            return WeatherContract.WeatherEntity.DATE + " >= " + (normalizedUtcNow / 1000);

        }

        /**
         * Create uri for a date
         *
         * @param date in milliseconds
         * @return uri for details of specific date
         */
        public static Uri buildWeatherUriWithDate(long date) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(date))
                    .build();
        }
    }
}
