package com.janedoe.mywalkingapp.Application;

import android.app.Application;
import android.content.Context;

/**
 * Created by janedoe on 2/4/2016.
 */
public class MyApplication extends Application{
    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MyApplication getInstance(){
        return instance;
    }

    public static Context getAppContext(){
        return instance.getApplicationContext();
    }
}
