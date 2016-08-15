package com.onefengma.taobuxiu.views.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.views.core.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResetPasswordActivity extends BaseActivity {

    @BindView(R.id.mobile)
    EditText mobile;
    @BindView(R.id.msgCode)
    EditText msgCode;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.save)
    TextView save;

    public static void start(BaseActivity activity) {
        Intent intent = new Intent(activity, ResetPasswordActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);
    }

}
