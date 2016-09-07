package com.onefengma.taobuxiu.model.events;

/**
 * @author yfchu
 * @date 2016/8/12
 */
public class ActionMissEvent extends BaseStatusEvent {

    public ActionMissEvent(int status) {
        this.status = status;
    }
}
