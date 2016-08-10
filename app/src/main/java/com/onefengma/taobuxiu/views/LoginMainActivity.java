package com.onefengma.taobuxiu.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginMainActivity extends AppCompatActivity {

    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.goto_register)
    TextView gotoRegister;
    @BindView(R.id.goto_login)
    TextView gotoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        ButterKnife.bind(this);

//        gotoLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginMainActivity.this, LoginActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    @OnClick(R.id.goto_login)
    public void onGotoLoginClick() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
