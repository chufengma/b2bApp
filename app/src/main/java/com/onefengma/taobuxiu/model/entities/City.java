package com.onefengma.taobuxiu.model.entities;

import java.util.List;

/**
 * Created by chufengma on 16/8/27.
 */
public class City {
    public String name;
    public List<City> sub;
    public int type = -1;
    public String id;
    public String fatherId;
    public String pinyin;
}
