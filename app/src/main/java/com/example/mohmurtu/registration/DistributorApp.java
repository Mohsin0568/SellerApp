package com.example.mohmurtu.registration;

import android.app.Application;
import android.content.Context;

/**
 * Created by mohmurtu on 11/8/2015.
 */
public class DistributorApp extends Application {

    public static Context context ;

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = this;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
