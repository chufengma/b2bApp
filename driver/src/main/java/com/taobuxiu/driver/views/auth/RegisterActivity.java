package com.taobuxiu.driver.views.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taobuxiu.driver.R;
import com.taobuxiu.driver.views.core.BaseActivity;
import com.taobuxiu.driver.views.widgets.CountDownTextView;

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

    public static void start(Activity context) {
        Intent starter = new Intent(context, RegisterActivity.class);
        context.startActivity(starter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.goto_login)
    public void clickOnGotoLogin() {
        finish();
    }

}
