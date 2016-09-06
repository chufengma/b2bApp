package com.onefengma.taobuxiu.model.events;

import com.onefengma.taobuxiu.views.sales.SalesQtManager;

/**
 * @author yfchu
 * @date 2016/8/12
 */
public class QtListEvent extends BaseListStatusEvent {

    public SalesQtManager.SalesQtStatus qtStatus;

    public QtListEvent(int status, int type, SalesQtManager.SalesQtStatus qtStatus) {
        super(status, type);
        this.qtStatus = qtStatus;
    }

    public QtListEvent() {
    }
}
