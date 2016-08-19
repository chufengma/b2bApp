package com.onefengma.taobuxiu.model.events;

/**
 * @author yfchu
 * @date 2016/8/12
 */
public class LoginEvent extends BaseListStatusEvent {
    public LoginEvent(int status) {
        this.status = status;
    }

    public LoginEvent() {
    }
}
