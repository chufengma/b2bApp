package com.onefengma.taobuxiu.model.entities;

import java.io.Serializable;

/**
 * Created by baisoo on 16/9/24.
 */
public class Contact implements Serializable {
    private String mName;
    private int mType;
    public City city;

    public Contact(String name, int type) {
        mName = name;
        mType = type;
    }

    public String getmName() {
        return mName;
    }

    public int getmType() {
        return mType;
    }

}
