package com.onefengma.taobuxiu.views.buys;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.BuyManager;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.model.entities.IronBuyBrief;
import com.onefengma.taobuxiu.model.entities.IronBuyPush;
import com.onefengma.taobuxiu.model.events.MyIronsEventOutOfDate;
import com.onefengma.taobuxiu.utils.DialogUtils;
import com.onefengma.taobuxiu.utils.ThreadUtils;
import com.onefengma.taobuxiu.views.core.BaseFragment;
import com.onefengma.taobuxiu.views.widgets.listview.XListView;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chufengma on 16/8/7.
 */
public class BuyFragmentForOutOfDate extends BaseFragment {


    BuyListAdapter customAdapter;

    @BindView(R.id.recycler_view)
    XListView recyclerView;
    @BindView(R.id.emptyView)
    TextView emptyView;

    @Override
    public void onResume() {
        super.onResume();
        EventBusHelper.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBusHelper.unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy_sub, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        customAdapter = new BuyListAdapter(BuyManager.BuyStatus.OUT_OF_DATE);
        customAdapter.setOnBuyItemLongClickListener(new BuyListAdapter.OnBuyItemLongClickListener() {
            @Override
            public void onClickItem(final IronBuyBrief ironBuyBrief) {
                DialogUtils.showItemDialog(getActivity(), null, new String[]{"复制添加类似求购"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            IronBuyPush newPush = BuyManager.instance().transIronBuyPush(ironBuyBrief);
                            newPush.id = System.currentTimeMillis();
                            newPush.pushStatus = 0;
                            EditBuyActivity.start(getActivity(), newPush);
                        }
                    }
                });
            }
        });

        recyclerView.setAdapter(customAdapter);
        recyclerView.setOnRefreshListener(new XListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BuyManager.instance().reloadAllStatusBuys();
            }
        });

        recyclerView.setOnLoadMoreListener(new XListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                BuyManager.instance().lodeMoreMyIronBuysForOutForDate();
            }
        });

        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.fakePullRefresh();
                recyclerView.enableLoadMore(true);
                recyclerView.setAutoLoadMore(true);
            }
        }, 200);

        recyclerView.setEmptyView(emptyView);
    }

    @OnClick(R.id.emptyView)
    public void clickOnEmptyView() {
        recyclerView.fakePullRefresh();
    }

    @Subscribe
    public void onLoadMyIronBuys(MyIronsEventOutOfDate myIronsEvent) {
        if (myIronsEvent.isRefreshComplete()) {
            recyclerView.onRefreshComplete(false);
        }

        recyclerView.onLoadMoreComplete();

        recyclerView.enableLoadMore(BuyManager.instance().myIronsResponseForOutOfDate.buys != null
                && BuyManager.instance().myIronsResponseForOutOfDate.buys.size() > 0
                && BuyManager.instance().myIronsResponseForOutOfDate.buys.size() % 15 == 0);

        customAdapter.setMyBuys(BuyManager.instance().myIronsResponseForOutOfDate.buys);
    }

}
