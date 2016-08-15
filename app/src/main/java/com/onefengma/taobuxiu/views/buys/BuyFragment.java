package com.onefengma.taobuxiu.views.buys;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.model.events.OnBuyTabEvent;
import com.onefengma.taobuxiu.views.core.BaseFragment;
import com.onefengma.taobuxiu.views.widgets.ToolBar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chufengma on 16/8/7.
 */
public class BuyFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    ToolBar toolbar;
    @BindView(R.id.buy_tab)
    TabLayout buyTab;
    @BindView(R.id.buy_view_pager)
    ViewPager buyViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        BuyFragmentViewPagerAdapter adapter = new BuyFragmentViewPagerAdapter(getFragmentManager());
        buyViewPager.setOffscreenPageLimit(3);
        buyViewPager.setAdapter(adapter);

        buyViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(buyTab));
        buyTab.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(buyViewPager));

        setupTabs();
    }

    private void setupTabs() {
        TabLayout.Tab tabDoing = buyTab.newTab();
        TabItem tabItemDoing = new TabItem(getContext());
        tabItemDoing.setTitle("进行中");
        tabDoing.setCustomView(tabItemDoing);
        buyTab.addTab(tabDoing);

        TabLayout.Tab tabDone = buyTab.newTab();
        TabItem tabItemDone = new TabItem(getContext());
        tabItemDone.setTitle("已成交");
        tabDone.setCustomView(tabItemDone);
        buyTab.addTab(tabDone);

        TabLayout.Tab tabOut = buyTab.newTab();
        TabItem tabItemOut = new TabItem(getContext());
        tabItemOut.setTitle("已过期");
        tabOut.setCustomView(tabItemOut);
        buyTab.addTab(tabOut);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTabEvent(OnBuyTabEvent onBuyTabEvent) {
        System.out.println("-----------------onBuyTabEvent");
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusHelper.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusHelper.unregister(this);
    }

    public static class TabItem extends FrameLayout {

        @BindView(R.id.icon)
        ImageView iconView;
        @BindView(R.id.title)
        TextView titleView;

        public TabItem(Context context) {
            super(context);
            View view = inflate(context, R.layout.buy_tab_item, this);
            ButterKnife.bind(this, view);
        }

        public void setTitle(String title) {
            titleView.setText(title);
        }

        public void setIcon(int resId) {
            iconView.setImageResource(resId);
        }
    }
}
