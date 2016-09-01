package com.onefengma.taobuxiu.model.events;

import com.onefengma.taobuxiu.model.entities.MyOfferHistoryInfo;

/**
 * @author yfchu
 * @date 2016/8/12
 */
public class MyIronOfferHistoryEvent extends BaseStatusEvent {

    public MyOfferHistoryInfo myOfferHistoryInfo;

    public MyIronOfferHistoryEvent(int status, MyOfferHistoryInfo myOfferHistoryInfo) {
        this.status = status;
        this.myOfferHistoryInfo = myOfferHistoryInfo;
    }
}
