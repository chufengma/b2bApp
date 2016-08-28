package com.onefengma.taobuxiu.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.utils.StringUtils;
import com.onefengma.taobuxiu.utils.ViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chufengma on 16/8/7.
 */
public class ToolBar extends RelativeLayout {

    @BindView(R.id.left_image)
    ImageView leftImage;
    @BindView(R.id.title)
    TextView titleView;
    @BindView(R.id.right_image)
    ImageView rightImage;

    String title;
    int color;
    Drawable left;
    Drawable right;
    @BindView(R.id.right_image_layout)
    FrameLayout rightImageLayout;
    @BindView(R.id.left_image_layout)
    FrameLayout leftImageLayout;
    int size;

    @BindView(R.id.left_title)
    TextView leftTitleView;
    @BindView(R.id.right_title)
    TextView rightTitleView;

    String rightTitle;
    String leftTitle;

    public ToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        View view = inflate(getContext(), R.layout.view_title_bar, this);
        ButterKnife.bind(this, view);

        // Styleables from XML
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TaoToolBar);
        title = a.getString(R.styleable.TaoToolBar_tool_bar_title);
        color = a.getColor(R.styleable.TaoToolBar_tool_bar_bg, getContext().getResources().getColor(R.color.colorPrimary));
        left = a.getDrawable(R.styleable.TaoToolBar_tool_bar_left);
        right = a.getDrawable(R.styleable.TaoToolBar_tool_bar_right);
        size = a.getDimensionPixelSize(R.styleable.TaoToolBar_tool_bar_icon_size, ViewUtils.dipToPixels(24));
        rightTitle = a.getString(R.styleable.TaoToolBar_tool_bar_right_title);
        leftTitle = a.getString(R.styleable.TaoToolBar_tool_bar_left_title);
        a.recycle();

        setTitle(title);
        setBackgroundColor(color);
        if (left != null) {
            leftImage.setImageDrawable(left);
            leftImageLayout.setVisibility(VISIBLE);
            leftImage.getLayoutParams().width = size;
            leftImage.getLayoutParams().height = size;
            leftImage.requestLayout();
        }
        if (right != null) {
            rightImage.setImageDrawable(right);
            rightImageLayout.setVisibility(VISIBLE);
            rightImage.getLayoutParams().width = size;
            rightImage.getLayoutParams().height = size;
            rightImage.requestLayout();
        }

        if (!StringUtils.isEmpty(rightTitle)) {
            rightImageLayout.setVisibility(View.GONE);
            rightTitleView.setVisibility(View.VISIBLE);
            rightTitleView.setText(rightTitle);
        }

        if (!StringUtils.isEmpty(leftTitle)) {
            leftImageLayout.setVisibility(View.GONE);
            leftTitleView.setVisibility(View.VISIBLE);
            leftTitleView.setText(leftTitle);
        }
    }

    public void setTitle(CharSequence title) {
        titleView.setText(title);
    }

}
