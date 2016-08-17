package com.onefengma.taobuxiu.views.buys;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.views.core.BaseActivity;
import com.onefengma.taobuxiu.views.widgets.ToolBar;
import com.onefengma.taobuxiu.views.widgets.listview.XListView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BuyDetailActivity extends BaseActivity {

    @BindView(R.id.list_view)
    XListView listView;

    public static void start(BaseActivity activity) {
        Intent intent = new Intent(activity, BuyDetailActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_detail);
        ButterKnife.bind(this);

        View headerView = LayoutInflater.from(this).inflate(R.layout.buy_detail_header, null);
        listView.addHeaderView(headerView);
        listView.setAdapter(new BuyDetailSupplyListAdapter());
    }

    public static class BuyDetailSupplyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.buy_detail_item, parent, false);
            }
            return convertView;
        }
    }

}
