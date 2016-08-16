package com.onefengma.taobuxiu.model.events;

/**
 * @author yfchu
 * @date 2016/8/16
 */
public class GetBuyNumbersEvent {
    public int doing;
    public int done;
    public int outOfDate;

    public GetBuyNumbersEvent(int doing, int done, int outOfDate) {
        this.doing = doing;
        this.done = done;
        this.outOfDate = outOfDate;
    }
}
