package com.onefengma.taobuxiu.views.buys;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.model.entities.IronBuyBrief;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by chufengma on 16/8/12.
 */
public class BuyAdapter extends RecyclerView.Adapter<BuyAdapter.SimpleTextHolder> {

    public List<IronBuyBrief> myBuys = new ArrayList<>();

    public void setMyBuys(List<IronBuyBrief> myBuys) {
        this.myBuys.clear();
        this.myBuys.addAll(myBuys);
        notifyDataSetChanged();
    }

    @Override
    public SimpleTextHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buy_item, parent, false);
        return new SimpleTextHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleTextHolder holder, int position) {
        holder.textView.setText(myBuys.get(position).userId + ":" + myBuys.get(position).id);
        holder.imageView.setBackgroundColor(Color.rgb(new Random().nextInt(256), new Random().nextInt(256), new Random().nextInt(256)));
    }

    @Override
    public int getItemCount() {
        return this.myBuys.size();
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