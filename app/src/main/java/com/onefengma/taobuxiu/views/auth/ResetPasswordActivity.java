package com.onefengma.taobuxiu.views.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.AuthManager;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.model.events.BaseStatusEvent;
import com.onefengma.taobuxiu.model.events.OnResetPasswordEvent;
import com.onefengma.taobuxiu.utils.StringUtils;
import com.onefengma.taobuxiu.utils.ToastUtils;
import com.onefengma.taobuxiu.views.core.BaseActivity;
import com.onefengma.taobuxiu.views.widgets.ProgressDialog;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResetPasswordActivity extends BaseActivity {

    @BindView(R.id.mobile)
    EditText mobile;
    @BindView(R.id.msgCode)
    EditText msgCode;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.save)
    TextView save;

    private ProgressDialog progressDialog;

    public static void start(BaseActivity activity) {
        Intent intent = new Intent(activity, ResetPasswordActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);
        EventBusHelper.register(this);

        progressDialog = new ProgressDialog(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusHelper.unregister(this);
    }

    @OnClick(R.id.save)
    public void clickOnSave() {
        String msgCodeStr = msgCode.getText().toString();
        if (StringUtils.isEmpty(msgCodeStr)) {
            ToastUtils.showInfoTasty("验证码不能为空");
        }
        AuthManager.instance().doResetPassword(mobile.getText().toString(),
                msgCodeStr, password.getText().toString());
    }

    @OnClick(R.id.verify_code_counter)
    public void onMsgCodeClick() {
        AuthManager.instance().doGetMsgCode(mobile.getText().toString());
    }

    @Subscribe
    public void onEvent(OnResetPasswordEvent event) {
        if (event.status == BaseStatusEvent.STARTED) {
            progressDialog.show("更改中");
            return;
        } else if (event.status == BaseStatusEvent.SUCCESS) {
            ToastUtils.showSuccessTasty("更改成功，请重新登陆");
            LoginActivity.start(this);
        }
        progressDialog.dismiss();
    }

}
