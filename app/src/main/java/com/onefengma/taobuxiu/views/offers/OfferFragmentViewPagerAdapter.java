package com.onefengma.taobuxiu.views.offers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.onefengma.taobuxiu.views.buys.BuyFragmentForDoing;
import com.onefengma.taobuxiu.views.buys.BuyFragmentForDone;
import com.onefengma.taobuxiu.views.buys.BuyFragmentForOutOfDate;

/**
 * Created by chufengma on 16/8/7.
 */
public class OfferFragmentViewPagerAdapter extends FragmentPagerAdapter {

    public OfferFragmentViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(BaseOfferStatusFragment.OFFER_STATUS, position);
        BaseOfferStatusFragment baseOfferStatusFragment = new BaseOfferStatusFragment();
        baseOfferStatusFragment.setArguments(bundle);
        return baseOfferStatusFragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Sub " + position;
    }
}
