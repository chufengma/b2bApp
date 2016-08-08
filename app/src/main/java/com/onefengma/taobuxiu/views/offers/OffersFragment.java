package com.onefengma.taobuxiu.views.offers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.model.EventBusHelper;
import com.onefengma.taobuxiu.model.events.OnOfferTabEvent;
import com.onefengma.taobuxiu.views.core.BaseFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by chufengma on 16/8/7.
 */
public class OffersFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_offers, container, false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTabEvent(OnOfferTabEvent onOfferTabEvent) {
        System.out.println("-----------------onOfferTabEvent");
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusHelper.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusHelper.unregister(this);
    }

}
