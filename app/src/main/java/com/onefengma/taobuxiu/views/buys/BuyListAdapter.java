package com.onefengma.taobuxiu.views.buys;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.model.entities.IronBuyBrief;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author yfchu
 * @date 2016/8/16
 */
public class BuyListAdapter extends BaseAdapter {

    public List<IronBuyBrief> myBuys = new ArrayList<>();
    @Override
    public int getCount() {
        return myBuys.size();
    }

    public void setMyBuys(List<IronBuyBrief> myBuys) {
        this.myBuys.clear();
        this.myBuys.addAll(myBuys);
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return myBuys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SimpleTextHolder simpleTextHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.buy_item, parent, false);
            simpleTextHolder = new SimpleTextHolder(convertView);
            convertView.setTag(simpleTextHolder);
        } else {
            simpleTextHolder = (SimpleTextHolder) convertView.getTag();
        }

        simpleTextHolder.textView.setText(myBuys.get(position).userId + ":" + myBuys.get(position).id);
        simpleTextHolder.imageView.setBackgroundColor(Color.rgb(new Random().nextInt(256), new Random().nextInt(256), new Random().nextInt(256)));

        return convertView;
    }

    public static class SimpleTextHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public ImageView imageView;

        public SimpleTextHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text);
            imageView = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
