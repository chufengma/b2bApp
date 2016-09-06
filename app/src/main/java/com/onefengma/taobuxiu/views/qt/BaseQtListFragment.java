package com.onefengma.taobuxiu.views.qt;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.QtManager;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.model.entities.QtDetail;
import com.onefengma.taobuxiu.model.events.QtListEvent;
import com.onefengma.taobuxiu.views.core.BaseFragment;
import com.onefengma.taobuxiu.views.sales.SalesQtManager;
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
public class BaseQtListFragment extends BaseFragment {

    @BindView(R.id.list_view)
    XListView listView;
    @BindView(R.id.emptyView)
    TextView emptyView;

    QtListAdapter qtListAdapter;

    SalesQtStatus qtStatus;

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
        qtListAdapter = new QtListAdapter();

        listView.setAdapter(qtListAdapter);

        listView.setEmptyView(emptyView);

        listView.setOnRefreshListener(new XListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                QtManager.instance().refreshQtList(qtStatus);
            }
        });

        listView.setOnLoadMoreListener(new XListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                QtManager.instance().loadMoreQtList(qtStatus);
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
    public void onQtListEvent(QtListEvent event) {
        if (event.qtStatus != qtStatus) {
            return;
        }
        if (event.isRefreshComplete()) {
            listView.onRefreshComplete(false);
        }

        if (event.isLoadComplete()) {
            listView.onLoadMoreComplete();
        }

        List<QtDetail> qtList = QtManager.instance().qtListResponses[qtStatus.ordinal()].qts;
        listView.enableLoadMore(qtList != null
                && qtList.size() > 0
                && qtList.size() % 15 == 0);

        qtListAdapter.setMyBuys(qtList);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusHelper.register(this);
        qtStatus = SalesQtStatus.values()[getArguments().getInt("status", 0)];
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusHelper.unregister(this);
    }
}
