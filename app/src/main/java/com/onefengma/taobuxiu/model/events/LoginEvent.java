package com.onefengma.taobuxiu.model.events;

/**
 * @author yfchu
 * @date 2016/8/12
 */
public class LoginEvent extends BaseStatusEvent {
    public LoginEvent(int status) {
        super(status);
    }

    public LoginEvent() {
    }
}
