package com.onefengma.taobuxiu.model.events;

/**
 * @author yfchu
 * @date 2016/8/12
 */
public class MyIronsEventOutOfDate extends BaseListStatusEvent {

    public MyIronsEventOutOfDate(int status, int loadType) {
        this.status = status;
        this.type = loadType;
    }

    public MyIronsEventOutOfDate() {
    }

}
