package com.onefengma.taobuxiu.model.events.sales;

import com.onefengma.taobuxiu.model.events.BaseListStatusEvent;
import com.onefengma.taobuxiu.views.sales.SalesQtManager;
import com.onefengma.taobuxiu.views.sales.SalesQtManager.SalesQtStatus;

/**
 * @author yfchu
 * @date 2016/8/12
 */
public class SalesQtListEvent extends BaseListStatusEvent {

    public SalesQtStatus qtStatus;

    public SalesQtListEvent(int status, int type, SalesQtStatus qtStatus) {
        super(status, type);
        this.qtStatus = qtStatus;
    }

    public SalesQtListEvent() {
    }

}
