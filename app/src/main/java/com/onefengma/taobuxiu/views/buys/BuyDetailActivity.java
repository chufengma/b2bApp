package com.onefengma.taobuxiu.views.buys;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.BuyManager;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.model.events.MyIronDetailEvent;
import com.onefengma.taobuxiu.views.core.BaseActivity;
import com.onefengma.taobuxiu.views.widgets.ProgressDialog;
import com.onefengma.taobuxiu.views.widgets.listview.XListView;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BuyDetailActivity extends BaseActivity {

    private static final String IRON_ID = "ironId";

    @BindView(R.id.list_view)
    XListView listView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.sub_title)
    TextView subTitle;
    @BindView(R.id.buy_num)
    TextView buyNum;
    @BindView(R.id.salesman)
    TextView salesman;
    @BindView(R.id.desc)
    TextView desc;
    @BindView(R.id.supply_count)
    TextView supplyCount;

    private String ironId;
    private ProgressDialog progressDialog;

    public static void start(BaseActivity activity, String ironId) {
        Intent intent = new Intent(activity, BuyDetailActivity.class);
        intent.putExtra(IRON_ID, ironId);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_detail);
        ButterKnife.bind(this);
        EventBusHelper.register(this);
        progressDialog = new ProgressDialog(this);

        View headerView = LayoutInflater.from(this).inflate(R.layout.buy_detail_header, null);
        listView.addHeaderView(headerView);
        listView.setAdapter(new BuyDetailSupplyListAdapter());

        ironId = getIntent().getStringExtra(IRON_ID);

        BuyManager.instance().loadIronBuyDetail(ironId);
    }

    @Subscribe
    public void onDetailEvent(MyIronDetailEvent event) {
        if (event.isStarted()) {
            progressDialog.show("加载中...");
        } else {
            progressDialog.dismiss();
        }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusHelper.unregister(this);
    }

}
