package com.onefengma.taobuxiu.views.buys;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by chufengma on 16/8/7.
 */
public class BuyFragmentViewPagerAdapter extends FragmentPagerAdapter {

    public BuyFragmentViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new BuySubFragment();
            case 1:
                return new BuySub1Fragment();
            case 2:
                return new BuySub2Fragment();
        }
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
