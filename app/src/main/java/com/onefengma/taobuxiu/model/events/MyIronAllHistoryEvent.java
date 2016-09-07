package com.onefengma.taobuxiu.model.events;

import com.onefengma.taobuxiu.model.entities.MyAllHistoryInfo;

/**
 * @author yfchu
 * @date 2016/8/12
 */
public class MyIronAllHistoryEvent extends BaseStatusEvent {

    public MyAllHistoryInfo myAllHistoryInfo;

    public MyIronAllHistoryEvent(int status, MyAllHistoryInfo myAllHistoryInfo) {
        this.status = status;
        this.myAllHistoryInfo = myAllHistoryInfo;
    }
}
