package com.onefengma.taobuxiu.views.sales;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.onefengma.taobuxiu.MainApplication;
import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.PushManager;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.model.events.BaseListStatusEvent;
import com.onefengma.taobuxiu.model.events.sales.SalesLoginEvent;
import com.onefengma.taobuxiu.utils.StringUtils;
import com.onefengma.taobuxiu.views.core.BaseActivity;
import com.onefengma.taobuxiu.views.widgets.ProgressDialog;
import com.onefengma.taobuxiu.views.widgets.ToolBar;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SalesLoginActivity extends BaseActivity implements TextWatcher {

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
        setContentView(R.layout.activity_sales_login);
        ButterKnife.bind(this);

        mobile.addTextChangedListener(this);
        password.addTextChangedListener(this);
        EventBusHelper.register(this);
        progressDialog = new ProgressDialog(this);
    }

    public static void start(BaseActivity activity) {
        Intent intent = new Intent(activity, SalesLoginActivity.class);
        activity.startActivity(intent);
    }

    @OnClick(R.id.login)
    public void doLogin() {
        SalesAuthManager.instance().login(mobile.getText().toString(), password.getText().toString());
    }

    @Subscribe
    public void onLoginEvent(SalesLoginEvent event) {
        if (event.status == BaseListStatusEvent.STARTED) {
            progressDialog.show("登陆中...");
            return;
        }
        progressDialog.dismiss();
        if (event.status == BaseListStatusEvent.SUCCESS) {
            MainApplication.getContext().finishActivities();
            PushManager.instance().setCurrentUserAccount();
            SalesMainActivity.start(this);
        }
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
