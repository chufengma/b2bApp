/**
 * @file XListViewHeader.java
 * @create Apr 18, 2012 5:22:27 PM
 * @author Maxwin
 * @description XListView's header
 */
package com.onefengma.taobuxiu.views.widgets.listview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.utils.ViewUtils;

public class XListViewHeader extends LinearLayout {
    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_REFRESHING = 2;

    private LinearLayout mContainer;
    private ImageView mArrowImageView;
    private ProgressBar mProgressBar;

    // header view content, use it to calculate the Header's height. And hide it
    // when disable pull refresh.
    private RelativeLayout mViewContent;

    private int mState = STATE_NORMAL;

    private int headerOffset;

    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;

    public XListViewHeader(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.XListView);
        headerOffset = array.getDimensionPixelSize(R.styleable.XListView_headerOffset, 0);
        array.recycle();

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, ViewUtils.dipToPixels(getResources().getDisplayMetrics(), headerOffset));
        mContainer = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.xlist_view_header, null);
        addView(mContainer, lp);
        setGravity(Gravity.BOTTOM);

        mViewContent = (RelativeLayout) findViewById(R.id.xlist_view_header_content);
        mArrowImageView = (ImageView) findViewById(R.id.xlist_view_header_arrow);

        mProgressBar = (ProgressBar) findViewById(R.id.xlist_view_header_progressbar);

        // Rotate clockwise if toDegrees is bigger than fromDegrees
        mRotateUpAnim = new RotateAnimation(0.0f, 180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        mRotateUpAnim.setDuration(180);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateDownAnim.setDuration(180);
        mRotateDownAnim.setFillAfter(true);
    }

    public RelativeLayout getViewContent() {
        return mViewContent;
    }

    public void setState(int state) {
        if (state == mState) {
            return;
        }

        if (state == STATE_REFRESHING) { // 显示进度
            mArrowImageView.clearAnimation();
            mArrowImageView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
        } else { // 显示箭头图片
            mArrowImageView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
        }

        switch (state) {
            case STATE_NORMAL:
                if (mState == STATE_READY) {
                    mArrowImageView.clearAnimation();
                    mArrowImageView.startAnimation(mRotateDownAnim);
                } else if (mState == STATE_REFRESHING) {
                    mArrowImageView.clearAnimation();
                }
                break;
            case STATE_READY:
                if (mState != STATE_READY) {
                    mArrowImageView.clearAnimation();
                    mArrowImageView.startAnimation(mRotateUpAnim);
                }
                break;
            default:
                break;
        }

        mState = state;
    }

    public int getHeaderOffset() {
        return headerOffset;
    }

    public void setHeaderOffset(int valueInPixel) {
        headerOffset = valueInPixel;
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        lp.height = headerOffset;
        mContainer.setLayoutParams(lp);
    }

    public void setVisiableHeight(int height) {
        if (height < headerOffset) {
            height = headerOffset;
        }

        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    public int getVisiableHeight() {
        return mContainer.getLayoutParams().height;
    }

    public int getState() {
        return mState;
    }

}