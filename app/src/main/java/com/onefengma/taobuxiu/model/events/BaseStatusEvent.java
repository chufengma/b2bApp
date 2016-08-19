package com.onefengma.taobuxiu.model.events;

/**
 * @author yfchu
 * @date 2016/8/19
 */
public class BaseStatusEvent {
    public int status = 0;
    public static final int STARTED = 0;
    public static final int SUCCESS = 1;
    public static final int FAILED = 2;

    public boolean isStarted() {
        return status == STARTED;
    }

    public boolean isSuccess() {
        return status == STARTED;
    }

    public boolean isFailed() {
        return status == STARTED;
    }
}
