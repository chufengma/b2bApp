package com.onefengma.taobuxiu.model.events;

import com.onefengma.taobuxiu.model.entities.MyIronBuyDetail;

/**
 * @author yfchu
 * @date 2016/8/12
 */
public class SelectSupplyEvent extends BaseStatusEvent {

    public float totalMoney;
    public String ironId;

    public SelectSupplyEvent(int status, float totalMoney, String ironId) {
        this.status = status;
        this.totalMoney = totalMoney;
        this.ironId = ironId;
    }
}
