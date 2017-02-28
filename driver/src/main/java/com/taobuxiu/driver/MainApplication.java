package com.taobuxiu.driver;

import android.app.Application;
import android.content.Context;

import com.taobuxiu.driver.views.core.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chufengma on 16/8/7.
 */
public class MainApplication extends Application {

    private List<BaseActivity> activities = new ArrayList<>();
    private BaseActivity currentActivity;
    private boolean isClearAll = false;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    private static MainApplication instance;

    public static MainApplication ins() {
        if (instance == null) {
            instance = new MainApplication();
        }
        return instance;
    }


    public void addActivity(BaseActivity activity) {
        activities.add(activity);
    }

    public void setCurrentActivity(BaseActivity activity) {
        this.currentActivity = activity;
    }

    public BaseActivity getCurrentActivity() {
        return currentActivity;
    }

    public void removeActivity(BaseActivity activity) {
        if (!isClearAll) {
            activities.remove(activity);
        }
    }

    public void finishActivities() {
        isClearAll = true;
        for(BaseActivity activity : activities) {
            if (activity != null) {
                activity.finish();
            }
        }
        activities.clear();
        isClearAll = false;
    }

    public static Context getContext() {
        return instance;
    }
}
