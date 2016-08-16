package com.onefengma.taobuxiu.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.util.ArrayMap;

import com.alibaba.fastjson.JSON;
import com.onefengma.taobuxiu.MainApplication;
import com.orhanobut.logger.Logger;

/**
 * @author yfchu
 * @date 2016/8/11
 */
public class SPHelper {

    private static final String COMMON_SP = "taobuxiu_common_sp";
    private static final String BUY_SP = "taobuxiu_buy_sp";

    private static ArrayMap<String, SPHelper> spHelperArrayMap = new ArrayMap<>();
    private SharedPreferences.Editor editor;
    private SharedPreferences sp;

    public SPHelper(String name) {
        sp = MainApplication.getContext().getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    // common sp
    public static SPHelper common() {
        SPHelper spHelper = spHelperArrayMap.get(COMMON_SP);
        if (spHelper == null) {
            spHelper = new SPHelper(COMMON_SP);
            spHelperArrayMap.put(COMMON_SP, spHelper);
        }
        return spHelper;
    }

    // common sp
    public static SPHelper buy() {
        SPHelper spHelper = spHelperArrayMap.get(BUY_SP);
        if (spHelper == null) {
            spHelper = new SPHelper(BUY_SP);
            spHelperArrayMap.put(BUY_SP, spHelper);
        }
        return spHelper;
    }

    public SharedPreferences sp() {
        return sp;
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
        try {
            return JSON.parseObject(sp.getString(key, ""), clazz);
        } catch (Exception e) {
            return null;
        }
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
