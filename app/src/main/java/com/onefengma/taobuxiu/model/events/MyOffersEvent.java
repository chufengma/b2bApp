package com.onefengma.taobuxiu.model.events;

import com.onefengma.taobuxiu.manager.OfferManager;
import com.onefengma.taobuxiu.manager.OfferManager.OfferStatus;

/**
 * @author yfchu
 * @date 2016/8/12
 */
public class MyOffersEvent extends BaseListStatusEvent {

    public OfferStatus offerStatus;

    public MyOffersEvent(int status, int type, OfferStatus offerStatus) {
        super(status, type);
        this.offerStatus = offerStatus;
    }

    public MyOffersEvent() {
    }

}
