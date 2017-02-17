package com.onefengma.taobuxiu.model;

import android.support.v4.util.ArrayMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dev on 2017/2/17.
 */

public class GoodsCategory {
    public static ArrayMap<String, List<String>> goods = new ArrayMap<>();
    public static List<String> titles = Arrays.asList("钢材", "塑料", "机械", "石材", "化工原料", "木材");

    static {
        goods.put("钢材", Arrays.asList("PC钢棒", "金属粉末", "优特钢", "型材", "线材", "盘螺", "螺纹钢", "管材", "钢板", "炉料", "有色金属", "钢培", "钢卷", "圆钢", "废金属"));
        goods.put("塑料", Arrays.asList("塑料粒子"));
        goods.put("机械", Arrays.asList("汽车", "开平机"));
        goods.put("石材", Arrays.asList("水泥", "方砖"));
        goods.put("化工原料", Arrays.asList("煤炭"));
        goods.put("木材", Arrays.asList("木材"));

    }

    public static List<String> getTitle() {
        return titles;
    }

    public static List<String> getContent(String title) {
        return goods.get(title);
    }

    public static List<String> getContent(int position) {
        return goods.get(titles.get(position));
    }
}
