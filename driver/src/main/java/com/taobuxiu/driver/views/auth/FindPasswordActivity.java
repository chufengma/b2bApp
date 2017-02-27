package com.taobuxiu.driver.views.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.taobuxiu.driver.R;
import com.taobuxiu.driver.views.core.BaseActivity;

public class FindPasswordActivity extends BaseActivity {

    public static void start(Activity context) {
        Intent starter = new Intent(context, FindPasswordActivity.class);
        context.startActivity(starter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
    }
}
