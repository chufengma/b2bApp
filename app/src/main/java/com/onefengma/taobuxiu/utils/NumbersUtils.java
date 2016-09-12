package com.onefengma.taobuxiu.utils;

import java.math.BigDecimal;

/**
 * @author yfchu
 * @date 2016/5/24
 */
public class NumbersUtils {

    public static String round(float number, int count) {
        float value = roundFloat(number, count);
        if (value-(int)value == 0) {
            return (int)value + "";
        } else {
            return value + "";
        }
    }

    public static float roundFloat(float number, int count) {
        BigDecimal bd = new BigDecimal(number + "");
        float value = bd.setScale(count, BigDecimal.ROUND_HALF_UP).floatValue();
        return value;
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

    public static float parseFloat(String value) {
        if (StringUtils.isEmpty(value)) {
            return 0;
        }
        return Float.parseFloat(value);
    }
}
