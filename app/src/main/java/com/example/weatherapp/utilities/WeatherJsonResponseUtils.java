package com.example.weatherapp.utilities;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

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
