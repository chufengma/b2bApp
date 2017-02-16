package com.onefengma.taobuxiu.model.events.logistics;

/**
 * Created by dev on 2017/2/16.
 */

public class ChooseDeadLineEvent {
    public int day;
    public int hour;
    public int minute;

    public ChooseDeadLineEvent(int day, int hour, int minute) {
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }
}
