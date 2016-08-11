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
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.views.core.BaseFragment;
import com.sdsmdg.tastytoast.TastyToast;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chufengma on 16/8/7.
 */
public class BuySubFragment extends BaseFragment {
    @BindView(R.id.recycler_view)
    PullToRefreshRecyclerView recyclerView;

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

        recyclerView.getRefreshableView().setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new SimpleTextHolder(new TextView(parent.getContext()));
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((TextView)holder.itemView).setText("Item " + position);
            }

            @Override
            public int getItemCount() {
                return 240;
            }
        });

        recyclerView.getRefreshableView().setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setMode(PullToRefreshBase.Mode.BOTH);

        recyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                TastyToast.makeText(getContext(), "刷新中", TastyToast.LENGTH_LONG, TastyToast.WARNING);
                refreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.onRefreshComplete();
                    }
                }, 2000);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                TastyToast.makeText(getContext(), "下拉更新中", TastyToast.LENGTH_LONG, TastyToast.DEFAULT);
                refreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.onRefreshComplete();
                    }
                }, 2000);
            }
        });

    }

    public static class SimpleTextHolder extends RecyclerView.ViewHolder {
        public SimpleTextHolder(TextView itemView) {
            super(itemView);
        }
    }

}
