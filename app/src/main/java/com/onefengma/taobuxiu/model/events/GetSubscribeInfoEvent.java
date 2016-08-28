package com.onefengma.taobuxiu.model.events;

import com.onefengma.taobuxiu.model.entities.SubscribeInfo;

/**
 * @author yfchu
 * @date 2016/8/12
 */
public class GetSubscribeInfoEvent extends BaseStatusEvent {

    public SubscribeInfo subscribeInfo;

    public GetSubscribeInfoEvent(int status, SubscribeInfo subscribeInfo) {
        this.status = status;
        this.subscribeInfo = subscribeInfo;
    }
}
