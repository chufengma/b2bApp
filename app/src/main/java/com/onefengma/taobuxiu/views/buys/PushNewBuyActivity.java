package com.onefengma.taobuxiu.views.buys;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.views.core.BaseActivity;
import com.onefengma.taobuxiu.views.widgets.ToolBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chufengma on 16/8/21.
 */
public class PushNewBuyActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    ToolBar toolbar;
    @BindView(R.id.list)
    LinearLayout list;
    @BindView(R.id.goto_add)
    TextView gotoAdd;

    public static void start(BaseActivity activity) {
        Intent intent = new Intent(activity, PushNewBuyActivity.class);
        activity.startActivity(intent);
    }


    @OnClick(R.id.right_image)
    public void clickOnRightImage() {
        EditBuyActivity.start(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_new_buy);
        ButterKnife.bind(this);
    }

}
