package com.onefengma.taobuxiu.views.buys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.BuyManager;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.model.entities.IronBuyBrief;
import com.onefengma.taobuxiu.model.events.BaseStatusEvent;
import com.onefengma.taobuxiu.model.events.MyIronsEvent;
import com.onefengma.taobuxiu.views.core.BaseFragment;
import com.onefengma.taobuxiu.views.widgets.XRecyclerView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chufengma on 16/8/7.
 */
public class BuySubFragment extends BaseFragment {

    CustomAdapter customAdapter;

    @BindView(R.id.recycler_view)
    XRecyclerView recyclerView;

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

        customAdapter = new CustomAdapter();
        recyclerView.setAdapter(customAdapter);

        recyclerView.getRecyclerView().setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.getPullToRefreshRecyclerView().setMode(PullToRefreshBase.Mode.BOTH);

        recyclerView.getPullToRefreshRecyclerView().setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                BuyManager.instance().reloadMyIronBuys();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                BuyManager.instance().lodeMoreMyIronBuys();
            }
        });

    }

    @Subscribe
    public void onLoadMyIronBuys(MyIronsEvent myIronsEvent) {
        if (myIronsEvent.status == BaseStatusEvent.STARTED) {
            return;
        }
        recyclerView.getPullToRefreshRecyclerView().onRefreshComplete();
        customAdapter.setMyBuys(BuyManager.instance().ironBuys);
    }

    public static class CustomAdapter extends RecyclerView.Adapter {

        public List<IronBuyBrief> myBuys = new ArrayList<>();

        public void setMyBuys(List<IronBuyBrief> myBuys) {
            this.myBuys.clear();
            this.myBuys.addAll(myBuys);
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SimpleTextHolder(new TextView(parent.getContext()));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((TextView) holder.itemView).setText(myBuys.get(position).userId + ":" + myBuys.get(position).id);
        }

        @Override
        public int getItemCount() {
            return this.myBuys.size();
        }
    }

    ;

    public static class SimpleTextHolder extends RecyclerView.ViewHolder {
        public SimpleTextHolder(TextView itemView) {
            super(itemView);
        }
    }

}
