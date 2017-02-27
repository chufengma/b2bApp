package com.taobuxiu.driver.views.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taobuxiu.driver.R;
import com.taobuxiu.driver.utils.StringUtils;
import com.taobuxiu.driver.utils.ToastUtils;
import com.taobuxiu.driver.views.core.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements TextWatcher, CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.mobile_edit)
    EditText mobileEdit;
    @BindView(R.id.password_edit)
    EditText passwordEdit;
    @BindView(R.id.agree_btn)
    CheckBox agreeBtn;
    @BindView(R.id.login)
    TextView login;
    @BindView(R.id.activity_login)
    LinearLayout activityLogin;

    public static void start(Activity context) {
        Intent starter = new Intent(context, LoginActivity.class);
        context.startActivity(starter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        login.setEnabled(false);

        mobileEdit.addTextChangedListener(this);
        passwordEdit.addTextChangedListener(this);

        agreeBtn.setOnCheckedChangeListener(this);
    }

    @OnClick(R.id.login)
    public void onClick() {
        ToastUtils.showInfoTasty("dengluing");
    }


    @OnClick(R.id.register)
    public void onRegisterClick() {
        RegisterActivity.start(this);
    }

    @OnClick(R.id.find_password)
    public void onFindPasswordClick() {
        FindPasswordActivity.start(this);
    }

    private void check() {
        String mobile = mobileEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        login.setEnabled(!StringUtils.isEmpty(mobile)
                && !StringUtils.isEmpty(password)
                && agreeBtn.isChecked());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        check();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        check();
    }
}
