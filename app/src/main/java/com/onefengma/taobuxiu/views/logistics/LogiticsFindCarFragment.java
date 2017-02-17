package com.onefengma.taobuxiu.views.logistics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.model.CityCategory;
import com.onefengma.taobuxiu.model.IconDataCategory;
import com.onefengma.taobuxiu.model.events.logistics.ChooseDeadLineEvent;
import com.onefengma.taobuxiu.model.events.logistics.EditMessageEvent;
import com.onefengma.taobuxiu.model.events.logistics.EditOtherDemandEvent;
import com.onefengma.taobuxiu.model.events.logistics.GoodsChooseEvent;
import com.onefengma.taobuxiu.model.events.logistics.RegionChooseEvent;
import com.onefengma.taobuxiu.utils.StringUtils;
import com.onefengma.taobuxiu.views.core.BaseFragment;
import com.onefengma.taobuxiu.views.widgets.GoodsAddView;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dev on 2017/2/15.
 */

public class LogiticsFindCarFragment extends BaseFragment {

    @BindView(R.id.time_limit)
    TextView timeLimit;
    @BindView(R.id.goods_choose)
    TextView goodsChoose;
    @BindView(R.id.goods_add)
    ImageView goodsAdd;
    @BindView(R.id.other_message)
    TextView otherMessage;
    @BindView(R.id.other_message_desc)
    TextView otherMessageDesc;
    @BindView(R.id.edit_other_message)
    ImageView editOtherMessage;
    @BindView(R.id.message)
    TextView message;
    @BindView(R.id.message_clean)
    ImageView messageClean;
    @BindView(R.id.edit_content)
    ScrollView editContent;
    @BindView(R.id.edit_btns)
    LinearLayout editBtns;
    @BindView(R.id.goods_layout)
    LinearLayout goodsLayout;
    @BindView(R.id.start_point)
    LinearLayout startPoint;
    @BindView(R.id.end_point)
    LinearLayout endPoint;
    @BindView(R.id.start_point_text)
    TextView startPointText;
    @BindView(R.id.end_point_text)
    TextView endPointText;

    private int day;
    private int hour;
    private int minute;

    private List<String> otherDemands;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.logitics_find_car_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBusHelper.register(this);

        startPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseCityActivity.start(getActivity(), "startPoint");
            }
        });

        endPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseCityActivity.start(getActivity(), "endPoint");
            }
        });

        goodsChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseGoodsActivity.start(getActivity(), "0");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBusHelper.unregister(this);
    }

    @OnClick(R.id.time_limit)
    public void onTimeLimitClick(View view) {
        ChooseDeadlineActivity.start(getActivity(), day, hour, minute);
    }

    @OnClick(R.id.goods_add)
    public void onGoodsAdd() {
        for (int i = 0; i < goodsLayout.getChildCount(); i++) {
            if (goodsLayout.getChildAt(i) instanceof GoodsAddView) {
                if (((GoodsAddView) goodsLayout.getChildAt(i)).isEmpty()) {
                    return;
                }
            }
        }
        final GoodsAddView goodsAddViewTmp = new GoodsAddView(getContext());
        goodsAddViewTmp.setOnGoodsDecreaseClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goodsLayout.removeView(goodsAddViewTmp);
            }
        });
        goodsAddViewTmp.setOnGoodsChooseClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseGoodsActivity.start(getActivity(), "" + goodsLayout.indexOfChild(goodsAddViewTmp));
            }
        });
        goodsLayout.addView(goodsAddViewTmp);
    }

    @OnClick(R.id.other_message)
    public void onOtherMessage() {
        EditOtherDemandActivity.start(getActivity());
    }

    @OnClick(R.id.edit_other_message)
    public void onEditOtherMessage() {
        EditOtherDemandActivity.start(getActivity());
    }

    @OnClick(R.id.message)
    public void onMessage() {
        MessageEditActivity.start(getActivity(), message.getText().toString());
    }

    @OnClick(R.id.message_clean)
    public void onMessageClean() {
        messageClean.setVisibility(View.GONE);
        message.setText("");
    }

    @Subscribe
    public void onEvent(RegionChooseEvent event) {
        if (event.requestId.equalsIgnoreCase("startPoint")) {
            startPointText.setText(CityCategory.instance().getCityDesc2(event.city.id));
        } else if (event.requestId.equalsIgnoreCase("endPoint")) {
            endPointText.setText(CityCategory.instance().getCityDesc2(event.city.id));
        }
    }

    @Subscribe
    public void onEvent(EditMessageEvent event) {
        message.setText(event.message);
        messageClean.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void onEvent(GoodsChooseEvent event) {
        if (StringUtils.equals(event.requestID, "0")) {
            goodsChoose.setText(event.goodTitle + " " + event.goodContent);
        } else {
            int index = Integer.parseInt(event.requestID);
            GoodsAddView goodsAddView = (GoodsAddView) goodsLayout.getChildAt(index);
            goodsAddView.setGoods(event.goodTitle + " " + event.goodContent);
        }
    }

    @Subscribe
    public void onEvent(EditOtherDemandEvent event) {
        if (event.demands.isEmpty()) {
            otherMessage.setVisibility(View.VISIBLE);
            otherMessageDesc.setVisibility(View.GONE);
            otherMessageDesc.setText("");
            editOtherMessage.setVisibility(View.GONE);
            otherDemands = event.demands;
        } else {
            otherDemands = event.demands;
            otherMessage.setVisibility(View.GONE);
            otherMessageDesc.setVisibility(View.VISIBLE);
            editOtherMessage.setVisibility(View.VISIBLE);
            StringBuilder stringBuilder = new StringBuilder();
            for (String text : event.demands) {
                stringBuilder.append(text);
                if (event.demands.indexOf(text) != event.demands.size() - 1) {
                    stringBuilder.append("|");
                }
            }
            otherMessageDesc.setText(stringBuilder);
        }
    }

    @Subscribe
    public void onEvent(ChooseDeadLineEvent event) {
        timeLimit.setText("找车有期限  " + event.day + "天" + event.hour + "小时" + event.minute + "分钟");
        this.day = event.day;
        this.hour = event.hour;
        this.minute = event.minute;
    }

}

