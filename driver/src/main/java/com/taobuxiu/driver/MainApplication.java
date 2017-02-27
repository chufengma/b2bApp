package com.taobuxiu.driver;

import android.app.Application;
import android.content.Context;

/**
 * Created by chufengma on 16/8/7.
 */
public class MainApplication extends Application {

    private static MainApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }
}
