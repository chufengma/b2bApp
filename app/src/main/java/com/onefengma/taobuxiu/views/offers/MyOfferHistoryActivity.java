package com.onefengma.taobuxiu.views.offers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.OfferManager;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.model.entities.MyOfferHistoryInfo;
import com.onefengma.taobuxiu.model.events.MyIronOfferHistoryEvent;
import com.onefengma.taobuxiu.utils.NumbersUtils;
import com.onefengma.taobuxiu.utils.ToastUtils;
import com.onefengma.taobuxiu.views.core.BaseActivity;
import com.onefengma.taobuxiu.views.widgets.HistoryCardView;
import com.onefengma.taobuxiu.views.widgets.ProgressDialog;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyOfferHistoryActivity extends AppCompatActivity {


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
        Intent intent = new Intent(activity, MyOfferHistoryActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_offer_history);
        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(this);

        EventBusHelper.register(this);
        OfferManager.instance().getIronOfferHistroy();
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
            setupViews(event.myOfferHistoryInfo);
        } else {
            ToastUtils.showErrorTasty("加载求购历史失败, 请重试！");
            finish();
        }
    }

    private void setupViews(MyOfferHistoryInfo myBuyHistoryInfo) {
        todayBuy.setValue(myBuyHistoryInfo.todayOffer + "次");
        todayDone.setValue(myBuyHistoryInfo.todayWin + "次");
        todayDoneRate.setValue(NumbersUtils.getHS(NumbersUtils.round(myBuyHistoryInfo.todayWinRate, 4)));

        monthBuy.setValue(myBuyHistoryInfo.monthOffer + "次");
        monthDone.setValue(myBuyHistoryInfo.monthWin + "次");
        monthDoneRate.setValue(NumbersUtils.getHS(NumbersUtils.round(myBuyHistoryInfo.monthWinRate, 4)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusHelper.unregister(this);
    }
}
