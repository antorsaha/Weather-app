package com.example.weatherapp.utilities;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.weatherapp.data.WeatherContract;
import com.example.weatherapp.data.WeatherContract.WeatherEntity;
import com.example.weatherapp.models.CurrentWeather;
import com.example.weatherapp.models.DailyWeather;
import com.example.weatherapp.models.HourlyWeather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class WeatherJsonResponseUtils {
    private static final String TAG = "abc";
    //current array from http response
    private static final String CURRENT = "current";
    private static final String HOURLY = "hourly";
    private static final String DAILY = "daily";

    public static String getCurrentWeatherString(Context context, String jsonResponseString) throws JSONException {

        JSONObject jsonResponse = new JSONObject(jsonResponseString);
        StringBuilder sb = new StringBuilder();

        if (isConnectionOk(jsonResponse)) {
            JSONObject current = jsonResponse.getJSONObject(CURRENT);

            long dateInMills = current.getLong(WeatherEntity.DATE);
            String dateString = SunshineDateUtils.
                    getDateFromUTCTimestamp(dateInMills);

            sb.append("Date: ").append(dateString);
            sb.append("\nSunrise: ").append(SunshineDateUtils.
                    getTimeFromUTCTimestamp(current.getLong(WeatherEntity.SUNRISE)));
            sb.append("\nSunset: ").append(SunshineDateUtils.
                    getTimeFromUTCTimestamp(current.getLong(WeatherEntity.SUNSET)));
            sb.append("\nTemperature: ").append(current.getDouble(WeatherEntity.TEMPERATURE));
            sb.append("\nFeels Like: ").append(current.getDouble(WeatherEntity.TEMPERATURE_FEEL_LIKE));
            sb.append("\nAtmospheric pressure: ").append(current.getDouble(WeatherEntity.ATMOSPHERIC_PRESSURE));
            sb.append("\nHumidity: ").append(current.getDouble(WeatherEntity.HUMIDITY));
            sb.append("\nClouds: ").append(current.getInt(WeatherEntity.CLOUDINESS));
            sb.append("\nUV index: ").append(current.getInt(WeatherEntity.UV_INDEX));
            sb.append("\nWind Speed: ").append(current.getDouble(WeatherEntity.WIND_SPEED));
            sb.append("\nWeather: ").append(current.getJSONArray(WeatherEntity.WEATHER).
                    getJSONObject(0).getString(WeatherEntity.MAIN));
            sb.append("\nDescription: ").append(current.getJSONArray(WeatherEntity.WEATHER).
                    getJSONObject(0).getString(WeatherEntity.DESCRIPTION));
        }
        return sb.toString();
    }

    /**
     * retrieving Current weather data
     *
     * @param context            application context
     * @param jsonResponseString is response from api call as string
     * @return hourly weather data as CurrentWeather object
     * @throws JSONException because of json parsing
     */
    public static CurrentWeather getCurrentWeather(Context context, String jsonResponseString)
            throws JSONException {
        JSONObject jsonResponse = new JSONObject(jsonResponseString);

        CurrentWeather currentWeather = new CurrentWeather();
        if (isConnectionOk(jsonResponse)) {
            JSONObject current = jsonResponse.getJSONObject(CURRENT);

            //Setting values to current weather
            currentWeather.setDate(current.getLong(WeatherEntity.DATE));
            currentWeather.setSunrise(current.getDouble(WeatherEntity.SUNRISE));
            currentWeather.setSunset(current.getDouble(WeatherEntity.SUNSET));
            currentWeather.setTemp(current.getDouble(WeatherEntity.TEMPERATURE));
            currentWeather.setFeelsLike(current.getDouble(WeatherEntity.TEMPERATURE_FEEL_LIKE));
            currentWeather.setPressure(current.getDouble(WeatherEntity.ATMOSPHERIC_PRESSURE));
            currentWeather.setHumidity(current.getDouble(WeatherEntity.HUMIDITY));
            currentWeather.setClouds(current.getDouble(WeatherEntity.CLOUDINESS));
            currentWeather.setUvIndex(current.getDouble(WeatherEntity.UV_INDEX));
            currentWeather.setWindSpeed(current.getDouble(WeatherEntity.WIND_SPEED));
            currentWeather.setWeather(current.getString(WeatherEntity.WEATHER));
            currentWeather.setWeatherDescription(current.getString(WeatherEntity.DESCRIPTION));

        }
        return currentWeather;
    }

    /**
     * retrieving hourly weather data
     *
     * @param context            application context
     * @param jsonResponseString is response from api call as string
     * @return hourly weather data as hourlyWeather arraylist
     * @throws JSONException because of json parsing
     */
    public static ArrayList<HourlyWeather> getHourlyWeather(Context context, String jsonResponseString)
            throws JSONException {

        ArrayList<HourlyWeather> hourlyWeatherList = new ArrayList<>();
        JSONObject jsonResponse = new JSONObject(jsonResponseString);

        if (isConnectionOk(jsonResponse)) {
            JSONArray hourly = jsonResponse.getJSONArray(HOURLY);
            for (int i = 0; i < hourly.length(); i++) {
                JSONObject object = hourly.getJSONObject(i);
                HourlyWeather hourlyWeather = new HourlyWeather();

                //Setting values to hourly weather
                hourlyWeather.setDate(object.getLong(WeatherEntity.DATE));
                hourlyWeather.setTemp(object.getDouble(WeatherEntity.TEMPERATURE));
                hourlyWeather.setFeelsLike(object.getDouble(WeatherEntity.TEMPERATURE_FEEL_LIKE));
                hourlyWeather.setPressure(object.getDouble(WeatherEntity.ATMOSPHERIC_PRESSURE));
                hourlyWeather.setHumidity(object.getDouble(WeatherEntity.HUMIDITY));
                hourlyWeather.setClouds(object.getDouble(WeatherEntity.CLOUDINESS));
                hourlyWeather.setUvIndex(object.getDouble(WeatherEntity.UV_INDEX));
                hourlyWeather.setWindSpeed(object.getDouble(WeatherEntity.WIND_SPEED));
                hourlyWeather.setWeather(object.getString(WeatherEntity.WEATHER));
                hourlyWeather.setWeatherDescription(object.getString(WeatherEntity.DESCRIPTION));

                hourlyWeatherList.add(hourlyWeather);
            }
        }
        return hourlyWeatherList;
    }

    /**
     * retrieving daly weather data
     *
     * @param context            application context
     * @param jsonResponseString is response from api call as string
     * @return hourly weather data as dailyWeather arraylist
     * @throws JSONException because of json parsing
     */
    @SuppressLint("LongLogTag")
    public static List<DailyWeather> getDailyWeather(Context context, String jsonResponseString)
            throws JSONException {

        ArrayList<DailyWeather> dailyWeatherList = new ArrayList<>();
        JSONObject jsonResponse = new JSONObject(jsonResponseString);

        StringBuilder stringBuilder = new StringBuilder();

        if (isConnectionOk(jsonResponse)) {
            JSONArray daily = jsonResponse.getJSONArray(DAILY);

            int i;
            for (i = 0; i < daily.length(); i++) {
                JSONObject object = daily.getJSONObject(i);
                DailyWeather dailyWeather = new DailyWeather();

                //Setting values to daily weather
                dailyWeather.setDate(object.getLong(WeatherEntity.DATE));
                dailyWeather.setTempMin(object.getJSONObject(WeatherEntity.TEMPERATURE)
                        .getDouble(WeatherEntity.MINIMUM));
                dailyWeather.setTempMax(object.getJSONObject(WeatherEntity.TEMPERATURE)
                        .getDouble(WeatherEntity.MAXIMUM));
                dailyWeather.setSunrise(object.getDouble(WeatherEntity.SUNRISE));
                dailyWeather.setSunset(object.getDouble(WeatherEntity.SUNSET));
                dailyWeather.setPressure(object.getDouble(WeatherEntity.ATMOSPHERIC_PRESSURE));
                dailyWeather.setHumidity(object.getDouble(WeatherEntity.HUMIDITY));
                dailyWeather.setUvIndex(object.getDouble(WeatherEntity.UV_INDEX));
                dailyWeather.setWindSpeed(object.getDouble(WeatherEntity.WIND_SPEED));
                dailyWeather.setWeather(object.getString(WeatherEntity.WEATHER));
                dailyWeather.setWeatherDescription(object.getJSONArray("weather")
                        .getJSONObject(0).getString(WeatherEntity.DESCRIPTION));

                dailyWeatherList.add(dailyWeather);

                stringBuilder.append(object.getDouble(WeatherEntity.DATE)).append("\t")
                        .append(object.getJSONObject(WeatherEntity.TEMPERATURE).getDouble(WeatherEntity.MINIMUM)).append("\t")
                        .append(object.getJSONObject(WeatherEntity.TEMPERATURE).getDouble(WeatherEntity.MAXIMUM)).append("\t")
                        .append(object.getDouble(WeatherEntity.UV_INDEX));
            }
            Log.d(TAG, "getDailyWeather: i = " + i);
        } else
            Log.d(TAG, "getDailyWeather: Connection is not okay");
        Log.d(TAG, "getDailyWeather: " + dailyWeatherList.size());
        return dailyWeatherList;

    }

    @SuppressLint("LongLogTag")
    public static void loadDailyWeather(Context context, String jsonResponseString)
            throws JSONException {

        List<ContentValues> dailyWeatherList = new ArrayList<>();
        JSONObject jsonResponse = new JSONObject(jsonResponseString);

        if (isConnectionOk(jsonResponse)) {
            JSONArray daily = jsonResponse.getJSONArray(DAILY);
            Log.d(TAG, "loadDailyWeather: connection ok");

            int i;
            for (i = 0; i < daily.length(); i++) {
                JSONObject object = daily.getJSONObject(i);
                ContentValues dailyWeather = new ContentValues();

                dailyWeather.put(WeatherEntity.DATE, object.getLong(WeatherEntity.DATE));

                dailyWeather.put(WeatherEntity.TEMP_MIN,
                        object.getJSONObject(WeatherEntity.TEMPERATURE)
                                        .getDouble(WeatherEntity.MINIMUM));

                dailyWeather.put(WeatherEntity.TEMP_MAX,
                        object.getJSONObject(WeatherEntity.TEMPERATURE)
                                .getDouble(WeatherEntity.MAXIMUM));
                dailyWeather.put(WeatherEntity.SUNRISE, object.getDouble(WeatherEntity.SUNRISE));
                dailyWeather.put(WeatherEntity.SUNSET, object.getDouble(WeatherEntity.SUNSET));
                dailyWeather.put(WeatherEntity.ATMOSPHERIC_PRESSURE,
                        object.getDouble(WeatherEntity.ATMOSPHERIC_PRESSURE));
                dailyWeather.put(WeatherEntity.HUMIDITY, object.getDouble(WeatherEntity.HUMIDITY));
                dailyWeather.put(WeatherEntity.UV_INDEX, object.getDouble(WeatherEntity.UV_INDEX));
                dailyWeather.put(WeatherEntity.CLOUDINESS,
                        object.getDouble(WeatherEntity.CLOUDINESS));
                dailyWeather.put(WeatherEntity.WIND_SPEED,
                        object.getDouble(WeatherEntity.WIND_SPEED));
                dailyWeather.put(WeatherEntity.WEATHER,
                        object.getString(WeatherEntity.WEATHER));
                dailyWeather.put(WeatherEntity.DESCRIPTION, object.getJSONArray("weather")
                        .getJSONObject(0).getString(WeatherEntity.DESCRIPTION));
                dailyWeather.put(WeatherEntity.WEATHER_ICON, object.getJSONArray("weather")
                        .getJSONObject(0).getString(WeatherEntity.WEATHER_ICON));

                dailyWeatherList.add(dailyWeather);
            }
            long normalizedUtcNow = SunshineDateUtils.normalizeDate(System.currentTimeMillis());
            String selection = WeatherContract.WeatherEntity.DATE + " < " + (normalizedUtcNow / 1000);
            context.getContentResolver().delete(WeatherEntity.CONTENT_URI, selection, null);

            context.getContentResolver().bulkInsert(WeatherEntity.CONTENT_URI,
                    dailyWeatherList.toArray(new ContentValues[7]));


        } else {
            Log.d(TAG, "getDailyWeather: Connection is not okay");
            //Toast.makeText(context, "LDW connection problem", Toast.LENGTH_SHORT).show();
        }
        //Toast.makeText(context, "LDW ", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "LDW: " + dailyWeatherList.size());

    }

    /**
     * This helper method check that the connection is ok or not
     *
     * @param jsonResponse response from http request
     * @return ture if all ok
     * @throws JSONException can throw exception
     */
    private static boolean isConnectionOk(JSONObject jsonResponse) throws JSONException {
        final String RESPONSE_CODE = "cod";

        //check the connection is ok or not
        if (jsonResponse.has(RESPONSE_CODE)) {
            int responseCode = jsonResponse.getInt(RESPONSE_CODE);

            switch (responseCode) {
                case HttpURLConnection.HTTP_OK:
                    return true;
                case HttpURLConnection.HTTP_NOT_FOUND:
                default:
                    return false;
            }
        }
        return true;
    }
}
