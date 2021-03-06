package com.onefengma.taobuxiu.views.offers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.OfferManager;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.manager.helpers.SystemHelper;
import com.onefengma.taobuxiu.model.entities.IronBuyBrief;
import com.onefengma.taobuxiu.model.entities.OfferDetail;
import com.onefengma.taobuxiu.model.events.ActionMissEvent;
import com.onefengma.taobuxiu.model.events.ActionSupplyEvent;
import com.onefengma.taobuxiu.model.events.MyOfferDetailEvent;
import com.onefengma.taobuxiu.utils.DialogUtils;
import com.onefengma.taobuxiu.utils.NumbersUtils;
import com.onefengma.taobuxiu.utils.StringUtils;
import com.onefengma.taobuxiu.utils.ToastUtils;
import com.onefengma.taobuxiu.views.core.BaseActivity;
import com.onefengma.taobuxiu.views.widgets.ProgressDialog;
import com.onefengma.taobuxiu.views.widgets.ToolBar;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OfferDetailActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    ToolBar toolbar;
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.surface)
    TextView surface;
    @BindView(R.id.material)
    TextView material;
    @BindView(R.id.proPlace)
    TextView proPlace;
    @BindView(R.id.locateCity)
    TextView locateCity;
    @BindView(R.id.info)
    TextView info;
    @BindView(R.id.tolerate)
    TextView tolerate;
    @BindView(R.id.message)
    TextView message;
    @BindView(R.id.buy_times)
    TextView buyTimes;
    @BindView(R.id.buy_rate)
    TextView buyRate;
    @BindView(R.id.my_offer_title)
    TextView myOfferTitle;
    @BindView(R.id.unit)
    TextView unit;
    @BindView(R.id.price_edit)
    EditText priceEdit;
    @BindView(R.id.message_edit)
    EditText messageEdit;
    @BindView(R.id.supply)
    TextView supply;
    @BindView(R.id.offer_edit_layout)
    RelativeLayout offerEditLayout;
    @BindView(R.id.my_price)
    TextView myPrice;
    @BindView(R.id.my_message)
    TextView myMessage;
    @BindView(R.id.my_offer_layout)
    LinearLayout myOfferLayout;
    @BindView(R.id.numbers)
    TextView numbers;
    @BindView(R.id.totalMoney)
    TextView totalMoney;
    @BindView(R.id.buy_company)
    TextView buyCompany;
    @BindView(R.id.buy_contact)
    TextView buyContact;
    @BindView(R.id.contact)
    TextView contact;

    private ProgressDialog progressDialog;
    private static final String IRON_ID = "ironId";

    private String ironId;
    private OfferDetail offerDetail;

    public static void start(BaseActivity activity, String ironId) {
        Intent intent = new Intent(activity, OfferDetailActivity.class);
        intent.putExtra(IRON_ID, ironId);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_detail);
        ButterKnife.bind(this);
        EventBusHelper.register(this);
        progressDialog = new ProgressDialog(this);
        ironId = getIntent().getStringExtra(IRON_ID);
        OfferManager.instance().loadIronOfferDetail(ironId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusHelper.unregister(this);
    }

    @Subscribe
    public void onDetailEvent(MyOfferDetailEvent event) {
        if (event.isStarted()) {
            progressDialog.show("加载中...");
            return;
        } else {
            progressDialog.dismiss();
        }
        if (event.isFailed() || event.offerDetail == null) {
            ToastUtils.showErrorTasty("加载求购详情失败！");
            finish();
            return;
        }
        offerDetail = event.offerDetail;
        if (event.isSuccess()) {
            setUpViews(event.offerDetail);
        }
    }

    @OnClick(R.id.supply)
    public void onSupplyClick() {
        if (offerDetail == null) {
            return;
        }
        String price = priceEdit.getText().toString();
        if (StringUtils.isEmpty(price)) {
            ToastUtils.showInfoTasty("请输入价格");
            return;
        }
        String message = messageEdit.getText().toString();
        String unit = offerDetail.buy.unit;
        String ironId = offerDetail.buy.id;
        OfferManager.instance().offerIronBuy(ironId, Float.valueOf(price), message, unit);
    }

    @OnClick(R.id.miss)
    public void onMissClick() {
        DialogUtils.showAlertDialog(this, "是否确认放弃对此单的报价", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                OfferManager.instance().missIronBuyOffer(ironId);
            }
        });
    }

    @Subscribe
    public void onSupplyEvent(ActionSupplyEvent event) {
        if (event.isStarted()) {
            progressDialog.show("提交中...");
            return;
        } else {
            progressDialog.dismiss();
        }
        if (event.isFailed()) {
            ToastUtils.showErrorTasty("提交报价信息失败，请重试！");
            return;
        }
        if (event.isSuccess()) {
            ToastUtils.showSuccessTasty("提交报价信息成功！");
            OfferManager.instance().loadIronOfferDetail(ironId);
        }

    }

    @Subscribe
    public void onSupplyEvent(ActionMissEvent event) {
        if (event.isStarted()) {
            progressDialog.show("提交中...");
            return;
        } else {
            progressDialog.dismiss();
        }
        if (event.isFailed()) {
            ToastUtils.showErrorTasty("操作失败，请重试！");
            return;
        }
        if (event.isSuccess()) {
            ToastUtils.showSuccessTasty("没关系，以后还有合作机会");
            finish();
        }
    }


    private void setUpViews(final OfferDetail offerDetail) {
        IronBuyBrief buy = offerDetail.buy;
        type.setText(getString(R.string.offer_detail_type, offerDetail.buy.ironType));
        surface.setText(getString(R.string.offer_detail_surface, offerDetail.buy.surface));
        material.setText(getString(R.string.offer_detail_material, offerDetail.buy.material));
        proPlace.setText(getString(R.string.offer_detail_pro_place, offerDetail.buy.proPlace));
        locateCity.setText(getString(R.string.offer_detail_locate_place, offerDetail.buy.sourceCity));
        info.setText(getString(R.string.offer_detail_info, buy.height + "*" + buy.width + "*" + buy.length));
        tolerate.setText(getString(R.string.offer_detail_tolerate, buy.tolerance));
        message.setText(getString(R.string.offer_detail_message, buy.message));
        numbers.setText(StringUtils.getString(R.string.offer_detail_numbers, buy.numbers + "" + buy.unit));

        if (offerDetail.myOffer != null) {
            myPrice.setText(getString(R.string.offer_detail_my_price, offerDetail.myOffer.price + "元/" + offerDetail.myOffer.unit));
            myMessage.setText(getString(R.string.offer_detail_my_message, offerDetail.myOffer.supplyMsg));
            totalMoney.setText(getString(R.string.offer_detail_total_money, NumbersUtils.round(offerDetail.myOffer.price * buy.numbers.floatValue(), 2)));
        }
        unit.setText("/" + buy.unit);

        if (offerDetail.userBuyInfo != null) {
            buyTimes.setText(getString(R.string.offer_detail_buy_times, offerDetail.userBuyInfo.buyTimes));
            buyRate.setText(getString(R.string.offer_detail_buy_rate, NumbersUtils.getHS(offerDetail.userBuyInfo.buySuccessRate)));
        }

        if (offerDetail.buyerSeller != null && offerDetail.buy.status == 4) {
            buyContact.setVisibility(View.VISIBLE);
            buyCompany.setVisibility(View.VISIBLE);
            buyContact.setText(StringUtils.getString(R.string.buy_detail_buy_contact, offerDetail.buyerSeller.contact));
            buyCompany.setText(StringUtils.getString(R.string.buy_detail_buy_company, offerDetail.buyerSeller.companyName));
            contact.setVisibility(View.VISIBLE);
        } else {
            buyCompany.setVisibility(View.GONE);
            buyContact.setVisibility(View.GONE);
            contact.setVisibility(View.GONE);
        }

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (offerDetail.buyerSeller == null) {
                    DialogUtils.showAlertDialog(v.getContext(), "拨打电话:" + offerDetail.buyerMobile, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SystemHelper.call(offerDetail.buyerMobile);
                        }
                    });
                } else {
                    DialogUtils.showAlertDialog(v.getContext(), "拨打电话:" + offerDetail.buyerSeller.cantactTel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SystemHelper.call(offerDetail.buyerSeller.cantactTel);
                        }
                    });
                }
            }
        });

        if (buy.status == 0) {
            offerEditLayout.setVisibility(View.VISIBLE);
            myOfferLayout.setVisibility(View.GONE);
        } else {
            offerEditLayout.setVisibility(View.GONE);
            myOfferLayout.setVisibility(View.VISIBLE);
        }

        String offerTitle = "";
        if (buy.status == 3) {
            offerTitle = "(等待中)";
        } else if (buy.status == 4) {
            offerTitle = "(已中标)";
        } else if (buy.status == 6) {
            offerTitle = "(未中标)";
        }

        totalMoney.setVisibility(buy.status == 4 ? View.VISIBLE : View.GONE);

        myOfferTitle.setText("我的报价" + offerTitle);
    }

}
