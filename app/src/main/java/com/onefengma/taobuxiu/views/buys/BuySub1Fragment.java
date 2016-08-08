package com.onefengma.taobuxiu.views.buys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.BuyManager;
import com.onefengma.taobuxiu.views.core.BaseFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chufengma on 16/8/7.
 */
public class BuySub1Fragment extends BaseFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy_sub1, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.get)
    public void onGetClick() {
        BuyManager.demoGet();
    }

    @OnClick(R.id.get_params)
    public void onGeParamstClick() {
        BuyManager.demoGetWithParams();
    }

    @OnClick(R.id.post)
    public void onPostClick() {
        BuyManager.demoPost();
    }

    @OnClick(R.id.file)
    public void onFileClick() {
        BuyManager.demoFile();
    }
}
