package com.onefengma.taobuxiu.utils;

/**
 * @author yfchu
 * @date 2016/8/8
 */
public class StringUtils {

    public static boolean equals(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equals(str2);
    }

    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equalsIgnoreCase(str2);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.equals("");
    }
}
