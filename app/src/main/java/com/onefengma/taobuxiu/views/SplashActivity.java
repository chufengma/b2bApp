package com.onefengma.taobuxiu.views;

import android.content.Intent;
import android.os.Bundle;

import com.onefengma.taobuxiu.MainApplication;
import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.AuthManager;
import com.onefengma.taobuxiu.manager.PushManager;
import com.onefengma.taobuxiu.model.Constant;
import com.onefengma.taobuxiu.model.entities.UserProfile;
import com.onefengma.taobuxiu.utils.SPHelper;
import com.onefengma.taobuxiu.utils.ThreadUtils;
import com.onefengma.taobuxiu.views.core.BaseActivity;
import com.onefengma.taobuxiu.views.sales.SalesAuthManager;
import com.onefengma.taobuxiu.views.sales.SalesMainActivity;
import com.onefengma.taobuxiu.views.sales.SalesManDetail;

public class SplashActivity extends BaseActivity {

    public static void start(BaseActivity activity) {
        Intent intent = new Intent(activity, SplashActivity.class);
        activity.startActivity(intent);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(MainApplication.IS_SALES_APP) {
                    SalesManDetail salesManDetail = SPHelper.top().get(Constant.StorageKeys.SALES_PROFILE, SalesManDetail.class);
                    if (salesManDetail == null) {
                        SalesAuthManager.startLoginActivity();
                    } else {
                        PushManager.instance().setCurrentUserAccount();
                        SalesMainActivity.start(SplashActivity.this);
                    }
                } else {
                    UserProfile userProfile = SPHelper.top().get(Constant.StorageKeys.USER_PROFILE, UserProfile.class);
                    if (userProfile == null) {
                        AuthManager.startLoginActivity();
                    } else {
                        PushManager.instance().setCurrentUserAccount();
                        MainActivity.start(SplashActivity.this);
                    }
                }
                finish();
            }
        }, 1000);
    }

}
