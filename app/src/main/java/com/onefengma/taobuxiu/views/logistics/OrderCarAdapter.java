package com.onefengma.taobuxiu.views.logistics;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.onefengma.taobuxiu.R;

/**
 * Created by dev on 2017/2/22.
 */

public class OrderCarAdapter extends BaseAdapter {

    @Override
    public int getCount() {
        return 20;
    }

    @Override
    public Object getItem(int position) {
        return new String("ssss");
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.logistics_order_car_list_item, parent, false);
        }
        return convertView;
    }

}
