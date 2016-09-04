package com.onefengma.taobuxiu.views.qt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.views.core.BaseActivity;

public class QtDescActivity extends BaseActivity {

    public static void start(BaseActivity activity) {
        Intent intent = new Intent(activity, QtDescActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qt_desc);
    }

}
