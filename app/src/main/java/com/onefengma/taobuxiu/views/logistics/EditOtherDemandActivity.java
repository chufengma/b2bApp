package com.onefengma.taobuxiu.views.logistics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.model.events.logistics.EditOtherDemandEvent;
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
    @BindView(R.id.self)
    CheckBox self;
    @BindView(R.id.store)
    CheckBox store;

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
        ArrayList<String> data = new ArrayList<>();
        if (taxi.isChecked()) {
            data.add("含税");
        }
        if (self.isChecked()) {
            data.add("自卸车");
        }
        if (store.isChecked()) {
            data.add("垫出库费");
        }
        EventBusHelper.post(new EditOtherDemandEvent(data));
        finish();
    }

}
