package com.example.mohmurtu.registration.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by mohmurtu on 10/4/2015.
 */
public class NetworkUtility {

    public static boolean isNetworkConnectionAvailable(Context context)
    {
        boolean isNetworkConnectionAvailable = false;

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if(activeNetworkInfo != null)
        {
            isNetworkConnectionAvailable = activeNetworkInfo.getState() == NetworkInfo.State.CONNECTED;
        }
        return isNetworkConnectionAvailable;
    }
}