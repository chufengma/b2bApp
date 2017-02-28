package com.taobuxiu.driver.views.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taobuxiu.driver.R;
import com.taobuxiu.driver.utils.StringUtils;
import com.taobuxiu.driver.utils.ToastUtils;
import com.taobuxiu.driver.views.core.BaseActivity;
import com.taobuxiu.driver.views.widgets.CountDownTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity  implements TextWatcher, CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.left_image_view)
    ImageView leftImageView;
    @BindView(R.id.left_image)
    FrameLayout leftImage;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.mobile_edit)
    EditText mobileEdit;
    @BindView(R.id.code_edit)
    EditText codeEdit;
    @BindView(R.id.get_code)
    CountDownTextView getCode;
    @BindView(R.id.password_edit)
    EditText passwordEdit;
    @BindView(R.id.agree_btn)
    CheckBox agreeBtn;
    @BindView(R.id.login)
    TextView login;
    @BindView(R.id.goto_login)
    TextView gotoLogin;
    @BindView(R.id.activity_login)
    LinearLayout activityLogin;

    public static void start(Activity context) {
        Intent starter = new Intent(context, RegisterActivity.class);
        context.startActivity(starter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        mobileEdit.addTextChangedListener(this);
        passwordEdit.addTextChangedListener(this);
        codeEdit.addTextChangedListener(this);
        agreeBtn.setOnCheckedChangeListener(this);
    }

    @OnClick(R.id.goto_login)
    public void clickOnGotoLogin() {
        finish();
    }

    @OnClick(R.id.login)
    public void onClick() {
        String mobile = mobileEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        String code = codeEdit.getText().toString();

    }

    private void check() {
        String mobile = mobileEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        String code = codeEdit.getText().toString();
        login.setEnabled(!StringUtils.isEmpty(mobile)
                && !StringUtils.isEmpty(password)
                && !StringUtils.isEmpty(code)
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
