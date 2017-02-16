package com.onefengma.taobuxiu.views.logistics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.model.events.logistics.ChooseDeadLineEvent;
import com.onefengma.taobuxiu.model.events.logistics.EditOtherDemandEvent;
import com.onefengma.taobuxiu.utils.ToastUtils;
import com.onefengma.taobuxiu.views.core.BaseActivity;
import com.onefengma.taobuxiu.views.widgets.ToolBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditOtherDemandActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    ToolBar toolbar;
    @BindView(R.id.done)
    TextView done;
    @BindView(R.id.activity_edit_other_demand)
    LinearLayout activityEditOtherDemand;
    @BindView(R.id.taxi)
    CheckBox taxi;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, EditOtherDemandActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_other_demand);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.done)
    public void onDone() {
        EventBusHelper.post(new EditOtherDemandEvent(taxi.isChecked() ? Arrays.asList("含税") : Collections.<String>emptyList()));
        finish();
    }

}
