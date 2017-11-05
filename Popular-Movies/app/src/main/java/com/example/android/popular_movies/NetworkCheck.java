package com.example.android.popular_movies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class NetworkCheck {


    public static boolean isOnline(Context context) {

        ConnectivityManager connectivityManager;
        boolean connected = false;

        connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        connected = networkInfo != null && networkInfo.isConnected();
        return connected;


    }
}
