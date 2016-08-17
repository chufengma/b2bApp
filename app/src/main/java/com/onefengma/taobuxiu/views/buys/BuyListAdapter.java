package com.onefengma.taobuxiu.views.buys;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.BuyManager;
import com.onefengma.taobuxiu.model.entities.IronBuyBrief;
import com.onefengma.taobuxiu.utils.DateUtils;
import com.onefengma.taobuxiu.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author yfchu
 * @date 2016/8/16
 */
public class BuyListAdapter extends BaseAdapter {

    public List<IronBuyBrief> myBuys = new ArrayList<>();

    private BuyManager.BuyStatus buyStatus = BuyManager.BuyStatus.DOING;

    public BuyListAdapter(BuyManager.BuyStatus buyStatus) {
        this.buyStatus = buyStatus;
    }

    @Override
    public int getCount() {
        return myBuys.size();
    }

    public void setMyBuys(List<IronBuyBrief> myBuys) {
        this.myBuys.clear();
        this.myBuys.addAll(myBuys);
        notifyDataSetChanged();
    }

    @Override
    public IronBuyBrief getItem(int position) {
        return myBuys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.buy_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        IronBuyBrief ironBuyBrief = getItem(position);

        switch (buyStatus) {
            case DOING:
                viewHolder.supplyCount.setBackgroundResource(R.drawable.buy_item_icon_bg);
                viewHolder.statusDesc.setText("进行中");
                viewHolder.statusDesc.setTextColor(parent.getResources().getColor(R.color.main_yellow));
                viewHolder.redDot.setVisibility(ironBuyBrief.newSupplyNum > 0 ? View.VISIBLE : View.GONE);
                break;
            case DONE:
                viewHolder.supplyCount.setBackgroundResource(R.drawable.buy_item_icon_bg_done);
                viewHolder.statusDesc.setText("恭喜成交");
                viewHolder.statusDesc.setTextColor(parent.getResources().getColor(R.color.main_green));
                break;
            case OUT_OF_DATE:
                viewHolder.supplyCount.setBackgroundResource(R.drawable.buy_item_icon_bg_out_of_date);
                viewHolder.statusDesc.setText("已过期");
                viewHolder.statusDesc.setTextColor(parent.getResources().getColor(R.color.main_red));
                break;
        }

        viewHolder.title.setText(ironBuyBrief.ironType + "/" + ironBuyBrief.material + "/" + ironBuyBrief.surface + "/" + ironBuyBrief.proPlace + "( " + ironBuyBrief.sourceCity + ")");
        viewHolder.subTitle.setText(ironBuyBrief.length + "*" + ironBuyBrief.width + "*" + ironBuyBrief.height + " " + ironBuyBrief.tolerance + " " + ironBuyBrief.numbers + "" + ironBuyBrief.unit);
        viewHolder.message.setText(StringUtils.getString(R.string.buy_item_message, ironBuyBrief.message));
        viewHolder.deadLine.setText(StringUtils.getString(R.string.buy_item_time_limit, DateUtils.getDateStr(ironBuyBrief.pushTime + ironBuyBrief.timeLimit)));

        viewHolder.supplyCount.setText(ironBuyBrief.supplyCount + "");

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.supply_count)
        TextView supplyCount;
        @BindView(R.id.status_desc)
        TextView statusDesc;
        @BindView(R.id.red_dot)
        View redDot;
        @BindView(R.id.supply_count_layout)
        RelativeLayout supplyCountLayout;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.sub_title)
        TextView subTitle;
        @BindView(R.id.message)
        TextView message;
        @BindView(R.id.dead_line)
        TextView deadLine;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
