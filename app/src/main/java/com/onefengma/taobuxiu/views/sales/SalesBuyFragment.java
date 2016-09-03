package com.onefengma.taobuxiu.views.sales;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.views.core.BaseFragment;
import com.onefengma.taobuxiu.views.widgets.ToolBar;

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
}
