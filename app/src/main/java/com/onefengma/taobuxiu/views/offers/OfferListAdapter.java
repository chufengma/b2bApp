package com.onefengma.taobuxiu.views.offers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.model.entities.IronBuyBrief;
import com.onefengma.taobuxiu.model.entities.QtDetail;
import com.onefengma.taobuxiu.utils.DateUtils;
import com.onefengma.taobuxiu.utils.StringUtils;
import com.onefengma.taobuxiu.views.buys.BuyDetailActivity;
import com.onefengma.taobuxiu.views.core.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author yfchu
 * @date 2016/8/16
 */
public class OfferListAdapter extends BaseAdapter {

    public List<IronBuyBrief> details = new ArrayList<>();

    @Override
    public int getCount() {
        return details.size();
    }

    public void setMyBuys(List<IronBuyBrief> myBuys) {
        if (myBuys == null) {
            return;
        }
        this.details.clear();
        this.details.addAll(myBuys);
        notifyDataSetChanged();
    }

    @Override
    public IronBuyBrief getItem(int position) {
        return details.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final IronBuyBrief ironBuyBrief = getItem(position);

        viewHolder.title.setText(ironBuyBrief.ironType + "/" + ironBuyBrief.material + "/" + ironBuyBrief.surface + "/" + ironBuyBrief.proPlace + "( " + ironBuyBrief.sourceCity + ")");
        viewHolder.subTitle.setText(ironBuyBrief.length + "*" + ironBuyBrief.width + "*" + ironBuyBrief.height + " " + ironBuyBrief.tolerance + " " + ironBuyBrief.numbers + "" + ironBuyBrief.unit);
        viewHolder.message.setText(StringUtils.getString(R.string.buy_item_message, ironBuyBrief.message));
        viewHolder.deadLine.setText(StringUtils.getString(R.string.buy_item_time_limit, DateUtils.getDateStr(ironBuyBrief.pushTime + ironBuyBrief.timeLimit)));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OfferDetailActivity.start((BaseActivity) parent.getContext(), ironBuyBrief.id);
            }
        });

        return convertView;
    }

    static class ViewHolder {
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
