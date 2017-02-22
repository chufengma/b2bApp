package com.onefengma.taobuxiu.views.logistics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.views.buys.BuyFragmentViewPagerAdapter;
import com.onefengma.taobuxiu.views.core.BaseFragment;
import com.onefengma.taobuxiu.views.widgets.TabItem;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dev on 2017/2/15.
 */

public class LogiticsCarBookingFragment extends BaseFragment {

    @BindView(R.id.finding_car_tab)
    TabLayout findingCarTab;
    @BindView(R.id.finding_car_view_pager)
    ViewPager findingCarViewPager;

    TabLayout.Tab tabDoing;
    TabLayout.Tab tabDone;
    TabLayout.Tab tabOut;

    TabItem tabItemDoing;
    TabItem tabItemDone;
    TabItem tabItemOut;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.logitics_finding_car_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FindCarFragmentViewPagerAdapter adapter = new FindCarFragmentViewPagerAdapter(getFragmentManager());
        findingCarViewPager.setOffscreenPageLimit(3);
        findingCarViewPager.setAdapter(adapter);

        findingCarViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(findingCarTab));
        findingCarTab.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(findingCarViewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                if (tab == tabDoing) {
                    findingCarTab.setSelectedTabIndicatorColor(getResources().getColor(R.color.main_blue));
                } else if (tab == tabDone) {
                    findingCarTab.setSelectedTabIndicatorColor(getResources().getColor(R.color.main_blue));
                } else if (tab == tabOut) {
                    findingCarTab.setSelectedTabIndicatorColor(getResources().getColor(R.color.main_blue));
                }
            }
        });

        setupTabs();
    }

    private void setupTabs() {
        tabDoing = findingCarTab.newTab();
        tabItemDoing = new TabItem(getContext());
        tabItemDoing.setTitle(getString(R.string.find_car_doing_tab, 0 + ""));

        tabItemDoing.setIcon(R.drawable.finding_car_tab_doing);
        tabItemDoing.titleView.setTextColor(getResources().getColorStateList(R.color.finding_car_indicator_text_colors));
        tabDoing.setCustomView(tabItemDoing);
        findingCarTab.addTab(tabDoing);

        tabDone = findingCarTab.newTab();
        tabItemDone = new TabItem(getContext());
        tabItemDone.setIcon(R.drawable.finding_car_tab_done);
        tabItemDone.setTitle(getString(R.string.find_car_done_tab, 0 + ""));
        tabItemDone.titleView.setTextColor(getResources().getColorStateList(R.color.finding_car_indicator_text_colors));
        tabDone.setCustomView(tabItemDone);
        findingCarTab.addTab(tabDone);

        tabOut = findingCarTab.newTab();
        tabItemOut = new TabItem(getContext());
        tabItemOut.setIcon(R.drawable.finding_car_tab_out);
        tabItemOut.setTitle(getString(R.string.find_car_out_tab, 0 + ""));
        tabItemOut.titleView.setTextColor(getResources().getColorStateList(R.color.finding_car_indicator_text_colors));
        tabOut.setCustomView(tabItemOut);
        findingCarTab.addTab(tabOut);
    }
}
