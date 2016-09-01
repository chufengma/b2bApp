package com.onefengma.taobuxiu.model.events.sales;

import com.onefengma.taobuxiu.model.events.BaseListStatusEvent;

/**
 * @author yfchu
 * @date 2016/8/12
 */
public class SalesLoginEvent extends BaseListStatusEvent {
    public SalesLoginEvent(int status) {
        this.status = status;
    }

    public SalesLoginEvent() {
    }
}
