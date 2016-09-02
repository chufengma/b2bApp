package com.onefengma.taobuxiu.manager.helpers;

/**
 * @author yfchu
 * @date 2016/9/2
 */
public class ImageHelper {

    private static ImageHelper instance;

    public static ImageHelper instance() {
        if (instance == null) {
            instance = new ImageHelper();
        }
        return instance;
    }

}
