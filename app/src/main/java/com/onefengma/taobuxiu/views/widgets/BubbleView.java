package com.onefengma.taobuxiu.views.widgets;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.daasuu.bl.ArrowDirection;
import com.daasuu.bl.BubbleLayout;
import com.daasuu.bl.BubblePopupHelper;
import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.utils.ViewUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author yfchu
 * @date 2016/9/1
 */
public class BubbleView {

    @BindView(R.id.title)
    TextView titleView;
    @BindView(R.id.list)
    ListView list;

    PopupWindow popupWindow;

    public interface OnItemSelectedListener {
        void onItemSelected(String item);
    }

    public void init(Context context, final View view, String title, List<String> data, final OnItemSelectedListener listener) {
        final BubbleLayout bubbleLayout = (BubbleLayout) LayoutInflater.from(context).inflate(R.layout.bubble_view_layout, null);
        ButterKnife.bind(this, bubbleLayout);

        popupWindow = BubblePopupHelper.create(context, bubbleLayout);
        bubbleLayout.setArrowDirection(ArrowDirection.BOTTOM);

        final StringAdapter stringAdapter = new StringAdapter(data, listener);
        list.setAdapter(stringAdapter);

        titleView.setText(title);

        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                int[] location = new int[2];
                view.getLocationInWindow(location);
                bubbleLayout.measure(View.MeasureSpec.makeMeasureSpec(ViewUtils.getDisplayMetrics().widthPixels, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(ViewUtils.getDisplayMetrics().heightPixels, View.MeasureSpec.AT_MOST));
                popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1] - bubbleLayout.getMeasuredHeight());
            }
        }, 100);
    }

    class StringAdapter extends BaseAdapter {

        public List<String> strings;
        OnItemSelectedListener listener;

        public StringAdapter(List<String> strings, OnItemSelectedListener listener) {
            this.strings = strings;
            this.listener = listener;
        }

        @Override
        public int getCount() {
            return strings.size();
        }

        @Override
        public String getItem(int position) {
            return strings.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final TextView textView = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.bubble_text_view_layout, null);
            textView.setText(getItem(position));

            textView.setClickable(true);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemSelected(textView.getText().toString());
                    }
                    popupWindow.dismiss();
                }
            });

            return textView;
        }
    }

}
