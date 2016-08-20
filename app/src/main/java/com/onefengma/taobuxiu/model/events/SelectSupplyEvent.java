package com.onefengma.taobuxiu.model.events;

import com.onefengma.taobuxiu.model.entities.MyIronBuyDetail;

/**
 * @author yfchu
 * @date 2016/8/12
 */
public class SelectSupplyEvent extends BaseStatusEvent {

    public SelectSupplyEvent(int status) {
        this.status = status;
    }
}
