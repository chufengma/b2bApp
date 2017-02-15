package com.onefengma.taobuxiu.views.logistics;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.views.core.BaseFragment;
import com.onefengma.taobuxiu.views.qt.LogiticsPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dev on 2017/2/15.
 */

public class LogiticsFragment extends BaseFragment {

    @BindView(R.id.im_adv)
    ImageView imAdv;
    @BindView(R.id.header)
    RelativeLayout header;
    @BindView(R.id.tab)
    TabLayout tabLayout;
    @BindView(R.id.qt_view_pager)
    ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.framgnet_logistics, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LogiticsPagerAdapter adapter = new LogiticsPagerAdapter(getFragmentManager());
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                header.setVisibility(tab.getPosition() == 0 ? View.VISIBLE : View.GONE);
                if (tab.getPosition() == 0) {
                    tabLayout.setBackgroundResource(R.color.white);
                    tabLayout.setTabTextColors(Color.parseColor("#aaaaaa"), getResources().getColor(R.color.main_blue));
                    tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.main_blue));
                } else {
                    tabLayout.setBackgroundResource(R.color.main_blue);
                    tabLayout.setTabTextColors(Color.WHITE, Color.WHITE);
                    tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
