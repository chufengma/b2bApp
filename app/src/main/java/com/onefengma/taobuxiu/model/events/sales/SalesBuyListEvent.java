package com.onefengma.taobuxiu.model.events.sales;

import com.onefengma.taobuxiu.manager.BuyManager;
import com.onefengma.taobuxiu.manager.BuyManager.BuyStatus;
import com.onefengma.taobuxiu.model.events.BaseListStatusEvent;
import com.onefengma.taobuxiu.views.sales.SalesQtManager.SalesQtStatus;

/**
 * @author yfchu
 * @date 2016/8/12
 */
public class SalesBuyListEvent extends BaseListStatusEvent {

    public BuyStatus buyStatus;

    public SalesBuyListEvent(int status, int type, BuyStatus qtStatus) {
        super(status, type);
        this.buyStatus = qtStatus;
    }

    public SalesBuyListEvent() {
    }

}
