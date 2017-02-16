package com.onefengma.taobuxiu.model.events.logistics;

import java.util.List;

/**
 * Created by dev on 2017/2/16.
 */

public class EditOtherDemandEvent {
    public List<String> demands;

    public EditOtherDemandEvent(List<String> demands) {
        this.demands = demands;
    }
}
