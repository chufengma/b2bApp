package com.onefengma.taobuxiu.views.mine;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.BuyManager;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.model.events.MyIronAllHistoryEvent;
import com.onefengma.taobuxiu.utils.ToastUtils;
import com.onefengma.taobuxiu.views.core.BaseActivity;
import com.onefengma.taobuxiu.views.widgets.ProgressDialog;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyHistoryActivity extends BaseActivity {


    ProgressDialog progressDialog;
    @BindView(R.id.buy_times)
    TextView buyTimes;
    @BindView(R.id.buy_win_times)
    TextView buyWinTimes;
    @BindView(R.id.offer_times)
    TextView offerTimes;
    @BindView(R.id.offer_win_times)
    TextView offerWinTimes;

    public static void start(BaseActivity activity) {
        Intent intent = new Intent(activity, MyHistoryActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_history);
        ButterKnife.bind(this);
        EventBusHelper.register(this);
        progressDialog = new ProgressDialog(this);

        BuyManager.instance().getIronAllHistroy();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusHelper.unregister(this);
    }

    @Subscribe
    public void onIronBuyHistroyEvent(MyIronAllHistoryEvent event) {
        if (event.isStarted()) {
            progressDialog.show("加载中...");
            return;
        } else {
            progressDialog.dismiss();
        }

        if (event.isSuccess() && event.myAllHistoryInfo != null) {
            buyTimes.setText(event.myAllHistoryInfo.buyTimes + "");
            buyWinTimes.setText(event.myAllHistoryInfo.buyWinTimes + "");
            offerTimes.setText(event.myAllHistoryInfo.offerTimes + "");
            offerWinTimes.setText(event.myAllHistoryInfo.offerWinTimes + "");
        } else {
            ToastUtils.showErrorTasty("加载历史失败, 请重试！");
            finish();
        }
    }

}
