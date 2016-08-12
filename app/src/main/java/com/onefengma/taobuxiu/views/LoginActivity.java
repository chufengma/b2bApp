package com.onefengma.taobuxiu.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.onefengma.taobuxiu.MainApplication;
import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.AuthManager;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.model.events.BaseStatusEvent;
import com.onefengma.taobuxiu.model.events.LoginEvent;
import com.onefengma.taobuxiu.utils.StringUtils;
import com.onefengma.taobuxiu.views.core.BaseActivity;
import com.onefengma.taobuxiu.views.widgets.ProgressDialog;
import com.onefengma.taobuxiu.views.widgets.ToolBar;

import org.greenrobot.eventbus.Subscribe;

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

    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mobile.addTextChangedListener(this);
        password.addTextChangedListener(this);
        EventBusHelper.register(this);
        progressDialog = new ProgressDialog(this);
    }

    public static void start(BaseActivity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }

    @OnClick(R.id.login)
    public void doLogin() {
        AuthManager.instance().doLogin(mobile.getText().toString(), password.getText().toString());
    }

    @Subscribe
    public void onLoginEvent(LoginEvent event) {
        if (event.status == BaseStatusEvent.STARTED) {
            progressDialog.show("登陆中...");
            return;
        }
        progressDialog.hide();
        if (event.status == BaseStatusEvent.SUCCESS) {
            MainApplication.getContext().finishActivities();
            MainActivity.start(this);
        }
    }

    @OnClick(R.id.register)
    public void doRegister() {
        RegisterActivity.start(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusHelper.unregister(this);
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
