package com.onefengma.taobuxiu.views.sales;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.model.events.sales.SalesBuyListEvent;
import com.onefengma.taobuxiu.views.core.BaseFragment;
import com.onefengma.taobuxiu.views.widgets.ToolBar;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author yfchu
 * @date 2016/9/1
 */
public class SalesBuyFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    ToolBar toolbar;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.sales_buy_view_pager)
    ViewPager salesBuyViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sales_buy, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SalesBuyPagerAdapter adapter = new SalesBuyPagerAdapter(getFragmentManager());
        salesBuyViewPager.setOffscreenPageLimit(3);
        salesBuyViewPager.setAdapter(adapter);

        tab.setupWithViewPager(salesBuyViewPager);
        toolbar.getLeftImageLayout().setVisibility(View.GONE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusHelper.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusHelper.unregister(this);
    }

    @Subscribe
    public void onQtListEvent(SalesBuyListEvent event) {
        switch (event.buyStatus) {
            case DOING:
                tab.getTabAt(0).setText("求购中(" + SalesBuyManager.instance().salesIronsBuyResponses[event.buyStatus.ordinal()].maxCount  + ")");
                break;
            case DONE:
                tab.getTabAt(1).setText("已完成(" +SalesBuyManager.instance().salesIronsBuyResponses[event.buyStatus.ordinal()].maxCount  + ")");
                break;
            case OUT_OF_DATE:
                tab.getTabAt(2).setText("已过期(" +SalesBuyManager.instance().salesIronsBuyResponses[event.buyStatus.ordinal()].maxCount  + ")");
                break;
        };
    }

}
