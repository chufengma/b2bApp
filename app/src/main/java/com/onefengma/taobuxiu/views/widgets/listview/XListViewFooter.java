/**
 * @file XFooterView.java
 * @create Mar 31, 2012 9:33:43 PM
 * @author Maxwin
 * @description XListView's footer
 */
package com.onefengma.taobuxiu.views.widgets.listview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;


public class XListViewFooter extends LinearLayout {
    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_LOADING = 2;

    private View mContentView;
    private ImageView mArrowImageView;
    private View mProgressBar;
    private TextView mHintTextView;

    private int mState = STATE_NORMAL;

    private int footerOffset;
    private int footerHintNormal;
    private int footerHintReady;
    private int footerHintLoading;

    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;

    public XListViewFooter(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.XListView);
        footerOffset = array.getInteger(R.styleable.XListView_footerOffset, getResources().getDimensionPixelSize(R.dimen.xlist_view_footer_offset));
        footerHintNormal = array.getResourceId(R.styleable.XListView_footerHintNormal, R.string.xlist_view_footer_hint_normal);
        footerHintReady = array.getResourceId(R.styleable.XListView_footerHintReady, R.string.xlist_view_footer_hint_ready);
        footerHintLoading = array.getResourceId(R.styleable.XListView_footerHintLoading, R.string.xlist_view_footer_hint_loading);
        int hintTextColor = array.getColor(R.styleable.XListView_hintTextColor, getResources().getColor(R.color.xlist_view_hint_text));
        boolean showFooterArrow = array.getBoolean(R.styleable.XListView_showFooterArrow, false);
        array.recycle();

        mContentView = LayoutInflater.from(context).inflate(R.layout.xlist_view_footer, null);
        mContentView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, footerOffset));
        addView(mContentView);

        if (showFooterArrow) {
            mArrowImageView = (ImageView) ((ViewStub) mContentView.findViewById(R.id.xlist_view_footer_arrow)).inflate();
            mArrowImageView.setVisibility(View.VISIBLE);
        }

        mProgressBar = mContentView.findViewById(R.id.xlist_view_footer_progressbar);

        mHintTextView = (TextView) mContentView.findViewById(R.id.xlist_view_footer_hint_textview);
        mHintTextView.setTextColor(hintTextColor);
        mHintTextView.setText(footerHintNormal);

        mRotateUpAnim = new RotateAnimation(-180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateUpAnim.setDuration(180);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(0.0f, 180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateDownAnim.setDuration(180);
        mRotateDownAnim.setFillAfter(true);
    }

    public void setState(int state) {
        if (state == mState) {
            return;
        }

        if (state == STATE_LOADING) { // 显示进度
            if (mArrowImageView != null) {
                mArrowImageView.clearAnimation();
                mArrowImageView.setVisibility(View.INVISIBLE);
            }
            mProgressBar.setVisibility(View.VISIBLE);
        } else { // 显示箭头图片
            if (mArrowImageView != null) {
                mArrowImageView.setVisibility(View.VISIBLE);
            }
            mProgressBar.setVisibility(View.INVISIBLE);
        }

        switch (state) {
            case STATE_NORMAL:
                if (mArrowImageView != null) {
                    if (mState == STATE_READY) {
                        mArrowImageView.clearAnimation();
                        mArrowImageView.startAnimation(mRotateUpAnim);
                    } else if (mState == STATE_LOADING) {
                        mArrowImageView.clearAnimation();
                    }
                }
                mHintTextView.setText(footerHintNormal);
                break;
            case STATE_READY:
                if (mState != STATE_READY) {
                    if (mArrowImageView != null) {
                        mArrowImageView.clearAnimation();
                        mArrowImageView.startAnimation(mRotateDownAnim);
                    }
                    mHintTextView.setText(footerHintReady);
                }
                break;
            case STATE_LOADING:
                mHintTextView.setText(footerHintLoading);
                break;
            default:
                break;
        }

        mState = state;
    }

    public void setVisiableHeight(int height) {
        if (mContentView.getVisibility() != View.VISIBLE) {
            return;
        }

        if (height < footerOffset) {
            height = footerOffset;
        }

        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        lp.height = height;
        mContentView.setLayoutParams(lp);

    }

    public int getVisiableHeight() {
        return mContentView.getLayoutParams().height;
    }

    public int getFooterOffset() {
        return footerOffset;
    }

    /**
     * normal status
     */
    public void normal() {
        mHintTextView.setText(R.string.xlist_view_footer_hint_normal);
        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * loading status
     */
    public void loading() {
        mHintTextView.setText(R.string.xlist_view_hint_loading);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * hide footer when disable pull load more
     */
    public void hide() {
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        lp.height = 0;
        mContentView.setLayoutParams(lp);

        mContentView.setVisibility(View.INVISIBLE);
    }

    /**
     * show footer
     */
    public void show() {
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        lp.height = footerOffset;
        mContentView.setLayoutParams(lp);

        mContentView.setVisibility(View.VISIBLE);
    }

    public View getViewContent() {
        return mContentView;
    }

    public void setArrowImage(int resId) {
        if (mArrowImageView != null) {
            mArrowImageView.setImageResource(resId);
        }
    }

    public void setHintTextColor(int color) {
        mHintTextView.setTextColor(color);
    }
}