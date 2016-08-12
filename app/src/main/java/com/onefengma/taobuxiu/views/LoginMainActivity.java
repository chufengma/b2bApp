package com.onefengma.taobuxiu.views;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.views.core.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginMainActivity extends BaseActivity {

    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.goto_register)
    TextView gotoRegister;
    @BindView(R.id.goto_login)
    TextView gotoLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.goto_login)
    public void onGotoLoginClick() {
        LoginActivity.start(this);
    }

    @OnClick(R.id.goto_register)
    public void onGotoRegisterClick() {
        RegisterActivity.start(this);
    }
}
