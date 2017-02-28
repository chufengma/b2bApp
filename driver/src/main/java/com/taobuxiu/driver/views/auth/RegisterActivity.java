package com.taobuxiu.driver.views.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
<<<<<<< Updated upstream
import android.support.v7.app.AppCompatActivity;
=======
import android.text.Editable;
import android.text.TextWatcher;
>>>>>>> Stashed changes
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taobuxiu.driver.MainActivity;
import com.taobuxiu.driver.MainApplication;
import com.taobuxiu.driver.R;
<<<<<<< Updated upstream
=======
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
>>>>>>> Stashed changes
import com.taobuxiu.driver.views.core.BaseActivity;
import com.taobuxiu.driver.views.widgets.CountDownTextView;
import com.taobuxiu.driver.views.widgets.ProgressDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

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

    ProgressDialog progressDialog;

    public static void start(Activity context) {
        Intent starter = new Intent(context, RegisterActivity.class);
        context.startActivity(starter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
<<<<<<< Updated upstream
=======
        progressDialog = new ProgressDialog(this);

        mobileEdit.addTextChangedListener(this);
        passwordEdit.addTextChangedListener(this);
        codeEdit.addTextChangedListener(this);
        agreeBtn.setOnCheckedChangeListener(this);
>>>>>>> Stashed changes
    }

    @OnClick(R.id.goto_login)
    public void clickOnGotoLogin() {
        finish();
    }

<<<<<<< Updated upstream
=======
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

    @OnClick(R.id.login)
    public void onClick() {
        String mobile = mobileEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        String code = codeEdit.getText().toString();
        progressDialog.show();
        HttpHelper.wrap(HttpHelper.create(AuthManager.AuthService.class).register(mobile, password, code)).subscribe(new HttpHelper.SimpleNetworkSubscriber<BaseResponse>() {

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                super.onFailed(baseResponse, e);
                progressDialog.hide();
            }

            @Override
            public void onSuccess(BaseResponse baseResponse) {
                progressDialog.hide();
                Driver userProfile = JSONHelper.parse(baseResponse.data.toString(), Driver.class);
                SPHelper.top().save(Constant.StorageKeys.DRIVER_PROFILE, userProfile);
                MainApplication.ins().finishActivities();
                MainActivity.start(RegisterActivity.this);
            }
        });
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


>>>>>>> Stashed changes
}
