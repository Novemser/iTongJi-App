package com.example.aitongji.Utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by Novemser on 4/1/2016.
 */
public class Global extends Application {
    private static boolean isRefreshing;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        isRefreshing = false;
    }

    public static Context getContext() {
        return context;
    }

    public static boolean isRefreshing() {
        return isRefreshing;
    }

    public static void setIsRefreshing(boolean isRefreshing) {
        Global.isRefreshing = isRefreshing;
    }
}
