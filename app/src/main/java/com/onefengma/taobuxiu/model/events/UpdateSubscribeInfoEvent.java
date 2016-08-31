package com.onefengma.taobuxiu.model.events;

import com.onefengma.taobuxiu.model.entities.SubscribeInfo;

/**
 * @author yfchu
 * @date 2016/8/12
 */
public class UpdateSubscribeInfoEvent extends BaseStatusEvent {

    public UpdateSubscribeInfoEvent(int status) {
        this.status = status;
    }
}
