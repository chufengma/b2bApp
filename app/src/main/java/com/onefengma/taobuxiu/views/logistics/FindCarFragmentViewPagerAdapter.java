package com.onefengma.taobuxiu.views.logistics;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.onefengma.taobuxiu.views.buys.BuyFragmentForDoing;
import com.onefengma.taobuxiu.views.buys.BuyFragmentForDone;
import com.onefengma.taobuxiu.views.buys.BuyFragmentForOutOfDate;

/**
 * Created by chufengma on 16/8/7.
 */
public class FindCarFragmentViewPagerAdapter extends FragmentPagerAdapter {

    public FindCarFragmentViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new LogisticsFindingCarListFragment();
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
