package com.taobuxiu.driver.utils.helpers;

import android.text.TextUtils;


import com.taobuxiu.driver.utils.StringUtils;
import com.taobuxiu.driver.utils.ToastUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yfchu
 * @date 2016/8/12
 */
public class VerifyHelper {
    /**
     * 验证手机格式
     */
    public static boolean isMobile(String mobiles) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    public static boolean checkMobile(String mobile) {
        if (StringUtils.isEmpty(mobile)) {
            ToastUtils.showInfoTasty("手机号码不能为空");
            return false;
        }

        if (!VerifyHelper.isMobile(mobile)) {
            ToastUtils.showInfoTasty("手机号码格式不正确");
            return false;
        }
        return true;
    }

    public static boolean checkPassword(String password) {
        if (StringUtils.isEmpty(password)) {
            ToastUtils.showInfoTasty("密码不能为空");
            return false;
        }

        if (password.length() < 6 && password.length() > 16) {
            ToastUtils.showInfoTasty("密码长度为6-16位");
            return false;
        }

        return true;
    }

    public static boolean checkMobileAndPassword(String mobile, String password) {
        return checkMobile(mobile) && checkPassword(password);
    }

    public static boolean isNumeric(String str) {
        String regEx = "^-?[0-9]+$";
        Pattern pat = Pattern.compile(regEx);
        Matcher mat = pat.matcher(str);
        if (mat.find()) {
            return true;
        } else {
            return false;
        }
    }

}
