package com.onefengma.taobuxiu.model.events;

import com.onefengma.taobuxiu.model.entities.MyBuyHistoryInfo;

/**
 * @author yfchu
 * @date 2016/8/12
 */
public class MyIronBuyHistoryEvent extends BaseStatusEvent {

    public MyBuyHistoryInfo myBuyHistoryInfo;

    public MyIronBuyHistoryEvent(int status, MyBuyHistoryInfo myBuyHistoryInfo) {
        this.status = status;
        this.myBuyHistoryInfo = myBuyHistoryInfo;
    }
}
