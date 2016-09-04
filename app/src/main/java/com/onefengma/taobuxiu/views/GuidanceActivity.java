package com.onefengma.taobuxiu.views;

import android.content.Intent;
import android.os.Bundle;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.views.core.BaseActivity;
import com.panxw.android.imageindicator.ImageIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuidanceActivity extends BaseActivity {

    @BindView(R.id.indicate_view)
    ImageIndicatorView imageIndicatorView;

    public static void start(BaseActivity activity) {
        Intent intent = new Intent(activity, GuidanceActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guidance);
        ButterKnife.bind(this);

        final Integer[] resArray = new Integer[] { R.drawable.account_guidance_bg_0, R.drawable.account_guidance_bg_1, R.drawable.account_guidance_bg_2, R.drawable.account_guidance_bg_3 };
        imageIndicatorView.setupLayoutByDrawable(resArray);
        imageIndicatorView.setIndicateStyle(ImageIndicatorView.INDICATE_USERGUIDE_STYLE);
        imageIndicatorView.show();
    }

    @OnClick(R.id.skip)
    public void clickOnSkip() {
        finish();
    }
}
