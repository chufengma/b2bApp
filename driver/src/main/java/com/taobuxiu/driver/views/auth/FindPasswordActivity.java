package com.taobuxiu.driver.views.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taobuxiu.driver.MainActivity;
import com.taobuxiu.driver.MainApplication;
import com.taobuxiu.driver.R;
import com.taobuxiu.driver.managers.AuthManager;
import com.taobuxiu.driver.model.BaseResponse;
import com.taobuxiu.driver.model.Constant;
import com.taobuxiu.driver.model.Driver;
import com.taobuxiu.driver.network.HttpHelper;
import com.taobuxiu.driver.utils.SPHelper;
import com.taobuxiu.driver.utils.StringUtils;
import com.taobuxiu.driver.utils.ToastUtils;
import com.taobuxiu.driver.utils.helpers.JSONHelper;
import com.taobuxiu.driver.utils.helpers.VerifyHelper;
import com.taobuxiu.driver.views.core.BaseActivity;
import com.taobuxiu.driver.views.widgets.CountDownTextView;
import com.taobuxiu.driver.views.widgets.ProgressDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindPasswordActivity extends BaseActivity  implements TextWatcher, CompoundButton.OnCheckedChangeListener  {

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
    @BindView(R.id.change)
    TextView change;
    @BindView(R.id.activity_login)
    LinearLayout activityLogin;

    ProgressDialog progressDialog;

    public static void start(Activity context) {
        Intent starter = new Intent(context, FindPasswordActivity.class);
        context.startActivity(starter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        ButterKnife.bind(this);


        progressDialog = new ProgressDialog(this);
        mobileEdit.addTextChangedListener(this);
        passwordEdit.addTextChangedListener(this);
        codeEdit.addTextChangedListener(this);
    }

    private void check() {
        String mobile = mobileEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        String code = codeEdit.getText().toString();
        change.setEnabled(!StringUtils.isEmpty(mobile)
                && !StringUtils.isEmpty(password)
                && !StringUtils.isEmpty(code));
    }

    @OnClick(R.id.get_code)
    public void clickGetCode() {
        String mobile = mobileEdit.getText().toString();
        if (StringUtils.isEmpty(mobile)) {
            ToastUtils.showInfoTasty("请输入手机号码");
            return;
        }
        if (!VerifyHelper.isMobile(mobile)) {
            ToastUtils.showInfoTasty("请输入正确的手机号码");
            return;
        }
        AuthManager.ins().doGetMsgCode(mobile);
    }

    @OnClick(R.id.change)
    public void onClick() {
        String mobile = mobileEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        String code = codeEdit.getText().toString();
        progressDialog.show();
        HttpHelper.wrap(HttpHelper.create(AuthManager.AuthService.class).changePassword(mobile, password, code)).subscribe(new HttpHelper.SimpleNetworkSubscriber<BaseResponse>() {

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                super.onFailed(baseResponse, e);
                progressDialog.hide();
            }

            @Override
            public void onSuccess(BaseResponse baseResponse) {
                progressDialog.hide();
                Driver userProfile =  SPHelper.top().get(Constant.StorageKeys.DRIVER_PROFILE, Driver.class);
                if (userProfile == null) {
                    Driver newUserProfile = JSONHelper.parse(baseResponse.data.toString(), Driver.class);
                    SPHelper.top().save(Constant.StorageKeys.DRIVER_PROFILE, newUserProfile);
                    MainApplication.ins().finishActivities();
                    MainActivity.start(FindPasswordActivity.this);
                } else {
                    finish();
                }
            }
        });
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
