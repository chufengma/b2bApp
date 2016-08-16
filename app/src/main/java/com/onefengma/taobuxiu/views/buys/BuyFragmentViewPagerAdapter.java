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
                return new BuyFragmentForDoing();
            case 1:
                return new BuyFragmentForDone();
            case 2:
                return new BuyFragmentForOutOfDate();
        }
        return new BuyFragmentForDoing();
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
