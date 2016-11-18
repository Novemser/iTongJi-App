package com.example.aitongji.Utils;

import android.app.Application;
import android.content.Context;

import com.example.aitongji.Utils.GPA.StudentGPA;

/**
 * Created by Novemser on 4/1/2016.
 */
public class AndroidResource extends Application {
    private static boolean isRefreshing;
    private static Context context;
    public static StudentGPA studentGPA;
    public static DataBundle dataBundle;

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
        AndroidResource.isRefreshing = isRefreshing;
    }
}
