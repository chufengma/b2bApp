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
import com.onefengma.taobuxiu.manager.BuyManager;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.model.events.MyIronsEventDoing;
import com.onefengma.taobuxiu.model.events.MyIronsEventDone;
import com.onefengma.taobuxiu.model.events.MyIronsEventOutOfDate;
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

    TabLayout.Tab tabDoing;
    TabLayout.Tab tabDone;
    TabLayout.Tab tabOut;

    TabItem tabItemDoing;
    TabItem tabItemDone;
    TabItem tabItemOut;

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
        tabDoing = buyTab.newTab();
        tabItemDoing = new TabItem(getContext());
        tabItemDoing.setTitle(getString(R.string.buy_doing_tab, 0));
        tabDoing.setCustomView(tabItemDoing);
        buyTab.addTab(tabDoing);

        tabDone = buyTab.newTab();
        tabItemDone = new TabItem(getContext());
        tabItemDone.setTitle(getString(R.string.buy_done_tab, 0));
        tabDone.setCustomView(tabItemDone);
        buyTab.addTab(tabDone);

        tabOut = buyTab.newTab();
        tabItemOut = new TabItem(getContext());
        tabItemOut.setTitle(getString(R.string.buy_out_tab, 0));
        tabOut.setCustomView(tabItemOut);
        buyTab.addTab(tabOut);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTabEvent(OnBuyTabEvent onBuyTabEvent) {
        System.out.println("-----------------onBuyTabEvent");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadDoingNumbers(MyIronsEventDoing myIronsEvent) {
        tabItemDoing.setTitle(getString(R.string.buy_doing_tab, BuyManager.instance().myIronsResponseForDoing.maxCount));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadDoneNumbers(MyIronsEventDone myIronsEvent) {
        tabItemDone.setTitle(getString(R.string.buy_done_tab, BuyManager.instance().myIronsResponseForDone.maxCount));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadOutOfDateNumbers(MyIronsEventOutOfDate myIronsEvent) {
        tabItemOut.setTitle(getString(R.string.buy_out_tab, BuyManager.instance().myIronsResponseForOutOfDate.maxCount));
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
