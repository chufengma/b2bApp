package com.onefengma.taobuxiu.views.buys;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lhh.ptrrv.library.PullToRefreshRecyclerView;
import com.lhh.ptrrv.library.footer.loadmore.BaseLoadMoreView;
import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.views.core.BaseFragment;

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

        TextView loadView = new TextView(getContext());
        loadView.setText("加载更多");

        TextView pullView = new TextView(getContext());
        pullView.setText("下拉刷新");

        recyclerView.setAdapter(new RecyclerView.Adapter() {
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

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    public static class SimpleTextHolder extends RecyclerView.ViewHolder {
        public SimpleTextHolder(TextView itemView) {
            super(itemView);
        }
    }

    public static class LoadMoreView extends BaseLoadMoreView {

        public LoadMoreView(Context context, RecyclerView recyclerView) {
            super(context, recyclerView);
        }


    }
}
