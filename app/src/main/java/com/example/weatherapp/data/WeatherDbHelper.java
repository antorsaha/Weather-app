package com.example.weatherapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.weatherapp.data.WeatherContract.WeatherEntity;

/**
 * Local database
 */
public class WeatherDbHelper extends SQLiteOpenHelper {
    //Database name
    public static final String DATABASE_NAME = "weather.db";

    //Database version
    private static final int DATABASE_VERSION = 2;

    public WeatherDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //SQL query for creating daily weather table
        final String SQL_CREATE_DAILY_WEATHER =
                "CREATE TABLE " + WeatherEntity.TABLE_DAILY_WEATHER + "(" +
                        WeatherEntity._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        WeatherEntity.DATE + " INTEGER NOT NULL, " +
                        WeatherEntity.SUNRISE + " INTEGER NOT NULL, " +
                        WeatherEntity.SUNSET + " INTEGER NOT NULL, " +
                        WeatherEntity.TEMP_MAX + " REAL NOT NULL, " +
                        WeatherEntity.TEMP_MIN + " REAL NOT NULL, " +
                        WeatherEntity.ATMOSPHERIC_PRESSURE + " REAL NOT NULL, " +
                        WeatherEntity.HUMIDITY + " REAL NOT NULL, " +
                        WeatherEntity.CLOUDINESS + " REAL NOT NULL, " +
                        WeatherEntity.UV_INDEX + " REAL NOT NULL, " +
                        WeatherEntity.WIND_SPEED + " REAL NOT NULL, " +
                        WeatherEntity.WEATHER + " TEXT NOT NULL, " +
                        WeatherEntity.DESCRIPTION + " TEXT NOT NULL," +
                        WeatherEntity.WEATHER_ICON + " TEXT NOT NULL," +

                        " UNIQUE (" + WeatherEntity.DATE + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_DAILY_WEATHER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + WeatherEntity.TABLE_DAILY_WEATHER);
        onCreate(db);
    }
}
