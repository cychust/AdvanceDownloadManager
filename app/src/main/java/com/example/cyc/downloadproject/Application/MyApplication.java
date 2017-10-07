package com.example.cyc.downloadproject.Application;

import android.app.Application;
import android.content.Context;

/**
 * Created by cyc on 17-10-7.
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
    }
    public static Context getContext(){
        return context;
    }
}
