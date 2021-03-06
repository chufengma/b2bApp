package com.onefengma.taobuxiu.views.sales;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

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
        if (position == 0) {
            bundle.putInt("status", 0);
        } else if (position == 1) {
            bundle.putInt("status", 3);
        } else if (position == 2) {
            bundle.putInt("status", 1);
        } else {
            bundle.putInt("status", 2);
        }
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
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:return "等待质检";
            case 1:return "质检中";
            case 2:return "质检完成";
            default:return "质检取消";
        }
    }
}
