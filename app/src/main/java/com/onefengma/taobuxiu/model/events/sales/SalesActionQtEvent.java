package com.onefengma.taobuxiu.model.events.sales;

import com.onefengma.taobuxiu.model.events.BaseListStatusEvent;
import com.onefengma.taobuxiu.model.events.BaseStatusEvent;
import com.onefengma.taobuxiu.views.sales.SalesQtManager.SalesQtStatus;

/**
 * @author yfchu
 * @date 2016/8/12
 */
public class SalesActionQtEvent extends BaseStatusEvent {

    public int qtStatus;

    public SalesActionQtEvent(int status, int qtStatus) {
        this.status = status;
        this.qtStatus = qtStatus;
    }

    public SalesActionQtEvent() {
    }

}
