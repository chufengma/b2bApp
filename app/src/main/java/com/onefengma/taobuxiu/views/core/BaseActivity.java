package com.onefengma.taobuxiu.views.core;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.onefengma.taobuxiu.MainApplication;
import com.onefengma.taobuxiu.R;

/**
 * Created by chufengma on 16/8/7.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View view = LayoutInflater.from(this).inflate(layoutResID, null);
        super.setContentView(view);

        View leftView = view.findViewById(R.id.left_image);
        if (leftView != null) {
            leftView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.getContext().addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainApplication.getContext().setCurrentActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainApplication.getContext().removeActivity(this);
    }
}
