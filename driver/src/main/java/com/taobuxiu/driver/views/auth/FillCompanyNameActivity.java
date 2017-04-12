package com.taobuxiu.driver.views.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.taobuxiu.driver.views.core.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FillCompanyNameActivity extends BaseActivity implements TextWatcher {

    @BindView(R.id.company)
    EditText company;
    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.activity_fill_company_name)
    LinearLayout activityFillCompanyName;

    public static void start(Activity context) {
        Intent starter = new Intent(context, FillCompanyNameActivity.class);
        context.startActivity(starter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_company_name);
        ButterKnife.bind(this);
        submit.setEnabled(false);

        company.addTextChangedListener(this);
    }

    @OnClick(R.id.submit)
    public void onClick()  {
        Driver userProfile =  SPHelper.top().get(Constant.StorageKeys.DRIVER_PROFILE, Driver.class);
        if (userProfile == null) {
            ToastUtils.showInfoTasty("未登录");
        }
        HttpHelper.wrap(HttpHelper.create(AuthManager.AuthService.class).fillCompany(userProfile.id, company.getText().toString())).subscribe(new HttpHelper.SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse baseResponse) {
                Driver userProfile = JSONHelper.parse(baseResponse.data.toString(), Driver.class);
                SPHelper.top().save(Constant.StorageKeys.DRIVER_PROFILE, userProfile);
                AuthManager.ins().checkToGoto(FillCompanyNameActivity.this);
                finish();
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        submit.setEnabled(!StringUtils.isEmpty(company.getText().toString()));
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
