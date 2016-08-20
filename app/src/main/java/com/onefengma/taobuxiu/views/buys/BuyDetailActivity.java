package com.onefengma.taobuxiu.views.buys;

import android.content.DialogInterface;
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
import com.onefengma.taobuxiu.manager.helpers.SystemHelper;
import com.onefengma.taobuxiu.model.entities.IronBuyBrief;
import com.onefengma.taobuxiu.model.entities.MyIronBuyDetail;
import com.onefengma.taobuxiu.model.entities.SalesMan;
import com.onefengma.taobuxiu.model.entities.SupplyBrief;
import com.onefengma.taobuxiu.model.events.DeleteIronBuyEvent;
import com.onefengma.taobuxiu.model.events.MyIronDetailEvent;
import com.onefengma.taobuxiu.model.events.SelectSupplyEvent;
import com.onefengma.taobuxiu.utils.DateUtils;
import com.onefengma.taobuxiu.utils.DialogUtils;
import com.onefengma.taobuxiu.utils.StringUtils;
import com.onefengma.taobuxiu.utils.ToastUtils;
import com.onefengma.taobuxiu.views.core.BaseActivity;
import com.onefengma.taobuxiu.views.widgets.ProgressDialog;
import com.onefengma.taobuxiu.views.widgets.listview.XListView;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BuyDetailActivity extends BaseActivity {

    private static final String IRON_ID = "ironId";

    @BindView(R.id.list_view)
    XListView listView;

    private String ironId;
    private ProgressDialog progressDialog;
    private BuyDetailSupplyListAdapter buyDetailSupplyListAdapter;
    private HeaderViewHolder headerViewHolder;

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
        headerViewHolder = new HeaderViewHolder(headerView);
        listView.addHeaderView(headerView);

        buyDetailSupplyListAdapter = new BuyDetailSupplyListAdapter();
        listView.setAdapter(buyDetailSupplyListAdapter);

        ironId = getIntent().getStringExtra(IRON_ID);

        listView.setOnRefreshListener(new XListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BuyManager.instance().loadIronBuyDetail(ironId);
            }
        });
    }

    @OnClick(R.id.right_image)
    public void onDeleteClick() {
        DialogUtils.showAlertDialog(this, "确定删除此次求购？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BuyManager.instance().deleteIronBuy(ironId);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        listView.fakePullRefresh();
    }

    @Subscribe
    public void onSelectSupplyEvent(SelectSupplyEvent event) {
        if (event.isStarted()) {
            progressDialog.show("提交中...");
            return;
        } else {
            progressDialog.dismiss();
        }
        if (event.isSuccess()) {
            ToastUtils.showSuccessTasty("选标成功");
            listView.fakePullRefresh();
        }
    }

    @Subscribe
    public void onDeleteIronBuyEvent(DeleteIronBuyEvent event) {
        if (event.isStarted()) {
            progressDialog.show("提交中...");
            return;
        } else {
            progressDialog.dismiss();
        }
        if (event.isSuccess()) {
            ToastUtils.showSuccessTasty("删除成功");
            finish();
        }
    }

    @Subscribe
    public void onDetailEvent(MyIronDetailEvent event) {
        if (event.isStarted()) {
            progressDialog.show("加载中...");
            return;
        } else {
            listView.onRefreshComplete(false);
            progressDialog.dismiss();
        }
        if (event.isFailed() || event.detail == null) {
            ToastUtils.showErrorTasty("加载求购详情失败！");
            finish();
            return;
        }
        if (event.isSuccess()) {
            setUpViews(event.detail);
        }
    }

    private void setUpViews(MyIronBuyDetail detail) {
        headerViewHolder.display(detail);
        buyDetailSupplyListAdapter.setDetail(detail);
    }

    public static class BuyDetailSupplyListAdapter extends BaseAdapter {

        MyIronBuyDetail detail;

        public void setDetail(MyIronBuyDetail detail) {
            this.detail = detail;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (detail != null && detail.supplies != null) {
                return detail.supplies.size();
            }
            return 0;
        }

        @Override
        public SupplyBrief getItem(int position) {
            return detail.supplies.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.buy_detail_item, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.display(getItem(position), detail.buy);
            return convertView;
        }

        static class ViewHolder {
            @BindView(R.id.title)
            TextView title;
            @BindView(R.id.price)
            TextView price;
            @BindView(R.id.message)
            TextView message;
            @BindView(R.id.info)
            TextView info;
            @BindView(R.id.choose_supply)
            TextView chooseSupply;
            @BindView(R.id.contact)
            TextView contact;
            @BindView(R.id.winner)
            TextView winnerView;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }

            public void display(final SupplyBrief supplyBrief, final IronBuyBrief ironBuyBrief) {
                title.setText(supplyBrief.companyName);
                price.setText(supplyBrief.supplyPrice + "/" + supplyBrief.unit);
                message.setText(StringUtils.getString(R.string.buy_item_message, supplyBrief.supplyMsg));
                info.setText(StringUtils.getString(R.string.buy_detail_info, supplyBrief.winningTimes + "", supplyBrief.contact));
                chooseSupply.setVisibility(ironBuyBrief.status == BuyManager.BuyStatus.DOING.ordinal() ? View.VISIBLE : View.GONE);
                winnerView.setVisibility(supplyBrief.isWinner ? View.VISIBLE : View.GONE);

                chooseSupply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtils.showAlertDialog(winnerView.getContext(), "确定选「" + supplyBrief.companyName + "」中标？", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BuyManager.instance().selectSupply(ironBuyBrief.id, supplyBrief.sellerId);
                            }
                        });
                    }
                });

                contact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtils.showAlertDialog(winnerView.getContext(), "拨打电话:" + supplyBrief.mobile, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SystemHelper.call(supplyBrief.mobile);
                            }
                        });
                    }
                });
            }
        }
    }

    public static class HeaderViewHolder {

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
        @BindView(R.id.time_limit)
        TextView timeLimit;
        @BindView(R.id.edit)
        View editView;

        HeaderViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void display(MyIronBuyDetail detail) {
            IronBuyBrief ironBuyBrief = detail.buy;
            if (ironBuyBrief != null) {
                title.setText(ironBuyBrief.ironType + "/" + ironBuyBrief.material + "/" + ironBuyBrief.surface + "/" + ironBuyBrief.proPlace + "( " + ironBuyBrief.sourceCity + ")");
                subTitle.setText(ironBuyBrief.length + "*" + ironBuyBrief.width + "*" + ironBuyBrief.height + " " + ironBuyBrief.tolerance + " " + ironBuyBrief.numbers + "" + ironBuyBrief.unit);

                int count = detail.supplies == null ? 0 : detail.supplies.size();
                supplyCount.setText(StringUtils.getString(R.string.buy_detail_supply_count, count + ""));
                buyNum.setText(StringUtils.getString(R.string.buy_detail_buy_num, ironBuyBrief.id));

                String timePrefix = detail.buy.status == BuyManager.BuyStatus.DONE.ordinal() ? "成交时间：" : "过期时间：";
                String timeStr = detail.buy.status == BuyManager.BuyStatus.DONE.ordinal() ? DateUtils.getDateStr(ironBuyBrief.supplyWinTime) :  DateUtils.getDateStr(ironBuyBrief.timeLimit + ironBuyBrief.pushTime);
                timeLimit.setText(timePrefix + timeStr);
                editView.setVisibility(ironBuyBrief.status == BuyManager.BuyStatus.DOING.ordinal() ? View.VISIBLE : View.GONE);
            }

            SalesMan salesMan = detail.salesMan;
            if (salesMan != null) {
                salesman.setText(StringUtils.getString(R.string.buy_detail_salesman, salesMan.id + "  " + salesMan.tel));
            } else {
                salesman.setText(StringUtils.getString(R.string.buy_detail_salesman, "暂无"));
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusHelper.unregister(this);
    }

}
