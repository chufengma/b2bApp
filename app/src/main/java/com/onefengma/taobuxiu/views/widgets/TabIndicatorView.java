package com.onefengma.taobuxiu.views.widgets;

import android.content.Context;
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
public class TabIndicatorView extends RelativeLayout {

    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.red_dot)
    ImageView redDot;
    @BindView(R.id.unread_count_num)
    TextView unreadCountNum;
    @BindView(R.id.unread_count_layout)
    RelativeLayout unreadCountLayout;

    private boolean redDotMode = false;
    private TabParams tabParams;

    public TabIndicatorView(Context context, TabParams tabParams) {
        super(context);
        this.tabParams = tabParams;
        redDotMode = tabParams.redDotMode;
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.view_tab_indicator, this);
        ButterKnife.bind(this, view);

        text.setText(tabParams.text);
        image.setImageResource(tabParams.backgroundResId);
        setUnreadCount(0);
    }

    public void setUnreadCount(int num) {
        if (num < 0) {
            return;
        }
        // 小红点模式 并且 数量大于0 则显示小红点
        redDot.setVisibility(redDotMode && num > 0 ? View.VISIBLE : View.GONE);

        // 未读数模式 并且 数量大于0 则显示未读数
        unreadCountLayout.setVisibility(!redDotMode && num > 0 ? View.VISIBLE : View.GONE);
        unreadCountNum.setText(num > 99 ? "99+" : (num + ""));
    }


    public static class TabParams {
        public int backgroundResId;
        public String text;
        public boolean redDotMode=false;

        public TabParams(int backgroundResId, String text, boolean redDotMode) {
            this.backgroundResId = backgroundResId;
            this.text = text;
            this.redDotMode = redDotMode;
        }
    }

    public static TabIndicatorView newBuyTabIndicator(Context context) {
        TabParams tabParams = new TabParams(R.drawable.ic_main_tab_home, "求购", false);
        return new TabIndicatorView(context, tabParams);
    }

    public static TabIndicatorView newLogisticsTabIndicator(Context context) {
        TabParams tabParams = new TabParams(R.drawable.ic_main_tab_logistics, "找物流", false);
        return new TabIndicatorView(context, tabParams);
    }

    public static TabIndicatorView newQTTabIndicator(Context context) {
        TabParams tabParams = new TabParams(R.drawable.ic_main_tab_qt, "找质检", false);
        return new TabIndicatorView(context, tabParams);
    }

    public static TabIndicatorView newOfferTabIndicator(Context context) {
        TabParams tabParams = new TabParams(R.drawable.ic_main_tab_offer, "报价", false);
        return new TabIndicatorView(context, tabParams);
    }

    public static TabIndicatorView newMineTabIndicator(Context context) {
        TabParams tabParams = new TabParams(R.drawable.ic_main_tab_mine, "我", false);
        return new TabIndicatorView(context, tabParams);
    }

    public static TabIndicatorView newSalesUserTabIndicator(Context context) {
        TabParams tabParams = new TabParams(R.drawable.ic_main_tab_home, "绑定用户", false);
        return new TabIndicatorView(context, tabParams);
    }

    public static TabIndicatorView newSalesQTTabIndicator(Context context) {
        TabParams tabParams = new TabParams(R.drawable.ic_main_tab_qt, "质检", false);
        return new TabIndicatorView(context, tabParams);
    }

    public static TabIndicatorView newSalesBuyTabIndicator(Context context) {
        TabParams tabParams = new TabParams(R.drawable.ic_main_tab_offer, "求购", false);
        return new TabIndicatorView(context, tabParams);
    }

    public static TabIndicatorView newSalesMineTabIndicator(Context context) {
        TabParams tabParams = new TabParams(R.drawable.ic_main_tab_mine, "账号", false);
        return new TabIndicatorView(context, tabParams);
    }
}
