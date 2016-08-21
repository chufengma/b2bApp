package com.onefengma.taobuxiu.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.onefengma.taobuxiu.MainApplication;

/**
 * @author yfchu
 * @date 2016/8/8
 */
public class DialogUtils {

    public static void showAlertDialog(Context context, String message, DialogInterface.OnClickListener confirmListener) {
        showAlertDialog(context, "提示", message, confirmListener);
    }

    public static void showAlertDialog(Context context, String title, String message, DialogInterface.OnClickListener confirmListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", confirmListener).create();
        alertDialog.show();
    }
}
