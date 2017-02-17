package com.onefengma.taobuxiu.views.logistics;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.model.entities.City;

import java.util.List;

/**
 * Created by baisoo on 16/9/24.
 */

public class HotCityHolder extends RecyclerView.ViewHolder {

    private final RecyclerView mRecyHotCity;
    private Context mContext;

    public HotCityHolder(View itemView, Context mContext) {
        super(itemView);
        mRecyHotCity = (RecyclerView) itemView.findViewById(R.id.recy_hot_city);
        this.mContext =mContext;

        mRecyHotCity.setLayoutManager(new GridLayoutManager(mContext,3));

    }

    public void setDate(List<City> mHotCity, CityRecyclerAdapter.OnCityClickListener listener){
        HotCityAdapter hotCityAdapter = new HotCityAdapter(mContext, mHotCity, listener);
        mRecyHotCity.setAdapter(hotCityAdapter);
    }

}
