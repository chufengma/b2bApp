package com.onefengma.taobuxiu.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yfchu
 * @date 2016/8/17
 */
public class DateUtils {

    public static String getDateStr(long timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date(timestamp));
    }

}
