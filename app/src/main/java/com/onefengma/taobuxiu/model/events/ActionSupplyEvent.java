package com.onefengma.taobuxiu.model.events;

/**
 * @author yfchu
 * @date 2016/8/12
 */
public class ActionSupplyEvent extends BaseStatusEvent {

    public ActionSupplyEvent(int status) {
        this.status = status;
    }
}
