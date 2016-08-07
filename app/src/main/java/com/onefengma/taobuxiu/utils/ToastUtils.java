package com.onefengma.taobuxiu.utils;

import android.graphics.Color;
import android.support.annotation.StringRes;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.onefengma.taobuxiu.MainApplication;
import com.onefengma.taobuxiu.R;

public class ToastUtils {

    private static Toast applicationToast;

    private static Toast show(Toast toast, String msg, int duration) {
        if (toast != null) {
            ((TextView) toast.getView()).setText(msg);
        } else {
            toast = new Toast(MainApplication.getContext());
            TextView tv = new TextView(MainApplication.getContext());
            tv.setBackgroundResource(R.drawable.notice_dialog_bg);
            tv.setGravity(Gravity.CENTER_VERTICAL);

            tv.setTextColor(Color.WHITE);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            tv.setText(msg);

            toast.setView(tv);
            toast.setGravity(Gravity.CENTER, 0, 0);
        }
        toast.setDuration(duration);
        toast.show();

        return toast;
    }

    public static void show(@StringRes int msgRes) {
        show(MainApplication.getContext().getString(msgRes), Toast.LENGTH_LONG);
    }

    public static void show(String msg) {
        applicationToast = show(applicationToast, msg, Toast.LENGTH_LONG);
    }

    /**
     * @see {@link ToastUtils#show(String, int)}
     */
    public static void show(@StringRes int msgRes, int duration) {
        show(MainApplication.getContext().getString(msgRes), duration);
    }

    /**
     * Show a piece of Toast if you want to keep the toast outside of any activity's lifecycle.
     * In other words, one toast may keep showing even if the source activity was paused or even destroyed.
     */
    public static void show(String msg, int duration) {
        applicationToast = show(applicationToast, msg, duration);
    }
}