package com.onefengma.taobuxiu.model.events;

/**
 * @author yfchu
 * @date 2016/8/12
 */
public class MyIronsEvent extends BaseStatusEvent {

    public static final int RELOAD = 0;
    public static final int LOAD_MORE = 1;

    public int loadType = 0;

    public MyIronsEvent(int status, int loadType) {
        super(status);
        this.loadType = loadType;
    }

    public MyIronsEvent() {
    }

}
