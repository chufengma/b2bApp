package com.onefengma.taobuxiu.views.sales;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.onefengma.taobuxiu.views.buys.BuyFragmentForDoing;

/**
 * Created by chufengma on 16/8/7.
 */
public class SalesUserPagerAdapter extends FragmentPagerAdapter {

    public SalesUserPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SalesSellerUserFragment();
            default:
                return new SalesNormalUserFragment();
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return position == 0 ? "商家用户" : "普通用户";
    }
}
