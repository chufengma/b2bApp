package com.taobuxiu.driver.utils;

import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.taobuxiu.driver.MainApplication;

/**
 * Created by lds on 2015/8/26.
 */
public class ViewUtils {

    public static DisplayMetrics getDisplayMetrics() {
        return MainApplication.getContext().getResources().getDisplayMetrics();
    }

    public static int getScreenWidth() {
        return getDisplayMetrics().widthPixels;
    }

    public static void setVisibility(View view, int visibility) {
        if (view != null && view.getVisibility() != visibility) {
            view.setVisibility(visibility);
        }
    }

    public static int dipToPixels(DisplayMetrics displayMetrics, float dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, displayMetrics);
    }

    public static int dipToPixels(float dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getDisplayMetrics());
    }

}
