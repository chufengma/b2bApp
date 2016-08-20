package com.onefengma.taobuxiu.model.events;

/**
 * @author yfchu
 * @date 2016/8/12
 */
public class DeleteIronBuyEvent extends BaseStatusEvent {

    public DeleteIronBuyEvent(int status) {
        this.status = status;
    }
}
