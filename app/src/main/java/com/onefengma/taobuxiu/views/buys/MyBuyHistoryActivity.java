package com.onefengma.taobuxiu.views.buys;

import android.content.Intent;
import android.os.Bundle;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.BuyManager;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.model.entities.MyBuyHistoryInfo;
import com.onefengma.taobuxiu.model.events.MyIronBuyHistoryEvent;
import com.onefengma.taobuxiu.utils.NumbersUtils;
import com.onefengma.taobuxiu.utils.ToastUtils;
import com.onefengma.taobuxiu.views.core.BaseActivity;
import com.onefengma.taobuxiu.views.widgets.HistoryCardView;
import com.onefengma.taobuxiu.views.widgets.ProgressDialog;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyBuyHistoryActivity extends BaseActivity {

    @BindView(R.id.today_buy)
    HistoryCardView todayBuy;
    @BindView(R.id.today_done)
    HistoryCardView todayDone;
    @BindView(R.id.today_done_rate)
    HistoryCardView todayDoneRate;
    @BindView(R.id.month_buy)
    HistoryCardView monthBuy;
    @BindView(R.id.month_done)
    HistoryCardView monthDone;
    @BindView(R.id.month_done_rate)
    HistoryCardView monthDoneRate;

    ProgressDialog progressDialog;

    public static void start(BaseActivity activity) {
        Intent intent = new Intent(activity, MyBuyHistoryActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_buy_history);
        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(this);

        EventBusHelper.register(this);
        BuyManager.instance().getIronBuyHistroy();
    }

    @Subscribe
    public void onIronBuyHistroyEvent(MyIronBuyHistoryEvent event) {
        if (event.isStarted()) {
            progressDialog.show("加载中...");
            return;
        } else {
            progressDialog.dismiss();
        }

        if (event.isSuccess()) {
            setupViews(event.myBuyHistoryInfo);
        } else {
            ToastUtils.showErrorTasty("加载求购历史失败, 请重试！");
            finish();
        }
    }

    private void setupViews(MyBuyHistoryInfo myBuyHistoryInfo) {
        todayBuy.setValue(myBuyHistoryInfo.todayBuy + "次");
        todayDone.setValue(myBuyHistoryInfo.todayDone + "次");
        todayDoneRate.setValue(NumbersUtils.getHS(NumbersUtils.round(myBuyHistoryInfo.todayDoneRate, 4)));

        monthBuy.setValue(myBuyHistoryInfo.monthBuy + "次");
        monthDone.setValue(myBuyHistoryInfo.monthDone + "次");
        monthDoneRate.setValue(NumbersUtils.getHS(NumbersUtils.round(myBuyHistoryInfo.monthDoneRate, 4)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusHelper.unregister(this);
    }
}
