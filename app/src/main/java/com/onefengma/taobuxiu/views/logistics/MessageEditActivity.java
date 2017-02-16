package com.onefengma.taobuxiu.views.logistics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.model.events.logistics.EditMessageEvent;
import com.onefengma.taobuxiu.utils.StringUtils;
import com.onefengma.taobuxiu.utils.ToastUtils;
import com.onefengma.taobuxiu.views.core.BaseActivity;
import com.onefengma.taobuxiu.views.widgets.ToolBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageEditActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    ToolBar toolbar;
    @BindView(R.id.content)
    EditText content;
    @BindView(R.id.done)
    TextView done;
    @BindView(R.id.activity_message_edit)
    LinearLayout activityMessageEdit;


    public static void start(Activity activity, String message) {
        Intent intent = new Intent(activity, MessageEditActivity.class);
        intent.putExtra("message", message);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_edit);
        ButterKnife.bind(this);

        String message = getIntent().getStringExtra("message");
        content.setText(message);
    }

    @OnClick(R.id.done)
    public void onDone() {
        if (StringUtils.isEmpty(content.getText().toString())) {
            ToastUtils.showInfoTasty("请输入备注");
            return;
        }
        EventBusHelper.post(new EditMessageEvent(content.getText().toString()));
        finish();
    }

}
