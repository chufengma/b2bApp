package com.onefengma.taobuxiu.views.widgets;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chufengma on 16/8/27.
 */
public class TabItem extends FrameLayout {
    @BindView(R.id.icon)
    public ImageView iconView;
    @BindView(R.id.title)
    public TextView titleView;
    @BindView(R.id.container)
    public View container;

    public TabItem(Context context) {
        super(context);
        View view = inflate(context, R.layout.buy_tab_item, this);
        ButterKnife.bind(this, view);
    }

    public void setTitle(String title) {
        titleView.setText(title);
    }

    public void setIcon(int resId) {
        iconView.setImageResource(resId);
    }

    public void setWidth(int width) {
        container.setMinimumWidth(width);
    }

}
