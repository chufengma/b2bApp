package com.onefengma.taobuxiu.views.widgets;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.utils.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dev on 2017/2/16.
 */

public class GoodsAddView extends FrameLayout {

    @BindView(R.id.goods_choose)
    TextView goodsChoose;
    @BindView(R.id.goods_decrease)
    ImageView goodsDecrease;

    public GoodsAddView(Context context) {
        super(context);
        View view = inflate(context, R.layout.goods_layout, this);
        ButterKnife.bind(this, view);
    }

    public void setOnGoodsChooseClickListener(View.OnClickListener listener) {
        goodsChoose.setOnClickListener(listener);
    }

    public void setOnGoodsDecreaseClickListener(View.OnClickListener listener) {
        goodsDecrease.setOnClickListener(listener);
    }

    public void setGoods(String s) {
        goodsChoose.setText(s);
    }

    public boolean isEmpty() {
        return StringUtils.isEmpty(goodsChoose.getText().toString());
    }

}
