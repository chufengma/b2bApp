package com.onefengma.taobuxiu.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
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
    ImageView leftImage;
    @BindView(R.id.title)
    TextView titleView;
    @BindView(R.id.right_image)
    ImageView rightImage;

    public ToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.view_title_bar, this);
        ButterKnife.bind(this, view);
    }

    public void setTitle(CharSequence title) {
        titleView.setText(title);
    }

}
