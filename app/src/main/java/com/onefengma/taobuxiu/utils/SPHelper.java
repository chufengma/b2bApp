package com.onefengma.taobuxiu.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.onefengma.taobuxiu.MainApplication;
import com.orhanobut.logger.Logger;

/**
 * @author yfchu
 * @date 2016/8/11
 */
public class SPHelper {

    private static final String SP_FILE_NAME = "common_taobuxiu_sp";

    private static SPHelper instance;
    private static SharedPreferences.Editor editor;
    private static SharedPreferences sp;


    public SPHelper() {
        sp = MainApplication.getContext().getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public static SPHelper instance() {
        if (instance == null) {
            instance = new SPHelper();
        }
        return instance;
    }

    public static SharedPreferences sp() {
        if (instance == null) {
            instance = new SPHelper();
        }
        return instance.sp;
    }

    public void save(String key, Object value) {
        if (StringUtils.isEmpty(key) || value == null) {
            return;
        }
        try {
            editor.putString(key, JSON.toJSONString(value)).commit();
        } catch (Exception e) {
            Logger.d("error when save json data:" + value);
            editor.putString(key, value.toString()).commit();
        }
    }

    public String get(String key) {
        if (StringUtils.isEmpty(key)) {
            return "";
        }
        return sp.getString(key, "");
    }

    public <T> T get(String key, Class<T> clazz) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }

        return JSON.parseObject(sp.getString(key, ""), clazz);
    }


    public int getInt(String key, int defValue) {
        try {
            return Integer.parseInt(sp().getString(key, defValue + ""));
        } catch (Exception e) {
            return defValue;
        }
    }

    public long getLong(String key, long defValue) {
        try {
            return Long.parseLong(sp().getString(key, defValue + ""));
        } catch (Exception e) {
            return defValue;
        }
    }

    public float getFloat(String key, float defValue) {
        try {
            return Float.parseFloat(sp().getString(key, defValue + ""));
        } catch (Exception e) {
            return defValue;
        }
    }

    public boolean getBoolean(String key, boolean defValue) {
        try {
            return Boolean.parseBoolean(sp().getString(key, defValue + ""));
        } catch (Exception e) {
            return defValue;
        }
    }

}
