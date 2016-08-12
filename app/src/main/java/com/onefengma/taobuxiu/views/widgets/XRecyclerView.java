package com.onefengma.taobuxiu.views.widgets;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.onefengma.taobuxiu.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author yfchu
 * @date 2016/8/12
 */
public class XRecyclerView extends LinearLayout {

    @BindView(R.id.emptyView)
    TextView emptyView;
    @BindView(R.id.recycler_view_content)
    PullToRefreshRecyclerView recyclerView;

    public XRecyclerView(Context context) {
        this(context, null);
    }

    public XRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.recycler_view_x, this);
        ButterKnife.bind(this, view);
    }

    final private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkIfEmpty();
        }
    };

    public RecyclerView getRecyclerView() {
        return recyclerView.getRefreshableView();
    }

    public PullToRefreshRecyclerView getPullToRefreshRecyclerView() {
        return recyclerView;
    }

    private void checkIfEmpty() {
        if (emptyView != null && recyclerView.getRefreshableView().getAdapter() != null) {
            final boolean emptyViewVisible =
                    recyclerView.getRefreshableView().getAdapter().getItemCount() == 0;
            emptyView.setVisibility(emptyViewVisible ? VISIBLE : GONE);
        }
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        final RecyclerView.Adapter oldAdapter = recyclerView.getRefreshableView().getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        recyclerView.getRefreshableView().setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }

        checkIfEmpty();
    }

    public void setEmptyView(View emptyView) {
        checkIfEmpty();
    }

}
