package com.onefengma.taobuxiu.model.push;


/**
 * @author yfchu
 * @date 2016/8/19
 */
public class BasePushData {
    public String title;
    public String desc;
    public String userId;
    public String type;

    public static final String PUSH_TYPE_BUY = "buy";
    public static final String PUSH_TYPE_WIN_OFFER = "win_offer";
    public static final String PUSH_TYPE_NEW_IRON_BUY = "new_iron_buy";
    public static final String PUSH_TYPE_OFFER_MISS = "offer_miss";
    public static final String PUSH_TYPE_IRON_QT = "iron_qt";

    public static final String PUSH_TYPE_KEY = "type";
    public static final String PUSH_USER_ID_KEY = "userId";
}

