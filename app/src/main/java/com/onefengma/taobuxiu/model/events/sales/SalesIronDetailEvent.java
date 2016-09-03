package com.onefengma.taobuxiu.model.events.sales;

import com.onefengma.taobuxiu.model.entities.MyIronBuyDetail;
import com.onefengma.taobuxiu.model.entities.SalesIronBuyDetail;
import com.onefengma.taobuxiu.model.events.BaseStatusEvent;

/**
 * @author yfchu
 * @date 2016/8/12
 */
public class SalesIronDetailEvent extends BaseStatusEvent {

    public SalesIronBuyDetail detail;

    public SalesIronDetailEvent(int status, SalesIronBuyDetail detail) {
        this.status = status;
        this.detail = detail;
    }
}
