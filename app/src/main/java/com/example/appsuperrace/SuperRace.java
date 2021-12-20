package com.example.appsuperrace;

import android.app.Application;
import android.content.Context;

// To get context for static class
public class SuperRace extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        SuperRace.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return SuperRace.context;
    }
}
