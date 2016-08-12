package com.onefengma.taobuxiu.model.events;

/**
 * @author yfchu
 * @date 2016/8/12
 */
public class BaseStatusEvent {
    public int status;

    public static final int STARTED = 0;
    public static final int SUCCESS = 1;
    public static final int FAILED = 2;

    public BaseStatusEvent(int status) {
        this.status = status;
    }

    public BaseStatusEvent() {
    }
}
