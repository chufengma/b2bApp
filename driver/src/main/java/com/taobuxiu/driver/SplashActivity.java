package com.taobuxiu.driver;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.taobuxiu.driver.model.Constant;
import com.taobuxiu.driver.model.LogisticisDriver;
import com.taobuxiu.driver.utils.SPHelper;
import com.taobuxiu.driver.utils.ThreadUtils;
import com.taobuxiu.driver.views.auth.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    public static void start(Activity context) {
        Intent starter = new Intent(context, SplashActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LogisticisDriver userProfile = SPHelper.top().get(Constant.StorageKeys.DRIVER_PROFILE, LogisticisDriver.class);
                if (userProfile == null) {
                    LoginActivity.start(SplashActivity.this);
                } else {
                    MainActivity.start(SplashActivity.this);
                }
                finish();
            }
        }, 1000);
    }
}
