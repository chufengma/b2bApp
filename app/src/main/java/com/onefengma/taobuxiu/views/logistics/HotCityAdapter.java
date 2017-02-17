package com.onefengma.taobuxiu.views.logistics;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.model.entities.City;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baisoo on 16/9/24.
 */
public class HotCityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<City> mCitys;
    private CityRecyclerAdapter.OnCityClickListener onCityClickListener;

    public HotCityAdapter(Context mContext, List<City> mCitys, CityRecyclerAdapter.OnCityClickListener listener) {
        this.mContext = mContext;
        this.mCitys = mCitys;
        this.onCityClickListener = listener;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_hot_city_gridview, parent, false);
        return new HotCityItemHolder(inflate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((HotCityItemHolder) holder).mTvHotCityName.setText(mCitys.get(position).name);

        ((HotCityItemHolder) holder).mTvHotCityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //返回定位城市
                if (onCityClickListener != null){
                    onCityClickListener.onCityClick(mCitys.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCitys.size();
    }

    class HotCityItemHolder extends RecyclerView.ViewHolder {


        public TextView mTvHotCityName;

        public HotCityItemHolder(View itemView) {
            super(itemView);
            mTvHotCityName = (TextView) itemView.findViewById(R.id.tv_hot_city_name);

        }
    }


}
