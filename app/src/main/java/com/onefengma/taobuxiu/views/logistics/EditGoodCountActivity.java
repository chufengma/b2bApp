package com.onefengma.taobuxiu.views.logistics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.model.events.logistics.GoodsChooseEvent;
import com.onefengma.taobuxiu.utils.ToastUtils;
import com.onefengma.taobuxiu.views.core.BaseActivity;
import com.onefengma.taobuxiu.views.widgets.ToolBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditGoodCountActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    ToolBar toolbar;
    @BindView(R.id.content)
    EditText content;
    @BindView(R.id.done)
    TextView done;
    @BindView(R.id.activity_message_edit)
    LinearLayout activityMessageEdit;

    public static void start(Activity activity, String goodsTitle, String goodsContent, String requestId) {
        Intent intent = new Intent(activity, EditGoodCountActivity.class);
        intent.putExtra("goodsTitle", goodsTitle);
        intent.putExtra("goodsContent", goodsContent);
        intent.putExtra("requestId", requestId);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_good_count);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.done)
    public void onClick() {
        try {
            if (Double.parseDouble(content.getText().toString()) != 0) {
                EventBusHelper.post(new GoodsChooseEvent(getIntent().getStringExtra("requestId"), getIntent().getStringExtra("goodsTitle"), getIntent().getStringExtra("goodsContent"), Double.parseDouble(content.getText().toString())));
                finish();
            } else {
                ToastUtils.showInfoTasty("请输入正确的运货数量");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showInfoTasty("请输入正确的运货数量");
        }
    }
}
