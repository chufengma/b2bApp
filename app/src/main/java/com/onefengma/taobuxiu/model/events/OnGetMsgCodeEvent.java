package com.onefengma.taobuxiu.model.events;

/**
 * @author yfchu
 * @date 2016/8/12
 */
public class OnGetMsgCodeEvent extends BaseListStatusEvent {
    public OnGetMsgCodeEvent(int status) {
        this.status = status;
    }
}
