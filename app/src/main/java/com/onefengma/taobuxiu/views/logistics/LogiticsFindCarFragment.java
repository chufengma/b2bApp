package com.onefengma.taobuxiu.views.logistics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.views.core.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dev on 2017/2/15.
 */

public class LogiticsFindCarFragment extends BaseFragment {

    @BindView(R.id.time_limit)
    View timeLimit;
    @BindView(R.id.edit)
    ImageView edit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.logitics_find_car_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @OnClick(R.id.time_limit)
    public void onTimeLimitClick(View view) {
        ChooseDeadlineActivity.start(getActivity());
    }
}

