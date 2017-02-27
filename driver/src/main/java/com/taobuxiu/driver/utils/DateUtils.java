package com.taobuxiu.driver.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yfchu
 * @date 2016/8/17
 */
public class DateUtils {

    public static String getDateStr(long timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return dateFormat.format(new Date(timestamp));
    }

    public static long dayTime() {
        return 1000 * 60 * 60 * 24;
    }

    public static long hourTime() {
        return 1000 * 60 * 60;
    }

    public static long minuteTime() {
        return 1000 * 60;
    }

}
