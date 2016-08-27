package com.onefengma.taobuxiu.model.events;

import com.onefengma.taobuxiu.manager.OfferManager.OfferStatus;
import com.onefengma.taobuxiu.model.entities.OfferDetail;

/**
 * @author yfchu
 * @date 2016/8/12
 */
public class MyOfferDetailEvent extends BaseStatusEvent {

    public OfferDetail offerDetail;

    public MyOfferDetailEvent(int status, OfferDetail offerDetail) {
        this.status = status;
        this.offerDetail = offerDetail;
    }


}
