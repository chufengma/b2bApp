package com.taobuxiu.driver.model.events;

/**
 * @author yfchu
 * @date 2016/8/12
 */
public class BaseListStatusEvent extends BaseStatusEvent {
    public int type = RELOAD;

    public static final int LOAD_MORE = 3;
    public static final int RELOAD = 4;

    public BaseListStatusEvent(int status, int type) {
        this.status = status;
        this.type = type;
    }

    public BaseListStatusEvent() {
    }

    public boolean isLoadComplete() {
        return type == LOAD_MORE && status != STARTED;
    }

    public boolean isRefreshComplete() {
        return type == RELOAD && status != STARTED;
    }
}
