package com.onefengma.taobuxiu.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.model.Constant;
import com.onefengma.taobuxiu.model.entities.UserProfile;
import com.onefengma.taobuxiu.model.events.OnBuyTabEvent;
import com.onefengma.taobuxiu.model.events.OnMineTabEvent;
import com.onefengma.taobuxiu.model.events.OnOfferTabEvent;
import com.onefengma.taobuxiu.utils.SPHelper;
import com.onefengma.taobuxiu.utils.StringUtils;
import com.onefengma.taobuxiu.utils.ToastUtils;
import com.onefengma.taobuxiu.views.buys.BuyFragment;
import com.onefengma.taobuxiu.views.core.BaseActivity;
import com.onefengma.taobuxiu.views.core.FragmentTabHost;
import com.onefengma.taobuxiu.views.mine.MineFragment;
import com.onefengma.taobuxiu.views.offers.OffersFragment;
import com.onefengma.taobuxiu.views.widgets.TabIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    private static final String TAB_FLAG_BUYS = "tabForBuys";
    private static final String TAB_FLAG_OFFERS = "tabForOffers";
    private static final String TAB_FLAG_MINE = "tabForMine";

    @BindView(R.id.tab)
    FragmentTabHost tab;

    private TabIndicatorView buyIndicatorView;
    private TabIndicatorView offerIndicatorView;
    private TabIndicatorView mineIndicatorView;

    public static void start(Context activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        if (!(activity instanceof BaseActivity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initTabs();
    }

    private void initTabs() {
        buyIndicatorView = TabIndicatorView.newBuyTabIndicator(this);
        offerIndicatorView = TabIndicatorView.newOfferTabIndicator(this);
        mineIndicatorView = TabIndicatorView.newMineTabIndicator(this);

        tab.setup(this, getSupportFragmentManager(), R.id.content);
        tab.addTab(tab.newTabSpec(TAB_FLAG_BUYS).setIndicator(buyIndicatorView), BuyFragment.class, null);
        tab.addTab(tab.newTabSpec(TAB_FLAG_OFFERS).setIndicator(offerIndicatorView), OffersFragment.class, null);
        tab.addTab(tab.newTabSpec(TAB_FLAG_MINE).setIndicator(mineIndicatorView), MineFragment.class, null);
        tab.getTabWidget().setDividerDrawable(null);

        tab.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (StringUtils.equals(TAB_FLAG_BUYS, tabId)) {
                    EventBusHelper.post(new OnBuyTabEvent());
                } else if (StringUtils.equals(TAB_FLAG_OFFERS, tabId)) {
                    EventBusHelper.post(new OnOfferTabEvent());
                } else if (StringUtils.equals(TAB_FLAG_MINE, tabId)) {
                    EventBusHelper.post(new OnMineTabEvent());
                }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }
}
