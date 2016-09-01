package com.onefengma.taobuxiu.manager;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import com.onefengma.taobuxiu.BuildConfig;
import com.onefengma.taobuxiu.MainApplication;
import com.onefengma.taobuxiu.model.Constant;
import com.onefengma.taobuxiu.model.entities.UserProfile;
import com.onefengma.taobuxiu.utils.SPHelper;
import com.onefengma.taobuxiu.utils.StringUtils;
import com.onefengma.taobuxiu.views.sales.SalesManDetail;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

/**
 * Created by chufengma on 16/8/13.
 */
public class PushManager {

    public static final String APP_ID = "2882303761517500719";
    public static final String APP_KEY = "5321750018719";
    public static final String TAG = "com.onefengma.taobuxiu";

    public static final String DEV = "DEV";
    public static final String PRO = "PRO";

    private static PushManager instance;

    public static PushManager instance() {
        if (instance == null) {
            instance = new PushManager();
        }
        return instance;
    }

    public void initPushService() {
        //初始化push推送服务
        if(shouldInit()) {
            MiPushClient.registerPush(MainApplication.getContext(), APP_ID, APP_KEY);
        }
        //打开Log
        LoggerInterface newLogger = new LoggerInterface() {

            @Override
            public void setTag(String tag) {
                // ignore
            }

            @Override
            public void log(String content, Throwable t) {
                Log.d(TAG, content, t);
            }

            @Override
            public void log(String content) {
                Log.d(TAG, content);
            }
        };
        Logger.setLogger(MainApplication.getContext(), newLogger);
    }

    public void setCurrentUserAccount() {
        String userId = "";
        if (MainApplication.IS_SALES_APP) {
            SalesManDetail salesManDetail = SPHelper.common().get(Constant.StorageKeys.SALES_PROFILE, SalesManDetail.class);
            if (salesManDetail != null) {
                userId = salesManDetail.id + "";
            }
        } else {
            UserProfile userProfile = SPHelper.common().get(Constant.StorageKeys.USER_PROFILE, UserProfile.class);
            if (userProfile != null) {
                userId = userProfile.userId;
            }
        }
        if (!StringUtils.isEmpty(userId)) {
            String accountId = (BuildConfig.DEBUG ? DEV : PRO) + "_" + userId;
            MiPushClient.setUserAccount(MainApplication.getContext(), accountId, null);
        }
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) MainApplication.getContext().getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = MainApplication.getContext().getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
