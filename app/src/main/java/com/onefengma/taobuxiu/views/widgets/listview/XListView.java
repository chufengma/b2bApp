/**
 * @file XListView.java
 * @package me.maxwin.view
 * @create Mar 18, 2012 6:28:41 PM
 * @author Maxwin
 * @description An ListView support (a) Pull down to refresh, (b) Pull up to load more.
 * Implement IXListViewListener, and see stopRefresh() / stopLoadMore().
 */
package com.onefengma.taobuxiu.views.widgets.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Scroller;

import com.onefengma.taobuxiu.R;

import java.util.Date;

public class XListView extends ListView implements OnScrollListener {
    private float mLastY = -1; // save event y
    private Scroller mScroller; // used for scroll back
    private OnScrollListener mScrollListener; // user's scroll listener

    // the interface to trigger refresh and load more.
    private OnRefreshListener onRefreshListener;
    private OnLoadMoreListener onLoadMoreListener;

    // -- header view
    protected XListViewHeader mHeaderView;
    private int mHeaderViewHeight; // header view's height
    private boolean mEnablePullRefresh = true;
    protected boolean mPullRefreshing = false; // is refreashing.

    private boolean computeHeaderLoadingScrollStarted;
    private boolean resetHeaderRequested;

    // -- footer view
    private XListViewFooter mFooterView;
    private boolean mEnableLoadMore;
    private boolean autoLoadMore = true;
    private boolean mLoadingMore;
    private boolean mIsFooterReady = false;
    private int pullLoadMoreDelta;

    // total list items, used to detect is at the bottom of listview.
    private int mTotalItemCount;

    private int lastTotalItemCount;

    // for mScroller, scroll back from header or footer.
    private int mScrollBack;
    private final static int SCROLLBACK_HEADER = 0;
    private final static int SCROLLBACK_FOOTER = 1;

    // scroll back duration
    private final static int SCROLL_DURATION = 300;

    // support iOS like pull feature.
    private final static float OFFSET_RADIO = 1.8f;

    /**
     * @param context
     */
    public XListView(Context context) {
        super(context);
        initWithContext(context, null);
    }

    public XListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWithContext(context, attrs);
    }

    public XListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWithContext(context, attrs);
    }

    private void initWithContext(Context context, AttributeSet attrs) {
        mScroller = new Scroller(context, new DecelerateInterpolator());
        // XListView need the scroll event, and it will dispatch the event to
        // user's listener (as a proxy).
        super.setOnScrollListener(this);

        // init header view
        mHeaderView = new XListViewHeader(context, attrs);
        addHeaderView(mHeaderView);
        // init footer view
        mFooterView = new XListViewFooter(context, attrs);

        // init header height
        mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mHeaderViewHeight = mHeaderView.getViewContent().getHeight();
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        pullLoadMoreDelta = context.getResources().getDimensionPixelSize(R.dimen.xlist_view_pull_load_more_delta);
        // 防止MX等系统自带下拉刷新的ListView, 下拉停顿和下拉高度不准确
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        // make sure XListViewFooter is the last footer view, and only add once.
        if (mIsFooterReady == false) {
            mIsFooterReady = true;
            addFooterView(mFooterView);
            mFooterView.hide();
        }
        super.setAdapter(adapter);
    }

    /**
     * enable or disable pull down refresh feature.
     *
     * @param enable
     */
    public void enablePullRefresh(boolean enable) {
        if (mEnablePullRefresh == enable) {
            return;
        }

        mEnablePullRefresh = enable;
        if (!mEnablePullRefresh) { // disable, hide the content
            mHeaderView.getViewContent().setVisibility(View.INVISIBLE);
        } else {
            mHeaderView.getViewContent().setVisibility(View.VISIBLE);
        }
    }

    public boolean isEnablePullRefresh() {
        return mEnablePullRefresh;
    }

    /**
     * Enable or disable pull up load more feature.
     * Must be call before the data in the attached adapter refreshed.
     *
     * @param enable
     */
    public void enableLoadMore(boolean enable) {
        if (mEnableLoadMore == enable) {
            return;
        }

        mEnableLoadMore = enable;
        if (!mEnableLoadMore) {
            mFooterView.hide();
            mFooterView.setOnClickListener(null);
        } else {
            mFooterView.show();
            mFooterView.setState(XListViewFooter.STATE_NORMAL);
            // both "pull up" and "click" will invoke load more.
            mFooterView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startLoadMore();
                }
            });
        }
    }

    /**
     * autoLoadMore is enabled by default.
     */
    public void setAutoLoadMore(boolean autoLoadMore) {
        this.autoLoadMore = autoLoadMore;
    }

    /**
     * stop refresh, reset header view.
     */
    public boolean onRefreshComplete(boolean setNewTime) {
        resetAutoLoadMoreState();
        if (mPullRefreshing == true) {
            mPullRefreshing = false;
            resetHeaderHeight();
            mHeaderView.setState(XListViewHeader.STATE_NORMAL);
            if (setNewTime) {
                setRefreshTime(new Date());
            }
            return true;
        }
        return false;
    }

    public void resetAutoLoadMoreState() {
        lastTotalItemCount = 0;
    }

    /**
     * stop load more, reset footer view.
     */
    public void onLoadMoreComplete() {
        if (mLoadingMore == true) {
            mLoadingMore = false;
            resetFooterHeight();
            mFooterView.setState(XListViewFooter.STATE_NORMAL);
        }
    }

    public void setRefreshTime(Date date) {
        if (date == null) {
            return;
        }
    }

    public void setHeaderOffset(int valueInPixel) {
        mHeaderView.setHeaderOffset(valueInPixel);
    }

    public int getHeaderOffset() {
        return mHeaderView.getHeaderOffset();
    }

    private void invokeOnScrolling() {
        if (mScrollListener instanceof OnXScrollListener) {
            OnXScrollListener l = (OnXScrollListener) mScrollListener;
            l.onXScrolling(this);
        }
    }

    private void updateHeaderHeight(float delta) {
        mHeaderView.setVisiableHeight((int) (delta) + mHeaderView.getVisiableHeight());
        if (mEnablePullRefresh && !mPullRefreshing) { // 未处于刷新状态，更新箭头
            if (onRefreshListener != null &&
                    mHeaderView.getVisiableHeight() > getHeaderActionHeight() && delta >= 0) {
                mHeaderView.setState(XListViewHeader.STATE_READY);
            } else if (delta < 0 && mHeaderView.getVisiableHeight() < getHeaderActionHeight()) {
                mHeaderView.setState(XListViewHeader.STATE_NORMAL);
            }
        }

        if (delta > 0 && mPullRefreshing) {
            setSelection(0); // scroll to top each time
        }
    }

    private int getHeaderActionHeight() {
        return mHeaderView.getHeaderOffset() + mHeaderViewHeight;
    }

    /**
     * reset header view's height.
     */
    protected void resetHeaderHeight() {
        resetHeaderRequested = true;

        if (computeHeaderLoadingScrollStarted) {
            return;
        }

        resetHeaderRequested = false;

        int height = mHeaderView.getVisiableHeight();
        if (height == mHeaderView.getHeaderOffset()) {
            return;
        }
        // refreshing and header isn't shown fully. do nothing.
        if (mPullRefreshing && height <= getHeaderActionHeight()) {
            return;
        }
        int finalHeight = mHeaderView.getHeaderOffset(); // default: scroll back to dismiss header.
        // is refreshing, just scroll back to show all the header.
        if (mPullRefreshing && height > getHeaderActionHeight()) {
            finalHeight = getHeaderActionHeight();
        }
        mScrollBack = SCROLLBACK_HEADER;
        mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
        // trigger computeScroll
        invalidate();
    }

    private void updateFooterHeight(float delta) {
        int height = (int) delta + mFooterView.getVisiableHeight();
        if (mEnableLoadMore && !mLoadingMore) {

            if (height - mFooterView.getFooterOffset() > pullLoadMoreDelta) { // height enough to invoke load more.
                mFooterView.setState(XListViewFooter.STATE_READY);
            } else {
                mFooterView.setState(XListViewFooter.STATE_NORMAL);
            }
        }
        mFooterView.setVisiableHeight(height);

        if (delta < 0) {
            setSelection(mTotalItemCount - 1); // scroll to bottom
        }
    }

    private void resetFooterHeight() {
        int scrollBackDistance;
        if (mLoadingMore && mFooterView.getFooterOffset() == 0) {
            scrollBackDistance = mFooterView.getVisiableHeight() - pullLoadMoreDelta;
        } else {
            scrollBackDistance = mFooterView.getVisiableHeight() - mFooterView.getFooterOffset();
        }

        if (scrollBackDistance > 0) {
            mScrollBack = SCROLLBACK_FOOTER;
            mScroller.startScroll(0, mFooterView.getVisiableHeight(), 0, -scrollBackDistance, SCROLL_DURATION);
            invalidate();
        }
    }

    private void startLoadMore() {
        if (onLoadMoreListener != null && mLoadingMore != true) {
            mLoadingMore = true;
            mFooterView.setState(XListViewFooter.STATE_LOADING);
            onLoadMoreListener.onLoadMore();
        }
    }

    public void fakePullRefresh() {
        if (onRefreshListener == null) {
            return;
        }

        if (showLoadingState()) {
            onRefreshListener.onRefresh();
        }
    }

    public boolean isPullRefreshing() {
        return mPullRefreshing;
    }

    public boolean showLoadingState() {
        if (mPullRefreshing) {
            return false;
        }

        // mHeaderViewHeight could be 0 if this method called before mHeaderView layout completed.
        if (mHeaderViewHeight == 0) {
            // WARNING: We can directly get the view height by layout parameter because mHeaderView.getViewContent() has specify an exact size of height.
            mHeaderViewHeight = mHeaderView.getViewContent().getLayoutParams().height;
        }

        // scroll to top
        setSelection(0);


        mPullRefreshing = true;
        mHeaderView.setState(XListViewHeader.STATE_REFRESHING);

        enablePullRefresh(true);

        // Avoid triggering autoLoadMore
        if (autoLoadMore) {
            enableLoadMore(false);
        }

        mScrollBack = SCROLLBACK_HEADER;
        mScroller.startScroll(0, mHeaderView.getHeaderOffset(), 0,
                getHeaderActionHeight() - mHeaderView.getHeaderOffset(), SCROLL_DURATION / 2);
        computeHeaderLoadingScrollStarted = true;

        // trigger computeScroll
        invalidate();
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (mEnablePullRefresh && getFirstVisiblePosition() == 0
                        && (mHeaderView.getVisiableHeight() - mHeaderView.getHeaderOffset() >= 0)) {
                    // the first item is showing, header has shown or pull down.
                    updateHeaderHeight(deltaY / OFFSET_RADIO);
                    if (mHeaderView.getVisiableHeight() - mHeaderView.getHeaderOffset() > 0 && deltaY < 0) {
                        setSelection(0);
                    }
                    invokeOnScrolling();
                } else if (!autoLoadMore && mEnableLoadMore && getLastVisiblePosition() == mTotalItemCount - 1
                        && (mFooterView.getVisiableHeight() - mFooterView.getFooterOffset() > 0 || deltaY < 0)) {
                    // last item, already pulled up or want to pull up.
                    updateFooterHeight(-deltaY / OFFSET_RADIO);
                }
                break;
            default:
                mLastY = -1; // reset
                if (getFirstVisiblePosition() == 0) {
                    // invoke refresh
                    if (mEnablePullRefresh && mHeaderView.getVisiableHeight() > getHeaderActionHeight()
                            && mHeaderView.getState() == XListViewHeader.STATE_READY) {
                        mPullRefreshing = true;
                        mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
                        if (onRefreshListener != null) {
                            onRefreshListener.onRefresh();
                        }
                        break;
                    }
                    resetHeaderHeight();
                } else if (getLastVisiblePosition() == mTotalItemCount - 1) {
                    // invoke load more.
                    if (mEnableLoadMore && mFooterView.getVisiableHeight() - mFooterView.getFooterOffset() > pullLoadMoreDelta) {
                        startLoadMore();
                    }
                    resetFooterHeight();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mScrollBack == SCROLLBACK_HEADER) {
                mHeaderView.setVisiableHeight(mScroller.getCurrY());
            } else {
                mFooterView.setVisiableHeight(mScroller.getCurrY());
            }
            postInvalidate();
            invokeOnScrolling();
        } else {
            computeHeaderLoadingScrollStarted = false;
            if (resetHeaderRequested) {
                resetHeaderHeight();
            }
        }
        super.computeScroll();
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mScrollListener = l;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mScrollListener != null) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // send to user's listener
        mTotalItemCount = totalItemCount;
        if (mScrollListener != null) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }

        if (lastTotalItemCount == 0) {
            lastTotalItemCount = getFooterViewsCount() + getHeaderViewsCount();
        }

        if (autoLoadMore && mEnableLoadMore &&
                totalItemCount > lastTotalItemCount &&
                getLastVisiblePosition() == totalItemCount - 1) {
            lastTotalItemCount = totalItemCount;
            startLoadMore();
        }
    }

    /**
     * you can listen ListView.OnScrollListener or this one. it will invoke
     * onXScrolling when header/footer scroll back.
     */
    public interface OnXScrollListener extends OnScrollListener {
        public void onXScrolling(View view);
    }

    /**
     * implements this interface to get refresh
     */
    public interface OnRefreshListener {
        public void onRefresh();
    }

    /**
     * implements this interface to get load more event.
     */
    public interface OnLoadMoreListener {
        public void onLoadMore();
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public void setVisibility(int visibility) {
//        super.setVisibility(visibility);
    }

    public void setVisibilityTrue(int visibility) {
        super.setVisibility(visibility);
    }
}