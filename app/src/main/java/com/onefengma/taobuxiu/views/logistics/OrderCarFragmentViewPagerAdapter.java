package com.onefengma.taobuxiu.views.logistics;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by chufengma on 16/8/7.
 */
public class OrderCarFragmentViewPagerAdapter extends FragmentPagerAdapter {

    public static String[] data = new String[]{"全部", "待提货", "在途中", "已送达", "已完成"};

    public OrderCarFragmentViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new LogisticsOrderCarListFragment();
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return data[position];
    }
}
