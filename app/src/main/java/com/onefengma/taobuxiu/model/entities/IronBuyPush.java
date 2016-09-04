package com.onefengma.taobuxiu.model.entities;

import com.onefengma.taobuxiu.manager.helpers.SystemHelper;

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
}
