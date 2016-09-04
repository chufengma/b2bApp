package com.onefengma.taobuxiu.views.mine;

import android.content.Intent;
import android.os.Bundle;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.BuyManager;
import com.onefengma.taobuxiu.manager.OfferManager;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.model.events.MyIronBuyHistoryEvent;
import com.onefengma.taobuxiu.model.events.MyIronOfferHistoryEvent;
import com.onefengma.taobuxiu.utils.NumbersUtils;
import com.onefengma.taobuxiu.utils.ToastUtils;
import com.onefengma.taobuxiu.views.core.BaseActivity;
import com.onefengma.taobuxiu.views.widgets.HistoryCardView;
import com.onefengma.taobuxiu.views.widgets.ProgressDialog;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyHistoryActivity extends BaseActivity {


    ProgressDialog progressDialog;
    @BindView(R.id.month_buy)
    HistoryCardView monthBuy;
    @BindView(R.id.month_done)
    HistoryCardView monthDone;
    @BindView(R.id.month_done_rate)
    HistoryCardView monthDoneRate;
    @BindView(R.id.month_offer)
    HistoryCardView monthOffer;
    @BindView(R.id.month_win)
    HistoryCardView monthWin;
    @BindView(R.id.month_win_rate)
    HistoryCardView monthWinRate;

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

        BuyManager.instance().getIronBuyHistroy();
        OfferManager.instance().getIronOfferHistroy();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusHelper.unregister(this);
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
            monthBuy.setValue(event.myBuyHistoryInfo.monthBuy + "次");
            monthDone.setValue(event.myBuyHistoryInfo.monthDone + "次");
            monthDoneRate.setValue(NumbersUtils.getHS(NumbersUtils.round(event.myBuyHistoryInfo.monthDoneRate, 4)));
        } else {
            ToastUtils.showErrorTasty("加载历史失败, 请重试！");
            finish();
        }
    }

    @Subscribe
    public void onIronBuyHistroyEvent(MyIronOfferHistoryEvent event) {
        if (event.isStarted()) {
            progressDialog.show("加载中...");
            return;
        } else {
            progressDialog.dismiss();
        }

        if (event.isSuccess()) {
            monthOffer.setValue(event.myOfferHistoryInfo.monthOffer + "次");
            monthWin.setValue(event.myOfferHistoryInfo.monthWin + "次");
            monthWinRate.setValue(NumbersUtils.getHS(NumbersUtils.round(event.myOfferHistoryInfo.monthWinRate, 4)));
        } else {
            ToastUtils.showErrorTasty("加载历史失败, 请重试！");
            finish();
        }
    }
}
