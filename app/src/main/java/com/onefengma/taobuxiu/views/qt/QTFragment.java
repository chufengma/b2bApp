package com.onefengma.taobuxiu.views.qt;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.BuyManager;
import com.onefengma.taobuxiu.manager.QtManager;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.model.events.OnMineTabEvent;
import com.onefengma.taobuxiu.model.events.QtListEvent;
import com.onefengma.taobuxiu.views.core.BaseFragment;
import com.onefengma.taobuxiu.views.widgets.ToolBar;
import com.onefengma.taobuxiu.views.widgets.listview.XListView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chufengma on 16/8/7.
 */
public class QTFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    ToolBar toolbar;
    @BindView(R.id.list_view)
    XListView listView;
    @BindView(R.id.emptyView)
    TextView emptyView;

    QtListAdapter qtListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qt, container, false);
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
                QtManager.instance().refreshQtList();
            }
        });

        listView.setOnLoadMoreListener(new XListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                QtManager.instance().loadMoreQtList();
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
        if (event.isRefreshComplete()) {
            listView.onRefreshComplete(false);
        }

        if (event.isLoadComplete()) {
            listView.onLoadMoreComplete();
        }
        listView.enableLoadMore(QtManager.instance().qtListResponse.qts != null
                && QtManager.instance().qtListResponse.qts.size() > 0
                && QtManager.instance().qtListResponse.qts.size() % 15 == 0);

        qtListAdapter.setMyBuys(QtManager.instance().qtListResponse.qts);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTabEvent(OnMineTabEvent OnQtTabEvent) {
        System.out.println("-----------------OnQtTabEvent");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusHelper.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusHelper.unregister(this);
    }
}


