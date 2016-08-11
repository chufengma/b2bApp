package com.onefengma.taobuxiu.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chufengma on 16/8/7.
 */
public class ToolBar extends RelativeLayout {

    @BindView(R.id.left_image)
    View leftImage;
    @BindView(R.id.title)
    TextView titleView;
    @BindView(R.id.right_image)
    View rightImage;

    String title;

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
        a.recycle();

        setTitle(title);
    }

    public void setTitle(CharSequence title) {
        titleView.setText(title);
    }

}
