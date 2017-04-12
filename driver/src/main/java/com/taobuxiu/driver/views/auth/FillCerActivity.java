package com.taobuxiu.driver.views.auth;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.taobuxiu.driver.R;
import com.taobuxiu.driver.views.core.BaseActivity;

import butterknife.OnClick;

public class FillCerActivity extends BaseActivity {

    public static void start(Activity context) {
        Intent starter = new Intent(context, FillCerActivity.class);
        context.startActivity(starter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_cer);
    }

    @OnClick({R.id.license, R.id.tax, R.id.road, R.id.account, R.id.companyCode, R.id.})
    public void onImageAddClick() {

    }

}
