package com.onefengma.taobuxiu.model.entities;


/**
 * Created by chufengma on 16/8/21.
 */
public class QtDetail {
    public String qtId;
    public int salesmanId;
    public String ironBuyId;
    public int status; // 0等待质检 1质检完成 2质检取消
    public long pushTime;
    public long finishTime;
    public String userId;
    public IronBuyBrief ironBuyBrief;

    public String getStatusStr() {
        if (status == 0) {
            return "等待质检";
        } else if (status == 1) {
            return "质检完成";
        } else if (status == 3){
            return "质检中";
        } else {
            return "质检取消";
        }
    }
}