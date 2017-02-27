package com.taobuxiu.driver.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * @author yfchu
 * @date 2016/3/17
 */
public class ThreadUtils {

    private static Handler mainHandler;

    public static void runOnUiThread(Runnable runnable) {
        internalRunOnUiThread(runnable, 0);
    }

    public static void runOnUiThread(Runnable runnable, long delayMillis) {
        internalRunOnUiThread(runnable, delayMillis);
    }

    public static Handler getMainHandler() {
        if (mainHandler == null) {
            mainHandler = new Handler(Looper.getMainLooper());
        }
        return mainHandler;
    }

    private static void internalRunOnUiThread(Runnable runnable, long delayMillis) {
        getMainHandler();
        mainHandler.postDelayed(runnable, delayMillis);
    }

}
