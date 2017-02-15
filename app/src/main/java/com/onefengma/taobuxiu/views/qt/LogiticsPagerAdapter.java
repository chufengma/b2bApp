package com.onefengma.taobuxiu.views.qt;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.onefengma.taobuxiu.views.logistics.LogiticsCarBookingFragment;
import com.onefengma.taobuxiu.views.logistics.LogiticsCarOrderFragment;
import com.onefengma.taobuxiu.views.logistics.LogiticsFindCarFragment;

/**
 * Created by chufengma on 16/8/7.
 */
public class LogiticsPagerAdapter extends FragmentPagerAdapter {

    public LogiticsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        Fragment fragment = null;
        if (position == 0) {
            bundle.putInt("status", 0);
            fragment = new LogiticsFindCarFragment();
        } else if (position == 1) {
            bundle.putInt("status", 1);
            fragment = new LogiticsCarBookingFragment();
        } else {
            bundle.putInt("status", 2);
            fragment = new LogiticsCarOrderFragment();
        }
        fragment.setArguments(bundle);
        return fragment;
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
            case 0:return "询价找车";
            case 1:return "正在找车";
            default:return "订单跟踪";
        }
    }
}
