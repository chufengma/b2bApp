package com.onefengma.taobuxiu.manager.helpers;

import android.content.Intent;
import android.net.Uri;

import com.onefengma.taobuxiu.MainApplication;
import com.onefengma.taobuxiu.utils.ToastUtils;

/**
 * Created by chufengma on 16/8/20.
 */
public class SystemHelper {

    public static void call(String mobile) {
//        if (VerifyHelper.checkMobile(mobile)) {
//            ToastUtils.showErrorTasty("该手机号码不正确");
//        }
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + mobile);
        intent.setData(data);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MainApplication.getContext().startActivity(intent);
    }

    public static void open(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse(url);
        intent.setData(data);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MainApplication.getContext().startActivity(intent);
    }

}
