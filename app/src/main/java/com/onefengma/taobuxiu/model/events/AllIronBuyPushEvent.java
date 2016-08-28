package com.onefengma.taobuxiu.model.events;

/**
 * @author yfchu
 * @date 2016/8/12
 */
public class AllIronBuyPushEvent extends BaseStatusEvent {

    public AllIronBuyPushEvent(int status) {
        this.status = status;
    }
}
