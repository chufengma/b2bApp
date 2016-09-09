package com.onefengma.taobuxiu.manager.helpers;

import com.onefengma.taobuxiu.network.HttpHelper;

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

    public static String getImageUrl(String url) {
        return HttpHelper.BASE_URL + "" + url;
    }

}
