package com.onefengma.taobuxiu.views.buys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        toolbar.setTitle("我的求购");

        BuyFragmentViewPagerAdapter adapter = new BuyFragmentViewPagerAdapter(getFragmentManager());
        buyViewPager.setAdapter(adapter);

        buyViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(buyTab));
        buyTab.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(buyViewPager));

        for (int i = 0; i < adapter.getCount(); i++) {
            TabLayout.Tab tab = buyTab.newTab();
            TextView text = new TextView(getContext());
            text.setText("选项卡" + i);
            tab.setCustomView(text);
            buyTab.addTab(tab);
        }
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
}
