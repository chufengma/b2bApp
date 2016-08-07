package com.onefengma.taobuxiu.views.buys;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by chufengma on 16/8/7.
 */
public class BuyFragmentViewPagerAdapter extends FragmentStatePagerAdapter {

    public BuyFragmentViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new BuySubFragment();
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Sub " + position;
    }
}
