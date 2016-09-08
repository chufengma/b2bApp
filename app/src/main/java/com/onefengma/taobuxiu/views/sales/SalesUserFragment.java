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
import com.onefengma.taobuxiu.model.events.sales.SalesGetSellersEvent;
import com.onefengma.taobuxiu.model.events.sales.SalesGetUsers;
import com.onefengma.taobuxiu.views.core.BaseFragment;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author yfchu
 * @date 2016/9/1
 */
public class SalesUserFragment extends BaseFragment {

    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slaes_user, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SalesUserPagerAdapter adapter = new SalesUserPagerAdapter(getFragmentManager());
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);

        tab.setupWithViewPager(viewPager);
    }

    @Subscribe
    public void onLoadUserEvent(SalesGetUsers event) {
        tab.getTabAt(1).setText("普通用户(" + SalesUserManager.instance().salesBindUserResponse.maxCount  + ")");
    }

    @Subscribe
    public void onLoadSellersEvent(SalesGetSellersEvent event) {
        tab.getTabAt(0).setText("商家用户(" +SalesUserManager.instance().salesBindSellerResponse.maxCount  + ")");
    }

}
