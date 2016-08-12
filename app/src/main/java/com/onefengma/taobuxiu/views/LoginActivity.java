package com.onefengma.taobuxiu.views;

import android.content.Intent;
import android.os.Bundle;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.views.core.BaseActivity;
import com.onefengma.taobuxiu.views.widgets.ProgressDialog;
import com.onefengma.taobuxiu.views.widgets.ToolBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    ToolBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    public static void start(BaseActivity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }

}
