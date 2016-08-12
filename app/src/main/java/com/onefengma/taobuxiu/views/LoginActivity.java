package com.onefengma.taobuxiu.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.AuthManager;
import com.onefengma.taobuxiu.utils.StringUtils;
import com.onefengma.taobuxiu.views.core.BaseActivity;
import com.onefengma.taobuxiu.views.widgets.ToolBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements TextWatcher {

    @BindView(R.id.toolbar)
    ToolBar toolbar;
    @BindView(R.id.mobile)
    EditText mobile;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.login)
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mobile.addTextChangedListener(this);
        password.addTextChangedListener(this);
    }

    public static void start(BaseActivity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }

    @OnClick(R.id.login)
    public void doLogin() {
        AuthManager.instance().doLogin(mobile.getText().toString(), password.getText().toString());
    }

    @OnClick(R.id.register)
    public void doRegister() {
        RegisterActivity.start(this);
    }

    private void updateRegisterEnable() {
        login.setEnabled(!StringUtils.isEmpty(mobile.getText().toString())
                && !StringUtils.isEmpty(password.getText().toString()));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        updateRegisterEnable();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
