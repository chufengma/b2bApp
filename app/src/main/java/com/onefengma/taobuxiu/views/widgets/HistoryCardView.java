package com.onefengma.taobuxiu.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author yfchu
 * @date 2016/9/1
 */
public class HistoryCardView extends FrameLayout {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.value)
    TextView value;

    String titleStr;

    public HistoryCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.carrd_history_layout, this);
        ButterKnife.bind(this, view);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.HistoryCardView);
        titleStr = a.getString(R.styleable.HistoryCardView_history_title);
        a.recycle();

        title.setText(titleStr);
    }

    public void setValue(CharSequence value) {
        this.value.setText(value);
    }
}
