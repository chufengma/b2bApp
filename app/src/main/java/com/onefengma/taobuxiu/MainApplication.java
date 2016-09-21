package com.onefengma.taobuxiu;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.onefengma.taobuxiu.manager.PushManager;
import com.onefengma.taobuxiu.model.Constant;
import com.onefengma.taobuxiu.utils.SPHelper;
import com.onefengma.taobuxiu.utils.StringUtils;
import com.onefengma.taobuxiu.views.SplashActivity;
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

    public static boolean FEGNMA_FALG = true;

    public static boolean IS_SALES_APP = true;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        // push service
        PushManager.instance().initPushService();
        PushManager.instance().setCurrentUserAccount();

        ImageLoaderConfiguration configuration = (new ImageLoaderConfiguration.Builder(this).defaultDisplayImageOptions(new DisplayImageOptions.Builder().showImageOnFail(R.drawable.ic_detault_icon).build())).build();
        ImageLoader.getInstance().init(configuration);

        initFlags();

        IS_SALES_APP = SPHelper.top().sp().getBoolean(Constant.StorageKeys.SETTING_FLAG_SALES, FEGNMA_FALG);
    }

    private void initFlags() {
        try {
            ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            String open = applicationInfo.metaData.getString("SALES_FLAG");
            if (StringUtils.equalsIgnoreCase("open", open)) {
                FEGNMA_FALG = true;
            } else if (StringUtils.equalsIgnoreCase("close", open)) {
                FEGNMA_FALG = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void switchApp(BaseActivity context) {
        IS_SALES_APP = !IS_SALES_APP;
        SPHelper.top().sp().edit().putBoolean(Constant.StorageKeys.SETTING_FLAG_SALES, IS_SALES_APP).commit();
        finishActivities();
        SplashActivity.start(context);
    }

    public static MainApplication getContext() {
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

    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

}
