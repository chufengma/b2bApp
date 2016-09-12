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
import com.onefengma.taobuxiu.model.IconDataCategory;
import com.onefengma.taobuxiu.model.entities.IronBuyBrief;
import com.onefengma.taobuxiu.model.entities.IronBuyPush;
import com.onefengma.taobuxiu.model.entities.MyIronBuyDetail;
import com.onefengma.taobuxiu.model.entities.SalesMan;
import com.onefengma.taobuxiu.model.entities.SupplyBrief;
import com.onefengma.taobuxiu.model.events.DeleteIronBuyEvent;
import com.onefengma.taobuxiu.model.events.MyIronDetailEvent;
import com.onefengma.taobuxiu.model.events.QtIronBuyEvent;
import com.onefengma.taobuxiu.model.events.SelectSupplyEvent;
import com.onefengma.taobuxiu.utils.DateUtils;
import com.onefengma.taobuxiu.utils.DialogUtils;
import com.onefengma.taobuxiu.utils.NumbersUtils;
import com.onefengma.taobuxiu.utils.StringUtils;
import com.onefengma.taobuxiu.utils.ToastUtils;
import com.onefengma.taobuxiu.views.core.BaseActivity;
import com.onefengma.taobuxiu.views.widgets.ProgressDialog;
import com.onefengma.taobuxiu.views.widgets.ToolBar;
import com.onefengma.taobuxiu.views.widgets.listview.XListView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BuyDetailActivity extends BaseActivity {

    private static final String IRON_ID = "ironId";
    private static final String ONLY_SHOW_WINNER = "only_show_winner";

    @BindView(R.id.list_view)
    XListView listView;
    @BindView(R.id.right_image)
    View rightImage;
    @BindView(R.id.toolbar)
    ToolBar toolbar;

    private String ironId;
    private boolean isOnlyShowWinner;
    private ProgressDialog progressDialog;
    private BuyDetailSupplyListAdapter buyDetailSupplyListAdapter;
    private HeaderViewHolder headerViewHolder;

    public static void start(BaseActivity activity, String ironId) {
        Intent intent = new Intent(activity, BuyDetailActivity.class);
        intent.putExtra(IRON_ID, ironId);
        activity.startActivity(intent);
    }

    public static void start(BaseActivity activity, String ironId, String title) {
        Intent intent = new Intent(activity, BuyDetailActivity.class);
        intent.putExtra(IRON_ID, ironId);
        intent.putExtra("title", title);
        activity.startActivity(intent);
    }

    public static void start(BaseActivity activity, String ironId, boolean onlyShowWinner) {
        Intent intent = new Intent(activity, BuyDetailActivity.class);
        intent.putExtra(IRON_ID, ironId);
        intent.putExtra(ONLY_SHOW_WINNER, onlyShowWinner);
        activity.startActivity(intent);
    }

    public static void start(BaseActivity activity, String ironId, boolean onlyShowWinner, String title) {
        Intent intent = new Intent(activity, BuyDetailActivity.class);
        intent.putExtra(IRON_ID, ironId);
        intent.putExtra(ONLY_SHOW_WINNER, onlyShowWinner);
        intent.putExtra("title", title);
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
        isOnlyShowWinner = getIntent().getBooleanExtra(ONLY_SHOW_WINNER, false);

        listView.setOnRefreshListener(new XListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BuyManager.instance().loadIronBuyDetail(ironId);
            }
        });

        rightImage.setVisibility(View.GONE);

        String title = getIntent().getStringExtra("title");
        if (!StringUtils.isEmpty(title)) {
            toolbar.setTitle(title);
        }
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
    public void onSelectSupplyEvent(final SelectSupplyEvent event) {
        if (event.isStarted()) {
            progressDialog.show("提交中...");
            return;
        } else {
            progressDialog.dismiss();
        }
        if (event.isSuccess()) {
            ToastUtils.showSuccessTasty("选标成功");
            listView.fakePullRefresh();

            if (event.totalMoney >= 5000) {
                DialogUtils.showAlertDialog(this, "是否发送质检请求？", "您已达到质检服务门槛，点『确认』后，您的申请将发送至专员客户端。", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BuyManager.instance().qtIronBuy(event.ironId);
                    }
                });
            }
        }
    }

    @Subscribe
    public void onQtIronBuyEvent(QtIronBuyEvent event) {
        if (event.isStarted()) {
            progressDialog.show("提交中...");
            return;
        } else {
            progressDialog.dismiss();
        }
        if (event.isSuccess()) {
            ToastUtils.showSuccessTasty("申请成功！");
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
            if (isOnlyShowWinner && event.detail.supplies != null) {
                List<SupplyBrief> supplies = event.detail.supplies;
                for (SupplyBrief supplyBrief : supplies) {
                    if (supplyBrief.isWinner) {
                        event.detail.supplies = Arrays.asList(supplyBrief);
                        break;
                    }
                }
            }
            setUpViews(event.detail);
        }
    }

    private void setUpViews(MyIronBuyDetail detail) {

        if (detail.supplies != null) {
            List<SupplyBrief> newList = new ArrayList<>();
            for (SupplyBrief supplyBrief : detail.supplies) {
                if (supplyBrief.isWinner) {
                    newList.add(0, supplyBrief);
                } else {
                    newList.add(supplyBrief);
                }
            }
            detail.supplies = newList;
        }

        headerViewHolder.display(detail);
        buyDetailSupplyListAdapter.setDetail(detail);
        rightImage.setVisibility(detail.buy.status == BuyManager.BuyStatus.DOING.ordinal() ? View.VISIBLE : View.GONE);
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
                        String message = supplyBrief.companyName + " "
                                + supplyBrief.supplyPrice + "/"
                                + supplyBrief.unit + " ";

                        String moneyStr = NumbersUtils.round(ironBuyBrief.numbers.floatValue() * supplyBrief.supplyPrice, 2);
                        final float totalMoney = NumbersUtils.parseFloat(moneyStr);

                        DialogUtils.showAlertDialog(winnerView.getContext(), "确定选「" + message + "」中标？", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BuyManager.instance().selectSupply(ironBuyBrief.id, supplyBrief.sellerId, totalMoney);
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

    public class HeaderViewHolder {

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
        @BindView(R.id.supply_count_icon)
        View supplyCountIcon;
        @BindView(R.id.message)
        TextView messageView;
        @BindView(R.id.totalMoney)
        TextView totalMoney;

        HeaderViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void display(MyIronBuyDetail detail) {
            final IronBuyBrief ironBuyBrief = detail.buy;
            if (ironBuyBrief != null) {
                title.setText(ironBuyBrief.ironType + "/" + ironBuyBrief.material + "/" + ironBuyBrief.surface + "/" + ironBuyBrief.proPlace + "( " + ironBuyBrief.sourceCity + ")");
                subTitle.setText(ironBuyBrief.height + "*" + ironBuyBrief.width + "*" + ironBuyBrief.length + " " + ironBuyBrief.tolerance + " " + ironBuyBrief.numbers + "" + ironBuyBrief.unit);

                int count = detail.supplies == null ? 0 : detail.supplies.size();
                supplyCount.setText(StringUtils.getString(R.string.buy_detail_supply_count, count + ""));
                buyNum.setText(StringUtils.getString(R.string.buy_detail_buy_num, ironBuyBrief.id));

                totalMoney.setVisibility(detail.buy.status == BuyManager.BuyStatus.DONE.ordinal() ? View.VISIBLE : View.GONE);

                if (detail.supplies != null) {
                    for (SupplyBrief supplyBrief : detail.supplies) {
                        if (supplyBrief.isWinner) {
                            totalMoney.setText(StringUtils.getString(R.string.offer_detail_total_money, NumbersUtils.round(supplyBrief.supplyPrice * ironBuyBrief.numbers.floatValue(), 2)));
                        }
                    }
                }

                String timePrefix = detail.buy.status == BuyManager.BuyStatus.DONE.ordinal() ? "成交时间：" : "过期时间：";
                String timeStr = detail.buy.status == BuyManager.BuyStatus.DONE.ordinal() ? DateUtils.getDateStr(ironBuyBrief.supplyWinTime) : DateUtils.getDateStr(ironBuyBrief.timeLimit + ironBuyBrief.pushTime);
                timeLimit.setText(timePrefix + timeStr);
                editView.setVisibility(ironBuyBrief.status == BuyManager.BuyStatus.DOING.ordinal() && ironBuyBrief.editStatus == 0 ? View.VISIBLE : View.GONE);
                messageView.setText(StringUtils.getString(R.string.buy_item_message, ironBuyBrief.message));


            }

            editView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IronBuyPush ironBuyPush = new IronBuyPush();
                    ironBuyPush.width = ironBuyBrief.width;
                    ironBuyPush.height = ironBuyBrief.height;
                    ironBuyPush.length = ironBuyBrief.length;
                    ironBuyPush.ironType = ironBuyBrief.ironType;
                    ironBuyPush.surface = ironBuyBrief.surface;
                    ironBuyPush.material = ironBuyBrief.material;
                    ironBuyPush.proPlace = ironBuyBrief.proPlace;
                    ironBuyPush.locationCityId = ironBuyBrief.locationCityId;
                    ironBuyPush.numbers = ironBuyBrief.numbers.floatValue();
                    ironBuyPush.ironId = ironBuyBrief.id;
                    ironBuyPush.message = ironBuyBrief.message;
                    ironBuyPush.unit = ironBuyBrief.unit;
                    ironBuyPush.pushStatus = 1;
                    ironBuyPush.toleranceTo = ironBuyBrief.tolerance.split("-")[1];
                    ironBuyPush.toleranceFrom = ironBuyBrief.tolerance.split("-")[0];

                    ironBuyPush.unitIndex = IconDataCategory.get().units.indexOf(ironBuyPush.unit);
                    ironBuyPush.dayIndex = (int) (ironBuyBrief.timeLimit / (DateUtils.dayTime()));
                    ironBuyPush.hourIndex = (int) ((ironBuyBrief.timeLimit % DateUtils.dayTime()) / (DateUtils.hourTime()));
                    ironBuyPush.hourIndex = (int) (((ironBuyBrief.timeLimit % DateUtils.dayTime()) % DateUtils.hourTime() / (DateUtils.minuteTime())));
                    EditBuyActivity.start(BuyDetailActivity.this, ironBuyPush);
                }
            });

            SalesMan salesMan = detail.salesMan;
            if (salesMan != null) {
                salesman.setText(StringUtils.getString(R.string.buy_detail_salesman, salesMan.id + "  " + salesMan.name + "  " + salesMan.tel));
            } else {
                salesman.setText(StringUtils.getString(R.string.buy_detail_salesman, "暂无"));
            }
            supplyCount.setVisibility(BuyDetailActivity.this.isOnlyShowWinner ? View.GONE : View.VISIBLE);
            supplyCountIcon.setVisibility(BuyDetailActivity.this.isOnlyShowWinner ? View.GONE : View.VISIBLE);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusHelper.unregister(this);
    }

}
