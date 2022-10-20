package com.example.weatherapp.fragment;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.weatherapp.R;

public class SettingFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_setting);
    }
}
