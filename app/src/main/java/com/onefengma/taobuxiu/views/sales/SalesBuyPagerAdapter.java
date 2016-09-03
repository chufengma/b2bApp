package com.onefengma.taobuxiu.views.sales;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by chufengma on 16/8/7.
 */
public class SalesBuyPagerAdapter extends FragmentPagerAdapter {

    public SalesBuyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("status", position);
        SalesBaseBuyListFragment baseOfferStatusFragment = new SalesBaseBuyListFragment();
        baseOfferStatusFragment.setArguments(bundle);
        return baseOfferStatusFragment;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position) * 1000;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:return "求购中";
            case 1:return "已完成";
            default:return "已过期";
        }
    }
}
