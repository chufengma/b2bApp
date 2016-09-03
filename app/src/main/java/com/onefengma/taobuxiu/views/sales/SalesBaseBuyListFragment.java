package com.onefengma.taobuxiu.views.sales;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.BuyManager;
import com.onefengma.taobuxiu.manager.BuyManager.BuyStatus;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.model.entities.IronBuyBrief;
import com.onefengma.taobuxiu.model.entities.QtDetail;
import com.onefengma.taobuxiu.model.events.sales.SalesBuyListEvent;
import com.onefengma.taobuxiu.model.events.sales.SalesQtListEvent;
import com.onefengma.taobuxiu.views.buys.BuyListAdapter;
import com.onefengma.taobuxiu.views.core.BaseActivity;
import com.onefengma.taobuxiu.views.core.BaseFragment;
import com.onefengma.taobuxiu.views.sales.SalesQtManager.SalesQtStatus;
import com.onefengma.taobuxiu.views.widgets.listview.XListView;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chufengma on 16/9/3.
 */
public class SalesBaseBuyListFragment extends BaseFragment {

    @BindView(R.id.list_view)
    XListView listView;
    @BindView(R.id.emptyView)
    TextView emptyView;

    BuyListAdapter buyAdapter;

    BuyStatus buyStatus;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sales_qt_list_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buyAdapter = new BuyListAdapter(buyStatus);

        buyAdapter.setOnBuyItemClickListener(new BuyListAdapter.OnBuyItemClickListener() {
            @Override
            public void onClickItem(String ironId) {
                SalesBuyDetailActivity.start((BaseActivity) getActivity(), ironId);
            }
        });

        listView.setAdapter(buyAdapter);

        listView.setEmptyView(emptyView);

        listView.setOnRefreshListener(new XListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SalesBuyManager.instance().refreshQtList(buyStatus);
            }
        });

        listView.setOnLoadMoreListener(new XListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                SalesBuyManager.instance().loadMoreQtList(buyStatus);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        listView.fakePullRefresh();
    }

    @OnClick(R.id.emptyView)
    public void onEmptyViewClick() {
        listView.fakePullRefresh();
    }

    @Subscribe
    public void onQtListEvent(SalesBuyListEvent event) {
        if (event.buyStatus != buyStatus) {
            return;
        }
        if (event.isRefreshComplete()) {
            listView.onRefreshComplete(false);
        }

        if (event.isLoadComplete()) {
            listView.onLoadMoreComplete();
        }

        List<IronBuyBrief> data = SalesBuyManager.instance().salesIronsBuyResponses[buyStatus.ordinal()].buys;
        listView.enableLoadMore(data != null
                && data.size() > 0
                && data.size() % 15 == 0);

        buyAdapter.setMyBuys(data);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusHelper.register(this);
        buyStatus = BuyStatus.values()[getArguments().getInt("status", 0)];
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusHelper.unregister(this);
    }
}
