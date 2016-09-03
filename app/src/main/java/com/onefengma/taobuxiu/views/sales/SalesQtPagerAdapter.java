package com.onefengma.taobuxiu.views.sales;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.onefengma.taobuxiu.views.buys.BuyFragmentForDoing;
import com.onefengma.taobuxiu.views.offers.BaseOfferStatusFragment;
import com.onefengma.taobuxiu.views.qt.QTFragment;

/**
 * Created by chufengma on 16/8/7.
 */
public class SalesQtPagerAdapter extends FragmentPagerAdapter {

    public SalesQtPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("status", position);
        SalesBaseQtListFragment baseOfferStatusFragment = new SalesBaseQtListFragment();
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
            case 0:return "等待质检";
            case 1:return "质检完成";
            default:return "质检取消";
        }
    }
}
