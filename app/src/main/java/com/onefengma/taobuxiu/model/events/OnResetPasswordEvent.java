package com.onefengma.taobuxiu.model.events;

/**
 * @author yfchu
 * @date 2016/8/12
 */
public class OnResetPasswordEvent extends BaseListStatusEvent {
    public OnResetPasswordEvent(int status) {
        this.status = status;
    }
}
