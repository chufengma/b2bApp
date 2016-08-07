package com.onefengma.taobuxiu;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.widget.FrameLayout;

import com.onefengma.taobuxiu.views.TabIndicatorView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
    }

}
