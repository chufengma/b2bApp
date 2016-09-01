package com.onefengma.taobuxiu.views.sales;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.BuyManager;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.model.events.MyIronsEventDoing;
import com.onefengma.taobuxiu.views.core.BaseActivity;
import com.onefengma.taobuxiu.views.core.FragmentTabHost;
import com.onefengma.taobuxiu.views.widgets.TabIndicatorView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SalesMainActivity extends BaseActivity {

    private static final String TAB_FLAG_BUYS = "tabForBuys";
    private static final String TAB_FLAG_QT = "tabForQT";
    private static final String TAB_FLAG_OFFERS = "tabForOffers";
    private static final String TAB_FLAG_MINE = "tabForMine";

    @BindView(R.id.tab)
    FragmentTabHost tab;

    private TabIndicatorView buyIndicatorView;
    private TabIndicatorView offerIndicatorView;
    private TabIndicatorView mineIndicatorView;
    private TabIndicatorView qtIndicatorView;

    public static Intent getIntent(Context activity) {
        Intent intent = new Intent(activity, SalesMainActivity.class);
        if (!(activity instanceof BaseActivity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
    }

    public static void start(Context activity) {
        activity.startActivity(getIntent(activity));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initTabs();

        EventBusHelper.register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusHelper.unregister(this);
    }

    private void initTabs() {
        buyIndicatorView = TabIndicatorView.newBuyTabIndicator(this);
        offerIndicatorView = TabIndicatorView.newOfferTabIndicator(this);
        mineIndicatorView = TabIndicatorView.newMineTabIndicator(this);
        qtIndicatorView = TabIndicatorView.newQTTabIndicator(this);

        tab.setup(this, getSupportFragmentManager(), R.id.content);
        tab.addTab(tab.newTabSpec(TAB_FLAG_BUYS).setIndicator(buyIndicatorView), SalesUserFragment.class, null);
        tab.addTab(tab.newTabSpec(TAB_FLAG_QT).setIndicator(qtIndicatorView), SalesQtFragment.class, null);
        tab.addTab(tab.newTabSpec(TAB_FLAG_OFFERS).setIndicator(offerIndicatorView), SalesBuyFragment.class, null);
        tab.addTab(tab.newTabSpec(TAB_FLAG_MINE).setIndicator(mineIndicatorView), SalesMineFragment.class, null);

        tab.getTabWidget().setDividerDrawable(null);

        tab.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
//                if (StringUtils.equals(TAB_FLAG_BUYS, tabId)) {
//                    EventBusHelper.post(new OnBuyTabEvent());
//                } else if (StringUtils.equals(TAB_FLAG_OFFERS, tabId)) {
//                    EventBusHelper.post(new OnOfferTabEvent());
//                } else if (StringUtils.equals(TAB_FLAG_MINE, tabId)) {
//                    EventBusHelper.post(new OnMineTabEvent());
//                } else if (StringUtils.equals(TAB_FLAG_QT, tabId)) {
//                    EventBusHelper.post(new OnQtTabEvent());
//                }
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadDoingNumbers(MyIronsEventDoing myIronsEvent) {
        buyIndicatorView.setUnreadCount(BuyManager.instance().myIronsResponseForDoing.newSupplyNums);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }
}
