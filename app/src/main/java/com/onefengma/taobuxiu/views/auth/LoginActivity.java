package com.onefengma.taobuxiu.views.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.onefengma.taobuxiu.MainApplication;
import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.AuthManager;
import com.onefengma.taobuxiu.manager.PushManager;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.model.Constant;
import com.onefengma.taobuxiu.model.events.BaseListStatusEvent;
import com.onefengma.taobuxiu.model.events.LoginEvent;
import com.onefengma.taobuxiu.utils.SPHelper;
import com.onefengma.taobuxiu.utils.StringUtils;
import com.onefengma.taobuxiu.views.MainActivity;
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
    @BindView(R.id.switch_app)
    TextView switchApp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mobile.addTextChangedListener(this);
        password.addTextChangedListener(this);
        progressDialog = new ProgressDialog(this);

        switchApp.setVisibility(MainApplication.FEGNMA_FALG ? View.VISIBLE : View.GONE);
        switchApp.setText(MainApplication.IS_SALES_APP ? R.string.setting_switch_app : R.string.setting_switch_sales);
    }

    @OnClick(R.id.switch_app)
    public void doSwitch() {
        MainApplication.getContext().switchApp(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBusHelper.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBusHelper.unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String userTel = SPHelper.top().sp().getString(Constant.StorageKeys.USER_TEL, "");
        if (!StringUtils.isEmpty(userTel)) {
            mobile.setText(userTel);
        }
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
        if (event.status == BaseListStatusEvent.STARTED) {
            progressDialog.show("登陆中...");
            return;
        }
        progressDialog.dismiss();
        if (event.status == BaseListStatusEvent.SUCCESS) {
            MainApplication.getContext().finishActivities();
            PushManager.instance().setCurrentUserAccount();
            MainActivity.start(this);
        }
    }

    @OnClick(R.id.register)
    public void doRegister() {
        RegisterActivity.start(this);
    }

    @OnClick(R.id.reset_password)
    public void doReset() {
        ResetPasswordActivity.start(this);
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
