package com.onefengma.taobuxiu.views.sales;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.BuyManager;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.manager.helpers.SystemHelper;
import com.onefengma.taobuxiu.model.entities.IronBuyBrief;
import com.onefengma.taobuxiu.model.entities.SalesIronBuyDetail;
import com.onefengma.taobuxiu.model.entities.SalesMan;
import com.onefengma.taobuxiu.model.entities.SupplyBrief;
import com.onefengma.taobuxiu.model.events.sales.SalesActionQtEvent;
import com.onefengma.taobuxiu.model.events.sales.SalesIronDetailEvent;
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

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SalesBuyDetailActivity extends BaseActivity {


    private static final String IRON_ID = "ironId";
    private static final String ONLY_SHOW_WINNER = "only_show_winner";

    @BindView(R.id.list_view)
    XListView listView;
    @BindView(R.id.right_image)
    View rightImage;
    @BindView(R.id.toolbar)
    ToolBar toolbar;
    @BindView(R.id.totalMoney)
    TextView totalMoney;
    @BindView(R.id.done_qt)
    TextView doneQt;
    @BindView(R.id.cancel_qt)
    TextView cancelQt;
    @BindView(R.id.sales_actions)
    LinearLayout salesActions;
    @BindView(R.id.start_qt)
    TextView startQt;

    private String ironId;
    private String qtId;
    private boolean isOnlyShowWinner;
    private ProgressDialog progressDialog;
    private BuyDetailSupplyListAdapter buyDetailSupplyListAdapter;
    private HeaderViewHolder headerViewHolder;

    public static void start(BaseActivity activity, String ironId) {
        Intent intent = new Intent(activity, SalesBuyDetailActivity.class);
        intent.putExtra(IRON_ID, ironId);
        activity.startActivity(intent);
    }

    public static void start(BaseActivity activity, String ironId, boolean onlyShowWinner) {
        Intent intent = new Intent(activity, SalesBuyDetailActivity.class);
        intent.putExtra(IRON_ID, ironId);
        intent.putExtra(ONLY_SHOW_WINNER, onlyShowWinner);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_buy_detail);
        ButterKnife.bind(this);
        EventBusHelper.register(this);
        progressDialog = new ProgressDialog(this);

        View headerView = LayoutInflater.from(this).inflate(R.layout.sales_buy_detail_header, null);
        headerViewHolder = new HeaderViewHolder(headerView);
        listView.addHeaderView(headerView);

        buyDetailSupplyListAdapter = new BuyDetailSupplyListAdapter();
        listView.setAdapter(buyDetailSupplyListAdapter);

        ironId = getIntent().getStringExtra(IRON_ID);
        isOnlyShowWinner = getIntent().getBooleanExtra(ONLY_SHOW_WINNER, false);

        listView.setOnRefreshListener(new XListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SalesQtManager.instance().loadIronBuyDetail(ironId);
            }
        });

        rightImage.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        listView.fakePullRefresh();
    }

    @OnClick(R.id.start_qt)
    public void startQt() {
        if (!StringUtils.isEmpty(qtId)) {
            DialogUtils.showAlertDialog(this, "确定开始质检？", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SalesQtManager.instance().startQt(qtId);
                }
            });
        }
    }

    @OnClick(R.id.done_qt)
    public void doneQt() {
        if (!StringUtils.isEmpty(qtId)) {
            DialogUtils.showAlertDialog(this, "确定质检完成？", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SalesQtManager.instance().doneQt(qtId);
                }
            });
        }
    }

    @OnClick(R.id.cancel_qt)
    public void cancelQt() {
        if (!StringUtils.isEmpty(qtId)) {
            DialogUtils.showAlertDialog(this, "确定质检取消？", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SalesQtManager.instance().cancelQt(qtId);
                }
            });
        }
    }

    @Subscribe
    public void onQtIronActionEvent(SalesActionQtEvent event) {
        if (event.isStarted()) {
            progressDialog.show("提交中...");
            return;
        } else {
            progressDialog.dismiss();
        }
        if (event.isSuccess()) {
            ToastUtils.showSuccessTasty("操作成功！");
            listView.fakePullRefresh();
        }
    }

    @Subscribe
    public void onDetailEvent(SalesIronDetailEvent event) {
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
            if (event.detail.qtDetail != null) {
                qtId = event.detail.qtDetail.qtId;
            }
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

    private void setUpViews(SalesIronBuyDetail detail) {
        headerViewHolder.display(detail);
        buyDetailSupplyListAdapter.setDetail(detail);

        boolean find = false;
        for (SupplyBrief supplyBrief : detail.supplies) {
            if (supplyBrief.isWinner) {
                totalMoney.setText("总成交额：" + NumbersUtils.round(detail.buy.numbers.floatValue() * supplyBrief.supplyPrice, 2));
                find = true;
                break;
            }
        }
        totalMoney.setVisibility(find ? View.VISIBLE : View.GONE);

        salesActions.setVisibility(detail.qtDetail == null ? View.GONE : View.VISIBLE);

        if (detail.qtDetail != null) {
            if (detail.qtDetail.status == 0) {
                startQt.setVisibility(View.VISIBLE);
                cancelQt.setVisibility(View.VISIBLE);
                doneQt.setVisibility(View.GONE);
            } else if (detail.qtDetail.status == 3) {
                startQt.setVisibility(View.GONE);
                doneQt.setVisibility(View.VISIBLE);
                cancelQt.setVisibility(View.VISIBLE);
            } else {
                startQt.setVisibility(View.GONE);
                doneQt.setVisibility(View.GONE);
                cancelQt.setVisibility(View.GONE);
            }
        }
    }

    public static class BuyDetailSupplyListAdapter extends BaseAdapter {

        SalesIronBuyDetail detail;

        public void setDetail(SalesIronBuyDetail detail) {
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
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sales_buy_detail_item, parent, false);
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
                winnerView.setVisibility(supplyBrief.isWinner ? View.VISIBLE : View.GONE);

                contact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtils.showAlertDialog(winnerView.getContext(), "拨打卖家电话:" + supplyBrief.mobile, new DialogInterface.OnClickListener() {
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
        @BindView(R.id.supply_count_icon)
        View supplyCountIcon;
        @BindView(R.id.contact_user)
        View contactUser;
        @BindView(R.id.buy_company)
        TextView companyName;

        HeaderViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void display(final SalesIronBuyDetail detail) {
            IronBuyBrief ironBuyBrief = detail.buy;
            if (ironBuyBrief != null) {
                title.setText(ironBuyBrief.ironType + "/" + ironBuyBrief.material + "/" + ironBuyBrief.surface + "/" + ironBuyBrief.proPlace + "( " + ironBuyBrief.sourceCity + ")");
                subTitle.setText(ironBuyBrief.height + "*" + ironBuyBrief.width + "*" + ironBuyBrief.length + " " + ironBuyBrief.tolerance + " " + ironBuyBrief.numbers + "" + ironBuyBrief.unit);

                int count = detail.supplies == null ? 0 : detail.supplies.size();
                supplyCount.setText(StringUtils.getString(R.string.buy_detail_supply_count, count + ""));
                buyNum.setText(StringUtils.getString(R.string.buy_detail_buy_num, ironBuyBrief.id));

                String timePrefix = detail.buy.status == BuyManager.BuyStatus.DONE.ordinal() ? "成交时间：" : "过期时间：";
                String timeStr = detail.buy.status == BuyManager.BuyStatus.DONE.ordinal() ? DateUtils.getDateStr(ironBuyBrief.supplyWinTime) : DateUtils.getDateStr(ironBuyBrief.timeLimit + ironBuyBrief.pushTime);
                timeLimit.setText(timePrefix + timeStr);

                contactUser.setVisibility(detail.userInfo == null ? View.GONE : View.VISIBLE);
                contactUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtils.showAlertDialog(contactUser.getContext(), "拨打买家电话:" + detail.userInfo.mobile, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SystemHelper.call(detail.userInfo.mobile);
                            }
                        });
                    }
                });
            }

            companyName.setVisibility(detail.sellerInfo == null ? View.GONE : View.VISIBLE);
            if (detail.sellerInfo != null) {
                companyName.setText(StringUtils.getString(R.string.buy_detail_buy_company, detail.sellerInfo.companyName));
            }

            SalesMan salesMan = detail.salesMan;
            if (salesMan != null) {
                salesman.setText(StringUtils.getString(R.string.buy_detail_salesman, salesMan.id + "  " + salesMan.tel));
            } else {
                salesman.setText(StringUtils.getString(R.string.buy_detail_salesman, "暂无"));
            }
            supplyCount.setVisibility(SalesBuyDetailActivity.this.isOnlyShowWinner ? View.GONE : View.VISIBLE);
            supplyCountIcon.setVisibility(SalesBuyDetailActivity.this.isOnlyShowWinner ? View.GONE : View.VISIBLE);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusHelper.unregister(this);
    }

}
