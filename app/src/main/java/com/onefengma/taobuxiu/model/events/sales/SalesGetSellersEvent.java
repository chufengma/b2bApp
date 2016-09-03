package com.onefengma.taobuxiu.model.events.sales;

import com.onefengma.taobuxiu.model.events.BaseListStatusEvent;

/**
 * Created by chufengma on 16/9/3.
 */
public class SalesGetSellersEvent extends BaseListStatusEvent {

    public SalesGetSellersEvent(int status, int type) {
        super(status, type);
    }

    public SalesGetSellersEvent() {
    }
}
