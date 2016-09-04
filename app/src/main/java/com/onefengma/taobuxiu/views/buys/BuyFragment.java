package com.onefengma.taobuxiu.views.buys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.AuthManager;
import com.onefengma.taobuxiu.manager.BuyManager;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.model.entities.UserProfile;
import com.onefengma.taobuxiu.model.events.MyIronsEventDoing;
import com.onefengma.taobuxiu.model.events.MyIronsEventDone;
import com.onefengma.taobuxiu.model.events.MyIronsEventOutOfDate;
import com.onefengma.taobuxiu.model.events.OnBuyTabEvent;
import com.onefengma.taobuxiu.utils.ToastUtils;
import com.onefengma.taobuxiu.views.core.BaseActivity;
import com.onefengma.taobuxiu.views.core.BaseFragment;
import com.onefengma.taobuxiu.views.widgets.TabItem;
import com.onefengma.taobuxiu.views.widgets.ToolBar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @OnClick(R.id.right_image)
    public void clickOnRightImage() {
        if (AuthManager.instance().sellerCheck()) {
            PushNewBuyActivity.start((BaseActivity) getActivity());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        BuyFragmentViewPagerAdapter adapter = new BuyFragmentViewPagerAdapter(getFragmentManager());
        buyViewPager.setOffscreenPageLimit(3);
        buyViewPager.setAdapter(adapter);

        buyViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(buyTab));
        buyTab.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(buyViewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                if (tab == tabDoing) {
                    buyTab.setSelectedTabIndicatorColor(getResources().getColor(R.color.main_yellow));
                } else if (tab == tabDone) {
                    buyTab.setSelectedTabIndicatorColor(getResources().getColor(R.color.main_green));
                } else if (tab == tabOut) {
                    buyTab.setSelectedTabIndicatorColor(getResources().getColor(R.color.main_red));
                }
            }
        });

        setupTabs();

        setLeftViewListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyBuyHistoryActivity.start((BaseActivity) getActivity());
            }
        });
    }

    private void setupTabs() {
        tabDoing = buyTab.newTab();
        tabItemDoing = new TabItem(getContext());
        tabItemDoing.setTitle(getString(R.string.buy_doing_tab, 0));

        tabItemDoing.setIcon(R.drawable.buy_tab_doing);
        tabItemDoing.titleView.setTextColor(getResources().getColorStateList(R.color.buy_doing_indicator_text_colors));
        tabDoing.setCustomView(tabItemDoing);
        buyTab.addTab(tabDoing);

        tabDone = buyTab.newTab();
        tabItemDone = new TabItem(getContext());
        tabItemDone.setIcon(R.drawable.buy_tab_done);
        tabItemDone.setTitle(getString(R.string.buy_done_tab, 0));
        tabItemDone.titleView.setTextColor(getResources().getColorStateList(R.color.buy_done_indicator_text_colors));
        tabDone.setCustomView(tabItemDone);
        buyTab.addTab(tabDone);

        tabOut = buyTab.newTab();
        tabItemOut = new TabItem(getContext());
        tabItemOut.setIcon(R.drawable.buy_tab_out_of_date);
        tabItemOut.setTitle(getString(R.string.buy_out_tab, 0));
        tabItemOut.titleView.setTextColor(getResources().getColorStateList(R.color.buy_outofdate_indicator_text_colors));
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

}
