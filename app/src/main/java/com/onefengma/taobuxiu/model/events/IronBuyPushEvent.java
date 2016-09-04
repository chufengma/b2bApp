package com.onefengma.taobuxiu.model.events;

/**
 * @author yfchu
 * @date 2016/8/12
 */
public class IronBuyPushEvent extends BaseStatusEvent {

    public boolean isRePush = false;

    public IronBuyPushEvent(int status, boolean isRePush) {
        this.status = status;
        this.isRePush = true;
    }

    public IronBuyPushEvent(int status) {
        this.status = status;
    }
}
