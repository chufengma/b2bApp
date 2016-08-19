package com.onefengma.taobuxiu.model.events;

import com.onefengma.taobuxiu.model.entities.MyIronBuyDetail;

/**
 * @author yfchu
 * @date 2016/8/12
 */
public class MyIronDetailEvent extends BaseStatusEvent {

    MyIronBuyDetail detail;

    public MyIronDetailEvent(int status, MyIronBuyDetail detail) {
        this.status = status;
        this.detail = detail;
    }
}
