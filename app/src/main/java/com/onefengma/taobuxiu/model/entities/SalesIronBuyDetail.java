package com.onefengma.taobuxiu.model.entities;

import com.onefengma.taobuxiu.model.sales.NormalUserInfo;

import java.util.List;

/**
 * Created by chufengma on 16/7/5.
 */
public class SalesIronBuyDetail {
    public IronBuyBrief buy;
    public List<SupplyBrief> supplies;
    public String salesManPhone;
    public SalesMan salesMan;
    public QtDetail qtDetail;
    public NormalUserInfo userInfo;
}
