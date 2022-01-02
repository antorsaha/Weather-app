package com.example.weatherapp.utilities;

import android.content.Context;

import com.example.weatherapp.models.CurrentWeather;
import com.example.weatherapp.models.DailyWeather;
import com.example.weatherapp.models.HourlyWeather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class WeatherJsonResponseUtils {
    //current array from http response
    private static final String CURRENT = "current";
    private static final String HOURLY = "hourly";
    private static final String DAILY = "daily";

    //get date
    private static final String DATE = "dt";

    private static final String SUNRISE = "sunrise";
    private static final String SUNSET = "sunset";
    private static final String TEMPERATURE = "temp";
    private static final String TEMPERATURE_FEEL_LIKE = "feels_like";
    private static final String ATMOSPHERIC_PRESSURE = "pressure";
    private static final String HUMIDITY = "humidity";
    private static final String CLOUDINESS = "clouds";
    private static final String UV_INDEX = "uvi";
    private static final String WIND_SPEED = "wind_speed";
    private static final String WEATHER = "weather";
    private static final String MAIN = "main";
    private static final String DESCRIPTION = "description";
    private static final String MAXIMUM = "max";
    private static final String MINIMUM = "min";
    private static final String MOON_PHASE = "moon_phase";

    public static String getCurrentWeatherString(Context context, String jsonResponseString) throws JSONException {

        JSONObject jsonResponse = new JSONObject(jsonResponseString);
        StringBuilder sb = new StringBuilder();

        if (isConnectionOk(jsonResponse)) {
            JSONObject current = jsonResponse.getJSONObject(CURRENT);

            //current weather object
            CurrentWeather currentWeather = new CurrentWeather();


            sb.append("Date: ").append(current.getDouble(DATE));
            sb.append("\nSunrise: ").append(current.getDouble(SUNRISE));
            sb.append("\nSunset: ").append(current.getInt(SUNSET));
            sb.append("\nTemperature: ").append(current.getDouble(TEMPERATURE));
            sb.append("\nFeels Like: ").append(current.getDouble(TEMPERATURE_FEEL_LIKE));
            sb.append("\nAtmospheric pressure: ").append(current.getDouble(ATMOSPHERIC_PRESSURE));
            sb.append("\nHumidity: ").append(current.getDouble(HUMIDITY));
            sb.append("\nClouds: ").append(current.getInt(CLOUDINESS));
            sb.append("\nUV index: ").append(current.getInt(UV_INDEX));
            sb.append("\nWind Speed: ").append(current.getDouble(WIND_SPEED));
            sb.append("\nWeather: ").append(current.getJSONArray(WEATHER).getJSONObject(0).getString(MAIN));
            sb.append("\nDescription: ").append(current.getJSONArray(WEATHER).getJSONObject(0).getString(DESCRIPTION));
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
    public static CurrentWeather getCurrentWeather(Context context, String jsonResponseString) throws JSONException {
        JSONObject jsonResponse = new JSONObject(jsonResponseString);

        CurrentWeather currentWeather = new CurrentWeather();
        if (isConnectionOk(jsonResponse)) {
            JSONObject current = jsonResponse.getJSONObject(CURRENT);

            //Setting values to current weather
            currentWeather.setDate(current.getDouble(DATE));
            currentWeather.setSunrise(current.getDouble(SUNRISE));
            currentWeather.setSunset(current.getDouble(SUNSET));
            currentWeather.setTemp(current.getDouble(TEMPERATURE));
            currentWeather.setFeelsLike(current.getDouble(TEMPERATURE_FEEL_LIKE));
            currentWeather.setPressure(current.getDouble(ATMOSPHERIC_PRESSURE));
            currentWeather.setHumidity(current.getDouble(HUMIDITY));
            currentWeather.setClouds(current.getDouble(CLOUDINESS));
            currentWeather.setUvIndex(current.getDouble(UV_INDEX));
            currentWeather.setWindSpeed(current.getDouble(WIND_SPEED));
            currentWeather.setWeather(current.getString(WEATHER));
            currentWeather.setWeatherDescription(current.getString(DESCRIPTION));

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
    public static ArrayList<HourlyWeather> getHourlyWeather(Context context, String jsonResponseString) throws JSONException {

        ArrayList<HourlyWeather> hourlyWeatherList = new ArrayList<>();
        JSONObject jsonResponse = new JSONObject(jsonResponseString);

        if (isConnectionOk(jsonResponse)) {
            JSONArray hourly = jsonResponse.getJSONArray(HOURLY);
            for (int i = 0; i < hourly.length(); i++) {
                JSONObject object = hourly.getJSONObject(i);
                HourlyWeather hourlyWeather = new HourlyWeather();

                //Setting values to hourly weather
                hourlyWeather.setDate(object.getDouble(DATE));
                hourlyWeather.setTemp(object.getDouble(TEMPERATURE));
                hourlyWeather.setFeelsLike(object.getDouble(TEMPERATURE_FEEL_LIKE));
                hourlyWeather.setPressure(object.getDouble(ATMOSPHERIC_PRESSURE));
                hourlyWeather.setHumidity(object.getDouble(HUMIDITY));
                hourlyWeather.setClouds(object.getDouble(CLOUDINESS));
                hourlyWeather.setUvIndex(object.getDouble(UV_INDEX));
                hourlyWeather.setWindSpeed(object.getDouble(WIND_SPEED));
                hourlyWeather.setWeather(object.getString(WEATHER));
                hourlyWeather.setWeatherDescription(object.getString(DESCRIPTION));

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
    public static ArrayList<DailyWeather> getDailyWeather(Context context, String jsonResponseString) throws JSONException {

        ArrayList<DailyWeather> dailyWeatherList = new ArrayList<>();
        JSONObject jsonResponse = new JSONObject(jsonResponseString);

        if (isConnectionOk(jsonResponse)) {
            JSONArray daily = jsonResponse.getJSONArray(DAILY);
            for (int i = 0; i < daily.length(); i++) {
                JSONObject object = daily.getJSONObject(i);
                DailyWeather dailyWeather = new DailyWeather();

                //Setting values to hourly weather
                dailyWeather.setDate(object.getDouble(DATE));
                dailyWeather.setTempMin(object.getJSONObject(TEMPERATURE).getDouble(MINIMUM));
                dailyWeather.setTempMax(object.getJSONObject(TEMPERATURE).getDouble(MINIMUM));
                dailyWeather.setSunrise(object.getDouble(SUNRISE));
                dailyWeather.setSunset(object.getDouble(SUNSET));
                dailyWeather.setPressure(object.getDouble(ATMOSPHERIC_PRESSURE));
                dailyWeather.setHumidity(object.getDouble(HUMIDITY));
                dailyWeather.setUvIndex(object.getDouble(UV_INDEX));
                dailyWeather.setWindSpeed(object.getDouble(WIND_SPEED));
                dailyWeather.setWeather(object.getString(WEATHER));
                dailyWeather.setWeatherDescription(object.getString(DESCRIPTION));

                dailyWeatherList.add(dailyWeather);
            }
        }
        return dailyWeatherList;
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
