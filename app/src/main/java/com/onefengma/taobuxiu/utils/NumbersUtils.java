package com.onefengma.taobuxiu.utils;

import java.math.BigDecimal;

/**
 * @author yfchu
 * @date 2016/5/24
 */
public class NumbersUtils {

    public static float round(float number, int count) {
        BigDecimal bd = new BigDecimal(number + "");
        return bd.setScale(count, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    public static BigDecimal round(BigDecimal number, int count) {
        if (number == null) {
            return new BigDecimal(0);
        }
        int maxLength = number.toString().length();
        int zeroIndex = number.toString().indexOf(".");
        if (zeroIndex == -1) {
            return number;
        }
        if (maxLength - zeroIndex <= count) {
            return number;
        }
        return number.setScale(count, BigDecimal.ROUND_HALF_UP);
    }

    public static String getHS(float number) {
        return (number * 100) + "%";
    }
}
