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
    public float length;
    public float width;
    public float height;
    public float toleranceFrom;
    public float toleranceTo;
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

    public IronBuyPush(IronBuyPush oldPush) {
        this.ironType = ironType;
        this.material = material;
        this.surface = surface;
        this.proPlace = proPlace;
        this.locationCityId = locationCityId;
        this.message = message;
        this.length = length;
        this.width = width;
        this.height = height;
        this.toleranceFrom = toleranceFrom;
        this.toleranceTo = toleranceTo;
        this.numbers = numbers;
        this.timeLimit = timeLimit;
        this.unit = unit;
        this.unitIndex = unitIndex;
        this.dayIndex = dayIndex;
        this.hourIndex = hourIndex;
        this.minuteIndex = minuteIndex;
        this.id = id;
        this.ironId = ironId;
        this.pushStatus = pushStatus;
    }
}
