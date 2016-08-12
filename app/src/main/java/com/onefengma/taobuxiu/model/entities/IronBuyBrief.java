package com.onefengma.taobuxiu.model.entities;

import java.math.BigDecimal;

/**
 * @author yfchu
 * @date 2016/8/12
 */
public class IronBuyBrief {
    public String id;
    public String ironType;
    public String material;
    public String surface;
    public String proPlace;
    public String locationCityId;
    public String userId;
    public String message;
    public long pushTime;

    public String length;
    public String width;
    public String height;
    public String tolerance;
    public String unit;
    public BigDecimal numbers;
    public long timeLimit;
    public int status; // 0 待报价, 1 交易完成 , 2 过期, 3 已报价候选中, 4 我已中标
    public String supplyUserId;
    public long supplyWinTime;
    private int supplyCount;
}