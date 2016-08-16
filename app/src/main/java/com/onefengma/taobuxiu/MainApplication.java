package com.onefengma.taobuxiu;

import android.app.Application;

import com.onefengma.taobuxiu.manager.PushManager;
import com.onefengma.taobuxiu.views.core.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chufengma on 16/8/7.
 */
public class MainApplication extends Application {

    private static MainApplication instance;
    private List<BaseActivity> activities = new ArrayList<>();
    private boolean isClearAll = false;
    private BaseActivity currentActivity;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        // push service
        PushManager.instance().initPushService();
    }


    public static MainApplication getContext() {
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

}
