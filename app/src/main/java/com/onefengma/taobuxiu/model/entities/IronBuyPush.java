package com.onefengma.taobuxiu.model.entities;

import java.io.Serializable;

/**
 * Created by chufengma on 16/8/28.
 */
public class IronBuyPush implements Serializable {
    public String ironType;
    public String material;
    public String surface;
    public String proPlace;
    public String locationCityId;
    public String message;
    public String length;
    public String width;
    public String height;
    public String toleranceFrom;
    public String toleranceTo;
    public float numbers;
    public long timeLimit;
    public String unit;

    public int unitIndex;
    public int dayIndex;
    public int hourIndex;
    public int minuteIndex;

    public long id;
    public String ironId;

    public int pushStatus;// 0 表示发布新的 1 更新已发布的求购

    public IronBuyPush() {
        id = System.currentTimeMillis();
    }

    public IronBuyPush copy() {
        IronBuyPush newPush = new IronBuyPush();
        newPush.ironType = ironType;
        newPush.material = material;
        newPush.surface = surface;
        newPush.proPlace = proPlace;
        newPush.locationCityId = locationCityId;
        newPush.message = message;
        newPush.length = length;
        newPush.width = width;
        newPush.height = height;
        newPush.toleranceFrom = toleranceFrom;
        newPush.toleranceTo = toleranceTo;
        newPush.numbers = numbers;
        newPush.timeLimit = timeLimit;
        newPush.unit = unit;
        newPush.unitIndex = unitIndex;
        newPush.dayIndex = dayIndex;
        newPush.hourIndex = hourIndex;
        newPush.minuteIndex = minuteIndex;
        newPush.ironId = ironId;
        newPush.pushStatus = pushStatus;
        return newPush;
    }
}
