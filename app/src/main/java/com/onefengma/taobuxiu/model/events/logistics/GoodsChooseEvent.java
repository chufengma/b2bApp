package com.onefengma.taobuxiu.model.events.logistics;

/**
 * Created by dev on 2017/2/17.
 */

public class GoodsChooseEvent {
    public String requestID;
    public String goodTitle;
    public String goodContent;

    public GoodsChooseEvent(String requestID, String goodTitle, String goodContent) {
        this.requestID = requestID;
        this.goodTitle = goodTitle;
        this.goodContent = goodContent;
    }
}
