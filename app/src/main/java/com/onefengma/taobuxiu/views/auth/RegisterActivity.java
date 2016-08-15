package com.onefengma.taobuxiu.views.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.AuthManager;
import com.onefengma.taobuxiu.utils.StringUtils;
import com.onefengma.taobuxiu.views.core.BaseActivity;
import com.onefengma.taobuxiu.views.widgets.CountDownTextView;
import com.onefengma.taobuxiu.views.widgets.ToolBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, TextWatcher {

    @BindView(R.id.toolbar)
    ToolBar toolbar;
    @BindView(R.id.mobile)
    EditText mobile;
    @BindView(R.id.verify)
    EditText verify;
    @BindView(R.id.verify_code_counter)
    CountDownTextView verifyCodeCounter;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.register)
    TextView register;
    @BindView(R.id.agree_btn)
    CheckBox agreeBtn;

    public static void start(BaseActivity activity) {
        Intent intent = new Intent(activity, RegisterActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        agreeBtn.setOnCheckedChangeListener(this);
        mobile.addTextChangedListener(this);
        verify.addTextChangedListener(this);
        password.addTextChangedListener(this);
    }

    @OnClick(R.id.verify_code_counter)
    public void onMsgCodeClick() {
        AuthManager.instance().doGetMsgCode(mobile.getText().toString());
    }

    @OnClick(R.id.register)
    public void onRegisterClick() {
        AuthManager.instance().doRegister(mobile.getText().toString(), password.getText().toString(), verify.getText().toString());
    }

    private void updateRegisterEnable() {
        register.setEnabled(agreeBtn.isChecked() && !StringUtils.isEmpty(mobile.getText().toString())
                    && !StringUtils.isEmpty(verify.getText().toString())
                    && !StringUtils.isEmpty(password.getText().toString()));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        updateRegisterEnable();
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
