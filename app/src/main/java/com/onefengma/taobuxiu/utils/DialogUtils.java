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

    public static void showAlertDialog(Context context, String title, DialogInterface.OnClickListener confirmListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", confirmListener).create();
        alertDialog.show();
    }

}
