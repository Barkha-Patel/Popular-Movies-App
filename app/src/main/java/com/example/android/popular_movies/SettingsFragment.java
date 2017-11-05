package com.example.android.popular_movies;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;


public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



    }
    }