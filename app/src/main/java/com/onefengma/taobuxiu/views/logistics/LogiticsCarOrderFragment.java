package com.onefengma.taobuxiu.views.logistics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.views.core.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dev on 2017/2/15.
 */

public class LogiticsCarOrderFragment extends BaseFragment {


    @BindView(R.id.order_car_tab)
    TabLayout orderCarTab;
    @BindView(R.id.order_car_view_pager)
    ViewPager orderCarViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.logitics_car_order_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        OrderCarFragmentViewPagerAdapter adapter = new OrderCarFragmentViewPagerAdapter(getFragmentManager());
        orderCarViewPager.setOffscreenPageLimit(3);
        orderCarViewPager.setAdapter(adapter);
        orderCarTab.setupWithViewPager(orderCarViewPager);
    }

}
