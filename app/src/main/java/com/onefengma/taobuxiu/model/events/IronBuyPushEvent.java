package com.onefengma.taobuxiu.model.events;

/**
 * @author yfchu
 * @date 2016/8/12
 */
public class IronBuyPushEvent extends BaseStatusEvent {

    public IronBuyPushEvent(int status) {
        this.status = status;
    }
}
