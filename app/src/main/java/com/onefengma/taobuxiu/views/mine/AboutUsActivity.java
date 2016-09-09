package com.onefengma.taobuxiu.views.mine;

import android.content.Intent;
import android.os.Bundle;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.views.core.BaseActivity;

public class AboutUsActivity extends BaseActivity {

    public static void start(BaseActivity activity) {
        Intent intent = new Intent(activity, AboutUsActivity.class);
        activity.startActivity(intent);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
    }
}
