package com.taobuxiu.driver;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.taobuxiu.driver.managers.AuthManager;
import com.taobuxiu.driver.views.auth.FillCompanyNameActivity;

public class MainActivity extends AppCompatActivity {

    public static void start(Activity context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AuthManager.ins().checkToGoto(this);
    }

}
