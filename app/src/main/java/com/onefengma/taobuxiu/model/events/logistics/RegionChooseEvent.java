package com.onefengma.taobuxiu.model.events.logistics;

import com.onefengma.taobuxiu.model.entities.City;

/**
 * Created by dev on 2017/2/17.
 */

public class RegionChooseEvent {
    public City city;
    public String requestId;

    public RegionChooseEvent(City city, String requestId) {
        this.city = city;
        this.requestId = requestId;
    }
}
